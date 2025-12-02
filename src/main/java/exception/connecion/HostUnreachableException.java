package exception.connection;

public class HostUnreachableException extends ConnectionTestException {
    public HostUnreachableException(String host){
        super("unable to reach host: " + host);

    }

    public HostUnreachableException(String host,Exception cause){
        super("unable to reach host: " + host, cause);

    }
}
