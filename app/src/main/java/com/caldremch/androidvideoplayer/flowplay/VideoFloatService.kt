package com.caldremch.androidvideoplayer.flowplay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.View
import com.caldremch.androidvideoplayer.Activity.PlayerDemoActivity

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
class VideoFloatService : Service() {

    companion object {
        val DATA_KEY = "DATA_KEY"
    }

    val VIDEO_TYPE_KEY = "VIDEO_TYPE_KEY"
    private lateinit var mController: VideoFloatController
    private var mFlat: Int = 0;
    private lateinit var mContext: Context

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val typeFlat = intent?.getIntExtra(DATA_KEY, 0)

        if (typeFlat != 0) {
            mController.mVideoRatio = VideoFloatController.Video_Ratio.PC
            mController.open()
            mController.setListener(object : VideoFloatController.onViewClickListener {
                override fun onClick(v: View) {
                    //需要从WindowManager中removeView , 不然会有绘制错误
                    mController.close()
                    //设置为正常状态, 防止点击跳转
                    VideoFloatController.instance.mStatus = MainViewStatus.NORMAL
                    //播放器实例不销毁,直接复用
                    val detailIntent = Intent(mContext, PlayerDemoActivity::class.java)
                    detailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    mContext.startActivity(detailIntent)
                }
            })
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        mController = VideoFloatController.instance
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }




}