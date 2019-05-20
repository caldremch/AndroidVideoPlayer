package com.caldremch.androidvideoplayer;

import android.app.Application;
import android.content.Context;

import com.caldremch.androidvideoplayer.uitls.Utils;
import com.caldremch.common.base.BaseApp;

/**
 * @author Caldremch
 * @date 2019-04-21 10:39
 * @email caldremch@163.com
 * @describe
 **/
public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }

}
