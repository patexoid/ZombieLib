package lib.back.fileParser;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 15.03.2015.
 */
public class Fb2ParserTest {

    @Test
    public void testFb2Parser() throws Exception {
        Fb2FileParser fb2FileParser = new Fb2FileParser();
        InputStream resourceAsStream = getClass().getResourceAsStream("/fb2.fb2");
        Fb2BookDescription book = fb2FileParser.parseFile(resourceAsStream);
        assertEquals(book.getAuthor().getFirstName(),"Дмитрий");
        assertEquals(book.getAuthor().getMiddleName(),"Петрович");
        assertEquals(book.getAuthor().getLastName(),"Грибов");
        assertEquals(book.getAuthor().getHomePage(),"http://www.gribuser.ru");
        assertEquals(book.getAuthor().getEmail(),"grib@gribuser.ru");

        List<String> genres=new ArrayList<>();
        genres.add("sf");
        assertEquals(book.getGenres(),genres);

        assertEquals(book.getTitle(),"Тестовый платный документ FictionBook 2.1");

    }
}
