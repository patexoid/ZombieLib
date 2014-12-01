package lib.back.dataobj.mysqldumpparser;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
import org.skife.jdbi.com.fasterxml.classmate.util.MethodKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alex on 30.01.14.
 */
public class EntityParser<E> {
    private static Logger log = LoggerFactory.getLogger(EntityParser.class);
    private final Class<E> _class;
    private final EntityField[] _fields;
    private final Pattern _pattern;
    private final Constructor<E> _constructor;


    public EntityParser(Class<E> clazz, String... fields){
        _class = clazz;
        try {
            _constructor = _class.getConstructor();
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(),e);
            throw new IllegalArgumentException(e);
        }
        _fields = new EntityField[fields.length];
        String regexp="";
        for (int i = 0; i < fields.length; i++) {
            _fields[i]=new EntityField(_class, fields[i]);
            regexp+=i==0?"":",";
            regexp+=_fields[i].getFieldConverter().getRegexp();
        }
        _pattern = Pattern.compile(regexp,Pattern.DOTALL);
    }

    public E parse(String line, EntityManager entityManager){
        try {

            E instance = _constructor.newInstance();

            Matcher matcher = _pattern.matcher(line);
            if(matcher.matches()){
                for (int i = 0; i < _fields.length; i++) {
                    Class<?> parameterType = (_fields[i].getParameterType());
                    final String stringValue = matcher.group(i + 1);
                    Object fieldValue =_fields[i].getFieldConverter().convert(stringValue,parameterType,entityManager);
                    _fields[i].setValue(instance,fieldValue);
                }

            } else {
                log.warn("can't match line {}  for pattern {}",line, _pattern.pattern());
                return null;
            }
            return instance;
        } catch (ReflectiveOperationException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public Class<E> getClazz() {
        return _class;
    }
}
