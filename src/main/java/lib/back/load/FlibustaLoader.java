package lib.back.load;

import lib.back.dataobj.Book;
import lib.back.dataobj.BookFileName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by alex on 02.02.14.
 */

public class FlibustaLoader extends Loader{


    public FlibustaLoader(String loadFolder) {
        super(loadFolder);
    }

    @Override
    public File load(Book book,BookType type) throws IOException{
        URL url=new URL("http://flibusta.net/b/"+book.getBookId()+"/"+type);
        InputStream is = url.openConnection().getInputStream();
        File file = new File(_loadFolder + getFileName(book));
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[32768];
        while (true){
            int readBytesCount = is.read(buffer);
            if (readBytesCount == -1) {
                break;
            }
            if (readBytesCount > 0) {
                fos .write(buffer, 0, readBytesCount);
            }
        }
        fos.flush();
        fos.close();
        return file;
    }

    private String getFileName(Book book) {
        BookFileName bookFileName = null;//book.getBookFileName();
        String fileName;
        if(bookFileName==null){
            fileName=book.getTitle();
        } else {
            fileName=bookFileName.getFileName();
        }
        return book.getBookId()+"_"+fileName;
    }


}
