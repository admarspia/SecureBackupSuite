package backup;


import utils.Logger;
import config.user_config.schedule_config.*;
import java.time.ZonedDateTime;

public class BackupDaemon implements Runnable {

    private final BackupScheduler scheduler;
    private final BackupController controller;

    private static volatile boolean running = true;

    public BackupDaemon(BackupScheduler scheduler, BackupController controller) {
        this.scheduler = scheduler;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            while (running) {

                ZonedDateTime nextRun = ConfigService.getNextRuntime();
                long sleepMillis = java.time.Duration
                        .between(ZonedDateTime.now(ConfigService.zone), nextRun)
                        .toMillis();

                if (sleepMillis > 0) {
                    Thread.sleep(sleepMillis);
                }

                if (!running) break;
                
                System.out.println("before backup");
                controller.runBackup();
                System.out.println("Afeter backup");
                ConfigService.lastRun = ZonedDateTime.now(ConfigService.zone);

                Logger.log(
                    BackupScheduleConfigModel.Status.SUCCESS.name(),
                    "backup",
                    "Backup completed at " + ConfigService.lastRun
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.log(
                BackupScheduleConfigModel.Status.FAILED.name(),
                "backup",
                "Daemon interrupted"
            );
        } catch (Exception e) {
            Logger.log(
                BackupScheduleConfigModel.Status.FAILED.name(),
                "backup",
                e.getMessage()
            );
        }
    }

    public static void stop() {
        running = false;
    }
}

