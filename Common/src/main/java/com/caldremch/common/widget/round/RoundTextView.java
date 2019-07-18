package com.caldremch.common.widget.round;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author Caldremch
 * @date 2019-07-07 00:44
 * @email caldremch@163.com
 * @describe
 **/
public class RoundTextView extends AppCompatTextView {
    public RoundTextView(Context context) {
        this(context, null);
    }
    public RoundTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        new RoundWidgetDelegate(this, context, attrs);
    }
}
