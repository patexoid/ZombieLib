package lib.back.mysqldumpparser.desc;

import lib.back.dataobj.Book;
import lib.back.mysqldumpparser.SortOrder;

import javax.persistence.OneToOne;

/**
 * Created by Alexey on 03.01.2016.
 */
public class BookFileNameDesc {

    @SortOrder(0)
    Long book;

    @SortOrder(1)
    String fileName;


    public Long getBook() {
        return book;
    }

    public void setBook(Long book) {
        this.book = book;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
