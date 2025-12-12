package backup;

import java.time.*;

import config.user_config.schedule_config.*;

public class BackupScheduler {

    public boolean isTime() throws Exception {
        try {
            BackupScheduleConfigModel.Frequency freq = ConfigService.getFrequency();

            ZonedDateTime nextRun = ConfigService.getNextRuntime();
            ZonedDateTime now = ZonedDateTime.now(nextRun.getZone()); 
            System.out.println("now: " + now + "\tnext" + nextRun);
            // same zone

            switch (freq) {
                case WEEKLY:
                case MONTHLY:
                case ONCE:
                    return sameDateAndTime(now, nextRun);

                case DAILY:
                case HOURLY:
                case INTERVAL:
                    return sameTime(now, nextRun);

                default:
                    return false;
            }
        } catch (Exception ex){
            throw ex;
        }
    }

    private boolean sameDateAndTime(ZonedDateTime now, ZonedDateTime run) {
        return  now.getDayOfMonth() == run.getDayOfMonth() &&
                now.getHour() == run.getHour() &&
                now.getMinute() == run.getMinute();
    }

    private boolean sameTime(ZonedDateTime now, ZonedDateTime run) {
        return now.getHour() == run.getHour() &&
               now.getMinute() == run.getMinute();
    }
}



