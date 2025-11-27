package user;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import utils.EncryptionUtils;
import utils.SessionManager;
import exception.userservice.*;

import java.io.IOException;
import java.sql.SQLException;

public class UserServiceTest {

    private UserService userService;
    private String testUsername = "Abrham";
    private String testEmail = "test@exaple.com";
    private String testPassword = "Password123!";

    // @BeforeEach
    // void setUp() throws SQLException{
    //     userService = new UserService();
    // }
    //
   //  @Test
   // void testUserCreation() {
   //     assertDoesNotThrow(() -> {
   //         userService.createNewUser(testUsername, testEmail, testPassword);
   //     });
   // }
   //
    //
    // @Test
    // void testLogin(){
    //     assertDoesNotThrow(()-> {
    //         userService.userLoggin(testUsername, testPassword);
    //    });
    // }
    // //
    // 
    //
    // @Test
    // void testGetUserByUsername() {
    //     assertDoesNotThrow(()-> {
    //         userService.getUser(testUsername);
    //     });
    // }
    // 
    // @Test
    // void testGetUserByEmail() {
    //     assertDoesNotThrow(()-> {
    //         userService.getUserByEmail(testEmail);
    //     });
    // }

    // @Test 
    // void testRemoveUser() {
    //     assertDoesNotThrow(() -> {
    //         userService.removeUser();
    //     });
    // }

    
    //
    // @Test void  testUpdateUsername(){
    //     assertDoesNotThrow(() -> {
    //         userService.updateUsername("Abrham");
    //     });
    // }
    //
    // @Test void testUpdateEmail(){
    //     assertDoesNotThrow(() -> {
    //         userService.updateEmail("admarspis@gmail.com");
    //     });
    // }
    //
    // @Test void  testUpdatePassword(){
    //     assertDoesNotThrow(() -> {
    //         userService.updatePassword("Password12");
    //     });
    // }
    //
    //
    //
    // @Test void  testLogout(){
    //     assertDoesNotThrow(() -> {
    //         userService.logout();
    //     });
    // }
    //



}
