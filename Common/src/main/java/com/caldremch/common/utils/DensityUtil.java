package com.caldremch.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author Caldremch
 * @date 2019-04-24 10:55
 * @email caldremch@163.com
 * @describe
 **/
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px( float dpValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp( float pxValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 该高度, 不包含状态栏高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        int result = 0;

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            result = metrics.heightPixels;
        }

        return result;
    }

    public static int getScreenWidth(Context context) {
        int result = 0;

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            result = metrics.widthPixels;
        }

        return result;
    }
}
