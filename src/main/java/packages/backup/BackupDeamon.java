package backup;

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
        try {
            while (running) {
                
                if (scheduler.isTime()) {
                    controller.runBackup();
                } else {
                    Thread.sleep(30000); // sleep 30s
                }
            }

        } catch (InterruptedException ex) {
            running = false;
            throw new RuntimeException(ex);
        }
    }

    public void stop() {
        running = false;
    }
}

