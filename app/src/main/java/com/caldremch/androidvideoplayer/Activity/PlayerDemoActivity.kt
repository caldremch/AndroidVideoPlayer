package com.caldremch.androidvideoplayer.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.FrameLayout
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.flowplay.*
import com.caldremch.androidvideoplayer.uitls.asset.AssetUtils
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.utils.DensityUtil
import com.caldremch.common.utils.EventManager
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



        //todo 待实现, 这里需要注意的是 , 当从不同的视频播放起来的时候, 这个时候就不能复用 窗口的播放资源了

        playerView = VideoFloatController.instance.mMainView!!

        if (playerView.parent != null) {
            //先判断是否有父布局添加
            if (playerView.parent is ViewGroup) {
                (playerView.parent as ViewGroup).removeView(playerView)
            } else {
                VideoFloatController.instance.close()
            }
        }


        val para = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        playerView.layoutParams = para
        flContainer.addView(playerView)


        /**
         * 从窗口点击过来
         */
        val flag = intent.flags;

        if (flag != Intent.FLAG_ACTIVITY_NEW_TASK){
//            val file = AssetUtils.getAssetFile(context = mContext, fileName = "test.mp4")
//            val uri = Uri.fromFile(file)
            playerView.startPlay(Uri.parse("http://jdvod3btj0kfg.vod.126.net/jdvod3btj0kfg/82c1cbbd5de14f899ce14f1ee3536ab9_1557475222116_1557481210392_2027250991-00000.mp4"))
        }

        playerView.onState(MainViewStatus.NORMAL)

        handleKeyBord()
    }

    override fun onStop() {
        super.onStop()
        //开启小窗口 关闭Activity时 不暂停播放器
        if (finishToReleaseResource) {
            playerView.onStop()
        }
    }

    override fun onResume() {
        super.onResume()
        playerView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        //开启小窗口 关闭Activity时 不释放播放器
        if (finishToReleaseResource) {
            playerView.onDestroy()
        }
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

    fun closeToWindow(view: View) {
        if (!FloatPermission.isFlowViewPermissionGranted(this)) {
            FloatPermission.toAppDetail()
            return
        }
        EventManager.post(OpenFloatWindowEvent())
        finishToReleaseResource = false
        finish()
    }

    private var finishToReleaseResource: Boolean = true

}
