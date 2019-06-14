package com.caldremch.androidvideoplayer.widget.camera.camera1

import android.content.Context
import android.hardware.Camera
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.Toast
import com.caldremch.androidvideoplayer.uitls.CLog
import java.lang.Exception
import java.util.*
import kotlin.Comparator

/**
 * @author Caldremch
 * @date 2019-06-14 16:44
 * @email caldremch@163.com
 * @describe
 */
class Carmera1SurfaceView : SurfaceView , SurfaceHolder.Callback {

    private var surfaceHolder: SurfaceHolder? = null
    private lateinit var mCamera: Camera

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        surfaceHolder = holder
        surfaceHolder!!.addCallback(this)
    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mCamera
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {

        val cameraList = Camera.getNumberOfCameras()
        CLog.d("Camera.getNumberOfCameras = $cameraList")
        if (cameraList == 0) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            return
        }

        for(inx:Int in 0..cameraList){
            try {
                mCamera = Camera.open()
                setCameraParameters()
                mCamera.startPreview()
            }catch (e:Exception){
                e.printStackTrace()
            }

            return
        }
    }


    private fun setCameraParameters() {
        var parameters: Camera.Parameters = mCamera.parameters

        //设置最大分辨率
        var maxCameraSize: Camera.Size = Collections.max(mCamera.parameters.supportedPreviewSizes, object : Comparator<Camera.Size> {
            override fun compare(o1: Camera.Size?, o2: Camera.Size?): Int {
                return o1?.width!!.times(o1.height) - o2?.width!!.times(o2.height)
            }
        })

        CLog.d("width = $width height = $height")
        parameters.setPreviewSize(maxCameraSize.width, maxCameraSize.height)

        //分辨率要按照支持的来设置
//
//        var maxPicSize: Camera.Size = Collections.max( mCamera.parameters.supportedPictureSizes, object : Comparator<Camera.Size> {
//            override fun compare(o1: Camera.Size?, o2: Camera.Size?): Int {
//                return o1?.width!!.times(o1.height) - o2?.width!!.times(o2.height)
//            }
//        })
//        parameters.setPictureSize(maxPicSize.width, maxPicSize.height)

        if (Camera.Parameters.FOCUS_MODE_AUTO in parameters.supportedFocusModes) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        }

        mCamera.parameters = parameters

    }


}
