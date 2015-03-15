package lib.back.mysqldumpparser;

import lib.back.dataobj.*;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;

public abstract class Parser {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(Parser.class);

    protected EntityParser<Author> authorParser = new EntityParser<Author>(Author.class,
            "authorId",
            "FirstName",
            "MiddleName",
            "LastName",
            "NickName",
            "NoDonate",
            "uid",
            "WebPay",
            "Email",
            "Homepage",
            "Source",
            "State",
            "SourceId");

    protected EntityParser<Book> bookParser = new EntityParser<Book>(Book.class,
            "BookId",
            "FileSize",
            "Time",
            "Title",
            "Title1",
            "Lang",
            "FileType",
            "Year",
            "Deleted",
            "Ver",
            "FileAuthor",
            "N",
            "keywords",
            "md5",
            "Broken",
            "Modified",
            "Source",
            "State",
            "SourceId");

    protected EntityParser<Sequence> sequenceParser = new EntityParser<Sequence>(Sequence.class,
            "SeqId",
            "SeqName",
            "Source",
            "State",
            "SourceId");

    protected EntityParser<BookSequence> bookSequenceParser = new EntityParser<BookSequence>(BookSequence.class,
            "Book",
            "Sequence",
            "SeqNumb",
            "Level");

    protected EntityParser<BookFileName> bookFileNameParser = new EntityParser<BookFileName>(BookFileName.class,
            "Book",
            "FileName");


    protected EntityParser<AuthorAnnotation> authorAnnotation = new EntityParser<AuthorAnnotation>(AuthorAnnotation.class,
            "id", "Title", "Body");


    protected EntityParser<BookAnnotation> bookAnnotation = new EntityParser<BookAnnotation>(BookAnnotation.class,
            "id", "Title", "Body");

    @PersistenceContext
    private EntityManager _entityManager;


    @Transactional(rollbackFor = {IOException.class})
    public synchronized void parseAll(String path) throws IOException{
        long currentTimeMillis = System.currentTimeMillis();
        parseEntity(authorParser, path, "lib.libavtorname.sql.gz");
        parseEntity(bookParser, path, "lib.libbook.sql.gz");
        parseEntity(sequenceParser, path, "lib.libseqname.sql.gz");
        addRelations(path, "lib.libavtor.sql.gz", Book.class, new EntityField(Book.class, "authors"), Author.class);
        parseEntity(bookSequenceParser, path, "lib.libseq.sql.gz");
        deleteOrphans();
        parseEntity(bookFileNameParser, path, "lib.libfilename.sql.gz");
        parseEntity(authorAnnotation, path, "lib.a.annotations.sql.gz");
        parseEntity(bookAnnotation, path, "lib.b.annotations.sql.gz");
        flush();
        log.info("Finished in {} ms", (System.currentTimeMillis() - currentTimeMillis));

    }

    protected void addRelations(String path, String file, Class main, EntityField field, Class secondary) throws IOException {
        FileIterator fileIterator = new FileIterator(path, file);
        for (String subLine : fileIterator) {
            try {
                addRelation(main, field, secondary, subLine, subLine.split(","));
            } catch (Exception e) {
                log.warn(e.getLocalizedMessage());
            }
        }
    }

    protected <T> void parseEntity(EntityParser<T> entityParser, String path, String filename) throws IOException {
        log.info("started {}", filename);
        long l = System.currentTimeMillis();
        FileEntityIterator fileParser = new FileEntityIterator<T>(entityParser, path, filename, getEntityManager());
        for (Object entity : fileParser) {
            try {
                save(entity);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
        log.info("time for {} is {}", entityParser.getClazz().getName(), (System.currentTimeMillis() - l));
    }

    protected EntityManager getEntityManager() {
        return _entityManager;
    }


    protected abstract void save(Object entity);

    protected abstract void addRelation(Class main, EntityField collectionField, Class secondary, String subLine, String[] split);

    protected abstract void deleteOrphans();

    protected abstract void flush();
}
