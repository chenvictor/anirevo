package cvic.anirevo.model.calendar;

import cvic.anirevo.exceptions.InvalidTimeException;
import cvic.anirevo.utils.TimeUtils;

public class EventTime {

    private int hour;
    private int minute;

    public EventTime(int hour, int minute) throws InvalidTimeException {
        TimeUtils.checkMinute(minute);
        this.hour = hour;
        this.minute = minute;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    @Override
    public String toString() {
        return TimeUtils.formatTimeString(hour, minute);
    }

    public void increment() {
        this.hour += 24;
    }
}
