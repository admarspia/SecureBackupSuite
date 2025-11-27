package config.user_config.schedule_config;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

public class BackupScheduleConfigModel {

    public enum Frequency { DAILY, WEEKLY, MONTHLY, HOURLY, INTERVAL, ONCE }
    public enum Status { SUCCESS, FAILED, MISSED }
    public enum Policy { RUN_IMMEDIATELY, SKIP }

    private Frequency frequency;
    private boolean enabled;
    private int hours;
    private int minutes;
    private String timezone;
    private String startDate; 

    private Set<DayOfWeek> daysOfWeek = new TreeSet<>();
    private Set<Integer> daysOfMonth = new TreeSet<>();

    private int intervalMinutes;
    private Policy recoveryPolicy;

    // Getters
    public Frequency getFrequency() { return frequency; }
    public boolean isEnabled() { return enabled; }
    public int getHours() { return hours; }
    public int getMinutes() { return minutes; }
    public String getTimezone() { return timezone; }
    public Set<DayOfWeek> getDaysOfWeek() { return daysOfWeek; }
    public Set<Integer> getDaysOfMonth() { return daysOfMonth; }
    public int getIntervalMinutes() { return intervalMinutes; }

    public String getStartDate() { 
        return startDate != null ? startDate : null; 
    }

    public Policy getRecoveryPolicy() { return recoveryPolicy; }

    // Setters
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setHours(int hours) { this.hours = hours; }
    public void setMinutes(int minutes) { this.minutes = minutes; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) { this.daysOfWeek = daysOfWeek; }
    public void setDaysOfMonth(Set<Integer> daysOfMonth) { this.daysOfMonth = daysOfMonth; }
    public void setIntervalMinutes(int intervalMinutes) { this.intervalMinutes = intervalMinutes; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setRecoveryPolicy(Policy recoveryPolicy) { this.recoveryPolicy = recoveryPolicy; }
}

