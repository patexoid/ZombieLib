package lib.back.dataobj.mysqldumpparser;

import lib.back.dataobj.repository.AuthorRepository;
import lib.back.dataobj.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by apotekhin on 9/25/2014.
 */
@Service
@Lazy
public class JpaParserImpl extends Parser {

    private static Logger log = LoggerFactory.getLogger(JpaParserImpl.class);

    @Autowired
    AuthorRepository _authorRepository;

    @Autowired
    BookRepository _bookRepository;

    @PersistenceContext
    private EntityManager _entityManager;

    protected void deleteOrphans() {
        long authorCount = _authorRepository.count();
        log.debug("author count {}", authorCount);
        int deletedAuthors = _authorRepository.deleteOrphans();
        log.debug("{} empty authors was deleted", deletedAuthors);

        long bookCount = _bookRepository.count();
        log.debug("book count {}", bookCount);
        int deletedBook = _bookRepository.deleteOrphans();
        log.debug("{} orphans books was deleted", deletedBook);

    }

    protected void save(String subLine, EntityParser entityParser) {
        Object obj = entityParser.parse(subLine, _entityManager);
        save(obj);
    }

    protected void save(Object obj) {
        if (obj != null) {
            Object id = _entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(obj);
            if (id != null && _entityManager.find(obj.getClass(), id) != null) {
                _entityManager.merge(obj);
            } else {
                _entityManager.persist(obj);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void addRelation(Class main, EntityField collectionField, Class secondary, String subLine, String[] split) {
        try {
            Object mainObj = _entityManager.find(main, Long.valueOf(split[0]));
            Object secObj = _entityManager.find(secondary, Long.valueOf(split[1]));
            if (mainObj != null && secObj != null) {
                Collection collection = (Collection) collectionField.getValue(mainObj);
                if (!collection.contains(secObj)) {
                    collection.add(secObj);
                }
                save(mainObj);
//            } else {
//                log.warn("bad relation {} {} {}", subLine, mainObj, secObj);
            }

        } catch (RuntimeException | ReflectiveOperationException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    protected void flush() {

    }
}
