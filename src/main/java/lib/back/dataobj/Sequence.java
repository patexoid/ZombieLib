package lib.back.dataobj;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 26.01.14.
 */
@Entity

public class Sequence implements Comparable<Sequence>{


    @OneToMany(mappedBy = "sequence",fetch = FetchType.EAGER)
    @OrderBy("SeqNumb")
    private List<BookSequence> _books= new ArrayList<BookSequence>();


    @Id
    Long seqId;
    String seqName;
    Short source;
    Short state;
    Long sourceId;

    public Sequence() {
    }

    public Sequence(String name) {
        this.seqName = name;
    }


    public List<BookSequence> getBooks() {
        return _books;
    }

    @Override
    public int compareTo(Sequence o) {
        return seqName.compareTo(seqName);
    }

    @Override
    public String toString() {
        return seqName;
    }

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
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
