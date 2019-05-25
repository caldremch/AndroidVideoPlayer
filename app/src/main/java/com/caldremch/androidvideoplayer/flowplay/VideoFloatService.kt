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

    companion object {
        val DATA_KEY = "DATA_KEY"
    }
    val VIDEO_TYPE_KEY = "VIDEO_TYPE_KEY"
    private lateinit var mController: VideoFloatController
    private var mFlat:Int = 0;

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val typeFlat = intent?.getIntExtra(DATA_KEY, 0)

        if (typeFlat != 0){
            mController.mVideoRatio = VideoFloatController.Video_Ratio.PC
            mController.open()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        mController =  VideoFloatController.instance
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }




}