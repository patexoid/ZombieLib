package lib.back.mysqldumpparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by alex on 30.01.14.
 */
public class EntityField {
    private static Logger log = LoggerFactory.getLogger(EntityField.class);

    public static final ObjectConverter stringConverter = new StringConverter();
    public static final ObjectConverter booleanConverter = new BooleanConverter();
    public static final ObjectConverter longConverter = new LongConverter();
    public static final ObjectConverter shortConverter = new ShortConverter();
    public static final ObjectConverter dateConverter = new DateConverter();
    public static final EntityConverter entityConverter = new EntityConverter();
    private static Map<Class, ObjectConverter> _converters;

    static {
        _converters = new HashMap<Class, ObjectConverter>();
        _converters.put(String.class, stringConverter);
        _converters.put(Boolean.class, booleanConverter);
        _converters.put(Long.class, longConverter);
        _converters.put(Short.class, shortConverter);
        _converters.put(Date.class, dateConverter);

    }

    private Method set;
    private Method get;
    private Class parameterType;
    ObjectConverter converter;

    public EntityField(Class<?> clazz, String fieldName) {
        try {
            String meth = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            get = clazz.getMethod("get" + meth);
            parameterType = get.getReturnType();
            if (!Collection.class.isAssignableFrom(parameterType)) {
                set = clazz.getMethod("set" + meth, parameterType);
                converter = _converters.get(parameterType);
                if (converter == null) {
                    converter = entityConverter;
                }
            }
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage());
        }
    }

    public Class getParameterType() {
        return parameterType;
    }

    public void setValue(Object entity, Object value) throws ReflectiveOperationException {
        set.invoke(entity, value);

    }

    public Object getValue(Object entity) throws ReflectiveOperationException {
        return get.invoke(entity);
    }


    public ObjectConverter getFieldConverter() {
        return converter;
    }


    public static interface ObjectConverter<E> {
        E convert(String data, Class clazz, EntityManager entityManager);

        String getRegexp();
    }

    private static class StringConverter implements ObjectConverter<String> {
        @Override
        public String convert(String data, Class clazz, EntityManager entityManager) {
            data = replace(data, "\\'", "'");
            data = replace(data, "\\\\\\\\", "\\\\");
            data = replace(data, "\\n", "\n");
            data = replace(data, "\\r", "\r");
            data = replace(data, "\\t", "\t");
            return data;
        }

        private String replace(String data, String replaceString, String replacement) {
            if (data.contains(replaceString))
                return data.replaceAll(Matcher.quoteReplacement(replaceString), replacement);
            return data;
        }

        @Override
        public String getRegexp() {
            return "'(.*)'";
        }
    }

    private static class BooleanConverter implements ObjectConverter<Boolean> {
        @Override
        public Boolean convert(String data, Class clazz, EntityManager entityManager) {
            return !"0".equals(data);
        }

        @Override
        public String getRegexp() {
            return "'(.?)'";
        }
    }

    private static class LongConverter implements ObjectConverter<Long> {
        @Override
        public Long convert(String data, Class clazz, EntityManager entityManager) {
            return Long.valueOf(data);
        }

        @Override
        public String getRegexp() {
            return "(-?\\d*)";
        }
    }

    private static class ShortConverter implements ObjectConverter<Short> {
        @Override
        public Short convert(String data, Class clazz, EntityManager entityManager) {
            return Short.valueOf(data);
        }

        @Override
        public String getRegexp() {
            return "(-?\\d*)";
        }
    }

    private static class DateConverter implements ObjectConverter<Date> {
        SimpleDateFormat _sdt = new SimpleDateFormat("YYY-MM-dd HH:mm:ss");

        @Override
        public Date convert(String data, Class clazz, EntityManager entityManager) {
            try {
                return _sdt.parse(data);
            } catch (ParseException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }

        }

        @Override
        public String getRegexp() {
            return "'([\\d\\s\\:-]*)'";
        }
    }

    private static class EntityConverter implements ObjectConverter {
        private Map<Class, Method> cache = new HashMap<Class, Method>();

        @Override
        public Object convert(String data, Class clazz, EntityManager entityManager) {
            if (entityManager != null) {
                return entityManager.find(clazz, Long.valueOf(data));
            } else {
                try {
                    Object o = clazz.getConstructor().newInstance();
                    Method setMethod = cache.get(clazz);
                    if (setMethod == null) {
                        Field[] fields = clazz.getDeclaredFields();
                        main:
                        for (Field field : fields) {
                            for (Annotation annotation : field.getAnnotations()) {
                                if (Id.class.isAssignableFrom(annotation.annotationType())) {
                                    String fieldName = field.getName();
                                    setMethod = clazz.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), Long.class);
                                    cache.put(clazz, setMethod);
                                    break main;
                                }
                            }
                        }
                    }
                    if (setMethod != null) {
                        setMethod.invoke(o, Long.valueOf(data));
                        return o;
                    }
                } catch (ReflectiveOperationException e) {
                    log.error(e.getMessage(), e);
                }
                return null;
            }
        }

        @Override
        public String getRegexp() {
            return "(\\d*)";
        }
    }

}
