package cvic.anirevo.ui;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import cvic.anirevo.model.calendar.CalendarEvent;

public class ScheduleFragmentHitboxHandler implements EventDecoration.RectListener, View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "anirevo.hitbox";

    private static int STARHITBOX = 0;

    private float xPos = 0;
    private float yPos = 0;

    private EventTappedListener mListener;
    private Map<RectF, CalendarEvent> hitboxes;

    public static void setSTARHITBOX(int hitbox) {
        STARHITBOX = hitbox;
    }

    ScheduleFragmentHitboxHandler(EventTappedListener listener) {
        hitboxes = new HashMap<>();
        mListener = listener;
    }

    private void tap() {
        for (RectF rect : hitboxes.keySet()) {
            if (rect.contains(xPos, yPos)) {
                RectF starRect = new RectF();
                starRect.left = rect.right - STARHITBOX;
                starRect.right = rect.right;
                starRect.top = rect.top;
                starRect.bottom = rect.top + STARHITBOX;
                if (starRect.contains(xPos, yPos)) {
                    starTapped(hitboxes.get(rect));
                } else if (mListener != null) {
                    mListener.eventTapped(hitboxes.get(rect));
                }
                break;
            }
        }
    }

    private void starTapped(CalendarEvent event) {
        event.getEvent().toggleStarred();
        mListener.starToggled();
    }

    public void clearHitboxes() {
        hitboxes.clear();
    }

    @Override
    public void addRect(RectF rect, CalendarEvent calendarEvent) {
        hitboxes.put(rect, calendarEvent);
    }

    @Override
    public void onClick(View view) {
        tap();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        xPos = motionEvent.getX();
        yPos = motionEvent.getY();
        return false;
    }

    interface EventTappedListener {

        void eventTapped(CalendarEvent event);
        void starToggled();

    }

}
