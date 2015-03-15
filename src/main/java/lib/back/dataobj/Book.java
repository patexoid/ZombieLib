package lib.back.dataobj;

import lib.back.mysqldumpparser.SValid;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 26.01.14.
 */

@Entity
public class Book implements Comparable<Book>, SValid{

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @JoinTable(
            name = "AUTHOR_BOOK",
            joinColumns = @JoinColumn(name = "bookId"),
            inverseJoinColumns = @JoinColumn(name = "authorId"))
    private List<Author> authors = new ArrayList<Author>();

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    @OrderBy("SeqNumb")
    private List<BookSequence> series = new ArrayList<BookSequence>();

/*
    @OneToOne
    BookFileName _bookFileName;
*/


    @Id
    Long bookId;
    Long fileSize;
    Date time;
    String title;
    String title1;
    String lang;
    String fileType;
    Short year;
    Boolean deleted;
    String ver;
    String fileAuthor;
    Long n;
    String keywords;
    String md5;
    Boolean broken;
    Date modified;
    Short source;
    Short state;
    Long sourceId;

    public Book() {
    }

    public Book(String bookName) {
        title = bookName;
    }


    public List<Author> getAuthors() {
        return authors;
    }

    public List<BookSequence> getSeries() {
        return series;
    }

    @Override
    public int compareTo(Book o) {
        assert o != null;
        return title.compareTo(o.title);
    }

    @Override
    public String toString() {
        return title;
    }
/*

    public BookFileName getBookFileName() {
        return _bookFileName;
    }

    public void setBookFileName(BookFileName bookFileName) {
        _bookFileName = bookFileName;
    }

*/
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getFileAuthor() {
        return fileAuthor;
    }

    public void setFileAuthor(String fileAuthor) {
        this.fileAuthor = fileAuthor;
    }

    public Long getN() {
        return n;
    }

    public void setN(Long n) {
        this.n = n;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Boolean getBroken() {
        return broken;
    }

    public void setBroken(Boolean broken) {
        this.broken = broken;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Short getSource() {
        return source;
    }

    public void setSource(Short source) {
        this.source = source;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public boolean isValid() {
        return !deleted;
    }
}
