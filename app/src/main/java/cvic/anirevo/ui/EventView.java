package cvic.anirevo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cvic.anirevo.EventActivity;
import cvic.anirevo.R;
import cvic.anirevo.model.StarManager;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.calendar.CalendarEvent;

/**
 * Created by FRAMGIA\pham.van.khac on 07/07/2016.
 * Adapted for AniRevo by chenvictor
 */

public class EventView extends FrameLayout {

    private CalendarEvent mEvent;

    private EventClickListener mClickListener;

    private View mConstraint;
    private CardView mEventCard;
    private TextView mEventName;
    private TextView mAge;
    private TextView mStar;

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

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_event, this, true);

        mConstraint = findViewById(R.id.event_card_constraint);
        mEventCard = findViewById(R.id.calendar_event_card);
        mEventName = findViewById(R.id.item_event_name);
        mAge = findViewById(R.id.item_event_age);
        mStar = findViewById(R.id.item_event_star);
        mClickListener = new EventClickListener();
        mEventCard.setOnClickListener(mClickListener);
    }

    public void setEvent(CalendarEvent event) {
        this.mEvent = event;
        mEventName.setText(String.valueOf(event.getName()));
        mEventCard.setCardBackgroundColor(event.getColor());
        if (AgeRestriction.AGE_RESTRICTION_18.equals(event.getEvent().getRestriction())) {
            mAge.setVisibility(VISIBLE);
        } else {
            mAge.setVisibility(GONE);
        }
        setStarText();
        mStar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleStar();
            }
        });
        mClickListener.setEventId(event.getEvent().getId());
    }

    private void setStarText() {
        if (mEvent.getEvent().isStarred()) {
            mStar.setText(getContext().getString(R.string.star_filled));
        } else {
            mStar.setText(getContext().getString(R.string.star_empty));
        }
    }

    public void setPosition(Rect rect, int hourHeight){
        LayoutParams params =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = rect.top - hourHeight / 2;
        params.height = rect.height();
        setLayoutParams(params);
        ViewGroup.LayoutParams constraintParams = mConstraint.getLayoutParams();
        constraintParams.height = rect.height();
    }

    private void toggleStar() {
        if (mEvent.getEvent().toggleStarred()) {
            StarManager.getInstance().add(mEvent.getEvent());
        } else {
            StarManager.getInstance().remove(mEvent.getEvent());
        }
        setStarText();
    }

    class EventClickListener implements OnClickListener {

        private int eventId = 0;

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), EventActivity.class);
            intent.putExtra(ArEventAdapter.EXTRA_EVENT_ID, eventId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }

        void setEventId(int eventId) {
            this.eventId = eventId;
        }
    }
}
