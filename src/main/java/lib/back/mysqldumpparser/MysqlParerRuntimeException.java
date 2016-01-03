package lib.back.mysqldumpparser;

/**
 * Created by Alexey on 03.01.2016.
 */
public class MysqlParerRuntimeException extends RuntimeException {

    public MysqlParerRuntimeException() {
    }

    public MysqlParerRuntimeException(String message) {
        super(message);
    }

    public MysqlParerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MysqlParerRuntimeException(Throwable cause) {
        super(cause);
    }
}
