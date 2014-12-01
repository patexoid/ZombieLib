package lib.back.load;

import lib.back.dataobj.Book;

import java.io.File;
import java.io.IOException;

/**
 * Created by alex on 02.02.14.
 */
public abstract class Loader {

    protected final String _loadFolder;

    public Loader(String loadFolder) {
        _loadFolder = loadFolder;
    }

    public abstract File load(Book  book, BookType type) throws IOException;


    public static enum BookType{
        fb2, epub,mobi
    }

}
