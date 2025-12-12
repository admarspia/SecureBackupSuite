package backup;


public class BackupController<T extends Backupable> {

    private final T backupService;

    public BackupController(T backupService) {
        this.backupService = backupService;
    }

    public void runBackup() throws Exception {
        try {
        backupService.backup();
        } catch (Exception ex) {
            throw ex;
        }
    }
}

