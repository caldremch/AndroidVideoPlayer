package com.caldremch.androidvideoplayer.widget.camera

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView
import java.lang.IllegalArgumentException

/**
 * @author Caldremch
 *
 * @date 2019-05-12 16:46
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/

open class AutoFitSurfaceView : SurfaceView {

    private var mRatioWidth = 0;
    private var mRatioHeight = 0;

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }


}