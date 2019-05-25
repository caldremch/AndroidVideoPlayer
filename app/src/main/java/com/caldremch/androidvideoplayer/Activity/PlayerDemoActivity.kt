package com.caldremch.androidvideoplayer.Activity

import android.net.Uri
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.flowplay.SingletonPlayerView
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.uitls.asset.AssetUtils
import com.caldremch.common.base.BaseActivity

/**
 * @author Caldremch
 * @date 2019-04-21 11:13
 * @email caldremch@163.com
 * @describe
 */
class PlayerDemoActivity : BaseActivity() {

    private var playerView: SingletonPlayerView? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_player_demo
    }

    override fun initView() {
        playerView = findViewById(R.id.playerView)
        var file = AssetUtils.getAssetFile(context = mContext, fileName = "test.mp4")
        var uri = Uri.fromFile(file)
        playerView?.startPlay(uri)
    }

    override fun onStop() {
        super.onStop()
        CLog.d("onStop")
        playerView?.onStop()
    }

    override fun onResume() {
        super.onResume()
        playerView?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerView?.onDestroy()
    }
}
