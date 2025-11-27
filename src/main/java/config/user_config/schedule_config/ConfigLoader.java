package config.user_config.schedule_config;

import java.io.*;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;
import utils.SessionManager;
import java.util.Set;
import java.util.TreeSet;
import java.time.DayOfWeek;

public class ConfigLoader {
    private static final String path  = "schedule.yaml";

    public static BackupScheduleConfigModel load() {

        try {

            FileInputStream fis = new FileInputStream(path);

            LoaderOptions options = new LoaderOptions();
            Constructor constructor = new Constructor(BackupScheduleConfigModel.class, options);
            Yaml yaml = new Yaml(constructor);
            BackupScheduleConfigModel model = yaml.load(fis);
            
            System.out.println(path);

            if (model == null) {
                System.out.println("YAML parsed null → using defaults.");
                return loadDefault();
            }
            return model;

        } catch (FileNotFoundException ex){
            System.out.println("Config file not found: " + path);
            return loadDefault();

        } catch (IOException ex) {
            System.out.println("Error reading config. Using defaults.");
            return loadDefault();
        }
    }

    public static BackupScheduleConfigModel loadDefault() {
        BackupScheduleConfigModel m = new BackupScheduleConfigModel();

        m.setFrequency(BackupScheduleConfigModel.Frequency.WEEKLY);
        m.setEnabled(true);

        Set<DayOfWeek> d = new TreeSet<>();
        d.add(DayOfWeek.MONDAY);
        m.setDaysOfWeek(d);

        m.setHours(6);
        m.setMinutes(0);
        m.setTimezone("Africa/Addis_Ababa");
        m.setRecoveryPolicy(BackupScheduleConfigModel.Policy.SKIP);

        return m;
    }
}

