package com.caldremch.androidvideoplayer.Activity

import android.media.MediaRecorder
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.widget.WxRecordBtn
import com.gyf.barlibrary.ImmersionBar
import kotlinx.android.synthetic.main.activity_camera.*

/**
 * @author Caldremch
 *
 * @date 2019-06-17 21:07
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
class Camera1Activity : BaseActivity() {

    override fun compatStatusBar(isDarkFont: Boolean) {
        transparentNavigationBar()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_camera1
    }

    override fun initView() {
        handleControllerView()
    }

    private fun handleControllerView() {

        val controller = View.inflate(this, R.layout.camera_control, rootCl)

        val startBtn =  controller.findViewById<WxRecordBtn>(R.id.startBtn)

        if (ImmersionBar.hasNavigationBar(this)){
            val layoutPara: ConstraintLayout.LayoutParams = startBtn.layoutParams as ConstraintLayout.LayoutParams
            layoutPara.bottomMargin += ImmersionBar.getNavigationBarHeight(this)
            startBtn.layoutParams = layoutPara
        }

        startBtn.setListener(object : WxRecordBtn.OnClick{
            override fun takePic() {
                CLog.d("[takePic]")
            }

            override fun recordVideo() {
                CLog.d("[recordVideo]")
                val mediaRecord = MediaRecorder()
                mediaRecord.reset()
            }

        })

        //切换
        controller.findViewById<AppCompatImageButton>(R.id.swicthBtn).setOnClickListener {

        }

    }

}