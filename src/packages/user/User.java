package user;

public class User {
    private String username;
    private String email;
    private String passwordHash;
    private int userID;

    void setUsername(String username) throws InvalidNameException {
        validateUsername(username);
        this.username = username;

    }
    void setEmail(String email) throws InvalidEmailException {
            validateEmail(email)
            this.email = email;

    }

    void setPassword( String password) throws InvalidPasswordException, RunTimeException{
        validatePassword(password);
        this.password = EncryptionUtils.hashPassword(password);
    }

    void serUserID(){
        this.userID = UserService.getUniqueID();
    }
    
}
