package com.caldremch.androidvideoplayer.widget;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.caldremch.androidvideoplayer.uitls.CLog;

import java.util.Arrays;
import java.util.List;

/**
 * @author Caldremch
 * @date 2019-04-29 08:48
 * @email caldremch@163.com
 * @describe
 **/
public class MediaRecordSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private SurfaceHolder mHolder;
    private CameraDevice mCamera;
    private CameraManager mCameraManager;

    public MediaRecordSurfaceView(Context context) {
        super(context);
        initView();
    }

    private void initView() {

      mHolder = getHolder();
      mHolder.addCallback(this);

    }

    public MediaRecordSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public MediaRecordSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //初始化 Camera
        //this.mCamera = mCameraManager.
        initCamera();

    }

    private void initCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);

            try {

                String[]cameraList = mCameraManager.getCameraIdList();

                if (cameraList.length == 0){
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }

                CLog.d("getCameraIdList==>"+ Arrays.toString(cameraList));

                for (String cameraId: mCameraManager.getCameraIdList()){

                    //获取相机相关参数
                    CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                    //检查灯光是否支持
                    Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);

                    boolean real = available != null && available;
                    CLog.d("是否支持闪光:"+real);

                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }else{
            //不支持 Camer2Api 的使用
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    private void releaseCamera() {



    }

    @Override
    public void run() {

    }
}
