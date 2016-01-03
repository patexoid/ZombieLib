package lib.back.mysqldumpparser;

/**
 * Created by Alexey on 03.01.2016.
 */
public class MysqlParserException extends Exception {

    public MysqlParserException(String message) {
        super(message);
    }

    public MysqlParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public MysqlParserException(Throwable cause) {
        super(cause);
    }
}
