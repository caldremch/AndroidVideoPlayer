package com.caldremch.androidvideoplayer

import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity

import com.caldremch.androidvideoplayer.Activity.PlayerDemoActivity
import com.caldremch.androidvideoplayer.Activity.VideoDemoActivity
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.uitls.FilePermissionDelegate
import com.caldremch.androidvideoplayer.uitls.asset.AssetUtils
import com.caldremch.common.base.BaseActivity


class MainActivity : BaseActivity() {

    var filePermission = FilePermissionDelegate(this)

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    override fun initData() {
      if (!filePermission.hasPermission()){
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
    }
}
