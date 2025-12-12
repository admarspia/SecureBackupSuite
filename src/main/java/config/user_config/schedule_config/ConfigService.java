package config.user_config.schedule_config;

import java.time.*;
import java.nio.file.Path;
import java.nio.file.Files;

import config.user_config.*;

public class ConfigService {
    public static ZonedDateTime lastRun; // we keep track of lastrun for the interval backup

    private static BackupScheduleConfigModel m;
    private static RootConfig root;
    public static ZoneId zone;

    static {
        try {
            UserConfigLoader loader = new UserConfigLoader();
            root = loader.load();
            m = root.getSchedule();

            ConfigValidator.validate(m);

            zone = ZoneId.of(m.getTimezone());

        } catch (Exception ex) {
            System.err.println("ConfigService failed to initialize: " + ex);

            m = null;
            zone = ZoneId.systemDefault();
        }
    }

    public static BackupScheduleConfigModel.Status getLastStatus() {
        try {
            return Files.readAllLines(Path.of("backup_system.log"))
                .stream()
                .map(line -> line.split("\\|"))
                .filter(arr -> arr.length >= 4)
                .filter(arr -> arr[1].equals("backup"))
                .reduce((first, second) -> second)     
                .map(arr -> BackupScheduleConfigModel.Status.valueOf(arr[0]))  
                .orElse(BackupScheduleConfigModel.Status.UNKNOWN);

        } catch (Exception ex) {
            return BackupScheduleConfigModel.Status.UNKNOWN;
        }
    }
   
    public static BackupScheduleConfigModel.Frequency getFrequency(){
        return m.getFrequency();
    }

    public static ZonedDateTime getNextRuntime() {

        if (m == null || !m.isEnabled())
            return null;

        // Get current time here dynamically
        ZonedDateTime current = ZonedDateTime.now(zone);

        BackupScheduleConfigModel.Status lastStatus = getLastStatus();

        if (lastStatus == BackupScheduleConfigModel.Status.FAILED ||
            lastStatus == BackupScheduleConfigModel.Status.MISSED) {

            switch (m.getRecoveryPolicy()) {
                case SKIP:
                    break;
                case RUN_IMMEDIATELY:
                    return current;
            }
        }

        switch (m.getFrequency()) {
            case DAILY:
                return daily(current);

            case WEEKLY:
                return weekly(current);

            case MONTHLY:
                return monthly(current);

            case HOURLY:
                return current.plusHours(1)
                              .withMinute(m.getMinutes())
                              .withSecond(0);

            case INTERVAL:
                if (lastRun == null) {
                    lastRun = current;
                    return lastRun;
                }
                ZonedDateTime next = lastRun.plusMinutes(m.getIntervalMinutes());
                if (!next.isAfter(current)) {
                    next = current.plusMinutes(m.getIntervalMinutes()); 
                }
                return next;
            case ONCE:
                return once(current);

            default:
                return null;
        }
    }

    private static ZonedDateTime daily(ZonedDateTime now) {
        ZonedDateTime next = now.withHour(m.getHours())
                                .withMinute(m.getMinutes())
                                .withSecond(0);

        if (next.isBefore(now))
            next = next.plusDays(1);

        return next;
    }

    private static ZonedDateTime weekly(ZonedDateTime now) {
        ZonedDateTime next = null;

        DayOfWeek today = now.getDayOfWeek();

        for (DayOfWeek target : m.getDaysOfWeek()) {

            int diff = target.getValue() - today.getValue();
            if (diff <= 0) diff += 7;

            ZonedDateTime candidate =
                now.plusDays(diff)
                   .withHour(m.getHours())
                   .withMinute(m.getMinutes())
                   .withSecond(0);

            if (next == null || candidate.isBefore(next))
                next = candidate;
        }

        return next;
    }

    private static ZonedDateTime monthly(ZonedDateTime now) {
        ZonedDateTime next = null;

        for (int d : m.getDaysOfMonth()) {

            int safe = safeDay(now, d);

            ZonedDateTime candidate = now.withDayOfMonth(safe)
                                         .withHour(m.getHours())
                                         .withMinute(m.getMinutes())
                                         .withSecond(0);

            if (candidate.isBefore(now)) {
                ZonedDateTime nextMonth = now.plusMonths(1);
                int safeNext = safeDay(nextMonth, d);

                candidate = nextMonth.withDayOfMonth(safeNext)
                                     .withHour(m.getHours())
                                     .withMinute(m.getMinutes())
                                     .withSecond(0);
            }

            if (next == null || candidate.isBefore(next))
                next = candidate;
        }

        return next;
    }

    private static int safeDay(ZonedDateTime now, int day) {
        int last = now.toLocalDate().lengthOfMonth();
        return Math.min(day, last);
    }

    private static ZonedDateTime once(ZonedDateTime now) {
        LocalDate std = LocalDate.parse(m.getStartDate());
        ZonedDateTime t =   std
                           .atStartOfDay(now.getZone())
                           .withHour(m.getHours())
                           .withMinute(m.getMinutes())
                           .withSecond(0);

        if (t.isBefore(now))
            return null;

        return t;
    }

    public static void switchMode() {
        m.setEnabled(!m.isEnabled());
    }
}

