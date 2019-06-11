package com.caldremch.androidvideoplayer.widget.camera2

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Size
import android.widget.FrameLayout
import com.caldremch.androidvideoplayer.uitls.CLog
import kotlin.math.max

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
    private lateinit var preViewSize: PreViewSzie

    init {
        addView(surfaceView)
    }

    class PreViewSzie(var width: Int, var height: Int) {

    }

    fun setPreview(size: Size) {

        CLog.d("设置的当前最适合的 size: ${size.height} x ${size.width}")

        post {
            preViewSize = PreViewSzie(size.height, size.width);

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

    private fun layoutSurfaceView(preViewSize: PreViewSzie) {

//        var supporSize = cameraView.

        CLog.d("measuredWidth = $measuredWidth, measuredHeight=$measuredHeight")

        val scale = max(this.measuredWidth / preViewSize.width.toFloat(), this.measuredHeight / preViewSize.height.toFloat())

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