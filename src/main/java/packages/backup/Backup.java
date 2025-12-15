package backup;


import utils.Logger;
import config.user_config.schedule_config.BackupScheduleConfigModel;

public class Backup {
    public static void backup(BackupModel.Type type) {
        if (type == null)
            type = BackupModel.Type.INCREMENTAL;

        try {

        Backupable backupService =  BackupServiceResolver.getService(type);
        BackupScheduler scheduler = new BackupScheduler();
        BackupController controller = new BackupController(backupService);

        Thread daemon = new Thread(new BackupDaemon(scheduler, controller));
        daemon.setDaemon(false); 
        daemon.start();
        daemon.join();

        System.out.println("Backup daemon running...");
        } catch (Exception ex){
            Logger.log(BackupScheduleConfigModel.Status.FAILED.name(), "backup", ex.getMessage());

            System.out.println("Error: form main " + ex);
        }
    }
}

