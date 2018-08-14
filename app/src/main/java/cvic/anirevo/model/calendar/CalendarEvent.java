package cvic.anirevo.model.calendar;

import android.graphics.Color;

import cvic.anirevo.model.anirevo.ArEvent;

/**
 * Created by FRAMGIA\pham.van.khac on 07/07/2016.
 * Adapted for AniRevo by chenvictor
 */

public class CalendarEvent {

    private final ArEvent event;

    private CalendarDate date;
    private EventTime start;
    private EventTime end;

    private int color = Color.BLUE;

    public CalendarEvent(ArEvent event) {
        this.event = event;
    }

    public String getName() {
        return event.getTitle();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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
        if (start.after(end)) {
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
}
