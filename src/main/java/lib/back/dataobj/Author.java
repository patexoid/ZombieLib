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
    String name;

    String annotationTitle;

    @Lob
    String body;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "authors")
    @JsonIgnore
    private List<Book> books = new ArrayList<Book>();


    public Author() {
    }

    public Author(String authorName) {
        name = authorName;
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

        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
