package com.caldremch.androidvideoplayer.Activity

import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.widget.camera.camera1.CameraManager
import com.caldremch.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_camera;
    }

    override fun initView() {
//        CameraManager(surfaceView).open(0)
    }
}
