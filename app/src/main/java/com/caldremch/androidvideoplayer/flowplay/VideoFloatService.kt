package com.caldremch.androidvideoplayer.flowplay

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * @author Caldremch
 *
 * @date 2019-05-25 15:15
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
class VideoFloatService : Service(){

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }




}