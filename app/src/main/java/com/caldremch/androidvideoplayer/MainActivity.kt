package com.caldremch.androidvideoplayer

import android.content.Intent
import android.view.View
import com.caldremch.androidvideoplayer.Activity.PlayerDemoActivity
import com.caldremch.androidvideoplayer.Activity.VideoDemoActivity
import com.caldremch.androidvideoplayer.flowplay.CloseFloatWindowEvent
import com.caldremch.androidvideoplayer.flowplay.OpenFloatWindowEvent
import com.caldremch.androidvideoplayer.flowplay.VideoFloatService
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.uitls.FilePermissionDelegate
import com.caldremch.androidvideoplayer.uitls.asset.AssetUtils
import com.caldremch.common.base.BaseActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : BaseActivity() {

    var filePermission = FilePermissionDelegate(this)

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        var intent = Intent(this, VideoFloatService::class.java)
        startService(intent)
        //("可以通过通知, 来创建, 而不是直接创建, 创建后发送通知") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initData() {
        if (!filePermission.hasPermission()) {
            filePermission.requestPermission()
        }

        Thread(Runnable {
            AssetUtils.copy(context = mContext, fileName = "test.mp4")
        }).start()
    }

    fun handleVideo(view: View) {
        startActivity(Intent(this, VideoDemoActivity::class.java))
    }

    fun handleFFmpeg(view: View) {
        startActivity(Intent(this, FFmpegMainActivity::class.java))
        //       VedioEditUtils.mp4TransTs("/storage/emulated/0/DCIM/Camera/VID_20190414_184208.mp4");
    }

    fun videoPlayer(view: View) {
        startActivity(Intent(this, PlayerDemoActivity::class.java))
    }


    override fun onDestroy() {
        super.onDestroy()
        CLog.d("MainActivity onDestroy")
        var intent = Intent(this, VideoFloatService::class.java)
        stopService(intent)
    }

    override fun isUseEvent(): Boolean {
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun openFloatWindowEvent(openFloatWindowEvent: OpenFloatWindowEvent){
        val intent = Intent(this, VideoFloatService::class.java)
        intent.putExtra(VideoFloatService.DATA_KEY, 1)
        startService(intent)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun closeFloatWindowEvent(closeFloatWindowEvent: CloseFloatWindowEvent){

    }
}
