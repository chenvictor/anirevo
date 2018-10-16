package cvic.anirevo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cvic.anirevo.handlers.MapGestureDetector;

public class CustomMapView extends View implements MapGestureDetector.MapGestureListener {

    private Bitmap mBitmap;
    private MapGestureDetector mDetector;

    public CustomMapView(Context context) {
        super(context);
        init();
    }

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDetector = new MapGestureDetector(this);
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mDetector.resetMatrix(mBitmap);
        invalidate();   //force redraw
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector == null) {
            return false;
        }
        if (mDetector.onTouch(event)) {
            performClick();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mDetector.getMatrix(), null);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void onChange(MapGestureDetector detector) {
        invalidate();
    }
}
