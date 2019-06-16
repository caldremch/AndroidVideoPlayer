package com.caldremch.androidvideoplayer.Activity

import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import androidx.camera.core.*
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.uitls.AutoFitPreviewBuilder
import com.caldremch.androidvideoplayer.widget.camera.camera1.CameraManager
import com.caldremch.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : BaseActivity() {

    private var preView:Preview? =null
    private var imageCapture:ImageCapture? =null
    private var imageAnalysis:ImageAnalysis? =null

    override fun getLayoutId(): Int {
        return R.layout.activity_camera;
    }

    override fun initView() {
        textureView.post {
            bindCamera()
        }
    }

    private fun bindCamera() {
        CameraX.unbindAll()
        val metrics = DisplayMetrics().also {
            textureView.display.getRealMetrics(it)
        }
        Log.d(javaClass.simpleName, "Metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)

        val textureViewConfigy = PreviewConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.BACK)
            setTargetAspectRatio(screenAspectRatio)
            setTargetRotation(textureView.display.rotation)
        }.build()
        preView = AutoFitPreviewBuilder.build(textureViewConfigy, textureView)

        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.BACK)
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            setTargetAspectRatio(screenAspectRatio)
            setTargetRotation(textureView.display.rotation)
        }.build()
        imageCapture = ImageCapture(imageCaptureConfig)


        CameraX.bindToLifecycle(this, preView, imageCapture, imageAnalysis)
    }
}
