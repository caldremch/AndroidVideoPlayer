package com.caldremch.androidvideoplayer.widget

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.AttributeSet
import android.util.Size
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.uitls.CompareSizesByArea
import com.caldremch.common.utils.DensityUtil
import java.util.*
import kotlin.math.abs

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
    var nativeSizes:Array<Size>? = null

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

    @SuppressLint("MissingPermission")
    private fun initCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            mainHalder = Handler(Looper.getMainLooper())
            mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

            try {

                /**
                 * 获取相机id
                 */
                val cameraList = mCameraManager!!.cameraIdList
                if (cameraList.size == 0) {
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                }

                CLog.d("getCameraIdList==>" + Arrays.toString(cameraList))

                for (cameraId in mCameraManager!!.cameraIdList) {

                    //获取相机相关参数
                    val characteristics = mCameraManager!!.getCameraCharacteristics(cameraId)


                    val streamConfigurationMap: StreamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                            ?: continue

                    //获取最大的size, 支持的最大分辨率
                    //获取相机支持的分辨率
                     nativeSizes = streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)



                    //

   /*                 [4032x3024,
                        4000x3000,
                        4032x2268,
                        4032x2016,
                        3840x2160,
                        2880x2156,
                        2688x1512,
                        2592x1940,
                        2592x1458,
                        2592x1296,
                        1920x1440,
                        1920x1080,
                        1600x1200,
                        1280x960,
                        1280x720,
                        1280x640,
                        800x600,
                        720x480,
                        640x480,
                        640x360,
                        352x288,
                        320x240,
                        176x144]
*/
                    CLog.d("nativeSizes--->:${Arrays.toString(nativeSizes)}")


                    var ratio = width.toDouble()/ height.toDouble();
                    CLog.d("currentRatio--->:$height / $width = $ratio")
                    CLog.d("当前手机分辨率--->:${DensityUtil.getScreenWidth(context)} / ${DensityUtil.getScreenHeight(context)}")

                    var bigEng:Size? = null;
                    //获取足够大的
                    for (size in (nativeSizes as Array<out Size>?)!!){
                        var thisRatio = size.height.toDouble()/size.width.toDouble()
                        CLog.d("thisRatio: ${size.width}/${size.height} --->:$thisRatio")
                        if(thisRatio == ratio){
                            bigEng = size;
                            break
                        }
                    }

                    var bestSize = getBestSize();


                    //设置预览 view
//                    setAspectRatio(bestSize.width, bestSize.height)

                    CLog.d("bestSize--->:${bestSize?.width}---${bestSize?.height}")
                    CLog.d("bigEng--->:${bigEng?.width}---${bigEng?.height}")



                    //取出最合适的预览尺寸
//                    setAspectRatio()

                    var largetSize:Size = Collections.max(
                            Arrays.asList(*streamConfigurationMap.getOutputSizes(ImageFormat.JPEG)),
                            CompareSizesByArea()
                    )

                    CLog.d("最大预览:${largetSize.width}---${largetSize.height}")


                    imageReader = ImageReader.newInstance(largetSize.width, largetSize.height, ImageFormat.JPEG, 7)
                    imageReader?.setOnImageAvailableListener(onImageAvailableListener, mainHalder)




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

    fun getBestSize(): Size {

        var bestSize = Size(0, 0);
        var firstSize = nativeSizes?.get(0);
        var diff = abs(firstSize!!.width-width) + abs(firstSize!!.height-height)
        for (s in nativeSizes!!){
            var tempDiff = abs(s!!.width-width) + abs(s!!.height-height);
            CLog.d("每次比较--->:$tempDiff")
            if (tempDiff< diff){
                diff = tempDiff;
                bestSize = s;
            }
        }
        return bestSize;
//        return Collections.min(Arrays.asList(*supportSize), ClosestComp(width, height));
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


//    fun getSupportSizes(){
//        //获取相机相关参数
//        val characteristics = mCameraManager!!.getCameraCharacteristics(cameraId)
//
//
//        val streamConfigurationMap: StreamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
//                ?: continue
//
//        //获取最大的size, 支持的最大分辨率
//        //获取相机支持的分辨率
//        val nativeSizes = streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)
//
//
//
//    }


}
