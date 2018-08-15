package cvic.anirevo.model.calendar;

import android.content.Context;
import android.graphics.Rect;

import cvic.anirevo.DayView;
import cvic.anirevo.EventView;

/**
 * Created by FRAMGIA\pham.van.khac on 07/07/2016.
 * Adapted for AniRevo by chenvictor
 */

public class CalendarDayViewDecoration {

    private Context mContext;

    public CalendarDayViewDecoration(Context context) {
        this.mContext = context;
    }


    public EventView getEventView(CalendarEvent event, Rect eventBound, int hourHeight) {
        EventView eventView = new EventView(mContext);
        eventView.setEvent(event);
        eventView.setPosition(eventBound, -hourHeight, hourHeight);
        eventView.invalidate(); //Invalidate after setting height so constraints can be refreshed
        return eventView;
    }

    public DayView getDayView(int hour) {
        DayView dayView = new DayView(mContext);
        dayView.setText(formatHourString(hour));
        return dayView;
    }

    private String formatHourString(int hour) {
        //Format into a 12hr AM/PM with wraparound. eg(hour 25 = 1AM next day)
        int hourNum = hour % 12;
        if (hourNum == 0)
            hourNum = 12;
        String period = ((hour) % 24 < 12) ? "AM" : "PM";
        return String.valueOf(hourNum) + period;
    }

}
