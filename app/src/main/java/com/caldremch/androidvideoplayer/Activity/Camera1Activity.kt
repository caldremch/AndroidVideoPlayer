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
import com.caldremch.androidvideoplayer.widget.camera.CameraSize
import com.caldremch.androidvideoplayer.widget.camera.camera1.Camera1View
import com.caldremch.androidvideoplayer.widget.camera.camera1.CameraManager
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.utils.DensityUtil
import com.caldremch.common.widget.WxRecordBtn
import com.gyf.barlibrary.ImmersionBar
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_camera.rootCl
import kotlinx.android.synthetic.main.activity_camera1.*
import java.lang.Exception

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

    //视频录制相关
    private var mediaRecorder: MediaRecorder? = null

    private var lensFacing = Camera1View.LensFacing.BACK
    private lateinit var cameraManager: CameraManager
    private lateinit var displayManager: DisplayManager
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) {
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
        //controller 放这里, 才能显示
        cameraView.post {
            cameraManager = CameraManager(cameraView)
            handleControllerView()
        }
        displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        displayManager.registerDisplayListener(displayListener, null)
    }

    private lateinit var controllerCl: ConstraintLayout
    private fun handleControllerView() {

        val controller = View.inflate(this, R.layout.camera_control, rootCl)
        controllerCl = controller.findViewById<ConstraintLayout>(R.id.controllerCl)
        controllerCl.elevation = 10f
        val startBtn = controller.findViewById<WxRecordBtn>(R.id.startBtn)

        if (ImmersionBar.hasNavigationBar(this)) {
            val layoutPara: ConstraintLayout.LayoutParams = startBtn.layoutParams as ConstraintLayout.LayoutParams
            layoutPara.bottomMargin += ImmersionBar.getNavigationBarHeight(this)
            startBtn.layoutParams = layoutPara
        }

        startBtn.setListener(object : WxRecordBtn.OnClick {
            override fun onRecordStart() {

                try {
                    mediaRecorder = MediaRecorder().apply {

                        cameraManager.unLock()

                        setVideoSource(MediaRecorder.VideoSource.CAMERA)
                        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                        setVideoEncoder(MediaRecorder.VideoEncoder.H264)

                        setVideoFrameRate(30)
                        setVideoEncodingBitRate(3 * 1024 * 1024)
                        val size = cameraManager.getCloseSupportVideoSize()
                        CLog.d("[onRecordStart] = ${size?.width} ${size?.height}")
                        size?.let {
                            setVideoSize(size.height, size.width)
                        }
                        setPreviewDisplay(cameraManager.getSurface())
                        setOutputFile("/storage/emulated/0/Android/my.mp4")
                        prepare()
                        start()
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }


            }

            override fun onRecordEnd() {
                mediaRecorder?.let {
                    it.stop()
                    it.reset()
                    it.release()

                }
                mediaRecorder = null

            }

            override fun takePic() {
                CLog.d("[takePic]")
            }

        })

        //切换
        controller.findViewById<AppCompatImageButton>(R.id.swicthBtn).setOnClickListener {
            cameraManager.swithCamera()
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