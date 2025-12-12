package backup;


public class BackupServiceResolver {

    public static BackupServiceResolver getHandler(BackupModel.Type type) {
        switch (type) {
            case FULL:
                return new FullBackupService();
            case INCREMENTAL:
                return new IncrementalBackupService();
            case PREDICTIVE:
                return new PredictiveBackupService();
            default:
                throw new UnsupportedOperationException(
                        "Unsupported Operation: " + config.getType()
                );
        }
    }
}

