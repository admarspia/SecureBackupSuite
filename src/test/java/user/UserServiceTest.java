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
    private String testUsername = "test2";
    private String testEmail = "testexaple.com";
    private String testPassword = "Password123!";

    @BeforeEach
    void setUp() throws SQLException{
        userService = new UserService();
    }

    @Test
    void testUserCreation() {
        assertDoesNotThrow(() -> {
            userService.createNewUser(testUsername, testEmail, testPassword);
        });
    }

}
