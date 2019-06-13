package com.caldremch.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 *
 * @author Caldremch
 *
 * @date 2019-06-13 17:26
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
class WxRecordBtn : View {

    private lateinit var paint:Paint

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init();
    }

    private fun init() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.GRAY
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawCircle((width/2).toFloat(), (height/2).toFloat(), (width/2).toFloat(), paint)
        paint.color = Color.WHITE
        canvas?.drawCircle((width/2).toFloat().toFloat(), (height/2).toFloat(), (width/2).toFloat()-(height/(2*4)).toFloat(), paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var finalWidth:Int = getMeasre(widthMeasureSpec)
        var finalHeight:Int = getMeasre(heightMeasureSpec)
        setMeasuredDimension(finalWidth, finalHeight)
    }

    private fun getMeasre(measureSpec: Int): Int {

        var size:Int  = 200;

        var mode = MeasureSpec.getMode(measureSpec);

        when(mode){

            MeasureSpec.UNSPECIFIED ->{
                size = 200;
            }

            MeasureSpec.AT_MOST ->{
                size = MeasureSpec.getSize(measureSpec)
            }

            MeasureSpec.EXACTLY ->{
                size = MeasureSpec.getSize(measureSpec)
            }
        }

        return size;
      }


    private var downTime:Long = 0;
    private var upTime:Long = 0;
    private val recordTime:Long = 10*1000;

//    private var handler = Handler{
//
//
//    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                downTime = System.currentTimeMillis();
                handler
            }
            MotionEvent.ACTION_UP->{
                upTime = System.currentTimeMillis();

                if(upTime - downTime > 1000){
                    //录制成功
                }

            }
        }

        return false

    }

}