package user;

import java.util.Scanner;

public class UserUI {
    private static Scanner input = new Scanner(System.in);

    public static String receiveUsername() {
        boolean failed = false;
        do {
            try {

                String username;
                System.out.print("Enter username: ");
                username = input.nextLine();

                UserController.validateUsername(username);
                return username;


            } catch (InvalidUsernameException ex){
                System.out.println(ex);
                failed = true;

            } catch (UsernameExistsException ex){
                System.out.println(ex);
                failed = true;
            } 

        } while ( failed );
    }

    public static String receiveEmail() {
        boolean failed = false;
        do {
            try {
                String email;

                System.out.print("Enter email: ");
                email = input.nextLine();


                UserController.validateEmail(email);
                return email;

            } catch (InvalidEmailFormatException ex){
                System.out.println(ex);
                failed = true;

            } catch ( EmailExistsException ex) {
                System.out.println(ex);
                failed = true;
            }

        } while ( failed );
    }

    public static String receivePassword() {
        boolean failed = false;

        do {
            try {
                String password;
                Console console = System.console();

                System.out.print("Enter password: ");

                if (console != null){
                    char[] pw = console.readPassword();
                    password = new String(pw);
                } else 
                    password = input.nextLine();


                UserController.validatePassword(password);
                
                return password;
            } catch (InvalidPasswordException ex){
                System.out.println(ex);
                failed = true;
            }
        } while (failed);


    }
}
