package com.caldremch.androidvideoplayer.uitls;

import android.app.Application;
import android.content.Context;

/**
 * @author Caldremch
 * @date 2019-04-21 10:39
 * @email caldremch@163.com
 * @describe
 **/
public class Utils {

    private static Application sApplication;

    public static Context getContext() {

        if (sApplication == null){

            throw new RuntimeException("init in application");
        }

        return sApplication.getApplicationContext();
    }


    public static void init(Application application){
        sApplication = application;
    }


}
