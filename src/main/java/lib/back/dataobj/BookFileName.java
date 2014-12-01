package lib.back.dataobj;

import lib.back.dataobj.mysqldumpparser.SValid;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by alex on 02.02.14.
 */
@Entity
public class BookFileName  implements SValid {


    @Id
    @GeneratedValue
    Long id;

//    @OneToOne(mappedBy = "_bookFileName")
    @OneToOne(optional = false)
    Book book;

    String fileName;


    public BookFileName() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean isValid() {
        return book!=null;
    }
}
