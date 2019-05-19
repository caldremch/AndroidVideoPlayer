package com.caldremch.common.base;

import android.app.Application;

import com.caldremch.common.utils.Utils;

/**
 * @author Caldremch
 * @date 2019-04-24 11:04
 * @email caldremch@163.com
 * @describe
 **/
public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
