package cvic.anirevo.model.calendar;

import java.util.Locale;

import cvic.anirevo.exceptions.InvalidTimeException;

public class EventTime {

    private int hour;
    private int minute;

    public EventTime(int hour, int minute) throws InvalidTimeException {
        if (minute < 0 || minute > 59) {
            throw new InvalidTimeException();
        }
        this.hour = hour;
        this.minute = minute;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) throws InvalidTimeException{
        if (minute < 0 || minute > 59) {
            throw new InvalidTimeException();
        }
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        int hourNum = hour % 12;
        if (hourNum == 0)
            hourNum = 12;
        String period = ((hour) % 24 < 12) ? "AM" : "PM";
        return String.format(Locale.CANADA,"%d:%02d%s", hourNum, minute, period);
    }

    /**
     * Parse a timestring into EventTime
     * @param timeString    String in the format HH[:MM]PM/AM
     * @return              EventTime
     */
    public static EventTime parse(String timeString) {
        //last two chars are AM/PM
        String period = timeString.substring(timeString.length() - 2);
        timeString = timeString.substring(0, timeString.length() - 2);
        String[] time = timeString.split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = 0;
        if (hour == 12) {
            hour = 0;
        }
        if (time.length > 1) {
            minute = Integer.parseInt(time[1]);
        }
        if (period.equals("PM")) {
            hour += 12;
        }
        try {
            return new EventTime(hour, minute);
        } catch (InvalidTimeException e) {
            return null;
        }
    }

    //Return true if this is after end
    public boolean after(EventTime end) {
        if (this.hour > end.hour) {
            return true;
        } else if(this.hour == end.hour) {
            return this.minute > end.minute;
        } else {
            return false;
        }
    }

    public void increment() {
        this.hour += 24;
    }
}
