package com.caldremch.common.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caldremch.common.R;
import com.scwang.smartrefresh.layout.util.DensityUtil;



public class EasyTitleBar
        extends RelativeLayout {

    protected RelativeLayout leftLayout;
    protected ImageView leftImage;
    protected RelativeLayout rightLayout;
    protected ImageView rightImage, rightTwoImage;
    protected TextView titleView;
    protected RelativeLayout titleLayout;
    private TextView rightText;

    public EasyTitleBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public EasyTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EasyTitleBar(Context context) {
        super(context);
        init(context, null);
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.common_easy_widget_title_bar, this);
        leftLayout = findViewById(R.id.left_layout);
        leftImage = findViewById(R.id.left_image);
        rightLayout = findViewById(R.id.right_layout);
        rightImage = findViewById(R.id.right_image);
        titleView = findViewById(R.id.title);
        rightText = findViewById(R.id.right_text);
        titleLayout = findViewById(R.id.root);
        rightTwoImage = findViewById(R.id.right_two_image);
        leftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
        parseStyle(context, attrs);
    }

    public void setTitleAlpha(float alpha) {
        titleView.setAlpha(alpha);
    }

    public void setLeftImageGone(){
        leftImage.setVisibility(GONE);
    }

    private void parseStyle(Context context, AttributeSet attrs) {
        boolean isCompatStatusBar = true;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
            String title = ta.getString(R.styleable.TitleBar_titleBarTitle);
            titleView.setText(title);
            String text = ta.getString(R.styleable.TitleBar_titleBarRightText);
            rightText.setText(text);
            Drawable leftDrawable = ta.getDrawable(R.styleable.TitleBar_titleBarLeftImage);
            if (null != leftDrawable) {
                leftImage.setImageDrawable(leftDrawable);
            }
            Drawable rightDrawable = ta.getDrawable(R.styleable.TitleBar_titleBarRightImage);
            if (null != rightDrawable) {
                rightImage.setImageDrawable(rightDrawable);
            }

            Drawable rightTwoDrawable = ta.getDrawable(R.styleable.TitleBar_titleBarRightTwoImage);
            if (null != rightTwoDrawable) {
                rightTwoImage.setImageDrawable(rightTwoDrawable);
            }

            int titleColor = ta.getColor(R.styleable.TitleBar_titleBarTitleColor, getResources().getColor(android.R.color.black));
            titleView.setTextColor(titleColor);

            Drawable background = ta.getDrawable(R.styleable.TitleBar_titleBarBackground);
            if (null != background) {
                titleLayout.setBackgroundDrawable(background);
            }
            int type = ta.getInt(R.styleable.TitleBar_titleBarRightType, 0);
            if (type == 0) {
                rightText.setVisibility(VISIBLE);
                rightImage.setVisibility(GONE);
            } else {
                rightImage.setVisibility(VISIBLE);
                rightText.setVisibility(GONE);
            }
            if (ta.hasValue(R.styleable.TitleBar_isCompatStatusBar)) {
                isCompatStatusBar = ta.getBoolean(R.styleable.TitleBar_isCompatStatusBar, true);
            }
            ta.recycle();
        }
        if (isCompatStatusBar) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setPadding(0, getStatusBarHeight(getContext()), 0, 0);
                }
            });
        }
    }


    public static int getStatusBarHeight(Context context) {
        int result = DensityUtil.dp2px(25f);
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setLeftImageResource(int resId) {
        leftImage.setImageResource(resId);
    }

    public void setRightImageResource(int resId) {
        rightImage.setImageResource(resId);
    }

    public void setLeftLayoutClickListener(OnClickListener listener) {
        leftLayout.setOnClickListener(listener);
    }

    public void setRightLayoutClickListener(OnClickListener listener) {
        rightLayout.setOnClickListener(listener);
    }

    public void setLeftLayoutVisibility(int visibility) {
        leftLayout.setVisibility(visibility);
    }

    public void setRightLayoutVisibility(int visibility) {
        rightLayout.setVisibility(visibility);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setRightText(String text) {
        rightText.setText(text);
    }

    @Override
    public void setBackgroundColor(int color) {
        titleLayout.setBackgroundColor(color);
    }

    public RelativeLayout getLeftLayout() {
        return leftLayout;
    }

    public RelativeLayout getRightLayout() {
        return rightLayout;
    }

    public View getRightImgTowView() {
        return rightTwoImage;
    }

    public View getRightImgView() {
        return rightImage;
    }
}
