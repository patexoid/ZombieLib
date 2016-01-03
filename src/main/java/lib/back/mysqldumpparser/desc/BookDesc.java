package lib.back.mysqldumpparser.desc;

import lib.back.mysqldumpparser.SkipField;
import lib.back.mysqldumpparser.SortOrder;

import java.util.Date;

/**
 * Created by Alexey on 03.01.2016.
 */
public class BookDesc {

    @SortOrder(0)
    Long bookId;

    @SortOrder(1)
    Long fileSize;

    @SortOrder(2)
    Date time;

    @SortOrder(3)
    String title;

    @SortOrder(4)
    String title1;

    @SortOrder(5)
    @SkipField
    String lang;

    @SortOrder(6)
    String fileType;

    @SortOrder(7)
    Short year;

    @SortOrder(8)
    Boolean deleted;

    @SortOrder(9)
    @SkipField
    String ver;

    @SortOrder(10)
    @SkipField
    String fileAuthor;

    @SortOrder(11)
    @SkipField
    Long n;

    @SortOrder(12)
    @SkipField
    String keywords;

    @SortOrder(13)
    @SkipField
    String md5;

    @SortOrder(14)
    @SkipField
    Boolean broken;

    @SortOrder(15)
    Date modified;

    @SortOrder(16)
    @SkipField
    Short source;

    @SortOrder(17)
    @SkipField
    Short state;

    @SortOrder(18)
    @SkipField
    Long sourceId;

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
}
