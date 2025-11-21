package exception.userservice;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException() {
        super("Password must be 8 charchter or longer.");
    }
}
