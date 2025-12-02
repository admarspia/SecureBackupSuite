package exception.connection;

public class ShareNotFoundException extends ConnectionTestException {
    public ShareNotFoundException(String msg, Exception cause){
        super(msg, cause);

    }
}
