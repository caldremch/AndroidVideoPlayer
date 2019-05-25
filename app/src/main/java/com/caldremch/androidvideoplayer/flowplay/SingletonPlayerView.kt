package com.caldremch.androidvideoplayer.flowplay

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.caldremch.androidvideoplayer.R
import com.caldremch.playercore.player.MyExoPlayerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.video.VideoListener
import kotlinx.android.synthetic.main.single_playerview.view.*

/**
 * @author Caldremch
 *
 * @date 2019-05-25 15:19
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
class SingletonPlayerView : FrameLayout, ILifeCycle {

    private lateinit var mRootView: View
    private var mPlayerView: MyExoPlayerView? = null
    internal var simpleExoPlayer: SimpleExoPlayer? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView();
    }

    private fun initView() {
        mRootView = LayoutInflater.from(context).inflate(R.layout.single_playerview, null)

        addView(mRootView)

        mPlayerView = mRootView.findViewById(R.id.playerView)

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context)
        simpleExoPlayer!!.addVideoListener(object : VideoListener {
            override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {

            }

            override fun onSurfaceSizeChanged(width: Int, height: Int) {

            }

            override fun onRenderedFirstFrame() {
                progress_circular.hide();
            }
        })

        mPlayerView?.player = simpleExoPlayer
    }

    fun startPlay(uri: Uri) {
        val dataFactory = DefaultDataSourceFactory(context, "caldremch")
        val videoSource = ExtractorMediaSource.Factory(dataFactory).createMediaSource(uri)
        simpleExoPlayer!!.prepare(videoSource)
        simpleExoPlayer!!.playWhenReady = true
    }


    override fun onStop() {
        mPlayerView?.onPause()
    }

    override fun onResume() {
        mPlayerView?.onResume()
    }

    override fun onDestroy() {
        simpleExoPlayer!!.release()
        mPlayerView = null
        simpleExoPlayer = null
    }

}