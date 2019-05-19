package com.caldremch.common.utils;

import android.content.Context;

/**
 * @author Caldremch
 * @date 2019-04-24 10:54
 * @email caldremch@163.com
 * @describe
 **/
public class MetricsUtils {
    public static int getStatusBarHeight(Context context) {
        int result = DensityUtil.dp2px(25);
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
