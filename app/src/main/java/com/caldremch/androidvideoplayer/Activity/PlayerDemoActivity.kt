package com.caldremch.androidvideoplayer.Activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.caldremch.androidvideoplayer.Constant
import com.caldremch.androidvideoplayer.R
import com.caldremch.common.base.BaseActivity
import com.caldremch.playercore.player.MyExoPlayerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.video.VideoListener
import kotlinx.android.synthetic.main.activity_player_demo.*

/**
 * @author Caldremch
 * @date 2019-04-21 11:13
 * @email caldremch@163.com
 * @describe
 */
class PlayerDemoActivity : BaseActivity() {


    internal var simpleExoPlayer: SimpleExoPlayer? = null
    internal var playerView: MyExoPlayerView? = null

     override fun getLayoutId(): Int {
        return R.layout.activity_player_demo
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(getLayoutId())
//        initView()
//    }


     override fun initView() {

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this)
        simpleExoPlayer!!.addVideoListener(object : VideoListener {
            override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {

            }

            override fun onSurfaceSizeChanged(width: Int, height: Int) {

            }

            override fun onRenderedFirstFrame() {
                progress_circular.hide();
            }
        })

        playerView = findViewById(R.id.playerView)
        playerView!!.player = simpleExoPlayer

        val dataFactory = DefaultDataSourceFactory(this, "caldremch")
        val videoSource = ExtractorMediaSource.Factory(dataFactory).createMediaSource(Uri.parse(Constant.MP4_TEST_URL))
        simpleExoPlayer!!.prepare(videoSource)
        simpleExoPlayer!!.playWhenReady = true
        playerView!!.setFullScreenListener { Log.d("caldremch", "点击全屏了") }

        //        Intent intent = new Intent();
        //        intent.setAction("android.intent.action.VIEW");
        //        Uri uri = Uri.parse(Constant.MP4_TEST_URL4);
        //        intent.setData(uri);
        //        startActivity(intent);
    }

    override fun onStop() {
        super.onStop()
        playerView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        playerView!!.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer!!.release()
        playerView = null
        simpleExoPlayer = null
    }
}
