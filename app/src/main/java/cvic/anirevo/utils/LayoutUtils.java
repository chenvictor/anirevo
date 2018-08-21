package cvic.anirevo.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

public class LayoutUtils {

    public static GridLayoutManager createGridLayoutManager(Context ctx, float colWidth) {
        return new GridLayoutManager(ctx, LayoutUtils.calculateNumCols(ctx, colWidth));
    }

    private static int calculateNumCols(Context ctx, float colWidth) {
        return (int) (ctx.getResources().getDisplayMetrics().widthPixels / colWidth);
    }

}
