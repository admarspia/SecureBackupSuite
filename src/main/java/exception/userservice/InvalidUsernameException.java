package exception.userservice;

public class InvalidUsernameException extends Exception {
    public InvalidUsernameException(){
        super("Invalid username can't be to short or to long.");
    }
}
