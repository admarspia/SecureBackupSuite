package backup;


public class BackupController<T extends Backupable> {

    private final <T> backupService;

    public BackupController(<T> backupService) {
        this.backupService = backupService;
    }

    public void runBackup() {
        backupService.backup();
    }
}

