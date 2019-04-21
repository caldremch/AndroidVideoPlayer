package com.caldremch.androidvideoplayer;

import android.app.Application;
import android.content.Context;

import com.caldremch.androidvideoplayer.uitls.Utils;

/**
 * @author Caldremch
 * @date 2019-04-21 10:39
 * @email caldremch@163.com
 * @describe
 **/
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }

}
