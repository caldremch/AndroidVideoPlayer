package com.caldremch.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.caldremch.common.utils.MetricsUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.jetbrains.annotations.NotNull;

/**
 * @author Caldremch
 * @date 2019/1/24
 * @Email caldremch@163.com
 * @describe
 **/
public abstract class AbsActivity extends LifeCycleLogActivity{

    protected Activity mContext;

    //沉浸式
    protected ImmersionBar mBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mBar = ImmersionBar.with(this);
        mBar.init();
        compatStatusBar(true);
        if (isAlwaysPortrait()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    protected boolean isAlwaysPortrait() {
        return true;
    }


    protected void compatStatusBar(final View parentView) {
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                parentView.setPadding(0, MetricsUtils.getStatusBarHeight(mContext), 0, 0);
            }
        });
    }

    public void compatStatusBar(boolean isDarkFont) {
        compatStatusBar(isDarkFont, "#00ffffff");
    }

    final protected void transparentNavigationBar(){
        mBar.transparentNavigationBar().init();
    }

    public void compatStatusBar(boolean isDarkFont, String color) {
        if (ImmersionBar.isSupportStatusBarDarkFont()) {
            mBar.statusBarColor(color);
        } else {
            mBar.statusBarColor("#80000000");
        }
        if (getKeyBoardMode() >= 0) {
            mBar.keyboardEnable(true);
            mBar.keyboardMode(getKeyBoardMode());
        }
        mBar.statusBarDarkFont(isDarkFont).init();
    }

    protected int getKeyBoardMode() {
        return -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBar.destroy();

    }

    //设置字体不随系统大小的改变而改变
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources != null) {
            Configuration configuration = resources.getConfiguration();
            if (configuration != null) {
                configuration.fontScale = 1.f;
                return createConfigurationContext(configuration).getResources();
            }
        }
        return resources;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            View currentFocus = getCurrentFocus();
            if (currentFocus != null) {
                int[] location = new int[2];
                currentFocus.getLocationOnScreen(location);
                int l = location[0];
                int t = location[1];
                int r = l + currentFocus.getWidth();
                int b = t + currentFocus.getHeight();
                if (ev.getRawX() < l || ev.getRawX() > r || ev.getRawY() < t || ev.getRawY() > b) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
