package com.caldremch.common.widget.round;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

/**
 * @author Caldremch
 * @date 2019-07-06 19:34
 * @email caldremch@163.com
 * @describe
 **/
public class RoundRelativeLayout extends RelativeLayout {
    public RoundRelativeLayout(Context context) {
        this(context, null);
    }
    public RoundRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RoundRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        new RoundWidgetDelegate(this, context, attrs);
    }
}
