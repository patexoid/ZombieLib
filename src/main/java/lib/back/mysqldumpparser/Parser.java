package lib.back.mysqldumpparser;

import lib.back.dataobj.*;
import lib.back.mysqldumpparser.desc.*;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;

public abstract class Parser {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(Parser.class);

//    protected EntityParser<AuthorDesc> authorParser = new EntityParser<AuthorDesc>(AuthorDesc.class);


    @Transactional(rollbackFor = {IOException.class})
    public synchronized void parseAll(String path) throws IOException{
        try {
            long currentTimeMillis = System.currentTimeMillis();
            parseEntity(new EntityParser<>(AuthorDesc.class), path, "lib.libavtorname.sql.gz");
            parseEntity(new EntityParser<>(BookDesc.class), path, "lib.libbook.sql.gz");
            parseEntity(new EntityParser<>(SequenceDesc.class), path, "lib.libseqname.sql.gz");
//            addRelations(path, "lib.libavtor.sql.gz", Book.class, new EntityField(Book.class), Author.class);
            parseEntity(new EntityParser<>(BookSequenceDesc.class), path, "lib.libseq.sql.gz");
            deleteOrphans();
            parseEntity(new EntityParser<>(BookFileNameDesc.class), path, "lib.libfilename.sql.gz");
            parseEntity(new EntityParser<>(AuthorAnnotationDesc.class), path, "lib.a.annotations.sql.gz");
            parseEntity(new EntityParser<>(BookAnnotationDesc.class), path, "lib.b.annotations.sql.gz");
            flush();
            log.info("Finished in {} ms", (System.currentTimeMillis() - currentTimeMillis));
        } catch (MysqlParserException e) {
            throw new IOException(e);
        }

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
        FileEntityIterator fileParser = new FileEntityIterator<>(entityParser, path, filename);
        for (Object entity : fileParser) {
            try {
                save(entity);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
        log.info("time for {} is {}", entityParser.getClazz().getName(), (System.currentTimeMillis() - l));
    }

    protected abstract void save(Object entity);

    protected abstract void addRelation(Class main, EntityField collectionField, Class secondary, String subLine, String[] split);

    protected abstract void deleteOrphans();

    protected abstract void flush();

    public static void main(String[] args) throws IOException {
        new Parser(){
            @Override
            protected void save(Object entity) {

            }

            @Override
            protected void addRelation(Class main, EntityField collectionField, Class secondary, String subLine, String[] split) {

            }

            @Override
            protected void deleteOrphans() {

            }

            @Override
            protected void flush() {

            }
        }.parseAll("C:\\dev\\projects\\ZombieLib\\dump");
    }
}
