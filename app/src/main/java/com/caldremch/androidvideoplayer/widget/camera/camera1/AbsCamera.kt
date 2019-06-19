package com.caldremch.androidvideoplayer.widget.camera.camera1

import android.content.Context
import android.content.res.Configuration
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.widget.camera.CameraSize
import com.caldremch.androidvideoplayer.widget.camera.CameraUtils
import java.util.*
import kotlin.Comparator

/**
 * @author Caldremch
 * @date 2019-06-11 17:01
 * @email caldremch@163.com
 * @describe
 */
abstract class AbsCamera : ICamera, SurfaceHolder.Callback {

    protected lateinit var mSurfaceView: SurfaceView

    constructor(mSurfaceView: SurfaceView) {
        this.mSurfaceView = mSurfaceView
    }

    protected fun handleFocus(camera:Camera) {
        /* Set Auto focus */
        val parameters = camera.parameters
        val focusModes = parameters.supportedFocusModes
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        }
        camera.parameters = parameters
    }

    protected fun handlePreView(camera:Camera, cameraSupportPreSize:MutableList<CameraSize>) {
        var parameters: Camera.Parameters = camera.parameters
        if (cameraSupportPreSize.isEmpty()) {
            CameraUtils.transSize(parameters.supportedPreviewSizes, cameraSupportPreSize)
            CLog.d("[transSize] succ ${cameraSupportPreSize.size}")
        }
        val preViewSize = CameraUtils.getPreViewBestSize(cameraSupportPreSize)
        CLog.d("preViewSize = ${preViewSize.width} , ${preViewSize.height}")
        camera.parameters.also {
            it.setPreviewSize(preViewSize.width, preViewSize.height)
        }

        var maxPicSize: Camera.Size = Collections.max(camera.parameters.supportedPictureSizes, object : Comparator<Camera.Size> {
            override fun compare(o1: Camera.Size?, o2: Camera.Size?): Int {
                return o1?.width!!.times(o1.height) - o2?.width!!.times(o2.height)
            }
        })
        CLog.d("maxPicSize = ${maxPicSize.width} , ${maxPicSize.height}")
        camera.parameters.also {
            it.setPictureSize(maxPicSize.height, maxPicSize.width)
        }
    }


    protected fun getAndSetBestPreviewSize(camera:Camera, cameraSupportPreSize:MutableList<CameraSize>):CameraSize {
        var parameters: Camera.Parameters = camera.parameters
        if (cameraSupportPreSize.isEmpty()) {
            CameraUtils.transSize(parameters.supportedPreviewSizes, cameraSupportPreSize)
            CLog.d("[transSize] succ ${cameraSupportPreSize.size}")
        }
        val preViewSize = CameraUtils.getPreViewBestSize(cameraSupportPreSize)
        CLog.d("preViewSize = ${preViewSize.width} , ${preViewSize.height}")
        camera.parameters.also {
            it.setPreviewSize(preViewSize.width, preViewSize.height)
        }

        var maxPicSize: Camera.Size = Collections.max(camera.parameters.supportedPictureSizes, object : Comparator<Camera.Size> {
            override fun compare(o1: Camera.Size?, o2: Camera.Size?): Int {
                return o1?.width!!.times(o1.height) - o2?.width!!.times(o2.height)
            }
        })
        CLog.d("maxPicSize = ${maxPicSize.width} , ${maxPicSize.height}")
        camera.parameters.also {
            it.setPictureSize(maxPicSize.height, maxPicSize.width)
        }

        return preViewSize
    }

    protected fun handleRotation(camera:Camera, context: Context) {
        if (context.resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            camera.parameters.also {
                it.set("orientation", "portrait")
                camera.setDisplayOrientation(90)
                it.setRotation(90)
            }

        } else {
            camera.parameters.also {
                it.set("orientation", "landscape")
                camera.setDisplayOrientation(0)
                it.setRotation(0)
            }
        }
    }

    protected fun getPropPreviewSize(supportedPreviewSizes: List<Camera.Size>): Camera.Size {
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
