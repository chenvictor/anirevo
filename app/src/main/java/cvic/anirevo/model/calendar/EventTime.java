package cvic.anirevo.model.calendar;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventTime eventTime = (EventTime) o;
        return hour == eventTime.hour &&
                minute == eventTime.minute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hour);
    }

    @Override
    public String toString() {
        return TimeUtils.formatTimeString(hour, minute);
    }

    public void increment() {
        this.hour += 24;
    }

    public int toInt() {
        return hour * 60 + minute;
    }
}
