package backup;

import java.io.IOException;
import config.user_config.file_config.BackupFileConfigModel;
import config.user_config.storage_config.StorageConfigModel;
import config.user_config.schedule_config.BackupScheduleConfigModel;

public interface Backupable {
    void backup(StorageConfigModel storageType) throws RuntimeException;
}
