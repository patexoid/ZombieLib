package lib.back.dataobj.repository.jdbi;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Bind Annotation for support complex fields like :book.author.name
 */
@BindingAnnotation(BestBindBean.BestBeanBindFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface BestBindBean {
    String value();

    public static class BestBeanBindFactory implements BinderFactory {

        private final Map<Class, BeanGetter> _getters = new HashMap<>();
        private final Map<String, Set<String>> _fields = new HashMap<String, Set<String>>();

        /**
         * Called to build a Binder
         * @param annotation the {@link BindingAnnotation} which lead to this call
         * @return a binder to use
         */
        @Override
        public Binder<BestBindBean, Object> build(Annotation annotation) {
            return new Binder<BestBindBean, Object>() {
                public void bind(SQLStatement q, BestBindBean bind, Object arg) {
                    String rawSql = q.getContext().getRawSql();
                    String prefix = bind.value()+".";
                    Set<String> fields = getFields(rawSql);
                    BeanGetter beanGetter = getBeanGetter(arg.getClass());
                    for (String field : fields) {
                        if (field.startsWith(prefix)) {
                            Object value = beanGetter.getValue(arg, field.substring(prefix.length()));
                            if (value != null) {
                                q.dynamicBind(value.getClass(), field,value);
                            }
                        }
                    }
                }

                /**
                 * Get beanGetter from temporary cache
                 * @param clazz BeanGetter classs
                 * @return BeanGetter instance
                 */
                private BeanGetter getBeanGetter(Class clazz) {
                    synchronized (_getters) {
                        BeanGetter beanGetter = _getters.get(clazz);
                        if (beanGetter == null) {
                            beanGetter = new BeanGetter(clazz);
                            _getters.put(clazz, beanGetter);
                        }
                        return beanGetter;
                    }
                }

                /**
                 * get field list from SQL that should be binded using this beanGetter
                 * @param rawSql SQL wit fieds
                 * @return field list from SQL that should be binded using this beanGetter
                 */
                private Set<String> getFields(String rawSql) {
                    synchronized (_fields) {
                        Set<String> fields = _fields.get(rawSql);
                        if (fields == null) {
                            fields = new HashSet<>();
                            _fields.put(rawSql, fields);
                            int position = 0;
                            while (position != -1) {
                                position = rawSql.indexOf(":", position+1);
                                if (position != -1) {
                                    String field = getField(rawSql, position);
                                    fields.add(field);
                                    position += field.length();
                                }
                            }
                        }
                        return fields;
                    }
                }


                /**
                 * get single field from SQL started from positiion
                 * @param rawSql SQL with field
                 * @param position field position
                 * @return field
                 */
                private String getField(String rawSql, int position) {
                    int last = position+1 ;
                    String field = "";
                    while (rawSql.length()>last) {
                        char symbol = rawSql.charAt(last++);
                        if (Character.isLetterOrDigit(symbol) || '.' == symbol) {
                            field += symbol;
                        } else {
                            break;
                        }
                    }
                    return field;
                }
            };
        }

        /**
         *
         * @param <E>
         */
        private class BeanGetter<E> {
            private final Class<E> _clazz;
            private final Map<String, List<Method>> methods = new HashMap<String, List<Method>>();

            private BeanGetter(Class<E> clazz) {
                _clazz = clazz;
            }

            /**
             *
             * @param o object
             * @param field object field
             * @return field value
             */
            private Object getValue(E o, String field) {
                if (_clazz.isInstance(o)) {
                    List<Method> methodList = getMethods(_clazz, field);
                    Object value = o;
                    for (Method method : methodList) {
                        try {
                            value = method.invoke(value);
                            if (value == null) {
                                break;
                            }
                        } catch (ReflectiveOperationException e) {
                            throw new UnsupportedOperationException("No such method " + method.getName() + " for " + value.getClass());
                        }
                    }
                    return value;
                }
                throw new UnsupportedOperationException("wrong object " + o);
            }


            private synchronized List<Method> getMethods(Class clazz, String field) {
                synchronized (methods) {
                    List<Method> methodList = methods.get(field);
                    if (methodList == null) {
                        String[] subFields = field.split("\\.");
                        methodList = new ArrayList<>(subFields.length);
                        methods.put(field, methodList);
                        Class subclass = clazz;
                        for (String subField : subFields) {
                            Method method;
                            try {
                                method = subclass.getMethod("get" + subField.substring(0, 1).toUpperCase() + subField.substring(1));
                            } catch (NoSuchMethodException e) {
                                throw new UnsupportedOperationException("No such method " + subField + " for " + subclass.getClass());
                            }
                            subclass = method.getReturnType();
                            methodList.add(method);
                        }
                        methods.put(field, methodList);
                    }
                    return methodList;
                }
            }
        }

    }
}

