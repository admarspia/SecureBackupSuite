package backup;


import utils.Logger;
import config.user_config.schedule_config.*;
import java.time.ZonedDateTime;

public class BackupDaemon implements Runnable {

    private final BackupScheduler scheduler;
    private final BackupController controller;

    private volatile boolean running = true;

    public BackupDaemon(BackupScheduler scheduler, BackupController controller) {
        this.scheduler = scheduler;
        this.controller = controller;
    }

    @Override
    public void run() {
        boolean done = false;
        try {
            while (running) {
                
                if (scheduler.isTime() && !done) {

                    controller.runBackup();
                    ConfigService.lastRun = ZonedDateTime.now(ConfigService.zone); 
                    done = true;
                } else {
                    if (done){
                        Thread.sleep(55000);
                        System.out.println("Daemon is alive at " + java.time.LocalTime.now());
                        done = false;

                    }
                    else {
                        Thread.sleep(30000); 
                        System.out.println("Daemon is alive at " + java.time.LocalTime.now());

                    }

                }
            }

        } catch (InterruptedException ex) {

            running = false;
            Logger.log(BackupScheduleConfigModel.Status.FAILED.name(), "backup", ex.getMessage());
            System.out.println("Error interrupted: " + ex);

        } catch (Exception ex){
            Logger.log(BackupScheduleConfigModel.Status.FAILED.name(), "backup", ex.getMessage());

            System.out.println("Error: " + ex);

        }
    }

    public void stop() {
        running = false;
    }
}

