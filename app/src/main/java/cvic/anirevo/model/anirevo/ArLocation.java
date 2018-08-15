package cvic.anirevo.model.anirevo;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ArLocation implements Iterable<ArEvent>{

    /**
     * Represents a Location for an ArEvent
     */

    private String purpose;
    private String location;

    private List<ArEvent> events;

    ArLocation(String purpose) {
        this.purpose = purpose;
        events = new ArrayList<>();
    }

    public void addEvent(ArEvent event) {
        if (!events.contains(event)) {
            events.add(event);
        }
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
    public Iterator<ArEvent> iterator() {
        return events.iterator();
    }
}
