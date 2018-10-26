package cvic.anirevo.model.anirevo;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import cvic.anirevo.model.calendar.CalendarEvent;

public class ArLocation implements Iterable<CalendarEvent>{

    /**
     * Represents a Location for an ArEvent
     */

    private final int id;
    private final String purpose;
    private String location;
    private boolean schedule = false;   //true if the location should be shown in the schedule fragment

    private List<CalendarEvent> events;

    ArLocation(String purpose, int id) {
        this.purpose = purpose;
        this.id = id;
        events = new ArrayList<>();
    }

    public void addEvent(CalendarEvent event) {
        if (!events.contains(event)) {
            events.add(event);
            event.setLocation(this);
        }
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    boolean isSchedule() {
        return schedule;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArLocation that = (ArLocation) o;
        return Objects.equals(purpose, that.purpose) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purpose);
    }

    @NonNull
    @Override
    public Iterator<CalendarEvent> iterator() {
        return events.iterator();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return getPurpose() + " - " + getLocation();
    }
}
