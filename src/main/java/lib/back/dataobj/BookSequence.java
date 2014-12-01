package lib.back.dataobj;

import lib.back.dataobj.mysqldumpparser.SValid;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by alex on 31.01.14.
 */
@Entity
public class BookSequence implements SValid {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    Book book;

    @JsonIgnore
    @ManyToOne
    Sequence sequence;

    Long seqNumb;
    Short level;

    public BookSequence() {
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

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public Long getSeqNumb() {
        return seqNumb;
    }

    public void setSeqNumb(Long seqNumb) {
        this.seqNumb = seqNumb;
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return book.getTitle();
    }

    @Override
    public boolean isValid() {
        return book!=null&&sequence!=null;
    }
}
