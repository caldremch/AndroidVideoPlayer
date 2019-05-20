package com.caldremch.androidvideoplayer.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Size
import android.view.TextureView
import android.widget.FrameLayout
import com.caldremch.androidvideoplayer.uitls.CLog
import kotlinx.android.synthetic.main.activity_record.view.*
import kotlin.math.min

/**
 *
 * @author Caldremch
 *
 * @date 2019-05-20 18:57
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/

class CameraView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val surfaceView = MediaRecordSurfaceView(context)
    private lateinit var preViewSize: Size

    init {
        addView(surfaceView)
    }


    fun setPreview(size: Size) {
        post {
            preViewSize = size
            requestLayout()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        if (isInEditMode || !::preViewSize.isInitialized) {
            super.onLayout(changed, left, top, right, bottom)
        } else {
            layoutSurfaceView(preViewSize)
        }

    }

    private fun layoutSurfaceView(preViewSize: Size) {

//        var supporSize = cameraView.

        val scale = min(this.measuredWidth / preViewSize.width.toFloat(), this.measuredHeight / preViewSize.height.toFloat())

        CLog.d("scale = $scale")
        val w = (preViewSize.width * scale).toInt()
        val h = (preViewSize.height * scale).toInt()

        CLog.d("w = $w && $h")


        val extraX = Math.max(0, w - this.measuredWidth )
        val extraY = Math.max(0, h - this.measuredHeight)

        CLog.d("extraX&extraY  = $extraX && $extraY")

        var rect = Rect(-extraX / 2, -extraY / 2, w - extraX / 2, h - extraY / 2)

        (0 until childCount).forEach {
            getChildAt(it).layout(rect.left, rect.top, rect.right, rect.bottom)
        }

    }


}