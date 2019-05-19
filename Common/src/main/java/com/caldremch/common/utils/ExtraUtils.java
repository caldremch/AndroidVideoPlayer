package com.caldremch.common.utils;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Caldremch
 * @date 2019-05-07 09:08
 * @email caldremch@163.com
 * @describe
 **/
public class ExtraUtils {


    /**
     * InputManager 导致的内存泄漏
     * @param context
     */
    public static void fixInputMethodManagerLeak(Context context) {


        if (context == null) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputMethodManager == null) {
            return;
        }

        String[] viewArray = new String[]{"mCurRootView", "mServedView", "mNextServedView", "mLastSrvView"};

        Field field;
        Object fileObject;

        for (String view : viewArray) {

            try {
                field = inputMethodManager.getClass().getDeclaredField(view);
                if (field == null) {
                    continue;
                }

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                fileObject = field.get(inputMethodManager);
                if (fileObject instanceof View) {
                    View fieldView = (View) fileObject;
                    if (fieldView.getContext() == context) {
                        field.set(inputMethodManager, null); //置空
                    }
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }


    private void disableAPIDialog(){

        if (Build.VERSION.SDK_INT < 28){
            return;
        }

        try {
            Class clz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }


}
