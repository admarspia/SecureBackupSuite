package user;

import java.util.Scanner;
import java.io.*;
import java.sql.SQLException;
import exception.userservice.*;

public class UserUI {
    private static Scanner input = new Scanner(System.in);

    public static String receiveUsername() throws SQLException {
        while (true) {
            try {

                System.out.print("Enter username: ");
                String username = input.nextLine();
                System.out.println("After taking input");
                UserController.validateUsername(username);
                return username;
            } catch (InvalidUsernameException | UsernameExistsException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public static String receiveEmail() throws SQLException {
        while (true) {
            try {
                System.out.print("Enter email: ");
                String email = input.nextLine();
                UserController.validateEmail(email);
                return email;
            } catch (InvalidEmailFormatException | EmailExistsException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public static String receivePassword() {
        while (true) {
            try {
                System.out.print("Enter password: ");
                String password;
                Console console = System.console();
                if (console != null) {
                    char[] pw = console.readPassword();
                    password = new String(pw);
                } else {
                    password = input.nextLine();
                }
                UserController.validatePassword(password);
                return password;
            } catch (InvalidPasswordException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}

