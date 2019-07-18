package com.caldremch.common.widget.round;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * @author Caldremch
 * @date 2019-07-06 19:34
 * @email caldremch@163.com
 * @describe
 **/
public class RoundLinearLayout extends LinearLayout {
    public RoundLinearLayout(Context context) {
        this(context, null);
    }
    public RoundLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RoundLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        new RoundWidgetDelegate(this, context, attrs);
    }
}
