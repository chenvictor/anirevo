package cvic.anirevo.ui;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import cvic.anirevo.R;
import cvic.anirevo.handlers.ScheduleFragmentHitboxHandler;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.calendar.CalendarEvent;

public class EventDecoration extends RecyclerView.ItemDecoration {

    private static final float RECT_ROUND = 8f;
    private static float TEXT_SIZE = 40;
    private static float STAR_SIZE = 40;

    private static Paint PAINT_RECT = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static TextPaint PAINT_TEXT = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private static TextPaint PAINT_AGE = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private static TextPaint PAINT_STAR = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private static int COLOR_EVENT_BLOCK = Color.BLACK;

    private static String STAR_EMPTY = "";
    private static String STAR_FILLED = "";

    private static float mHourHeight = 50;
    private static float mMinuteHeight = mHourHeight / 60;
    private static float mLeftMargin = 0;
    private static float mMargin = 0;
    private static float mPadding = 0;

    static {
        PAINT_AGE.setTextSize(TEXT_SIZE);
        PAINT_AGE.setTextAlign(Paint.Align.LEFT);
        PAINT_RECT.setColor(COLOR_EVENT_BLOCK);
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
        mPadding = resources.getDimension(R.dimen.event_block_padding);
        STAR_EMPTY = resources.getString(R.string.star_empty);
        STAR_FILLED = resources.getString(R.string.star_filled);
        TEXT_SIZE = resources.getDimension(R.dimen.text_size);
        STAR_SIZE = resources.getDimension(R.dimen.star_size);
        PAINT_AGE.setTextSize(TEXT_SIZE);
        PAINT_TEXT.setTextSize(TEXT_SIZE);
        PAINT_TEXT.setColor(resources.getColor(R.color.calendarEventText));
        PAINT_STAR.setTextSize(STAR_SIZE);
        COLOR_EVENT_BLOCK = resources.getColor(R.color.calendarEventBlock);
        PAINT_RECT.setColor(COLOR_EVENT_BLOCK);
        ScheduleFragmentHitboxHandler.setStarHitbox((int) (STAR_SIZE + 2 * mPadding));
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
            drawDecoration(c, parent, position, hourBlock);
        }
    }

    private void drawDecoration(@NonNull Canvas c, @NonNull RecyclerView parent, int position, View hourBlock) {
        float rectTop = calculateRectTop(hourBlock, position);
        float rectHeight = calculateRectHeight();
        RectF rect = calculateBaseRect(parent, rectTop, rectHeight);

        c.save();
        drawBackgroundCard(c, rect);
        drawStar(c, rect);
        drawName(c, rect);
        c.restore();
        if (mListener != null) {
            mListener.addRect(rect, mEvent);
        }
    }

    private void drawName(@NonNull Canvas c, RectF rect) {
        String name = mEvent.getName();
        AgeRestriction restriction = mEvent.getEvent().getRestriction();
        PointF point = new PointF();
        point.x = rect.left + mPadding;
        point.y = rect.top + TEXT_SIZE + mPadding;

        if (restriction == null || restriction == AgeRestriction.DEFAULT) {
            name = ellipsize(name, calculateTextWidth(rect, false));
        } else {
            name = ellipsize(name, calculateTextWidth(rect, true));
            drawAgeTag(c, rect, restriction);
        }
        c.drawText(name, point.x, point.y, PAINT_TEXT);
    }

    private void drawAgeTag(@NonNull Canvas c, RectF rect, AgeRestriction restriction) {
        RectF tagRect = new RectF();
        tagRect.top = rect.top + mPadding;
        tagRect.bottom = tagRect.top + TEXT_SIZE + mPadding;
        tagRect.left = rect.right - 3 * mPadding - STAR_SIZE - 50;
        tagRect.right = rect.right - STAR_SIZE - 2 * mPadding;

        PointF agePos = new PointF();
        agePos.x = rect.right - 3 * mPadding - STAR_SIZE - 50;
        agePos.y = rect.top + TEXT_SIZE + mPadding;

        PAINT_AGE.setColor(restriction.getColor());
        c.drawRoundRect(tagRect, RECT_ROUND, RECT_ROUND, PAINT_AGE);
        PAINT_AGE.setColor(Color.WHITE);
        c.drawText(restriction.toString(), agePos.x, agePos.y, PAINT_AGE);
    }

    private void drawStar(@NonNull Canvas c, RectF rect) {
        c.drawText((mEvent.getEvent().isStarred() ? STAR_FILLED : STAR_EMPTY), rect.right - mPadding, rect.top + STAR_SIZE + mPadding, PAINT_STAR);
    }

    private void drawBackgroundCard(@NonNull Canvas c, RectF rect) {
        c.drawRoundRect(rect, RECT_ROUND, RECT_ROUND, PAINT_RECT);
    }

    private String ellipsize(String name, float width) {
        return TextUtils.ellipsize(name, PAINT_TEXT, width, TextUtils.TruncateAt.END).toString();
    }

    private float calculateTextWidth(RectF marginRect, boolean drawAge) {
        float width = marginRect.width() - 4 * mPadding - STAR_SIZE;
        if (drawAge) {
            width -= 50;
        }
        return width;
    }

    private RectF calculateBaseRect(RecyclerView parent, float rectTop, float rectHeight) {
        RectF rect = new RectF();
        rect.top = rectTop;
        rect.bottom = rectTop + rectHeight;
        rect.left = mLeftMargin;
        rect.right = parent.getWidth();
        applyMargins(rect);
        return rect;
    }

    /**
     * Applies mMargin to rectangle
     * @param rect  rect to apply margins on, rect is directly modified
     */
    private void applyMargins(RectF rect) {
        rect.top += mMargin;
        rect.bottom -= mMargin;
        rect.left += mMargin;
        rect.right -= mMargin;
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

    public interface RectListener {

        void addRect(RectF rect, CalendarEvent calendarEvent);

    }

}
