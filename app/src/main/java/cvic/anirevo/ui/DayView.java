package cvic.anirevo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cvic.anirevo.R;

/**
 * Created by FRAMGIA\pham.van.khac on 07/07/2016.
 * Adapted for AniRevo by chenvictor
 */

public class DayView extends FrameLayout {

    private TextView mTextHour;

    private View mSeparateHour;

    public DayView(Context context) {
        super(context);
        init(null);
    }

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_day, this, true);

        mTextHour = findViewById(R.id.text_hour);
        mSeparateHour = findViewById(R.id.separate_hour);
    }

    public void setText(String text) {
        mTextHour.setText(text);
    }

    public float getHourTextWidth() {
        return mTextHour.getWidth();
    }

    public float getHourTextHeight() {
        return mTextHour.getLineHeight();
    }

}
