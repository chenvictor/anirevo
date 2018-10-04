package cvic.anirevo.model.calendar;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cvic.anirevo.ui.DayView;
import cvic.anirevo.ui.EventView;
import cvic.anirevo.utils.TimeUtils;

/**
 * Created by FRAMGIA\pham.van.khac on 07/07/2016.
 * Adapted for AniRevo by chenvictor
 */

public class CalendarDayViewDecoration {

    private static final String TAG = "anirevo.cdvd";

    private List<EventView> buffer = new ArrayList<>();

    private int bufferIndex;

    private Context mContext;

    public CalendarDayViewDecoration(Context context) {
        this.mContext = context;
        resetBufferCounter();
//        //initialize with 8 views
//        for (int i = 0; i < 8; i++) {
//            getBufferedView();
//        }
    }

    public void resetBufferCounter() {
        bufferIndex = 0;
    }

    private EventView getBufferedView() {
        //Log.i(TAG, "Requesting view");
        while (bufferIndex >= buffer.size()) {
            //Buffer limit reached, add a new view
            //Log.i(TAG, "Adding new view to buffer");
            buffer.add(new EventView(mContext));
        }
        return buffer.get(bufferIndex++);
    }

    public EventView getEventView(CalendarEvent event, Rect eventBound, int hourHeight) {
        EventView eventView = getBufferedView();
        eventView.setEvent(event);
        eventView.setPosition(eventBound, hourHeight);
        eventView.invalidate(); //Invalidate after setting height so constraints can be refreshed
        return eventView;
    }

    public DayView getDayView(int hour) {
        DayView dayView = new DayView(mContext);
        dayView.setText(TimeUtils.formatHourString(hour));
        return dayView;
    }

}
