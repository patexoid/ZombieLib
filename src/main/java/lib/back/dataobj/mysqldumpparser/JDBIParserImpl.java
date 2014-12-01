package lib.back.dataobj.mysqldumpparser;

import lib.back.dataobj.*;
import lib.back.dataobj.repository.jdbi.JDBIIU;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 */

@Component
@Lazy
public class JDBIParserImpl extends Parser {
    private static Logger log = LoggerFactory.getLogger(JDBIParserImpl.class);

    @Autowired
    private DBI _dbi;
    private JDBIIU _batchIU;

    @PostConstruct
    private void load() {
        _batchIU = _dbi.open(JDBIIU.class);
        _batchMethodMap.put(Author.class, new BatchMethod<Author>() {
            @Override
            public void update(FileEntityIterator<Author> iterator) {
                _batchIU.insertAuthor(iterator);
                refreshAuthorIds();
            }
        });
        _batchMethodMap.put(Book.class, new BatchMethod<Book>() {
            @Override
            public void update(FileEntityIterator<Book> iterator) {
                _batchIU.insertBook(iterator);
                refreshBookIds();
            }
        });

        _batchMethodMap.put(Sequence.class, new BatchMethod<Sequence>() {
            @Override
            public void update(FileEntityIterator<Sequence> iterator) {
                _batchIU.insertSequence(iterator);
            }
        });


        _batchMethodMap.put(BookSequence.class, new BatchMethod<BookSequence>() {
            @Override
            public void update(FileEntityIterator<BookSequence> iterator) {
                refreshBookIds();
                refreshSequenceIds();
                iterator.setValidator(new Validator<BookSequence>() {
                    @Override
                    public boolean isValid(BookSequence bookSequence) {
                        return bookIds.contains(bookSequence.getBook().getBookId()) && sequenceIds.contains(bookSequence.getSequence().getSeqId());
                    }
                });
                _batchIU.insertBookSequence(iterator);
            }
        });

        _batchMethodMap.put(BookFileName.class, new BatchMethod<BookFileName>() {
            @Override
            public void update(FileEntityIterator<BookFileName> iterator) {
                refreshSequenceIds();
                iterator.setValidator(new Validator<BookFileName>() {
                    @Override
                    public boolean isValid(BookFileName bookFileName) {
                        return bookIds.contains(bookFileName.getBook().getBookId());
                    }
                });
                _batchIU.insertBookFileName(iterator);
            }
        });

        _batchMethodMap.put(AuthorAnnotation.class, new BatchMethod<AuthorAnnotation>() {
            @Override
            public void update(FileEntityIterator<AuthorAnnotation> iterator) {
                refreshAuthorIds();
                iterator.setValidator(new Validator<AuthorAnnotation>() {
                    @Override
                    public boolean isValid(AuthorAnnotation authorAnnotation) {
                        return authorIds.contains(authorAnnotation.getId());
                    }
                });
                _batchIU.insertAuthorAnnotation(iterator);
            }
        });

        _batchMethodMap.put(BookAnnotation.class, new BatchMethod<BookAnnotation>() {
            @Override
            public void update(FileEntityIterator<BookAnnotation> iterator) {
                refreshBookIds();
                iterator.setValidator(new Validator<BookAnnotation>() {
                    @Override
                    public boolean isValid(BookAnnotation bookAnnotation) {
                        return bookIds.contains(bookAnnotation.getId());
                    }
                });
                _batchIU.insertBookAnnotation(iterator);
            }
        });
    }

    private void refreshAuthorIds() {
        synchronized (authorIds) {
            if (authorIds.isEmpty()) {
                authorIds.addAll(_batchIU.getAuthorIds());
            }
        }
    }


    private void refreshBookIds() {
        synchronized (bookIds) {
            if (bookIds.isEmpty()) {
                bookIds.addAll(_batchIU.getBookIds());
            }
        }
    }

    private void refreshSequenceIds() {
        synchronized (sequenceIds) {
            if (sequenceIds.isEmpty()) {
                sequenceIds.addAll(_batchIU.getSequenceIds());
            }
        }
    }

    @PreDestroy
    private void unLoad() {
        _dbi.close(_batchIU);
    }


    private final Set<Long> authorIds = new HashSet<>();
    private final Set<Long> bookIds = new HashSet<>();
    private final Set<Long> sequenceIds = new HashSet<>();

    private static Map<Class<?>, BatchMethod<?>> _batchMethodMap = new HashMap<>();


    @Override
    public void parseEntity(EntityParser entityParser, String path, String filename) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        BatchMethod<?> batchMethod = _batchMethodMap.get(entityParser.getClazz());
        if (batchMethod != null) {
            batchMethod.update(new FileEntityIterator(entityParser, path, filename, null));
        } else {
            super.parseEntity(entityParser, path, filename);
        }
        log.info("time for {} is {}",entityParser.getClazz().getName(),(System.currentTimeMillis() - currentTimeMillis));
    }


    @Override
    protected void addRelation(Class main, EntityField collectionField, Class secondary, String subLine, String[] split) {
        if (main.equals(Book.class) && secondary.equals(Author.class)) {
            Long bookId = Long.valueOf(split[0]);
            Long authorId = Long.valueOf(split[1]);
            if (bookIds.contains(bookId) && authorIds.contains(authorId)) {
                try {
                    _batchIU.addAuthorBookRelation(authorId, bookId);
                } catch (Exception e) {
                    log.warn("error on relation update {} {}", subLine, e.getMessage());
                }
            } else {
                log.warn("non exist book or author {}", subLine);
            }
        }
    }

    protected void save(Object entity) {
        if (entity instanceof Author) {
            _batchIU.insertAuthor((Author) entity);
            authorIds.add(((Author) entity).getAuthorId());
        } else if (entity instanceof Book) {
            _batchIU.insertBook((Book) entity);
            bookIds.add(((Book) entity).getBookId());
        } else if (entity instanceof Sequence) {
            _batchIU.insertSequence((Sequence) entity);
            sequenceIds.add(((Sequence) entity).getSeqId());
        } else if (entity instanceof BookSequence) {
            _batchIU.insertBookSequence((BookSequence) entity);
        } else if (entity instanceof BookFileName) {
            _batchIU.insertBookFileName((BookFileName) entity);
        } else if (entity instanceof AuthorAnnotation) {
            _batchIU.insertAuthorAnnotation((AuthorAnnotation) entity);
        } else if (entity instanceof BookAnnotation) {
            _batchIU.insertBookAnnotation((BookAnnotation) entity);

        }
    }


    @Override
    protected void flush() {
        bookIds.clear();
        sequenceIds.clear();
        authorIds.clear();
    }

    @Override
    protected void deleteOrphans() {
        int lines = _batchIU.deleteEmptyAuthors();
        authorIds.clear();
        log.info("{} authors was deleted", lines);
        int deletedBooks = _batchIU.deleteEmptyBooks();
        log.info("{} books was deleted", deletedBooks);
        bookIds.clear();

    }


    @Override
    protected EntityManager getEntityManager() {
        return null;
    }

    private static interface BatchMethod<T> {
        public void update(FileEntityIterator<T> iterator);
    }
}
