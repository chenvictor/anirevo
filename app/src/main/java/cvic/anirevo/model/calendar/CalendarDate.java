package cvic.anirevo.model.calendar;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CalendarDate implements Iterable<CalendarEvent>{

    private String name;

    private List<CalendarEvent> events;

    public CalendarDate(String name) {
        this.name = name;
        events = new ArrayList<>();
    }

    public void addEvent(CalendarEvent calEvent) {
        if (!events.contains(calEvent)) {
            events.add(calEvent);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @NonNull
    @Override
    public Iterator<CalendarEvent> iterator() {
        return events.iterator();
    }
}
