package lib.back.dataobj.repository.jdbi;

import lib.back.dataobj.*;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by apotekhin on 9/25/2014.
 */
public interface JDBIIU {
    int CHUNK_SIZE = 5000;
    String mergeAuthor = "merge into AUTHOR ( authorId,    firstName,      middleName,     lastName,       nickName,       noDonate,       uid,    webPay,    email,       homepage,    source,    state,      sourceId)" +
            "values(    :a.authorId, :a.firstName,   :a.middleName,  :a.lastName,    :a.nickName,    :a.noDonate,    :a.uid, :a.webPay, :a.email,    :a.homepage, :a.source, :a.state,   :a.sourceId)";
//    String mergeAuthor = "MERGE INTO  author USING (SELECT * FROM (VALUES ('X'))) ON  authorId=:a.authorId" +
//            " WHEN MATCHED THEN" +
//            " UPDATE SET firstName=:a.firstName,   middleName=:a.middleName,  lastName=:a.lastName,    nickName=:a.nickName,    noDonate=:a.noDonate,    uid=:a.uid, webPay=:a.webPay, email=:a.email,    homepage=:a.homepage, source=:a.source, state=:a.state,   sourceId=:a.sourceId" +
//            " WHEN NOT MATCHED THEN" +
//            " INSERT(       authorId,    firstName,      middleName,     lastName,       nickName,       noDonate,       uid,    webPay,    email,       homepage,    source,    state,      sourceId)" +
//            " VALUES(    :a.authorId, :a.firstName,   :a.middleName,  :a.lastName,    :a.nickName,    :a.noDonate,    :a.uid, :a.webPay, :a.email,    :a.homepage, :a.source, :a.state,   :a.sourceId)";

    String merge_book = "merge into BOOK (bookId, fileSize, time, title, title1, lang, fileType, year, deleted, ver, fileAuthor, n, keywords, md5, broken, modified, source, state, sourceId)" +
            "values ( :b.bookId, :b.fileSize, :b.time, :b.title, :b.title1, :b.lang, :b.fileType, :b.year, :b.deleted, :b.ver, :b.fileAuthor, :b.n, :b.keywords, :b.md5, :b.broken, :b.modified, :b.source, :b.state, :b.sourceId)";
//    String merge_book = "MERGE INTO  book USING (SELECT * FROM (VALUES ('X'))) ON  bookId=:b.bookId" +
//            " WHEN MATCHED THEN" +
//            " UPDATE SET fileSize=:b.fileSize, time=:b.time, title=:b.title, title1=:b.title1, lang=:b.lang, fileType=:b.fileType, year=:b.year, deleted=:b.deleted, ver=:b.ver, fileAuthor=:b.fileAuthor, n=:b.n, keywords=:b.keywords, md5=:b.md5, broken=:b.broken, modified=:b.modified, source=:b.source, state=:b.state, sourceId=:b.sourceId" +
//            " WHEN NOT MATCHED THEN" +
//            " INSERT(bookId, fileSize, time, title, title1, lang, fileType, year, deleted, ver, fileAuthor, n, keywords, md5, broken, modified, source, state, sourceId)" +
//            " VALUES( :b.bookId, :b.fileSize, :b.time, :b.title, :b.title1, :b.lang, :b.fileType, :b.year, :b.deleted, :b.ver, :b.fileAuthor, :b.n, :b.keywords, :b.md5, :b.broken, :b.modified, :b.source, :b.state, :b.sourceId)";


    String merge_sequence = "merge into SEQUENCE(seqId, seqName, source, state, sourceId) " +
            "values (:s.seqId, :s.seqName, :s.source, :s.state, :s.sourceId)";

//    String merge_sequence = "MERGE INTO  sequence USING (SELECT * FROM (VALUES ('X'))) ON  seqId=:s.seqId " +
//            " WHEN MATCHED THEN" +
//            " UPDATE SET seqId=:s.seqId, seqName=:s.seqName, source=:s.source, state=:s.state, sourceId=:s.sourceId" +
//            " WHEN NOT MATCHED THEN" +
//            " INSERT (seqId, seqName, source, state, sourceId) VALUES (:s.seqId, :s.seqName, :s.source, :s.state, :s.sourceId)";


    String inser_bookSequence = "insert into BOOKSEQUENCE(book_bookid, sequence_seqid, seqNumb, level) " +
            "values (:bs.book.bookId, :bs.sequence.seqId, :bs.seqNumb, :bs.level)";

    String insert_bookFileName = "insert into BOOKFILENAME(book_bookId, fileName) " +
            "values (:bf.book.bookId, :bf.fileName)";


    String merge_authorAnotation = "merge into AUTHORANNOTATION(id,title,body) " +
            "values (:aa.id, :aa.title, :aa.body)";

