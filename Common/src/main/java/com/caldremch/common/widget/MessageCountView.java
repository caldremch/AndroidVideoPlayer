package com.caldremch.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.caldremch.common.utils.DensityUtil;

/**
 * @author Caldremch
 * @date 2019-04-25 10:46
 * @email caldremch@163.com
 * @describe
 **/
public class MessageCountView extends View {

    public MessageCountView(Context context) {
        super(context);
        init();
    }

    public MessageCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public MessageCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private int mViewHeight;
    private RectF mRectF;

    private Paint mPaint;
    private Paint mTextPaint;

    private int mColore;
    private int mTextSize;


    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    public void setColor(int color) {
        this.mColore = color;
    }

    private String mMsgCount;

    public void setMsgCount(int count) {
        this.mMsgCount = count + "";
        invalidate();
    }

    public void setMsgCount(String count) {
        this.mMsgCount = count;
        invalidate();

    }

    private float mRadius = 2;
    private Rect mRect;

    private final static String MAX_SPEC_TEXT = "99+";
    private final static String MAX_SPEC_TEXT_ONE = "9";
    private final static int PADDING = DensityUtil.dp2px(3);


    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(DensityUtil.dp2px(12));

        mRectF = new RectF();
        //测量 字符的宽高
        mRect = new Rect();
        //以 3 个字符来测量, 自定义 View 的最大的长度
        mTextPaint.getTextBounds(MAX_SPEC_TEXT_ONE, 0, MAX_SPEC_TEXT_ONE.length(), mRect);
        mRadius = PADDING * 3 + mRect.width();
        mTextPaint.getTextBounds(MAX_SPEC_TEXT, 0, MAX_SPEC_TEXT.length(), mRect);
        //自定义 View 的高度为:
        mViewHeight = PADDING * 2 + mRect.width();

    }


    @Override
    protected void onDraw(Canvas canvas) {

        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = mRect.width()+ PADDING *2;
        mRectF.bottom = mViewHeight;

        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
        canvas.drawText(this.mMsgCount, mViewHeight / 2f, mViewHeight / 2f + mRect.height()/2f, mTextPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mesureW(widthMeasureSpec), mesureW(widthMeasureSpec));
    }

    private int mesureW(int widthMeasureSpec) {

        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        Log.d("caldremch", "widthSize = "+widthSize);
        int re = 0;
        //对比值留着
        re = DensityUtil.dp2px(60);
        if (widthSpec == MeasureSpec.AT_MOST) {
            re = Math.min(mViewHeight, re);
        }

        return re;
    }

}
