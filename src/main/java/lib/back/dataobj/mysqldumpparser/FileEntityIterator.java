package lib.back.dataobj.mysqldumpparser;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by apotekhin on 10/6/2014.
 */
public class FileEntityIterator<T> implements Iterator<T>, Iterable<T> {


    private EntityParser<T> _entityParser;
    private EntityManager _entityManager;
    private FileIterator _fileIterator;
    private T _next;
    Validator<T> _validator= new Validator<T>() {
        @Override
        public boolean isValid(T t) {
            return isSValid&&((SValid)t).isValid();
        }
    };
    final boolean isSValid;

    public FileEntityIterator(EntityParser<T> entityParser, String path, String file, EntityManager entityManager) throws IOException {
        this(entityParser, new FileIterator(path, file), entityManager);
    }

    public FileEntityIterator(EntityParser<T> entityParser, FileIterator fileIterator, EntityManager entityManager) {
        _entityParser = entityParser;
        _fileIterator = fileIterator;
        _entityManager = entityManager;
        _next = getNext();
         isSValid = SValid.class.isAssignableFrom(_entityParser.getClazz());
    }

    @Override
    public boolean hasNext() {
        return _next != null;
    }

    @Override
    public T next() {
        T current = _next;
        _next = getNext();
        return current;
    }

    private T getNext() {
        while (_fileIterator.hasNext()) {
            String nextLine = _fileIterator.next();
            T next = _entityParser.parse(nextLine, _entityManager);
            if ((next != null) &&_validator.isValid(next)) {
                return next;
            }
        }
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    public void setValidator(Validator<T> validator) {
        _validator = validator;
    }
}
