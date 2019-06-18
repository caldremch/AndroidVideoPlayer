package com.caldremch.androidvideoplayer.widget.camera.camera1

import android.content.Context
import android.content.res.Configuration
import android.hardware.Camera
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.camera.core.CameraInfo
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.widget.camera.CameraSize
import com.caldremch.androidvideoplayer.widget.camera.CameraUtils
import java.lang.Exception

/**
 * @author Caldremch
 * @date 2019-06-14 16:44
 * @email caldremch@163.com
 * @describe
 */
class Carmera1SurfaceView : SurfaceView, SurfaceHolder.Callback {

    private var surfaceHolder: SurfaceHolder? = null
    private lateinit var mCamera: Camera
    private lateinit var cameraSupportPreSize: MutableList<CameraSize>

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
        mCamera.stopPreview()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        CLog.d("----surfaceCreated----")
        surfaceHolder = holder
        try {
//            handleRotation()
//            handlePreView()
//            handleFocus()
            mCamera.setPreviewDisplay(holder);
            startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
            CLog.d("surfaceCreated失败")
        }

    }

    fun startPreview() {
        mCamera.startPreview();
    }

    fun getSupportPreViewSize(): MutableList<CameraSize> {
        return cameraSupportPreSize;
    }

    fun switchCamera(type: Camera1View.LensFacing) {
        mCamera.stopPreview()
        mCamera.release()
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
                return
            }
            CLog.d("[switchCamera] cameraFrontIndex = $cameraFrontIndex, cameraBackIndex = $cameraBackIndex")

            if (type == Camera1View.LensFacing.FRONT) {
                CLog.d("[switchCamera] 打开前置")
                mCamera = Camera.open(cameraFrontIndex)
            } else if (type == Camera1View.LensFacing.BACK){
                CLog.d("[switchCamera] 打开后置")
                mCamera = Camera.open(cameraBackIndex)
            }
//            handleRotation(mCamera, context)
//            handlePreView()
//            handleFocus()
            mCamera.setPreviewDisplay(surfaceHolder)
            mCamera.startPreview()

        } catch (e: Exception) {
            e.printStackTrace()
            CLog.d("[switchCamera] 开始相机失败")
        }

    }





}
