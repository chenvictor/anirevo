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
import cvic.anirevo.model.calendar.CalendarEvent;

public class EventDecoration extends RecyclerView.ItemDecoration {

    private static final float RECT_ROUND = 8f;
    private static float TEXT_SIZE = 40;

    private static Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static Paint starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static String STAR_EMPTY = "";
    private static String STAR_FILLED = "";

    private static int mViewHeight = 100;
    private static float mHourHeight = 50;
    private static float mMinuteHeight = mHourHeight / 60;
    private static float mLeftMargin = 0;
    private static float mMargin = 0;

    static {
        rectPaint.setColor(Color.CYAN);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(TEXT_SIZE);
        starPaint.setTextAlign(Paint.Align.RIGHT);
        starPaint.setColor(Color.BLACK);
        starPaint.setTextSize(TEXT_SIZE);
    }

    private RectListener mListener;
    private final CalendarEvent mEvent;
    private int mStartHourOffset;

    public static void initialize(Resources resources) {
        mHourHeight = resources.getDimension(R.dimen.hour_height);
        mMinuteHeight = mHourHeight / 60;
        mLeftMargin = resources.getDimension(R.dimen.left_margin);
        mMargin = resources.getDimension(R.dimen.eventblock_margin);
        STAR_EMPTY = resources.getString(R.string.star_empty);
        STAR_FILLED = resources.getString(R.string.star_filled);
        TEXT_SIZE = resources.getDimension(R.dimen.text_size);
        textPaint.setTextSize(TEXT_SIZE);
        starPaint.setTextSize(TEXT_SIZE);
        ScheduleFragmentHitboxHandler.setSTARHITBOX((int) (TEXT_SIZE + 2 * mMargin));
    }

    public static void setViewHeight(int viewHeight) {
        mViewHeight = viewHeight;
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
                c.drawRoundRect(rect, RECT_ROUND, RECT_ROUND, rectPaint);
                c.drawText((mEvent.getEvent().isStarred() ? STAR_FILLED : STAR_EMPTY), rect.right - 10, rect.top + TEXT_SIZE + 10, starPaint);
                c.clipRect(rect.left + 50, rect.top + 8, rect.right - 50, rect.bottom);
                c.drawText(mEvent.getName(), rect.left + 50, rect.top + 8 + TEXT_SIZE, textPaint);
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
