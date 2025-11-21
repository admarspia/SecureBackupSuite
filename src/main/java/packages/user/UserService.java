package user;

import exception.userservice.*;
import java.sql.*;
import config.DbConfig;
import utils.*;
import java.io.IOException;


public class UserService {
    private UserModel user;
    private Connection conn;
    
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS users( username TEXT PRIMARY KEY, email TEXT, passwordHash TEXT);";
    private static final String INSERT = "INSERT INTO users(username, email, passwordHash) VALUES (? , ?, ?);";
    private static final String DELETE = "DELETE FROM users WHERE username = ?;";
    private static final String GETPASSWORD = "SELECT passwordHash FROM users WHERE username = ?;";
    private static final String GETEMAIL = "SELECT email FROM users WHERE username = ?;";
    private static final String GETUSER = "SELECT * FROM users WHERE username = ?;";
    private static final String GETUSERBYEMAIL = "SELECT * FROM users WHERE email = ?;";

    private static final String UPDATEUSERNAME = "UPDATE users SET username = ? WHERE username = ?;";
    private static final String UPDATEEMAIL = "UPDATE users SET email = ? WHERE username = ?;";
    private static final String UPDATEPASSWORD = "UPDATE users  SET passwordHash = ? WHERE username = ? ;";


    public UserService() throws SQLException {
        user = new UserModel();
        conn = DbConfig.getConnection("jdbc:sqlite:backup_system.db");
        Statement stmt = conn.createStatement();



        stmt.execute(CREATE);
        stmt.close();

     }

    public void createNewUser() throws SQLException {
        user.setUsername(UserUI.receiveUsername());  
        user.setEmail(UserUI.receiveEmail());
        user.setPassword(UserUI.receivePassword());

        PreparedStatement pstmt = conn.prepareStatement(INSERT);

        pstmt.setString(1,user.getUsername());
        pstmt.setString(2, user.getEmail());
        pstmt.setString(3, user.getPasswordHash());

        pstmt.executeUpdate();
        pstmt.close();

        Logger.log("success", "userservice", "User created successfully.");
    }
    // added for automated testing

    public void createNewUser(String username, String email, String password) throws SQLException, InvalidUsernameException, InvalidEmailFormatException, InvalidPasswordException, UsernameExistsException, EmailExistsException {

        UserController.validateUsername(username);
        UserController.validateEmail(email);
        UserController.validatePassword(password);

        user.setUsername(username);  
        user.setEmail(email);
        user.setPassword(password);


        PreparedStatement pstmt = conn.prepareStatement(INSERT);

        pstmt.setString(1,user.getUsername());
        pstmt.setString(2, user.getEmail());
        pstmt.setString(3, user.getPasswordHash());

        pstmt.executeUpdate();
        pstmt.close();

        Logger.log("success", "userservice", "User created successfully.");
    }

    public void userLoggin() throws  SQLException, UserNotFoundException,  InvalidCredentialsException, IOException {
        user.setUsername(UserUI.receiveUsername());
        String rawPassword = UserUI.receivePassword(); 

        PreparedStatement pstmt = conn.prepareStatement(GETPASSWORD);
        pstmt.setString(1, user.getUsername());

        ResultSet rs = pstmt.executeQuery();

        if (!rs.next()) throw new UserNotFoundException();

        boolean matched = EncryptionUtils.verifyPassword(rawPassword, rs.getString("passwordHash"));

        rs.close();
        pstmt.close();


        UserController.validateCredentials(matched);
            
        Logger.log("success", "userservice", "login successfull.");
        SessionManager.startSession(user.getUsername());   
    }

    public  UserModel getUser( String username ) throws SQLException {
        UserModel user = new UserModel();
        PreparedStatement pstmt = conn.prepareStatement(GETUSER);
        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();

        if (!rs.next()) return null;
        else {
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("passwordHash"));
        pstmt.close();
        rs.close();
            return user;
        }

    }

    public UserModel getUserByEmail( String email )  throws SQLException  {
        UserModel user = new UserModel();
        PreparedStatement pstmt = conn.prepareStatement(GETUSERBYEMAIL);
        pstmt.setString(1, email);

        ResultSet rs = pstmt.executeQuery();

        if (!rs.next()) return null;
        else {
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("passwordHash"));
        pstmt.close();
        rs.close();
            return user;
        }


    }

    public void removeUser() throws SQLException, UserNotFoundException, IOException {
       String username;
       username = SessionManager.who();
       if (username == null) throw new UserNotFoundException();

       PreparedStatement pstmt = conn.prepareStatement(DELETE);
       pstmt.setString(1, username);
       pstmt.executeUpdate();
       
       pstmt.close();
       SessionManager.endSession();
    }
        

    public void updateUsername()  throws SQLException, IOException  {
        user.setUsername(UserUI.receiveUsername()); 

        PreparedStatement pstmt = conn.prepareStatement(UPDATEUSERNAME);

        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, SessionManager.who());

        pstmt.executeUpdate();
        
        pstmt.close();
        Logger.log("success", "userservice", "Username changed successfully.");
    }

    public void updateEmail()  throws SQLException, IOException {
        user.setEmail(UserUI.receiveEmail()); 

        PreparedStatement pstmt = conn.prepareStatement(UPDATEEMAIL);
        pstmt.setString(1, user.getEmail());
        pstmt.setString(2, SessionManager.who());
        pstmt.executeUpdate();
        
        pstmt.close();
        Logger.log("success", "userservice", "Email changed successfully.");
    }

    public void updatePassword()  throws SQLException , IOException {
        user.setPassword(UserUI.receivePassword()); 

        PreparedStatement pstmt = conn.prepareStatement(UPDATEPASSWORD);
        pstmt.setString(1, user.getPasswordHash());
        pstmt.setString(2, SessionManager.who());

        pstmt.executeUpdate();

        pstmt.close();
        Logger.log("success", "userservice", "password changed successfully.");
    }

    public void logout() throws IOException {
        SessionManager.endSession();
    }


}
