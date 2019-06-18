package com.caldremch.androidvideoplayer.widget.camera.camera1

import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.widget.camera.CameraContainerView
import com.caldremch.androidvideoplayer.widget.camera.CameraSize
import com.caldremch.androidvideoplayer.widget.camera.CameraView
import java.lang.Exception
import java.util.*
import kotlin.Comparator

/**
 * @author Caldremch
 * @date 2019-06-11 16:59
 * @email caldremch@163.com
 * @describe
 */
class CameraManager(surfaceView: SurfaceView) : AbsCamera(surfaceView) {

    private val UNKNOW:Int = -1;

    private var mCamera: Camera? = null
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var cameraSupportPreSize: MutableList<CameraSize>
    private lateinit var bestPreViewSize: CameraSize

    private lateinit var autoFitContainer:CameraContainerView


    init {

        surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)
        cameraSupportPreSize = arrayListOf()
        bestPreViewSize = CameraSize(0,0)

        val parent:ViewGroup = surfaceView.parent as ViewGroup;
        parent.removeView(surfaceView)
        autoFitContainer =  CameraContainerView(surfaceView.context)
        autoFitContainer.addView(surfaceView)
        val para = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        parent.addView(autoFitContainer, para)
        surfaceView.post {
            init()
        }
    }

    override fun init() {
        open(getCameraId(Camera1View.LensFacing.BACK))
        //矫正拉伸
        CLog.d("bestPreViewSize= ${bestPreViewSize.width} ${bestPreViewSize.height}")
        autoFitContainer.setPreview(bestPreViewSize)
        mCamera?.startPreview()
    }

    fun getCameraId(type:Camera1View.LensFacing): Int{
        try {
            val cameraCount = Camera.getNumberOfCameras()
            var cameraInfo = Camera.CameraInfo()
            var cameraFrontIndex = -1;
            var cameraBackIndex = -1;
            for (ind in 0 until cameraCount) {
                Camera.getCameraInfo(ind, cameraInfo)
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    cameraFrontIndex = ind
                } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    cameraBackIndex = ind
                }
            }

            if (cameraBackIndex == -1 && cameraFrontIndex == -1) {
                CLog.d("[switchCamera] 没有照相机")
                return -1
            }

            if (type == Camera1View.LensFacing.FRONT) {
               return cameraFrontIndex
            } else if (type == Camera1View.LensFacing.BACK){
                return cameraBackIndex
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CLog.d("[switchCamera] 开始相机失败")
        }

        return UNKNOW
    }


    override fun open(cameraId: Int) {
        CLog.d("[open] $cameraId")
        //先停止预览
        mCamera?.let {
            it.stopPreview()
            it.release()
        }
        if (!openCamera(cameraId)) return
        mCamera?.let {
            CLog.d("[switchCamera] 开始预览")
            handleFocus(it)
            handleRotation(it, context = mSurfaceView.context)
            bestPreViewSize = getAndSetBestPreviewSize(it, cameraSupportPreSize)
            it.setPreviewDisplay(surfaceHolder)
        }
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

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        CLog.d("----surfaceChanged----")


    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        CLog.d("----surfaceDestroyed----")
        mCamera?.stopPreview()
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        CLog.d("----surfaceCreated----")
//        this.surfaceHolder = p0!!
        open(getCameraId(Camera1View.LensFacing.BACK))

    }

    fun release() {
        mCamera?.release()
    }
}
