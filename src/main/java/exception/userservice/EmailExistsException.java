package exception.userservice;

public class EmailExistsException extends Exception {
    public EmailExistsException() { // bc in java when exception is to be printed it uses the getMessage() here we we pass message to it
        super("Subscription found. Use another email or try Login.");
    }
}
