package lib.back.mysqldumpparser;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;

/**
 * Created by apotekhin on 10/6/2014.
 */
public class FileIterator implements Iterator<String>, Iterable<String> {
    private static Logger log = LoggerFactory.getLogger(EntityField.class);
    BufferedReader bufferedReader;
    LineParser _lineParser;
    public FileIterator(String path, String file) throws IOException {
        bufferedReader = openFile(new File(path, file));
        _lineParser=getLineParser();
    }

    @Override
    public boolean hasNext() {
        return _lineParser!=null&& _lineParser.hasNext();
    }

    @Override
    public String next() {
        String next = _lineParser.next();
        if(!_lineParser.hasNext()){
            try {
                _lineParser=getLineParser();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return next;
    }

    public LineParser getLineParser() throws IOException {
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                return null;
            }
            if (line.length() > 20 && line.substring(0, 20).toUpperCase().contains("INSERT INTO")) {
                return new LineParser(line);

            }

        }
    }


    private BufferedReader openFile(File file) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        BufferedInputStream in = new BufferedInputStream(fin,8192*10);
        GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
        return new BufferedReader(new InputStreamReader(gzIn, "utf8"));
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }
}
