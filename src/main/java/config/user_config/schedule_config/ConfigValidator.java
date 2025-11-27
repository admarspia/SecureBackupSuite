package config.user_config.schedule_config;

public class ConfigValidator {

    public static void validate(BackupScheduleConfigModel m)
            throws IllegalArgumentException {

        if (m == null)
            throw new IllegalArgumentException("Config is empty or malformed.");

        if (m.getHours() < 0 || m.getHours() > 23)
            throw new IllegalArgumentException("Hour must be 0–23.");

        if (m.getMinutes() < 0 || m.getMinutes() > 59)
            throw new IllegalArgumentException("Minute must be 0–59.");

        if (m.getFrequency() == BackupScheduleConfigModel.Frequency.WEEKLY &&
            m.getDaysOfWeek().isEmpty())
            throw new IllegalArgumentException("Weekly schedule requires daysOfWeek.");

        if (m.getFrequency() == BackupScheduleConfigModel.Frequency.MONTHLY) {
            for (int d : m.getDaysOfMonth()) {
                if (d < 1 || d > 31)
                    throw new IllegalArgumentException("Monthly backup days must be 1–31.");
            }
        }

        if (m.getFrequency() == BackupScheduleConfigModel.Frequency.INTERVAL &&
            m.getIntervalMinutes() <= 0)
            throw new IllegalArgumentException("Interval must be > 0.");
    }
}

