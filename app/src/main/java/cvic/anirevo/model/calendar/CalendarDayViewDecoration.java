package cvic.anirevo.model.calendar;

import android.content.Context;
import android.graphics.Rect;

import cvic.anirevo.ui.DayView;
import cvic.anirevo.ui.EventView;
import cvic.anirevo.utils.TimeUtils;

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
        dayView.setText(TimeUtils.formatHourString(hour));
        return dayView;
    }

}
