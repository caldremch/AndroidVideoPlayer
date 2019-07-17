package com.caldremch.common.widget.round;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author Caldremch
 * @date 2019-07-06 19:34
 * @email caldremch@163.com
 * @describe
 **/
public class RoundConstraintLayout extends ConstraintLayout {
    public RoundConstraintLayout(Context context) {
        this(context, null);
    }
    public RoundConstraintLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RoundConstraintLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        new RoundWidgetDelegate(this, context, attrs);
    }
}
