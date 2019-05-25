package com.caldremch.androidvideoplayer.Activity

import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.FrameLayout
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.flowplay.SingletonPlayerView
import com.caldremch.androidvideoplayer.flowplay.VideoFloatController
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.uitls.asset.AssetUtils
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.utils.DensityUtil
import com.caldremch.common.utils.MetricsUtils
import com.gyf.barlibrary.ImmersionBar
import kotlinx.android.synthetic.main.activity_player_demo.*

/**
 * @author Caldremch
 * @date 2019-04-21 11:13
 * @email caldremch@163.com
 * @describe
 */
class PlayerDemoActivity : BaseActivity() {

    private lateinit var playerView: SingletonPlayerView

    override fun getLayoutId(): Int {
        return R.layout.activity_player_demo
    }

    override fun initView() {
        playerView = VideoFloatController.instance.mMainView!!

        if (playerView.parent != null){
            if (playerView.parent is ViewGroup){
                (playerView.parent as ViewGroup).removeView(playerView)
            }else{
                VideoFloatController.instance.close()
            }
        }

        val para = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        playerView.layoutParams = para
        flContainer.addView(playerView)
        val file = AssetUtils.getAssetFile(context = mContext, fileName = "test.mp4")
        val uri = Uri.fromFile(file)
        playerView.startPlay(uri)

        handleKeyBord()
    }

    override fun onStop() {
        super.onStop()
        playerView.onStop()
    }

    override fun onResume() {
        super.onResume()
        playerView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerView.onDestroy()
    }

    private val visibleRect = Rect()
    private var visibleHeight: Int = 0
    private fun handleKeyBord() {
        val vp: ViewGroup = findViewById(android.R.id.content)
        vp.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                vp.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                vp.getWindowVisibleDisplayFrame(visibleRect)
                if (visibleHeight == 0) {
                    visibleHeight = visibleRect.bottom
                } else if (visibleHeight - visibleRect.bottom > 200) {
                    visibleHeight = visibleRect.bottom
                    val lp = editText.getLayoutParams() as ViewGroup.MarginLayoutParams
                    lp.bottomMargin = getScreenHeight(mContext) - visibleHeight + DensityUtil.dp2px(15f) + ImmersionBar.getNavigationBarHeight(mContext)
                    editText.requestLayout()
                } else if (visibleRect.bottom - visibleHeight > 200) {
                    visibleHeight = visibleRect.bottom
                    val lp = editText.getLayoutParams() as ViewGroup.MarginLayoutParams
                    lp.bottomMargin = +ImmersionBar.getNavigationBarHeight(mContext)
                    editText.requestLayout()
                }
                vp.getViewTreeObserver().addOnGlobalLayoutListener(this)
            }
        })

    }

    fun getScreenHeight(context: Context): Int {
        var result = 0

        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (manager != null) {
            val metrics = DisplayMetrics()
            manager.defaultDisplay.getMetrics(metrics)
            result = metrics.heightPixels
        }

        return result
    }

    fun closeToWindow(view: View) {}
}
