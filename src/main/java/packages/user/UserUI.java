package user;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.sql.SQLException;

import exception.userservice.*;
import utils.manifest.ManifestDisplay;
import recovery.SelectiveRecoveryService;

public class UserUI {
    private static Scanner input = new Scanner(System.in);
    private static Map<String, String> selected = new HashMap<>();

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

    public static void receiveTargetFilename(){
        ManifestDisplay.display();
        
        while (true){
            System.out.print("Enter file name to recover: ");
            String filename;
            filename = input.nextLine();
            System.out.print("Enter timestamp: ");
            String at = input.nextLine();

            selected.put(filename, at);
            String confirm;

            System.out.print("More files?y/n: ");
            confirm = input.nextLine();

            if (confirm.equals("y") || confirm.equals("Y"))
                continue;
            else break;

        }

        SelectiveRecoveryService.setTarget(selected);

    }



}

