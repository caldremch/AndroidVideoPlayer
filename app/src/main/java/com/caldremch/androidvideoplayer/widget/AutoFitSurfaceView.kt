package com.caldremch.androidvideoplayer.widget

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

    fun setAspectRatio(width:Int, height:Int){

        if (width<0 || height < 0){
            throw IllegalArgumentException("Size cannot be negative")
        }

        mRatioWidth = width
        mRatioHeight = height
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        if( 0 == mRatioWidth || 0 == mRatioHeight){
            //默认
            setMeasuredDimension(width, height)
        }else{

            //按比例
//            width/height=  mRatioWidth/mRatioHeight
//            width*mRatioHeight = mRatioWidth*height



            if (width<(height*mRatioWidth)/mRatioHeight){
                setMeasuredDimension(width, width*mRatioHeight/mRatioWidth)
            }else{
                setMeasuredDimension((height*mRatioWidth)/mRatioHeight, height)
            }
        }
    }


}