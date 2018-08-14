package cvic.anirevo;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import cvic.anirevo.model.calendar.CalendarEvent;

/**
 * Created by FRAMGIA\pham.van.khac on 07/07/2016.
 * Adapted for AniRevo by chenvictor
 */

public class EventView extends FrameLayout {

    protected CalendarEvent mEvent;

    protected OnEventClickListener mEventClickListener;

    protected CardView mEventCard;

    protected TextView mEventTime;

    protected TextView mEventName;

    public EventView(Context context) {
        super(context);
        init(null);
    }

    public EventView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_event, this, true);

        mEventCard = findViewById(R.id.calendar_event_card);
        mEventTime = findViewById(R.id.item_event_time);
        mEventName = findViewById(R.id.item_event_name);

        OnClickListener eventItemClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Event Tapped", Toast.LENGTH_SHORT).show();
            }
        };
        mEventCard.setOnClickListener(eventItemClickListener);
    }

    public void setOnEventClickListener(OnEventClickListener listener){
        this.mEventClickListener = listener;
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        throw new UnsupportedOperationException("you should use setOnEventClickListener()");
    }

    public void setEvent(CalendarEvent event) {
        this.mEvent = event;
        mEventName.setText(String.valueOf(event.getName()));
        mEventTime.setText(String.format(Locale.CANADA, "%s - %s", mEvent.getStartTime(), mEvent.getEndTime()));
        mEventCard.setCardBackgroundColor(event.getColor());
    }

    public void setPosition(Rect rect, int topMargin, int bottomMargin){
        int extraMargin = getResources().getDimensionPixelSize(R.dimen.cdv_event_margin) / 2;
        LayoutParams params =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = rect.top + topMargin + 13 + extraMargin;
        params.height = rect.height()
                + bottomMargin - 26 - 2 * extraMargin + 8;
        params.leftMargin = rect.left;
        setLayoutParams(params);
        //Set card layout
        ViewGroup.LayoutParams cardParams = mEventCard.getLayoutParams();
        cardParams.height = rect.height()
                + bottomMargin - 26 - 2 * extraMargin;
        mEventCard.setLayoutParams(cardParams);
    }

    public interface OnEventClickListener {
        void onEventClick(EventView view, CalendarEvent data);
        void onEventViewClick(View view, EventView eventView, CalendarEvent data);
    }
}
