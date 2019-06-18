package com.caldremch.androidvideoplayer.widget.camera.camera1

import android.content.Context
import android.content.res.Configuration
import android.hardware.Camera
import android.util.AttributeSet
import android.util.Size
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.Toast
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.widget.camera.CameraSize
import com.caldremch.androidvideoplayer.widget.camera.CameraUtils
import java.lang.Exception
import java.util.*
import kotlin.Comparator

/**
 * @author Caldremch
 * @date 2019-06-14 16:44
 * @email caldremch@163.com
 * @describe
 */
class Carmera1SurfaceView : SurfaceView, SurfaceHolder.Callback {

    private var surfaceHolder: SurfaceHolder? = null
    private lateinit var mCamera: Camera
    private lateinit var cameraSupportPreSize:MutableList<CameraSize>

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
        cameraSupportPreSize = arrayListOf()
        surfaceHolder = holder
        surfaceHolder!!.addCallback(this)
        try {
            mCamera = Camera.open()
        } catch (e: Exception) {
            e.printStackTrace()
            CLog.d("开始相机失败")
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        CLog.d("----surfaceChanged----")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        CLog.d("----surfaceDestroyed----")
        mCamera.release()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        CLog.d("----surfaceCreated----")
        surfaceHolder = holder
        try {
            handleRotation()
            handlePreView()
            handleFocus()
            mCamera.setPreviewDisplay(holder);
        } catch (e: Exception) {
            e.printStackTrace()
            CLog.d("surfaceCreated失败")
        }

    }

    fun startPreview(){
        mCamera.startPreview();
    }

    fun getSupportPreViewSize(): MutableList<CameraSize>{
        return cameraSupportPreSize;
    }

    private fun handleFocus() {
        /* Set Auto focus */
        val parameters = mCamera.parameters
        val focusModes = parameters.supportedFocusModes
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        }
        mCamera.parameters = parameters
    }

    private fun handlePreView() {
        var parameters: Camera.Parameters = mCamera.parameters

        if (cameraSupportPreSize.isEmpty()){
            CameraUtils.transSize(parameters.supportedPreviewSizes, cameraSupportPreSize)
            CLog.d("[transSize] succ ${cameraSupportPreSize.size}")
        }

        val preViewSize = getPropPreviewSize(parameters.supportedPreviewSizes)
//        var preViewSize: Camera.Size = Collections.max(mCamera.parameters.supportedPreviewSizes, object : Comparator<Camera.Size> {
//            override fun compare(o1: Camera.Size?, o2: Camera.Size?): Int {
//                return o1?.width!!.times(o1.height) - o2?.width!!.times(o2.height)
//            }
//        })

        CLog.d("preViewSize = ${preViewSize.width} , ${preViewSize.height}")
        mCamera.parameters.also {
            it.setPreviewSize(preViewSize.height, preViewSize.width)
        }

//        var maxPicSize: Camera.Size = Collections.max(mCamera.parameters.supportedPictureSizes, object : Comparator<Camera.Size> {
//            override fun compare(o1: Camera.Size?, o2: Camera.Size?): Int {
//                return o1?.width!!.times(o1.height) - o2?.width!!.times(o2.height)
//            }
//        })
//        CLog.d("maxPicSize = ${maxPicSize.width} , ${maxPicSize.height}")
//        mCamera.parameters.also {
//            it.setPictureSize(maxPicSize.height, maxPicSize.width)
//        }
    }

    private fun handleRotation() {
        if (resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mCamera.parameters.also {
                it.set("orientation", "portrait")
                mCamera.setDisplayOrientation(90)
                it.setRotation(90)
            }

        } else {
            mCamera.parameters.also {
                it.set("orientation", "landscape")
                mCamera.setDisplayOrientation(0)
                it.setRotation(0)
            }
        }
    }

    private fun getPropPreviewSize(supportedPreviewSizes: List<Camera.Size>): Camera.Size {
        val ratio = 0.1f
        val widthHeightRatio = 1080 * 1.0f / 1920
        var maxWidth = 0
        var sizeResult: Camera.Size = supportedPreviewSizes[0]
        for (size in supportedPreviewSizes) {
            if (Math.abs(widthHeightRatio - size.width * 1.0f / size.height) < ratio && size.width > maxWidth) {
                sizeResult = size
                maxWidth = size.width
            }
        }
        return sizeResult
    }


}
