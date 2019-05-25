package com.caldremch.androidvideoplayer.Activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.caldremch.androidvideoplayer.Constant
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.flowplay.ILifeCycle
import com.caldremch.androidvideoplayer.flowplay.SingletonPlayerView
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.uitls.asset.AssetUtils
import com.caldremch.common.base.BaseActivity
import com.caldremch.playercore.player.MyExoPlayerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.video.VideoListener
import kotlinx.android.synthetic.main.activity_player_demo.*
import java.io.File
import java.io.InputStream

/**
 * @author Caldremch
 * @date 2019-04-21 11:13
 * @email caldremch@163.com
 * @describe
 */
class PlayerDemoActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_player_demo
    }

    override fun initView() {
        var file = AssetUtils.getAssetFile(context = mContext, fileName = "test.mp4")
        var uri = Uri.fromFile(file)
        playerView.startPlay(uri)
    }

    override fun onStop() {
        super.onStop()
        playerView.onStop()
    }

//    override fun onResume() {
//        super.onResume()
//        playerView.onResume()
//    }

    override fun onDestroy() {
        super.onDestroy()
        playerView.onDestroy()
    }
}
