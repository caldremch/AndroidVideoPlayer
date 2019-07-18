package com.caldremch.common.widget.round;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author Caldremch
 * @date 2019-07-06 19:34
 * @email caldremch@163.com
 * @describe
 **/
public class RoundFrameLayout extends ConstraintLayout {
    public RoundFrameLayout(Context context) {
        this(context, null);
    }
    public RoundFrameLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RoundFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        new RoundWidgetDelegate(this, context, attrs);
    }
}
