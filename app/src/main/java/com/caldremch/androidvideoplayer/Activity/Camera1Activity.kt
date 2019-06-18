package com.caldremch.androidvideoplayer.Activity

import android.content.Context
import android.hardware.display.DisplayManager
import android.media.MediaRecorder
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.widget.camera.camera1.Camera1View
import com.caldremch.androidvideoplayer.widget.camera.camera1.CameraManager
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.widget.WxRecordBtn
import com.gyf.barlibrary.ImmersionBar
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_camera.rootCl
import kotlinx.android.synthetic.main.activity_camera1.*

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

    private var lensFacing = Camera1View.LensFacing.BACK
    private lateinit var cameraManager:CameraManager
    private lateinit var displayManager: DisplayManager
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int){
            val display = displayManager.getDisplay(displayId)
            CLog.d("[onDisplayChanged] $display")

        }

    }

    override fun compatStatusBar(isDarkFont: Boolean) {
        transparentNavigationBar()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_camera1
    }

    override fun initView() {
        cameraView.post {
            cameraManager = CameraManager(cameraView)
        }
        handleControllerView()
        displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        displayManager.registerDisplayListener(displayListener, null)
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
            if(lensFacing == Camera1View.LensFacing.FRONT){
                lensFacing = Camera1View.LensFacing.BACK
//                cameraView.switchCamera(Camera1View.LensFacing.BACK)
            }else{
                lensFacing = Camera1View.LensFacing.FRONT
//                cameraView.switchCamera(Camera1View.LensFacing.FRONT)
            }
        }

    }

    override fun isAlwaysPortrait(): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        displayManager.unregisterDisplayListener(displayListener)
        cameraManager.release()
    }

}