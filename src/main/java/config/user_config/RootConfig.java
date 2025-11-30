package config.user_config;

import config.user_config.schedule_config.*;
import config.user_config.file_config.*;
import config.YamlLoader;

public class RootConfig {

    private BackupFilesConfigModel files;
    private BackupScheduleConfigModel schedule;

    public BackupFilesConfigModel getFiles() {
        return files;
    }

    public void setFiles(BackupFilesConfigModel files) {
        this.files = files;
    }

    public BackupScheduleConfigModel getSchedule() {
        return schedule;
    }

    public void setSchedule(BackupScheduleConfigModel schedule) {
        this.schedule = schedule;
    }
}

