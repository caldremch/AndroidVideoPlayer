package com.caldremch.androidvideoplayer.uitls;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * @author Caldremch
 * @date 2019-04-21 10:34
 * @email caldremch@163.com
 * @describe
 **/
public class DeviceUtils {

    public static String getDeviceUUID(){

        String deviceId = "";
        if (Build.VERSION.SDK_INT < 21){
            TelephonyManager telecomManager = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telecomManager.getDeviceId();
        }

        CLog.d("deviceId:"+deviceId);
        return null;

    }

}
