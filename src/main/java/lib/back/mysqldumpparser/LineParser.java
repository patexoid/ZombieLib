package lib.back.mysqldumpparser;

import java.util.Iterator;

/**
 * Created by apotekhin on 10/6/2014.
 */
public class LineParser implements Iterator<String>, Iterable<String> {

    final private String line;
    final private char[] lineChars;
    private int openBrIndex;

    public LineParser(String line) {
        this.line = line;
        lineChars=line.toCharArray();
        openBrIndex = line.indexOf('(');
    }

    @Override
    public boolean hasNext() {
        return openBrIndex != -1;
    }

    @Override
    public String next() {
        int closeBrIndex = findCloseBr(openBrIndex);
        String subLine = new String(lineChars, openBrIndex + 1, closeBrIndex-(openBrIndex + 1));// line.substring(openBrIndex + 1, closeBrIndex);
        openBrIndex = findOpenBr(closeBrIndex);//line.indexOf('(', closeBrIndex);
        return subLine;
    }

    private int findOpenBr(int closeBrIndex){
        for (int i = closeBrIndex; i < lineChars.length; i++) {
            if (lineChars[i] == '(') {
                return i;
            }
        }
        return -1;

    }

    private int findCloseBr(int openBrIndex) {
        boolean isString = false;
        for (int i = openBrIndex + 1; i < lineChars.length; i++) {
            //need to avoid 'bla bla \' bla' and 'bla bla \\'
            if ((lineChars[i] == '\'' && !isEscaped(i))) {
                isString = !isString;
            }
            if (!isString && lineChars[i] == ')') {
                return i;
            }
        }
        return -1;
    }

    public boolean isEscaped(int index) {
        char previous = lineChars[index - 1];
        return previous == '\\' && !isEscaped(index - 1);
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
