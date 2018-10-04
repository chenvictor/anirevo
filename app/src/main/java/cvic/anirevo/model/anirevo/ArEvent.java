package cvic.anirevo.model.anirevo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import cvic.anirevo.model.StarManager;
import cvic.anirevo.model.Starrable;
import cvic.anirevo.model.calendar.CalendarEvent;

public class ArEvent extends Starrable{

    /**
     * Represents an event with a time, location, etc.
     */

    //Reference of the array position in EventManager
    private final int id;

    private final String title;
    private ArLocation location;
    private String desc;
    private AgeRestriction restriction;

    private List<CalendarEvent> timeblocks;
    private ArCategory category;
    private Set<ArGuest> guests;
    private Set<ArTag> tags;

    ArEvent(String title, int id) {
        this.id = id;
        this.title = title;
        guests = new HashSet<>();
        tags = new HashSet<>();
        timeblocks = new ArrayList<>();
    }

    public String getDetails() {
        if (timeblocks.size() == 1) {
            //If 1 event, details show specifics
            CalendarEvent calEvent = timeblocks.get(0);
            return String.format("%s, %s - %s | %s", calEvent.getDate(), calEvent.getStartTime(), calEvent.getEndTime(), getLocation().getPurpose());
        } else {
            //If multiple event, details show only each day
            StringBuilder details = new StringBuilder();
            for (CalendarEvent calEvent : timeblocks) {
                details.append(calEvent.getDate()).append(" | ");
            }
            details.append(getLocation().getPurpose());
            return details.toString();
        }
    }

    public void addGuest(ArGuest guest) {
        guests.add(guest);
    }

    public void addTag(ArTag tag) {
        tags.add(tag);
    }

    public void addTimeblock(CalendarEvent calendarEvent) {
        timeblocks.add(calendarEvent);
    }

    public List<CalendarEvent> getTimeblocks() {
        return Collections.unmodifiableList(timeblocks);
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLocation(ArLocation location) {
        this.location = location;
        location.addEvent(this);
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public ArLocation getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArEvent arEvent = (ArEvent) o;
        return  Objects.equals(title, arEvent.title) &&
                Objects.equals(location, arEvent.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    public ArCategory getCategory() {
        return category;
    }

    public void setCategory(ArCategory category) {
        this.category = category;
    }

    public AgeRestriction getRestriction() {
        return restriction;
    }

    public boolean isAgeRestricted() {
        return restriction != null;
    }

    public void setRestriction(AgeRestriction restriction) {
        this.restriction = restriction;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean toggleStarred() {
        boolean starred = super.toggleStarred();
        if (starred) {
            StarManager.getInstance().add(this);
        } else {
            StarManager.getInstance().remove(this);
        }
        return starred;
    }

}
