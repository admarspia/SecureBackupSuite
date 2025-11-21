package user;

import exception.userservice.*;
import utils.EncryptionUtils;

public class UserModel {
    private String username;
    private String email;
    private String passwordHash;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email)  {
            this.email = email;

    }

    public void setPassword( String password) { 
        this.passwordHash = EncryptionUtils.hashPassword(password);

    }
    public void setPasswordHash(String hashedPassword){
        this.passwordHash = hashedPassword;

    }

    public String getUsername(){
        return this.username;

    }

    public String getEmail(){
        return this.email;

    }

    public String getPasswordHash(){
        return this.passwordHash;
    }

}
