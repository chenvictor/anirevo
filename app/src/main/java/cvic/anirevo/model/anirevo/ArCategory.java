package cvic.anirevo.model.anirevo;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArCategory implements Iterable<ArEvent>{

    /**
     * Represents a general category for ArEvents
     */

    private String title;

    private List<ArEvent> events;

    public ArCategory(String title) {
        this.title = title;
        events = new ArrayList<>();
    }

    public void addEvent(ArEvent event) {
        events.add(event);
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public Iterator<ArEvent> iterator() {
        return events.iterator();
    }
}
