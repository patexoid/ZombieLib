package lib.back.mysqldumpparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by alex on 30.01.14.
 */
public class EntityParser<E> {
    private static Logger log = LoggerFactory.getLogger(EntityParser.class);
    private final Class<E> _class;
    private final List<EntityField> _fields;
    private final Pattern _pattern;
    private final Constructor<E> _constructor;


    public EntityParser(Class<E> clazz) throws MysqlParserException {
        _class = clazz;
        try {
            _constructor = _class.getConstructor();
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);
            throw new MysqlParserException("default constructor not exists", e);
        }
        try {
            _constructor.setAccessible(true);
        } catch (SecurityException e) {
            log.warn("unable to _constructor.setAccessible(true) security error: " + e.getMessage());
        }
        try {
            List<EntityField> fields = Arrays.asList(_class.getDeclaredFields()).stream().
                    map((field) -> new EntityField(_class, field)).
                    sorted().collect(Collectors.toList());
            String regexp = fields.stream().map(EntityField::getRegexp).reduce((s, s2) -> s.concat(",").concat(s2)).get();
            _pattern = Pattern.compile(regexp, Pattern.DOTALL);
            _fields = fields.stream().filter(EntityField::isRequired).collect(Collectors.toList());
        } catch (MysqlParerRuntimeException e) {
            throw new MysqlParserException(e);
        }
    }

    public E parse(String line) {
        try {

            E instance = _constructor.newInstance();

            Matcher matcher = _pattern.matcher(line);
            if (matcher.matches()) {
                for (int i = 0; i < _fields.size(); i++) {
                    EntityField field = _fields.get(i);
                    field.setValue(instance, matcher.group(i + 1));
                }

            } else {
                log.warn("can't match line {}  for pattern {}", line, _pattern.pattern());
                return null;
            }
            return instance;
        } catch (ReflectiveOperationException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public Class<E> getClazz() {
        return _class;
    }
}
