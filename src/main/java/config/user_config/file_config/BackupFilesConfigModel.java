package config.user_config.file_config;

import java.util.Set;
import java.util.TreeSet;
import java.time.LocalDate;

public class BackupFilesConfigModel {
    public enum Frequency { DAYLY, WEEKLY, MONTHLY, HOURLY, INTERVAL, ONCE };
    public enum Week { MON, TUE, WED, THU, FRI, SAT, SUN };
    public enum Status { SUCCESS, FAILED, MISSED };
    public enum Policy { RUN_IMMEDIATELY, SKIP, ASK_USER, RUN_IF_OLD };

    private Frequency frequency;
    private boolean enabled;
    private int hour;
    private int minute;
    private String timezone;

    private Set<Week> daysOfWeek = new TreeSet<Week>();
    private Set<Integer> daysOfMonth = new TreeSet<Integer>();
    private int intervalMinutes;
    private LocalDate startDate;
    private LocalDate lastRunTime;
    private Status lastRunStatus;
    private Policy recoveryPolicy;

    public void setFrequency( Frequency frequency){
        this.frequency = frequency;

    }

    public void setEnabled( boolean enable ){
        this.enabled = enable;

    }

    public void setHour( int hour ){
        this.hour = hour;
    }

    public void setMinute( int minute){
        this.minute = minute;

    }

    public void setTimezone ( String timezone ){
        this.timezone = timezone;

    }

    public void setDaysOfWeek( Set<Week> days ) {
        this.daysOfWeek.clear();
        this.daysOfWeek.addAll(days);

    }

    public void setDaysOfMonth( Set<Integer> days ) {
        this.daysOfMonth.clear();
        this.daysOfMonth.addAll(days);

    }

    public void setStartTime( LocalDate startDate){
        this.startDate = startDate;

    }

    public void setLastRunTime( LocalDate lastRunTime ){
        this.lastRunTime = lastRunTime;

    }

    public void setLastRunStatus( Status lastRunStatus){
        this.lastRunStatus = lastRunStatus;

    }
    
    public void setRecoveryPolicy( Policy recoveryPolicy ){
        this.recoveryPolicy = recoveryPolicy;

    }
    
}
