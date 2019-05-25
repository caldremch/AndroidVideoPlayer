package com.caldremch.androidvideoplayer.flowplay

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.uitls.CLog
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
class SingletonPlayerView : FrameLayout, ILifeCycle{
    override fun onState(status: MainViewStatus) {
        if (status == MainViewStatus.NORMAL){
            mPlayerView?.showController()
            btnClose.visibility = View.GONE
        }else{
            btnClose.visibility = View.VISIBLE
            mPlayerView?.hideController()
        }
    }

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

        btnClose.setOnClickListener{
            onDestroy()
            VideoFloatController.instance.close()
        }

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

        /**
         * 1.这个SingletonPlayerView 就是个壳, Service已创建就一直存在,
         * 里面的 播放器(资源载体),  会有生命周期的变化
         * 比如:
         * 1.view被销毁, 播放器关闭之类的
         *
         * 所以在进入开始播放的时候, 需要判断是否是一个被销毁了,如果被销毁了,  需要重新初始化
         */
        if (mPlayerView == null || simpleExoPlayer == null){

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

        val dataFactory = DefaultDataSourceFactory(context, "caldremch")
        val videoSource = ExtractorMediaSource.Factory(dataFactory).createMediaSource(uri)
        simpleExoPlayer!!.prepare(videoSource)
        simpleExoPlayer!!.playWhenReady = true
    }


    override fun onStop() {
        mPlayerView?.controller!!.findViewById<View>(R.id.exo_pause).performClick()
    }

    override fun onResume() {
        mPlayerView?.controller!!.findViewById<View>(R.id.exo_play).performClick()
    }

    override fun onDestroy() {
        simpleExoPlayer!!.release()
        mPlayerView = null
        simpleExoPlayer = null
    }

}