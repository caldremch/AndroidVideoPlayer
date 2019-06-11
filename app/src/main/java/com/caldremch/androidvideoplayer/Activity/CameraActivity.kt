package com.caldremch.androidvideoplayer.Activity

import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.widget.camera.CameraManager
import com.caldremch.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_camera;
    }

    override fun initView() {
        CameraManager(surfaceView).open(0)
    }
}
