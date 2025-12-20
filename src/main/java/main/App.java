package main;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import backup.*;
import recovery.*;
import utils.manifest.ManifestDisplay;
import utils.SessionManager;
import user.*;
import utils.Logger;
import exception.userservice.*;

public class App {

    public static void main(String[] args) {

        Welcome.display();

        if (args.length == 0) {
            Help.display();
            return;
        }

        String command = args[0];

        try {
            switch (command) {

                case "init":
                    init();
                    break;

                case "user":
                    handleUser(args);
                    break;

                case "backup":
                    handleBackup(args);
                    break;

                case "recover":
                    handleRecovery(args);
                    break;

                case "status":
                    ManifestDisplay.display();
                    break;

                case "help":
                case "--help":
                    Help.display();
                    break;

                default:
                    System.out.println("Unknown command: " + command);
                    Help.display();
            }

        } catch(UserNotFoundException e) {
            System.out.println(e.getMessage());
            Logger.log("ERROR","user-service", e.getMessage());
        } catch (SQLException e) {
            printSqlError(e);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            Logger.log("ERROR", "io", e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            Logger.log("ERROR", "general", e.getMessage());
        } finally {
            try {
            if (SessionManager.who() != null) {
                SessionManager.endSession();
            }
            } catch (FileNotFoundException e){
                Logger.log("ERROR", "session", e.getMessage());
            } catch (IOException e){
                Logger.log("ERROR", "session", e.getMessage());
            }

        }
    }


    private static void init() throws SQLException {
         new UserService() ;
        System.out.println("SecureBackupSuite initialized.");
    }


    private static void handleUser(String[] args)
            throws SQLException, IOException, UserNotFoundException {

        if (args.length < 2) {
            System.out.println("Missing user command.");
            return;
        }

        UserService userService = new UserService();

        if ("update".equals(args[1])) {
            for (int i = 2; i < args.length; i++) {
                switch (args[i]) {
                    case "-c":
                        userService.createNewUser();
                        return;
                    case "-u":
                        userService.updateUsername(args[++i]);
                        break;
                    case "-p":
                        userService.updatePassword(args[++i]);
                        break;
                    case "-e":
                        userService.updateEmail(args[++i]);
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Invalid option: " + args[i]);
                }
            }
        } else {
            throw new IllegalArgumentException(
                    "Unknown user command: " + args[1]);
        }
    }


    private static void handleBackup(String[] args)
            throws Exception {

        if (args.length < 2) {
            System.out.println("Missing backup command.");
            return;
        }

        ensureLogin();

        Backupable service = null;

        switch (args[1]) {

            case "full":
                Backup.backup(
                        BackupModel.Type.FULL);
                service.backup();
                break;

            case "incremental":
                service = BackupServiceResolver.getService(
                        BackupModel.Type.INCREMENTAL);
                service.backup();
                break;

            case "stop":
                BackupDaemon.stop();
                System.out.println("Backup stopped.");
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid backup command: " + args[1]);
        }
    }


    private static void handleRecovery(String[] args)
            throws Exception {

        if (args.length < 2) {
            System.out.println("Missing recovery command.");
            return;
        }

        ensureLogin();

        Recoveryable service;

        switch (args[1]) {

            case "full":
                service = RecoveryServiceResolver.getService(
                        RecoveryModel.Type.FULL);
                service.recover();
                break;

            case "selective":
                service = RecoveryServiceResolver.getService(
                        RecoveryModel.Type.SELECTIVE);
                service.recover();
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid recovery command: " + args[1]);
        }
    }


    private static void ensureLogin()
            throws IOException , SQLException , UserNotFoundException ,InvalidCredentialsException {
        if (SessionManager.who() == null) {
            String username = user.UserUI.takeUsername();
            UserService userService = new UserService();

            userService.userLoggin(username);
            SessionManager.startSession(username);
        }
    }


    private static void printSqlError(SQLException e) {

        System.err.println("Database error occurred:");

        SQLException ex = e;
        while (ex != null) {
            System.err.println("Message   : " + ex.getMessage());
            System.err.println("SQLState  : " + ex.getSQLState());
            System.err.println("ErrorCode : " + ex.getErrorCode());
            System.err.println("----------------------------------");

            Logger.log(
                "SQL_ERROR",
                "database",
                "Message=" + ex.getMessage() +
                ", SQLState=" + ex.getSQLState() +
                ", Code=" + ex.getErrorCode()
            );

            ex = ex.getNextException();
        }
    }
}