    //    String merge_authorAnotation =
//            "MERGE INTO  authorAnnotation USING (SELECT * FROM (VALUES ('X'))) ON  id=:aa.id" +
//                    " WHEN MATCHED THEN" +
//                    " UPDATE SET id=:aa.id, title=:aa.title, body=:aa.body" +
//                    " WHEN NOT MATCHED THEN" +
//                    " INSERT(id,title,body) " +
//                    " VALUES (:aa.id, :aa.title, :aa.body)";
    String merge_bookAnnotation = "merge into BOOKANNOTATION(id,title,body) " +
            "values (:ba.id, :ba.title, :ba.body)";

//    String merge_bookAnnotation =
//            "MERGE INTO  bookAnnotation USING (SELECT * FROM (VALUES ('X'))) ON  id=:ba.id" +
//                    " WHEN MATCHED THEN" +
//                    " UPDATE SET id=:ba.id, title=:ba.title, body=:ba.body" +
//                    " WHEN NOT MATCHED THEN" +
//                    " INSERT(id,title,body) " +
//                    " VALUES (:ba.id, :ba.title, :ba.body)";


    @SqlUpdate(mergeAuthor)
    public int insertAuthor(@BestBindBean("a") Author a);


    @SqlBatch(mergeAuthor)
    @BatchChunkSize(CHUNK_SIZE)
    public void insertAuthor(@BestBindBean("a") Iterator<Author> a);


    @SqlUpdate(merge_book)
    public int insertBook(@BestBindBean("b") Book book);

    @SqlBatch(merge_book)
    @BatchChunkSize(CHUNK_SIZE)
    public void insertBook(@BestBindBean("b") Iterator<Book> book);


    @SqlUpdate(merge_sequence)
    public int insertSequence(@BestBindBean("s") Sequence sequence);


    @SqlBatch(merge_sequence)
    @BatchChunkSize(CHUNK_SIZE)
    public void insertSequence(@BestBindBean("s") Iterator<Sequence> sequence);


    @SqlUpdate(inser_bookSequence)
    public int insertBookSequence(@BestBindBean("bs") BookSequence sequence);

    @SqlBatch(inser_bookSequence)
    @BatchChunkSize(CHUNK_SIZE)
    public void insertBookSequence(@BestBindBean("bs") Iterator<BookSequence> sequence);

/*
    @SqlUpdate(insert_bookFileName)
    public int insertBookFileName(@BestBindBean("bf") BookFileName bookFileName);

    @SqlBatch(insert_bookFileName)
    @BatchChunkSize(CHUNK_SIZE)
    public void insertBookFileName(@BestBindBean("bf") Iterator<BookFileName> bookFileName);


    @SqlUpdate(merge_authorAnotation)
    public int insertAuthorAnnotation(@BestBindBean("aa") AuthorAnnotation authorAnnotation);

    @SqlBatch(merge_authorAnotation)
    @BatchChunkSize(CHUNK_SIZE)
    public void insertAuthorAnnotation(@BestBindBean("aa") Iterator<AuthorAnnotation> authorAnnotation);


    @SqlUpdate(merge_bookAnnotation)
    public int insertBookAnnotation(@BestBindBean("ba") BookAnnotation authorAnnotation);

    @SqlBatch(merge_bookAnnotation)
    @BatchChunkSize(CHUNK_SIZE)
    public void insertBookAnnotation(@BestBindBean("ba") Iterator<BookAnnotation> authorAnnotation);
*/
    @SqlBatch("insert into AUTHOR_BOOK (authorId, bookId) values (:ab.author,:ab.book)")
    @BatchChunkSize(CHUNK_SIZE)
    public void addAuthorBookRelation(@BestBindBean("ab") Iterator<AuthorBookRelation> ab);


    @SqlUpdate("insert into AUTHOR_BOOK (authorId, bookId) values (:a,:b)")
    public int addAuthorBookRelation(@Bind("a") Long a, @Bind("b") Long b);


    @SqlQuery("SELECT authorId from AUTHOR")
    public Set<Long> getAuthorIds();

    @SqlQuery("SELECT seqId from SEQUENCE")
    public Set<Long> getSequenceIds();


    @SqlQuery("SELECT bookId from BOOK")
    public Set<Long> getBookIds();


    @SqlUpdate("DELETE FROM AUTHOR WHERE  authorId not IN (SELECT DISTINCT authorId FROM AUTHOR_BOOK)")
    public int deleteEmptyAuthors();

    @SqlUpdate("DELETE FROM BOOK WHERE  bookId not IN (SELECT DISTINCT bookId FROM AUTHOR_BOOK)")
    public int deleteEmptyBooks();
}
