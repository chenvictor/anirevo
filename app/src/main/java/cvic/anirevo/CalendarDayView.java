package cvic.anirevo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cvic.anirevo.model.calendar.CalendarDayViewDecoration;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.model.calendar.EventTime;

/**
 * Created by FRAMGIA\pham.van.khac on 07/07/2016.
 * Adapted for AniRevo by chenvictor
 */

public class CalendarDayView extends FrameLayout {

    private int mDayHeight = 0;

    private int mHourWidth = 120;

    private int mTimeHeight = 120;

    private int mStartHour = 0;

    private int mEndHour = 24;

    private LinearLayout mLayoutDayView;

    private FrameLayout mLayoutEvent;

    private CalendarDayViewDecoration mDecoration;

    private List<CalendarEvent> mEvents;

    public CalendarDayView(Context context) {
        super(context);
        init(null);
    }

    public CalendarDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CalendarDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_day_calendar, this, true);

        mLayoutDayView = findViewById(R.id.day_view_container);
        mLayoutEvent = findViewById(R.id.event_container);

        mDayHeight = getResources().getDimensionPixelSize(R.dimen.dayHeight);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarDayView);
            try {
                mDayHeight =
                    a.getDimensionPixelSize(R.styleable.CalendarDayView_dayHeight, mDayHeight);
                mStartHour = a.getInt(R.styleable.CalendarDayView_startHour, mStartHour);
                mEndHour = a.getInt(R.styleable.CalendarDayView_endHour, mEndHour);
            } finally {
                a.recycle();
            }
        }

        mEvents = new ArrayList<>();
        mDecoration = new CalendarDayViewDecoration(getContext());

        refresh();
    }

    public void refresh() {
        drawDayViews();

        drawEvents();
    }

    private void drawDayViews() {
        mLayoutDayView.removeAllViews();
        DayView dayView = null;
        for (int i = mStartHour; i <= mEndHour; i++) {
            dayView = getDecoration().getDayView(i);
            mLayoutDayView.addView(dayView);
        }
        mHourWidth = (int) dayView.getHourTextWidth();
        mTimeHeight = (int) dayView.getHourTextHeight();
    }

    private void drawEvents() {
        mLayoutEvent.removeAllViews();

        for (CalendarEvent event : mEvents) {
            Rect rect = getTimeBound(event);

            // add event view
            EventView eventView =
                getDecoration().getEventView(event, rect, mTimeHeight);
            if (eventView != null) {
                mLayoutEvent.addView(eventView, eventView.getLayoutParams());
            }
        }
    }

    private Rect getTimeBound(CalendarEvent event) {
        Rect rect = new Rect();
        rect.top = getPositionOfTime(event.getStartTime()) + mTimeHeight / 2;
        rect.bottom = getPositionOfTime(event.getEndTime()) + mTimeHeight / 2;
        rect.left = mHourWidth;
        rect.right = getWidth();
        return rect;
    }

    private int getPositionOfTime(EventTime time) {
        int hour = time.getHour() - mStartHour;
        int minute = time.getMinute();
        return hour * mDayHeight + (minute + 30) * mDayHeight / 60;
    }

    public void setEvents(List<CalendarEvent> events) {
        this.mEvents = events;
        refresh();
    }

    public void setLimitTime(int startHour, int endHour) {
        mStartHour = startHour;
        mEndHour = endHour;
        refresh();
    }

    public CalendarDayViewDecoration getDecoration() {
        return mDecoration;
    }
}
