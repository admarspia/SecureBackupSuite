package user;

import exception.userservice.*;
import utils.Logger;
import java.sql.SQLException;


public class UserController {
    private static UserService userService;

    static { // bc static runs when the class is loadded.
        try {
            userService = new UserService();
        } catch ( SQLException ex){
            ex.printStackTrace();
            throw new RuntimeException("Failed to Initialize UserService.");
        }
    }

    public static void validateCredentials( boolean matched ) throws InvalidCredentialsException {
        try {
            if (!matched) throw new InvalidCredentialsException();

        } catch ( InvalidCredentialsException ex) {
            Logger.log("Error", "userservice", ex.toString());
            throw ex;
        }

    }

    public static void validateUsername( String username) throws InvalidUsernameException, SQLException, UsernameExistsException {
        try {
            if (username == null || username.length() < 3 || username.length() > 30) 
                throw new InvalidUsernameException();

            if (userService.getUser(username) != null)
                throw new UsernameExistsException();

        } catch (InvalidUsernameException  ex){
                Logger.log("Error", "userservice", ex.toString());
                throw ex;
        } catch (UsernameExistsException ex){
            Logger.log("Error", "userservice", ex.toString());
            throw ex;
        }

    }

    public static void validateEmail( String email ) throws InvalidEmailFormatException, SQLException, EmailExistsException {
        try {
            String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

            if (!email.matches(emailPattern)) 
                throw new InvalidEmailFormatException();

            if (userService.getUserByEmail(email) != null)
                throw new EmailExistsException();


        }catch (InvalidEmailFormatException ex) {
            Logger.log("Error", "userservice", ex.toString());
            throw ex;
        }catch (EmailExistsException ex){
            Logger.log("Error", "userservice", ex.toString());
            throw ex;
        }

    }

    public static void validatePassword( String password ) throws InvalidPasswordException {
        try  {
            if (password == null || password.length() < 8 || password.length() > 256 )
                throw new InvalidPasswordException();

        } catch (InvalidPasswordException ex){
                Logger.log("Error", "userservice", ex.toString());
                throw ex;

        }

    }
 
}
