package lib.back.dataobj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apotekhin on 8/26/2014.
 */
public class AuthorWrapper {

    private final Author _author;
    private final AuthorAnnotation _authorAnnotation;
    private final List<Sequence> _sequences;
    private final List<Book> _books= new ArrayList<Book>();


    public AuthorWrapper(Author author, AuthorAnnotation authorAnnotation) {
        _author = author;
        _authorAnnotation=authorAnnotation;
        _sequences=author.getSeries();
        for (Book book : author.getBooks()) {
            if(book.getSeries().isEmpty()){
                _books.add(book);
            }
        }

    }

    public Author getAuthor() {
        return _author;
    }

    public List<Sequence> getSequences() {
        return _sequences;
    }

    public List<Book> getBooks() {
        return _books;
    }

    public AuthorAnnotation getAuthorAnnotation() {
        return _authorAnnotation;
    }
}
