package lib.back.dataobj;

/**
* Created by apotekhin on 10/20/2014.
*/
public class AuthorBookRelation {
    Long author;
    Long book;

    public AuthorBookRelation() {
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public Long getBook() {
        return book;
    }

    public void setBook(Long book) {
        this.book = book;
    }
}
