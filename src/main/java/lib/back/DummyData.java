package lib.back;

import lib.back.dataobj.Author;
import lib.back.dataobj.Book;
import lib.back.dataobj.Sequence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 26.01.14.
 */
public class DummyData {

    public static List<Author> getDummy(){
        List<Author> authors = new ArrayList<Author>();

        Author second = new Author("2");
        second.getBooks().add(new Book("2boo"));
        second.getBooks().add(new Book("2foo"));


        Sequence firstSec = new Sequence("33");
//        firstSec.getBooks().add(new Book("33boo"));
//        firstSec.getBooks().add(new Book("33Foo"));
//        second.getSeries().add(firstSec);

        authors.add(second);

        Author first = new Author("1");
        first.getBooks().add(new Book("boo"));
        first.getBooks().add(new Book("foo"));


        Sequence firstS = new Sequence("11");
///        firstS.getBooks().add(new Book("11boo"));
//        firstS.getBooks().add(new Book("11Foo"));
//        first.getSeries().add(firstS);

        Sequence secondS = new Sequence("22");
//        secondS.getBooks().add(new Book("22boo"));
//        secondS.getBooks().add(new Book("22Foo"));
//        first.getSeries().add(secondS);

        authors.add(first);


        return authors;
    }
}
