package com.caldremch.androidvideoplayer.Activity

import android.content.Context
import android.hardware.display.DisplayManager
import android.media.MediaRecorder
import android.os.Handler
import android.os.HandlerThread
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.view.TextureView
import android.view.View
import androidx.camera.core.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.uitls.AutoFitPreviewBuilder
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.uitls.LuminosityAnalyzer
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.widget.WxRecordBtn
import com.gyf.barlibrary.ImmersionBar
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : BaseActivity() {

    private lateinit var viewFinder: TextureView

    private var preView: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null

    private var displayId = -1
    private var lensFacing = CameraX.LensFacing.BACK

    private lateinit var displayManager: DisplayManager

    override fun compatStatusBar(isDarkFont: Boolean) {
        mBar.transparentNavigationBar().init()
    }

    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = findViewById<View>(android.R.id.content)?.let { view ->
            if (displayId == this@CameraActivity.displayId) {
                preView?.setTargetRotation(view.display.rotation)
                imageCapture?.setTargetRotation(view.display.rotation)
                imageAnalyzer?.setTargetRotation(view.display.rotation)
            }
        } ?: Unit
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_camera;
    }

    override fun initView() {

        viewFinder = findViewById(R.id.textureView)
        displayManager = viewFinder.context.getSystemService((Context.DISPLAY_SERVICE)) as DisplayManager
        displayManager.registerDisplayListener(displayListener, null)

        viewFinder.post {
            setCameraControl()
            bindCamera()
        }

    }

    private fun setCameraControl() {
        val controller = View.inflate(this, R.layout.camera_control, rootCl)

       val startBtn =  controller.findViewById<WxRecordBtn>(R.id.startBtn)

        if (ImmersionBar.hasNavigationBar(this)){
           val layoutPara:ConstraintLayout.LayoutParams = startBtn.layoutParams as ConstraintLayout.LayoutParams
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


    }

    override fun initEvent() {
        viewFinder.setOnClickListener {
            CLog.d("onClick")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        displayManager.unregisterDisplayListener(displayListener)
    }

    private fun bindCamera() {
        CameraX.unbindAll()
        val metrics = DisplayMetrics().also {
            viewFinder.display.getRealMetrics(it)
        }
        Log.d(javaClass.simpleName, "Metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)

        val textureViewConfigy = PreviewConfig.Builder().apply {
            setLensFacing(lensFacing)
            setTargetAspectRatio(screenAspectRatio)
            setTargetRotation(viewFinder.display.rotation)
        }.build()

        preView = AutoFitPreviewBuilder.build(textureViewConfigy, textureView)

        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setLensFacing(lensFacing)
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            setTargetAspectRatio(screenAspectRatio)
            setTargetRotation(viewFinder.display.rotation)
        }.build()
        imageCapture = ImageCapture(imageCaptureConfig)

        val analysisConfig = ImageAnalysisConfig.Builder().apply {
            setLensFacing(lensFacing)
            val analyzerThread = HandlerThread("MyAnalysis").apply { start() }
            setCallbackHandler(Handler(analyzerThread.looper))
            setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
            setTargetRotation(viewFinder.display.rotation)
        }.build()

        imageAnalyzer = ImageAnalysis(analysisConfig).apply {
            analyzer = LuminosityAnalyzer().apply {
                onFrameAnalyzed { luma ->
                    // Values returned from our analyzer are passed to the attached listener
                    // We log image analysis results here -- you should do something
                    // useful instead!
                    CLog.d("Average luminosity: $luma. " +
                            "Frames per second: ${"%.01f".format(framesPerSecond)}")
                }
            }
        }

        CameraX.bindToLifecycle(this, preView, imageCapture, imageAnalyzer)
    }
}
