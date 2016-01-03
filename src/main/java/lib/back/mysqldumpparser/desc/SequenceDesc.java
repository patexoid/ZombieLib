package lib.back.mysqldumpparser.desc;

import lib.back.mysqldumpparser.SortOrder;

/**
 * Created by Alexey on 03.01.2016.
 */
public class SequenceDesc {

    @SortOrder(0)
    Long seqId;

    @SortOrder(1)
    String seqName;
    @SortOrder(2)
    Short source;

    @SortOrder(3)
    Short state;

    @SortOrder(4)
    Long sourceId;

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
