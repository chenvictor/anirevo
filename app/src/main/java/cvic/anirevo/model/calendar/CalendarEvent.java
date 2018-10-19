package cvic.anirevo.model.calendar;

import cvic.anirevo.model.Starrable;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.ArGuest;
import cvic.anirevo.model.anirevo.ArLocation;
import cvic.anirevo.utils.TimeUtils;

public class CalendarEvent {

    public static final int TYPE_EVENT = 0;
    public static final int TYPE_GUEST = 1;

    private final int type;
    private final ArEvent event;
    private final ArGuest guest;

    private ArLocation location;
    private CalendarDate date;
    private EventTime start;
    private EventTime end;

    public CalendarEvent(ArEvent event) {
        this.event = event;
        this.guest = null;
        this.type = TYPE_EVENT;
    }

    public CalendarEvent(ArGuest guest) {
        this.guest = guest;
        this.event = null;
        this.type = TYPE_GUEST;
    }

    public int getType() {
        return type;
    }

    public ArEvent getEvent() {
        if (event == null) {
            throw new UnsupportedOperationException();
        }
        return event;
    }

    public ArGuest getGuest() {
        if (guest == null) {
            throw new UnsupportedOperationException();
        }
        return guest;
    }

    public AgeRestriction getRestriction() {
        switch (type) {
            case TYPE_EVENT: return getEvent().getRestriction();
            case TYPE_GUEST: return null;
            default: return null;
        }
    }

    public Starrable getStarrable() {
        switch (type) {
            case TYPE_EVENT: return getEvent();
            case TYPE_GUEST: return getGuest();
        }
        throw new UnsupportedOperationException();
    }

    public ArLocation getLocation () {
        return location;
    }

    public void setLocation(ArLocation location) {
        if (this.location != null && this.location.equals(location)) {
            return;
        }
        this.location = location;
        location.addEvent(this);
    }

    public String getName() {
        switch (type) {
            case TYPE_EVENT:
                if (event != null) {
                    return event.getTitle();
                }
                break;
            case TYPE_GUEST:
                if (guest != null) {
                    return guest.getName();
                }
                break;
        }
        return "Error";
    }

    public EventTime getStartTime() {
        return start;
    }

    public EventTime getEndTime() {
        return end;
    }

    public void setStart(EventTime start) {
        this.start = start;
    }

    public void setEnd(EventTime end) {
        //if end time 'before' start, add 24 hours
        if (TimeUtils.timeAfter(start, end)) {
            end.increment();
        }
        this.end = end;
    }

    public CalendarDate getDate() {
        return date;
    }

    public void setDate(CalendarDate date) {
        this.date = date;
    }

    //Extra Helpers to assist with Calendar Fragment
    public int getStartHour() {
        return start.getHour();
    }

    public int getEndHour() {
        if (end.getMinute() == 0) {
            return end.getHour();
        } else {
            return end.getHour() + 1;
        }
    }
}
