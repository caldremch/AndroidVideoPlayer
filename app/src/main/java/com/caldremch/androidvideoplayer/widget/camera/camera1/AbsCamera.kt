package com.caldremch.androidvideoplayer.widget.camera.camera1

import android.view.SurfaceView

/**
 * @author Caldremch
 * @date 2019-06-11 17:01
 * @email caldremch@163.com
 * @describe
 */
abstract class AbsCamera : ICamera{

    protected lateinit var mSurfaceView: SurfaceView


    constructor(mSurfaceView: SurfaceView) {
        this.mSurfaceView = mSurfaceView
    }
}
