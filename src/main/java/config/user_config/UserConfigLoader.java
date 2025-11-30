
package config.user_config;

import config.YamlLoader;
import java.time.DayOfWeek;
import java.util.Set;
import java.util.TreeSet;
import config.user_config.schedule_config.*;
import config.user_config.file_config.*;


public class UserConfigLoader extends YamlLoader<RootConfig> {

    public UserConfigLoader() {
        super(RootConfig.class, "config.yaml");
    }

    @Override
    public RootConfig loadDefault() {

        RootConfig root = new RootConfig();

        // for scheduler
        BackupScheduleConfigModel schedule = new BackupScheduleConfigModel();
        schedule.setEnabled(true);
        schedule.setFrequency(BackupScheduleConfigModel.Frequency.WEEKLY);

        Set<DayOfWeek> days = new TreeSet<>();
        days.add(DayOfWeek.MONDAY);
        schedule.setDaysOfWeek(days);

        schedule.setHours(6);
        schedule.setMinutes(0);
        schedule.setTimezone("Africa/Addis_Ababa");
        schedule.setRecoveryPolicy(BackupScheduleConfigModel.Policy.SKIP);

        // for file selector
        BackupFilesConfigModel file = new BackupFilesConfigModel();
        file.setSourcePaths(new TreeSet<>(Set.of("/home/gamma/")));
        file.setIncludePatterns(new TreeSet<>(Set.of("*.db", "*.log", "*.config")));
        file.setExcludePatterns(new TreeSet<>(Set.of("*.tmp", "./cache")));
        file.setFollowSymlinks(false);

        root.setSchedule(schedule);
        root.setFiles(file);

        return root;
    }
}

