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


}
