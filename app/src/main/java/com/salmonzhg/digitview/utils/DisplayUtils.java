package com.salmonzhg.digitview.utils;

import android.content.Context;

/**
 * Created by Salmon on 2016/4/22 0022.
 */
public class DisplayUtils {

    public static int dip2px(Context context, float dip) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5);
    }

    public static int px2dip(Context context, float px) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }
}
