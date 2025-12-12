package backup;


public class BackupServiceResolver {

    public static Backupable  getService(BackupModel.Type type) throws Exception {
        try {
            switch (type) {
                case FULL:
                    return new FullBackupService();
                case INCREMENTAL:
                    return new IncrementalBackupService();
                case PREDICTIVE:
                    return new PredictiveBackupService();
                default:
                    throw new UnsupportedOperationException(
                            "Unsupported Operation: " + type
                            );
            }
        } catch (Exception ex){
            throw ex;
        }
    }
}

