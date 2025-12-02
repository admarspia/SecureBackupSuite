package exception.connection;

public class ConnectionTestException extends Exception {
    public ConnectionTestException(String msg, Exception cause) {
        super(msg, cause);
    }

    public ConnectionTestException(String msg) {
        super(msg);
    }
}
