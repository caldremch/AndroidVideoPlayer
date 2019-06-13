package com.caldremch.common.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator

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

    private val TAG: String = WxRecordBtn::class.java.simpleName

    private lateinit var paint: Paint

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init();
    }

    private fun init() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.GRAY
    }


    private var rawRadius: Float = 0f;
    private var varRadius: Float = 0f;
    private var minRadius: Float = 0f;

    override fun onDraw(canvas: Canvas?) {
        paint.color = Color.GRAY
        canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), paint)
        paint.color = Color.WHITE
        rawRadius = (width / 2).toFloat() - (height / (2 * 4)).toFloat();
        minRadius = (rawRadius/4).toFloat()
        if (isRecord) {
            canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), varRadius, paint)
        } else {
            canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), rawRadius, paint)
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var finalWidth: Int = getMeasre(widthMeasureSpec)
        var finalHeight: Int = getMeasre(heightMeasureSpec)
        setMeasuredDimension(finalWidth, finalHeight)
    }

    private fun getMeasre(measureSpec: Int): Int {

        var size: Int = 200;

        var mode = MeasureSpec.getMode(measureSpec);

        when (mode) {

            MeasureSpec.UNSPECIFIED -> {
                size = 200;
            }

            MeasureSpec.AT_MOST -> {
                size = MeasureSpec.getSize(measureSpec)
            }

            MeasureSpec.EXACTLY -> {
                size = MeasureSpec.getSize(measureSpec)
            }
        }

        return size;
    }


    private var downTime: Long = 0;
    private var upTime: Long = 0;
    private val recordTime: Long = 10 * 1000;
    private val perTime: Long = 10 * 1000;
    private var seconds: Int = 0;
    private var isRecord: Boolean = false;

    private var animator: ValueAnimator? = null
    private var animatorBigger: ValueAnimator? = null
    private var wxBtnhandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            seconds++;
            if (seconds >= 1) {
                isRecord = true
//                varRadius = varRadius - (rawRadius-minRadius)/10
//                Log.d(TAG, "[handleMessage] $varRadius $rawRadius $minRadius ")

                if (animator == null){
                    animator = ObjectAnimator.ofFloat(rawRadius, (rawRadius-minRadius))
                }

                if (varRadius != (rawRadius-minRadius) && !animator?.isRunning!!){
                    animator?.interpolator = DecelerateInterpolator()
                    animator?.duration = 500
                    animator?.repeatCount = 0
                    animator?.start()
                    animator?.addUpdateListener {
                        var value:Float = it.getAnimatedValue() as Float
                        varRadius = value
                        postInvalidate()

                    }
                }


            }
            sendEmptyMessageDelayed(0, 1000)

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

//        Log.d(TAG, "[onTouchEvent] ${event?.action}")

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "[ACTION_DOWN] $seconds")
                downTime = System.currentTimeMillis();
                wxBtnhandler.sendEmptyMessage(0)
                varRadius = rawRadius
            }
            MotionEvent.ACTION_UP -> {
                upTime = System.currentTimeMillis();
                wxBtnhandler.removeCallbacksAndMessages(null)
                Log.d(TAG, "[ACTION_UP] $seconds")

                if (upTime - downTime > 1000) {
                    //录制
                    Log.d(TAG, "[ACTION_UP] 录制")

                    if (animatorBigger == null){
                        animatorBigger = ObjectAnimator.ofFloat((rawRadius-minRadius),rawRadius)
                    }

                    if (!animatorBigger?.isRunning!!){
                        animatorBigger?.interpolator = DecelerateInterpolator()
                        animatorBigger?.duration = 500
                        animatorBigger?.repeatCount = 0
                        animatorBigger?.start()
                        animatorBigger?.addUpdateListener {
                            var value:Float = it.getAnimatedValue() as Float
                            varRadius = value
                            postInvalidate()
                        }

                        animatorBigger?.addListener(object : Animator.AnimatorListener{
                            override fun onAnimationRepeat(animation: Animator?) {
                            }
                            override fun onAnimationEnd(animation: Animator?) {
                                isRecord = false
                                seconds = 0
                                animatorBigger?.cancel()
                            }

                            override fun onAnimationCancel(animation: Animator?) {
                            }

                            override fun onAnimationStart(animation: Animator?) {
                            }


                        })
                    }


                } else {
                    //拍照
                    Log.d(TAG, "[ACTION_UP] 拍照")
                }
            }

        }

        return true
    }

}