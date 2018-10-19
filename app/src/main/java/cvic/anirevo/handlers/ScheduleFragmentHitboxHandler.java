package cvic.anirevo.handlers;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.ui.EventDecoration;

public class ScheduleFragmentHitboxHandler implements EventDecoration.RectListener, View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "anirevo.hitbox";
    private static final int HITBOX_FAT_FINGER = 10;

    private static int STAR_HITBOX = 0;

    private float xPos = 0;
    private float yPos = 0;

    private EventTappedListener mListener;
    private Map<RectF, CalendarEvent> hitboxes;

    public static void setStarHitbox(int hitbox) {
        STAR_HITBOX = hitbox;
    }

    public ScheduleFragmentHitboxHandler(EventTappedListener listener) {
        hitboxes = new LinkedHashMap<>();
        mListener = listener;
    }

    private void tap() {
        List<RectF> keyset = new LinkedList<>(hitboxes.keySet());
        Collections.reverse(keyset);
        for (RectF rect : keyset) {
            if (rect.contains(xPos, yPos)) {
                RectF starRect = new RectF();
                starRect.left = rect.right - STAR_HITBOX - HITBOX_FAT_FINGER;
                starRect.right = rect.right;
                starRect.top = rect.top;
                starRect.bottom = rect.top + STAR_HITBOX + HITBOX_FAT_FINGER;
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
        event.getStarrable().toggleStarred();
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

    public interface EventTappedListener {

        void eventTapped(CalendarEvent event);
        void starToggled();

    }

}
