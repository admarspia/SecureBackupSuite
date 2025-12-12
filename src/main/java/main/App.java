package main;

import backup.Backup;
import recovery.Recovery;
import utils.manifest.ManifestDisplay;

import utils.EncryptionUtils;
import utils.SessionManager;
import user.*;

public class App {
    public static void main(String[] args) {
        //Backup.backup();
        //Recovery.recover();
        //ManifestDisplay.display();
    try{
        UserService userService = new UserService();
        userService.createNewUser();

    } catch (Exception ex){
        System.out.println(ex);
    }

    }
}

