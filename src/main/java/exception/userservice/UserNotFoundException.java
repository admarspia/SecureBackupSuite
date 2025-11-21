package exception.userservice;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(){
        super("No associated acount found with the given username.");
    }
}
