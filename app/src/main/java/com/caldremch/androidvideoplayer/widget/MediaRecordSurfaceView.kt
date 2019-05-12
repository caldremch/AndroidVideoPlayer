package com.caldremch.androidvideoplayer.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.annotation.RequiresApi

import com.caldremch.androidvideoplayer.uitls.CLog
import java.lang.Exception

import java.util.Arrays

/**
 * @author Caldremch
 * @date 2019-04-29 08:48
 * @email caldremch@163.com
 * @describe
 */
class MediaRecordSurfaceView : AutoFitSurfaceView, SurfaceHolder.Callback, Runnable {

    private var surfaceHolder: SurfaceHolder? = null
    private var cameraDevice: CameraDevice? = null
    private var mCameraManager: CameraManager? = null
    private var previewBuilder: CaptureRequest.Builder? = null;
    private var cameraCaptureSession: CameraCaptureSession? = null;

    constructor(context: Context) : super(context) {
        initView()
    }

    private fun initView() {
        surfaceHolder = holder
        surfaceHolder!!.addCallback(this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        //初始化 Camera
        //this.cameraDevice = mCameraManager.
        initCamera()

    }

    private var backgroudHandler: Handler? = null
    private var backgroudThread: HandlerThread? = null
    private var imageReader: ImageReader? = null
    private var mainHalder: Handler? = null

    private fun initCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            mainHalder = Handler(Looper.getMainLooper())
            mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 7)
            imageReader?.setOnImageAvailableListener(onImageAvailableListener, mainHalder)


            try {

                val cameraList = mCameraManager!!.cameraIdList


                if (cameraList.size == 0) {
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                }

                CLog.d("getCameraIdList==>" + Arrays.toString(cameraList))

                for (cameraId in mCameraManager!!.cameraIdList) {

                    //获取相机相关参数
                    val characteristics = mCameraManager!!.getCameraCharacteristics(cameraId)


                    val streamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

                    //获取相机支持的分辨率
                    val nativeSizes = streamConfigurationMap?.getOutputSizes(SurfaceTexture::class.java)

                    //获取最合适的分辨率

                    //检查灯光是否支持
                    val available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)

                    val real = available != null && available
                    CLog.d("是否支持闪光:$real")

                    //线程
                    startBackgroundThread()

                    mCameraManager!!.openCamera(cameraId, object : CameraDevice.StateCallback() {
                        override fun onOpened(camera: CameraDevice) {
                            CLog.d("openCamera onOpened")
                            cameraDevice = camera;


                            previewBuilder = cameraDevice?.createCaptureRequest(TEMPLATE_PREVIEW);
                            previewBuilder!!.addTarget(surfaceHolder!!.surface)
                            cameraDevice?.createCaptureSession(Arrays.asList(surfaceHolder?.surface, imageReader?.surface), sessionCallback, backgroudHandler)

                        }

                        override fun onDisconnected(camera: CameraDevice) {
                            CLog.d("openCamera onDisconnected")
                            closeCamera()
                        }

                        override fun onError(camera: CameraDevice, error: Int) {
                            CLog.d("openCamera onError")
                            closeCamera()

                        }

                    }, backgroudHandler);


                    break;
                }
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }

        } else {
            //不支持 Camer2Api 的使用
        }

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        releaseCamera()
    }

    private fun releaseCamera() {

    }

    override fun run() {

    }


    private val sessionCallback: CameraCaptureSession.StateCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession) {

        }

        override fun onConfigured(session: CameraCaptureSession) {
            cameraCaptureSession = session;
            //自动对焦
            previewBuilder?.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            //打开闪光灯
            previewBuilder?.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            //重复获取图像
            session.setRepeatingRequest(previewBuilder!!.build(), null, backgroudHandler)
        }

    }

    private val onImageAvailableListener: ImageReader.OnImageAvailableListener = @TargetApi(Build.VERSION_CODES.KITKAT)
    object : ImageReader.OnImageAvailableListener {
        override fun onImageAvailable(reader: ImageReader?) {
            //相机存储
        }
    }


    /**
     * 关闭相机
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun closeCamera() {
        try {
            if (cameraCaptureSession != null){
                cameraCaptureSession?.close()
                cameraCaptureSession = null
            }

            if (cameraDevice != null){
                cameraDevice?.close()
                cameraDevice = null
            }

            if (imageReader != null){
                imageReader?.close()
                imageReader = null
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    //开启后台线程
    fun startBackgroundThread(){
        backgroudThread = HandlerThread("camera backgroud thread");
        backgroudThread?.start();
        backgroudHandler = Handler(backgroudThread?.looper)
    }

    fun stopBackgroundThread(){
        //将当前handle中的消息派发处理， 新添加的消息将不会再处理
        backgroudThread?.quitSafely()
        try {
            //此时等待backgroudThread处理所有剩下的消息后，再交换cpu控制权
            backgroudThread?.join()
            backgroudThread = null
            backgroudHandler = null
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}
