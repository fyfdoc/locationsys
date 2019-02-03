package com.dtm.locationsys.chart;

import android.content.Context;

/**
 * Created by pq on 10/6/17.
 */

public class DensityUtils {

    public static int dipTopx(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F * (float) (dpValue >= 0.0F ? 1 : -1));
    }

    public static int pxTodip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);
    }

    public static int pxTosp(Context context, float pxValue, float fontScale) {
        return (int) (pxValue / fontScale + 0.5F);
    }

    public static int spTopx(float spValue, float fontScale) {
        return (int) (spValue * fontScale + 0.5F);
    }
}
