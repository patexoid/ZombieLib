package lib.back.mysqldumpparser.desc;

import lib.back.dataobj.Book;
import lib.back.dataobj.Sequence;
import lib.back.mysqldumpparser.SortOrder;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Alexey on 03.01.2016.
 */
public class BookSequenceDesc {

    @SortOrder(0)
    Long book;

    @SortOrder(1)
    Long sequence;

    @SortOrder(2)
    Long seqNumb;

    @SortOrder(3)
    Short level;

    public Long getBook() {
        return book;
    }

    public void setBook(Long book) {
        this.book = book;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
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
}
