package exception.userservice;

public class InvalidEmailFormatException extends Exception {
    public InvalidEmailFormatException() {
        super("Invalid Email format.");
    }
}
