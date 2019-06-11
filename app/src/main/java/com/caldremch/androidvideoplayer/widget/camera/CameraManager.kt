package com.caldremch.androidvideoplayer.widget.camera

import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*
import kotlin.Comparator

/**
 * @author Caldremch
 * @date 2019-06-11 16:59
 * @email caldremch@163.com
 * @describe
 */
class CameraManager(surfaceView: SurfaceView) : AbsCamera(surfaceView) {


    private lateinit var mCamera: Camera
    private lateinit var surfaceHolder: SurfaceHolder


    init {
        surfaceHolder = surfaceView.holder
    }

    override fun init() {

    }


    override fun open(cameraId: Int) {
        if (!openCamera(cameraId)) return
        setCameraParameters()
        setPreView()
        mCamera.setDisplayOrientation(0)
        mCamera.startPreview()
    }

    private fun setCameraParameters() {
        var parameters: Camera.Parameters = mCamera.parameters

        //设置最大分辨率
        var maxCameraSize: Camera.Size = Collections.max(mCamera.parameters.supportedPreviewSizes, object : Comparator<Camera.Size> {
            override fun compare(o1: Camera.Size?, o2: Camera.Size?): Int {
                return o1?.width!!.times(o1.height) - o2?.width!!.times(o2.height)
            }
        })

        parameters.setPreviewSize(maxCameraSize.width, maxCameraSize.height)

        if (Camera.Parameters.FOCUS_MODE_AUTO in parameters.supportedFocusModes) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        }

        mCamera.parameters = parameters

    }


    fun setPreView() {
        mCamera.setPreviewDisplay(surfaceHolder)
    }

    /**
     * 检查相机id
     */
    private fun checkCameraId(cameraId: Int): Boolean {
        return cameraId >= 0 && cameraId < Camera.getNumberOfCameras()
    }

    //打开相机实例
    private fun openCamera(cameraId: Int): Boolean {
        if (!checkCameraId(cameraId)) return false;
        mCamera = Camera.open(cameraId);
        return true;
    }


    override fun takePicture() {
    }
}
