package main;

public class Launcher {

    public static void main(String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("gui")) {
            APP_GUI.launchFx(args);
        } else {
            App.runCli(args);
        }
    }
}

