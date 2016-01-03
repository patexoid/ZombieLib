package lib.back.mysqldumpparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;


public class EntityField implements Comparable<EntityField> {
    public static final ObjectConverter stringConverter = new StringConverter();
    public static final ObjectConverter booleanConverter = new BooleanConverter();
    public static final ObjectConverter longConverter = new LongConverter();
    public static final ObjectConverter shortConverter = new ShortConverter();
    public static final ObjectConverter dateConverter = new DateConverter();
    private static Logger log = LoggerFactory.getLogger(EntityField.class);
    private static Map<Class, ObjectConverter> _converters;

    static {
        _converters = new HashMap<>();
        _converters.put(String.class, stringConverter);
        _converters.put(Boolean.class, booleanConverter);
        _converters.put(Long.class, longConverter);
        _converters.put(Short.class, shortConverter);
        _converters.put(Date.class, dateConverter);

    }

    private final int order;
    private final boolean skip;
    private final Method set;
    private final Method get;
    private final Class parameterType;
    ObjectConverter converter;

    public EntityField(Class<?> clazz, Field field) throws MysqlParerRuntimeException {
        String fieldName = field.getName();
        skip = field.getAnnotation(SkipField.class) != null;
        try {
            order = field.getAnnotation(SortOrder.class).value();
        } catch (NullPointerException e) {
            log.error("SortOrder annotation was not defined for field " + clazz + "." + fieldName);
            throw new MysqlParerRuntimeException("SortOrder annotation was not defined for field " + clazz + "." + fieldName);
        }
        try {
            String meth = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            get = clazz.getMethod("get" + meth);

            try {
                get.setAccessible(true);
            } catch (SecurityException e) {
                log.warn("unable to get.setAccessible(true) security error: " + e.getMessage());
            }
            parameterType = get.getReturnType();
            set = clazz.getMethod("set" + meth, parameterType);
            try {
                get.setAccessible(true);
            } catch (SecurityException e) {
                log.warn("unable to get.setAccessible(true) security error: " + e.getMessage());
            }
            converter = _converters.get(parameterType);
            if (converter == null) {
                throw new MysqlParerRuntimeException("unable to convert " + clazz + "." + fieldName + "type " + parameterType.getClass());
            }
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new MysqlParerRuntimeException(e);
        }
    }

    public String getRegexp() {
    return skip?getFieldConverter().getRegexpNoGroup():getFieldConverter().getRegexp();
    }


    public Class getParameterType() {
        return parameterType;
    }

    public void setValue(Object entity, String value) throws ReflectiveOperationException {

        set.invoke(entity, getFieldConverter().convert(value));

    }

    public Object getValue(Object entity) throws ReflectiveOperationException {
        return get.invoke(entity);
    }


    public ObjectConverter getFieldConverter() {
        return converter;
    }


    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(EntityField other) {
        return (order < other.order) ? -1 : ((order == other.order) ? 0 : 1);
    }


    public interface ObjectConverter<E> {
        E convert(String data);

        String getRegexp();

        String getRegexpNoGroup();
    }

    private static class StringConverter implements ObjectConverter<String> {
        @Override
        public String convert(String data) {
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

        @Override
        public String getRegexpNoGroup() {
            return "'.*'";
        }
    }

    private static class BooleanConverter implements ObjectConverter<Boolean> {
        @Override
        public Boolean convert(String data) {
            return !"0".equals(data);
        }

        @Override
        public String getRegexp() {
            return "'(.?)'";
        }

        @Override
        public String getRegexpNoGroup() {
            return "'.?'";
        }
    }

    private static class LongConverter implements ObjectConverter<Long> {
        @Override
        public Long convert(String data) {
            return Long.valueOf(data);
        }

        @Override
        public String getRegexp() {
            return "(-?\\d*)";
        }

        @Override
        public String getRegexpNoGroup() {
            return "-?\\d*";
        }
    }

    private static class ShortConverter implements ObjectConverter<Short> {
        @Override
        public Short convert(String data) {
            return Short.valueOf(data);
        }

        @Override
        public String getRegexp() {
            return "(-?\\d*)";
        }

        @Override
        public String getRegexpNoGroup() {
            return "-?\\d*";
        }
    }

    private static class DateConverter implements ObjectConverter<Date> {
        SimpleDateFormat _sdt = new SimpleDateFormat("YYY-MM-dd HH:mm:ss");

        @Override
        public Date convert(String data) {
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

        @Override
        public String getRegexpNoGroup() {
            return "'[\\d\\s\\:-]*'";
        }
    }

    public boolean isRequired() {
        return !skip;
    }
}
