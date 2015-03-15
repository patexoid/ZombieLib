package lib.back.mysqldumpparser;

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
import java.util.concurrent.CountDownLatch;

/**
 */

@Component
@Lazy
public class MultiThreadJDBIParserImpl extends Parser {

    private static Logger log = LoggerFactory.getLogger(MultiThreadJDBIParserImpl.class);

    @Autowired
    private DBI _dbi;
    JDBIIU _batchIU;

    private CountDownLatch _bookAuthor = new CountDownLatch(2);
    private CountDownLatch _bookAuthorRelation = new CountDownLatch(1);
    private CountDownLatch _bookLatch = new CountDownLatch(1);
    private CountDownLatch _authorlatch = new CountDownLatch(1);
    private CountDownLatch _sequencelatch = new CountDownLatch(1);

    private CountDownLatch _finishLatch;

    @PostConstruct
    private void load() {
        _batchIU = _dbi.open(JDBIIU.class);
        _batchMethodMap.put(Author.class, new BatchMethod<Author>() {
            @Override
            public void method(JDBIIU batchIU, FileEntityIterator<Author> iterator) {
                batchIU.insertAuthor(iterator);
                updateAuthorIds(batchIU);
                _bookAuthor.countDown();
                _finishLatch.countDown();
            }
        });
        _batchMethodMap.put(Book.class, new BatchMethod<Book>() {
            @Override
            public void method(JDBIIU batchIU, FileEntityIterator<Book> iterator) {
                batchIU.insertBook(iterator);
                updateBookIds(batchIU);
                _bookAuthor.countDown();
                _finishLatch.countDown();
            }
        });

        _batchMethodMap.put(Sequence.class, new BatchMethod<Sequence>() {
            @Override
            public void method(JDBIIU batchIU, FileEntityIterator<Sequence> iterator) {
                batchIU.insertSequence(iterator);
                sequenceIds.addAll(batchIU.getSequenceIds());
                _sequencelatch.countDown();
                _finishLatch.countDown();
            }
        });


        _batchMethodMap.put(BookSequence.class, new BatchMethod<BookSequence>() {
            @Override
            public void method(JDBIIU batchIU, FileEntityIterator<BookSequence> iterator) {
                await(_bookLatch);
                await(_sequencelatch);
                iterator.setValidator(new Validator<BookSequence>() {
                    @Override
                    public boolean isValid(BookSequence bookSequence) {
                        return bookIds.contains(bookSequence.getBook().getBookId()) && sequenceIds.contains(bookSequence.getSequence().getSeqId());
                    }
                });
                batchIU.insertBookSequence(iterator);
                _finishLatch.countDown();
            }
        });

        _batchMethodMap.put(BookFileName.class, new BatchMethod<BookFileName>() {
            @Override
            public void method(JDBIIU batchIU, FileEntityIterator<BookFileName> iterator) {
                await(_bookLatch);
                iterator.setValidator(new Validator<BookFileName>() {
                    @Override
                    public boolean isValid(BookFileName bookFileName) {
                        return bookIds.contains(bookFileName.getBook().getBookId());
                    }
                });
                batchIU.insertBookFileName(iterator);
                _finishLatch.countDown();
            }
        });

        _batchMethodMap.put(AuthorAnnotation.class, new BatchMethod<AuthorAnnotation>() {
            @Override
            public void method(JDBIIU batchIU, FileEntityIterator<AuthorAnnotation> iterator) {
                await(_authorlatch);
                iterator.setValidator(new Validator<AuthorAnnotation>() {
                    @Override
                    public boolean isValid(AuthorAnnotation authorAnnotation) {
                        return authorIds.contains(authorAnnotation.getId());
                    }
                });
                batchIU.insertAuthorAnnotation(iterator);
                _finishLatch.countDown();
            }
        });

        _batchMethodMap.put(BookAnnotation.class, new BatchMethod<BookAnnotation>() {
            @Override
            public void method(JDBIIU batchIU, FileEntityIterator<BookAnnotation> iterator) {
                await(_bookLatch);
                iterator.setValidator(new Validator<BookAnnotation>() {
                    @Override
                    public boolean isValid(BookAnnotation bookAnnotation) {
                        return bookIds.contains(bookAnnotation.getId());
                    }
                });
                batchIU.insertBookAnnotation(iterator);
                _finishLatch.countDown();
            }
        });
        _finishLatch = new CountDownLatch(_batchMethodMap.size());
    }

