package exception.userservice;

public class UsernameExistsException extends Exception {
    public UsernameExistsException() {
        super("username found. Use another username or try Login.");
    }
}
