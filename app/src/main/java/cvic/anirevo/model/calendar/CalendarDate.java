package cvic.anirevo.model.calendar;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CalendarDate implements Iterable<CalendarEvent>{

    private final String name;
    private final int id;

    private List<CalendarEvent> events;

    //Default is 10AM - 10PM
    private int startHour = 10;
    private int endHour = 22;

    CalendarDate(String name, int id) {
        this.name = name;
        this.id = id;
        events = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getId() {
        return id;
    }

    public void addEvent(CalendarEvent calEvent) {
        if (!events.contains(calEvent)) {
            if (events.size() == 0) {
                //first event, set start/end hour
                startHour = calEvent.getStartHour();
                endHour = calEvent.getEndHour();
            } else {
                startHour = Math.min(startHour, calEvent.getStartHour());
                endHour = Math.max(endHour, calEvent.getEndHour());
            }
            events.add(calEvent);
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarDate that = (CalendarDate) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @NonNull
    @Override
    public Iterator<CalendarEvent> iterator() {
        return events.iterator();
    }

}
