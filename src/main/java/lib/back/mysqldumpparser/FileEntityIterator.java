package lib.back.mysqldumpparser;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by apotekhin on 10/6/2014.
 */
public class FileEntityIterator<T> implements Iterator<T>, Iterable<T> {


    private EntityParser<T> _entityParser;

    private FileIterator _fileIterator;
    private T _next;

    public FileEntityIterator(EntityParser<T> entityParser, String path, String file) throws IOException {
        this(entityParser, new FileIterator(path, file));
    }

    public FileEntityIterator(EntityParser<T> entityParser, FileIterator fileIterator) {
        _entityParser = entityParser;
        _fileIterator = fileIterator;

        _next = getNext();
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
            T next = _entityParser.parse(nextLine);
            if ((next != null)) {
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

}