    private void updateBookIds(JDBIIU batchIU) {
        synchronized (bookIds) {
            bookIds.clear();
            bookIds.addAll(batchIU.getBookIds());
        }
    }

    private void updateAuthorIds(JDBIIU batchIU) {
        synchronized (authorIds) {
            authorIds.clear();
            authorIds.addAll(batchIU.getAuthorIds());
        }
    }

    @Override
    public synchronized void parseAll(String path) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        parseEntity(authorParser, path, "lib.libavtorname.sql.gz");
        parseEntity(bookParser, path, "lib.libbook.sql.gz");
        parseEntity(sequenceParser, path, "lib.libseqname.sql.gz");
        addRelations(path, "lib.libavtor.sql.gz", Book.class, new EntityField(Book.class,"authors"), Author.class);
        deleteOrphans();//TODO bad idea
        parseEntity(bookSequenceParser, path, "lib.libseq.sql.gz");
        parseEntity(bookFileNameParser, path, "lib.libfilename.sql.gz");
        parseEntity(authorAnnotation, path, "lib.a.annotations.sql.gz");
        parseEntity(bookAnnotation, path, "lib.b.annotations.sql.gz");
        await(_finishLatch);
        flush();
        log.info("Finished in {} ms",(System.currentTimeMillis() - currentTimeMillis));
    }

    private void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
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
    public void parseEntity(final EntityParser entityParser, final String path, final String filename) throws IOException {
        new Thread() {
            @Override
            public void run() {
                try {
                    long currentTimeMillis = System.currentTimeMillis();
                    BatchMethod<?> batchMethod = _batchMethodMap.get(entityParser.getClazz());
                    Thread.currentThread().setName("obj" + entityParser.getClazz().getName());
                    batchMethod.method(_batchIU, new FileEntityIterator(entityParser, path, filename, null));
                    log.info("time for {} is {}", entityParser.getClazz().getName(), (System.currentTimeMillis() - currentTimeMillis));
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }

            }
        }.start();
    }


    @Override
    protected void addRelations(final String path, final String file, final Class main, final EntityField field, final Class secondary) throws IOException {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().setName("obj relations");
                    if (main.equals(Book.class) && secondary.equals(Author.class)) {
                        await(_bookAuthor);
                        FileEntityIterator<AuthorBookRelation> abRelations = new FileEntityIterator<>(new EntityParser<>(AuthorBookRelation.class, "book", "author"), path, file, null);
                        abRelations.setValidator(new Validator<AuthorBookRelation>() {
                            @Override
                            public boolean isValid(AuthorBookRelation abRelation) {
                                return bookIds.contains(abRelation.getBook()) && authorIds.contains(abRelation.getAuthor());
                            }
                        });
                        _batchIU.addAuthorBookRelation(abRelations);
                        _bookAuthorRelation.countDown();
                    } else {
                        MultiThreadJDBIParserImpl.super.addRelations(path, file, main, field, secondary);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }.start();
    }

    @Override
    protected void addRelation(Class main, EntityField collectionField, Class secondary, String subLine, String[] split) {
    }

    protected void save(Object entity) {
    }


    @Override
    protected void flush() {
        bookIds.clear();
        sequenceIds.clear();
        authorIds.clear();
    }

    @Override
    protected void deleteOrphans() {
        new Thread() {
            @Override
            public void run() {
                Thread.currentThread().setName("delete orphans");
                await(_bookAuthorRelation);
                int deletedAuthors = _batchIU.deleteEmptyAuthors();
                log.info("{} authors was deleted", deletedAuthors);
                updateAuthorIds(_batchIU);
                _authorlatch.countDown();
                int deletedBooks = _batchIU.deleteEmptyBooks();
                log.info("{} books was deleted", deletedBooks);
                updateBookIds(_batchIU);
                _bookLatch.countDown();
            }
        }.start();
    }


    @Override
    protected EntityManager getEntityManager() {
        return null;
    }


    private static interface BatchMethod<T> {

        public abstract void method(JDBIIU batchIU, FileEntityIterator<T> iterator);
    }

}
