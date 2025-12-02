package exception.connection;

public class AuthFailedException extends ConnectionTestException {
    public AuthFailedException(String msg){
        super(msg);

    }

    public AuthFailedException(String msg, Exception cause){
        super(msg, cause);

    }
}
