package lib.back.dataobj;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 26.01.14.
 */

@Entity
public class Book implements Comparable<Book>{

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
    String lang;
    String fileType;
    Short year;
    String keywords;
    String md5;
    Date modified;

    String annotationTitle;

    @Lob
    String annotationBody;

    String fileName;

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
        return title.compareTo(o.title);
    }

    @Override
    public String toString() {
        return title;
    }

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

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getAnnotationTitle() {
        return annotationTitle;
    }

    public void setAnnotationTitle(String annotationTitle) {
        this.annotationTitle = annotationTitle;
    }

    public String getAnnotationBody() {
        return annotationBody;
    }

    public void setAnnotationBody(String annotationBody) {
        this.annotationBody = annotationBody;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
