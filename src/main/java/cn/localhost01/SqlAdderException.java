package cn.localhost01;

public class SqlAdderException extends Exception {
    public SqlAdderException() {
        super();
    }

    public SqlAdderException(String message) {
        super(message);
    }

    public SqlAdderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlAdderException(Throwable cause) {
        super(cause);
    }
}
