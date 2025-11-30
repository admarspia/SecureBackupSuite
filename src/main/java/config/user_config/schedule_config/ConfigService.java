package config.user_config.schedule_config;


import java.time.*;
import config.user_config.*;


public class ConfigService {

    private static BackupScheduleConfigModel m;
    private static RootConfig root;
    private static ZoneId zone;
    private static ZonedDateTime now;

    static {
        try {
            UserConfigLoader loader = new UserConfigLoader();
            root = loader.load();
            m = root.getSchedule();

            ConfigValidator.validate(m);

            zone = ZoneId.of(m.getTimezone());
            now = ZonedDateTime.now(zone);

        } catch (Exception ex) {
            System.err.println("ConfigService failed to initialize: " + ex);

            m = null;
            zone = ZoneId.systemDefault();
            now = ZonedDateTime.now(zone);
        }
    }

    public static ZonedDateTime getNextRuntime(BackupScheduleConfigModel.Status lastStatus) {

        if (m == null || !m.isEnabled())
            return null;

        if (lastStatus == BackupScheduleConfigModel.Status.FAILED ||
            lastStatus == BackupScheduleConfigModel.Status.MISSED) {

            switch (m.getRecoveryPolicy()) {
                case SKIP:
                    break;
                case RUN_IMMEDIATELY:
                    return now;
            }
        }

        switch (m.getFrequency()) {
            case DAILY:
                return daily(now);

            case WEEKLY:
                return weekly(now);

            case MONTHLY:
                return monthly(now);

            case HOURLY:
                return now.plusHours(1)
                          .withMinute(m.getMinutes())
                          .withSecond(0);

            case INTERVAL:
                return now.plusMinutes(m.getIntervalMinutes())
                          .withSecond(0);

            case ONCE:
                return once(now);

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

