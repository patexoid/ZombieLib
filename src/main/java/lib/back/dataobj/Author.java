package lib.back.dataobj;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by alex on 26.01.14.
 */
@Entity
public class Author implements Comparable<Author> {

    @Id
    Long authorId;
    String firstName;
    String middleName;
    String lastName;
    String nickName;
    Boolean noDonate;
    Long uid;
    String webPay;
    String email;
    String homepage;
    Short source;
    Short state;
    Long sourceId;


    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "authors")
    @JsonIgnore
    private List<Book> books = new ArrayList<Book>();


    public Author() {
    }

    public Author(String authorName) {
        firstName = authorName;
    }

    public List<Book> getBooks() {
        return books;
    }

    @JsonIgnore
    public List<Sequence> getSeries() {
        TreeSet<Sequence> sequences = new TreeSet<Sequence>();
        for (Book book : books) {
            for (BookSequence bookSequence : book.getSeries()) {
                if (bookSequence.getSequence() != null) {
                    sequences.add(bookSequence.getSequence());
                }
            }
        }
        return new ArrayList<Sequence>(sequences);
    }


    @Override
    public int compareTo(Author o) {

        return firstName.compareTo(o.firstName);
    }

    @Override
    public String toString() {
        return firstName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getNoDonate() {
        return noDonate;
    }

    public void setNoDonate(Boolean noDonate) {
        this.noDonate = noDonate;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getWebPay() {
        return webPay;
    }

    public void setWebPay(String webPay) {
        this.webPay = webPay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
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
