package cvic.anirevo.ui;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cvic.anirevo.R;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.calendar.CalendarEvent;

class EventDecoration extends RecyclerView.ItemDecoration {

    private static final float RECT_ROUND = 8f;
    private static float TEXT_SIZE = 40;
    private static float STAR_SIZE = 40;

    private static Paint PAINT_RECT = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static Paint PAINT_TEXT = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static Paint PAINT_STAR = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static int EVENT_DEFAULT = Color.BLUE;
    private static int EVENT_13 = Color.BLUE;
    private static int EVENT_18 = Color.BLUE;

    private static String STAR_EMPTY = "";
    private static String STAR_FILLED = "";

    private static float mHourHeight = 50;
    private static float mMinuteHeight = mHourHeight / 60;
    private static float mLeftMargin = 0;
    private static float mMargin = 0;

    static {
        PAINT_RECT.setColor(Color.CYAN);
        PAINT_TEXT.setColor(Color.WHITE);
        PAINT_TEXT.setTextAlign(Paint.Align.LEFT);
        PAINT_TEXT.setTextSize(TEXT_SIZE);
        PAINT_STAR.setTextAlign(Paint.Align.RIGHT);
        PAINT_STAR.setColor(Color.GRAY);
        PAINT_STAR.setTextSize(STAR_SIZE);
    }

    private RectListener mListener;
    private final CalendarEvent mEvent;
    private int mStartHourOffset;

    public static void initialize(Resources resources) {
        mHourHeight = resources.getDimension(R.dimen.hour_height);
        mMinuteHeight = mHourHeight / 60;
        mLeftMargin = resources.getDimension(R.dimen.left_margin);
        mMargin = resources.getDimension(R.dimen.event_block_margin);
        STAR_EMPTY = resources.getString(R.string.star_empty);
        STAR_FILLED = resources.getString(R.string.star_filled);
        TEXT_SIZE = resources.getDimension(R.dimen.text_size);
        STAR_SIZE = resources.getDimension(R.dimen.star_size);
        PAINT_TEXT.setTextSize(TEXT_SIZE);
        PAINT_STAR.setTextSize(STAR_SIZE);
        EVENT_DEFAULT = resources.getColor(R.color.calendarEventDefault);
        EVENT_13 = resources.getColor(R.color.calendarEvent13plus);
        EVENT_18 = resources.getColor(R.color.calendarEvent18plus);
        ScheduleFragmentHitboxHandler.setSTARHITBOX((int) (STAR_SIZE + 2 * mMargin));
    }

    EventDecoration(RectListener listener, @NonNull CalendarEvent event, int startHourOffset) {
        mListener = listener;
        mEvent = event;
        mStartHourOffset = startHourOffset;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        int position = layoutManager.findFirstVisibleItemPosition();
        View hourBlock = layoutManager.findViewByPosition(position);
        if (hourBlock != null) {
            float rectTop = calculateRectTop(hourBlock, position);
            float rectHeight = calculateRectHeight();
            if (isVisible(parent, rectTop, rectHeight)) {
                RectF rect = calculateRect(parent, rectTop, rectHeight);
                c.save();
                AgeRestriction restriction = mEvent.getEvent().getRestriction();
                if (restriction == null) {
                    PAINT_RECT.setColor(EVENT_DEFAULT);
                } else {
                    PAINT_RECT.setColor(restriction.getTextColor());
                }
                c.drawRoundRect(rect, RECT_ROUND, RECT_ROUND, PAINT_RECT);
                c.drawText((mEvent.getEvent().isStarred() ? STAR_FILLED : STAR_EMPTY), rect.right - 10, rect.top + TEXT_SIZE + 10, PAINT_STAR);
                c.clipRect(rect.left + 50, rect.top + 8, rect.right - 50, rect.bottom);
                c.drawText(mEvent.getName(), rect.left + 50, rect.top + 8 + TEXT_SIZE, PAINT_TEXT);
                c.restore();
                if (mListener != null) {
                    mListener.addRect(rect, mEvent);
                }
            }
        }
    }


    private RectF calculateRect(RecyclerView parent, float rectTop, float rectHeight) {
        RectF rect = new RectF();
        rect.top = rectTop + mMargin;
        rect.bottom = rectTop + rectHeight - mMargin;
        rect.left = mLeftMargin + mMargin;
        rect.right = parent.getWidth() - mMargin;
        return rect;
    }

    private float calculateRectTop(View topHourBlock, int topPosition) {
        int hourTop = topHourBlock.getTop();
        float positionOffset = topPosition * mHourHeight;
        float minuteOffset = mEvent.getStartTime().getMinute() * mMinuteHeight;
        float topY = positionOffset - hourTop;
        float eventTop = (mEvent.getStartHour() - mStartHourOffset) * mHourHeight + 30 * mMinuteHeight + minuteOffset;
        return eventTop - topY;
    }

    private float calculateRectHeight() {
        float hourHeight = (mEvent.getEndTime().getHour() - mEvent.getStartHour()) * mHourHeight;
        float minuteHeight = (mEvent.getEndTime().getMinute() - mEvent.getStartTime().getMinute()) * mMinuteHeight;
        return hourHeight + minuteHeight;
    }

    /**
     * Checks if a given rectangle will be visible in the recyclerview
     *
     * @param rectTop    top of the rectangle
     * @param rectHeight height of the rectangle
     * @return true if any portion of the rectangle will be visible, false otherwise
     */
    private boolean isVisible(RecyclerView parent, float rectTop, float rectHeight) {
//        NestedScrollView scrollView = (NestedScrollView) parent.getParent().getParent();
//        float rectBot = rectTop + rectHeight;
//        return rectTop < scrollView.getScrollY() + scrollView.getHeight() && rectBot > scrollView.getScrollY();
        return true;
    }

    interface RectListener {

        void addRect(RectF rect, CalendarEvent calendarEvent);

    }

}
