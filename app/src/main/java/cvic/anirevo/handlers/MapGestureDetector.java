package cvic.anirevo.handlers;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.MotionEvent;

public class MapGestureDetector {

    private static final String TAG = "anirevo.mgdetector";

    private static final float SCALE_MAX = 1f;
    private static final float SCALE_MIN = 0.4f;

    private int ptpIdx = 0;
    private Matrix mTempMatrix;
    private Matrix mMatrix;
    private float[] mSrc = new float[4];
    private float[] mDst = new float[4];
    private int mCount;

    private MapGestureListener mListener;

    public MapGestureDetector(MapGestureListener listener) {
        mMatrix = new Matrix();
        mTempMatrix = new Matrix();
        mListener = listener;
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    /**
     * Handles a motion event
     * @param event event to handle
     * @return  true if a click should be performed, false otherwise
     */
    public boolean onTouch(MotionEvent event) {
        if (event.getPointerCount() > 2) {
            return false;
        }

        int action = event.getActionMasked();
        int index = event.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                int idx = index * 2;
                mSrc[idx] = event.getX(index);
                mSrc[idx + 1] = event.getY(index);
                mCount++;
                ptpIdx = 0;
                break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < mCount; i++) {
                    idx = ptpIdx + i * 2;
                    mDst[idx] = event.getX(i);
                    mDst[idx + 1] = event.getY(i);
                }
                mTempMatrix.setPolyToPoly(mSrc, ptpIdx, mDst, ptpIdx, mCount);
                mMatrix.postConcat(mTempMatrix);
                if(mListener != null) {
                    mListener.onChange(this);
                }
                System.arraycopy(mDst, 0, mSrc, 0, mDst.length);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerId(index) == 0) ptpIdx = 2;
                mCount--;
                break;
        }
        return false; //TODO implement clicks
    }

    public void resetMatrix(Bitmap mBitmap) {
        mMatrix = new Matrix();
        mTempMatrix = new Matrix();
    }


    public interface MapGestureListener {

        void onChange (MapGestureDetector detector);

    }

}
