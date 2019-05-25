package com.caldremch.androidvideoplayer.flowplay

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.view.*
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.uitls.Utils

/**
 * @author Caldremch
 *
 * @date 2019-05-25 15:17
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/

class VideoFloatController private constructor() {


    private var mStartX: Float = 0.toFloat()
    private var mStartY: Float = 0.toFloat()
    private var mX0: Float = 0.toFloat()
    private var mY0: Float = 0.toFloat()

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    var mMainView: SingletonPlayerView? = null
    private var mParams: WindowManager.LayoutParams? = null
    private val mWm: WindowManager = Utils.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager;

    var mVideoRatio = Video_Ratio.MOBILE
    private var mIsShow: Boolean = false
    private var mStatus = MainViewStatus.NORMAL

    enum class Video_Ratio {
        MOBILE,
        PC;
    }

    companion object {
        val instance: VideoFloatController by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            VideoFloatController()
        }
    }

    init {
        mMainView = SingletonPlayerView(Utils.getContext())
    }

    private fun handleVideoRatio() {
        //默认视频比例: 16:9
        val ratio = 16f / 9f
        if (mVideoRatio == Video_Ratio.PC) {
            this.mHeight = (getScreenWidth() / 3f).toInt()
            this.mWidth = (this.mHeight * ratio).toInt()
            CLog.d("PC mWidth, mHeight-->" + mWidth + "x" + mHeight)
        } else {
            this.mWidth = (getScreenWidth() / 3f).toInt()
            this.mHeight = (this.mWidth * ratio).toInt()
            CLog.d("Mobile mWidth, mHeight-->" + mWidth + "x" + mHeight)
        }
    }


    /**
     * 开启窗口
     */
    fun open() {

        if (mIsShow) {
            return
        }


        handleVideoRatio()

        mIsShow = true

        mMainView?.setBackgroundColor(Color.BLACK)

        mMainView?.setOnTouchListener(mTouchListener)

        mMainView?.setOnClickListener {
            if (mClickListener != null) {
                CLog.d("点击到了吗?")
                mClickListener?.onClick(it)
            }
        }

        mStatus = MainViewStatus.WINDOW
        mMainView?.onState(mStatus)

        mParams = WindowManager.LayoutParams()
        //设置布局宽高
        mParams?.width = mWidth
        mParams?.height = mHeight
        //展示宽高
        CLog.d("展示宽高 mWidth, mHeight-->" + mWidth + "x" + mHeight)
        //顶部和左边对其 顶部居左
        mParams?.gravity = Gravity.TOP or Gravity.START
        //距离远点的坐标值
        mParams?.x = 0
        mParams?.y = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams?.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            mParams?.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        mParams?.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        mParams?.format = PixelFormat.RGBA_8888

        if (null != mMainView?.parent && mMainView?.parent is ViewGroup) {
            (mMainView?.parent as ViewGroup).removeView(mMainView)
        }

        //初始化拉流播放器, 点播播放器
        mWm.addView(mMainView, mParams)
    }

    /**
     * 关闭窗口
     */
    fun close() {
        if (mMainView != null && mIsShow) {
            mIsShow = false
            mStatus = MainViewStatus.NORMAL
            removeView()
        }
    }

    fun removeView() {
        mWm.removeView(mMainView)
    }

    private var mClickListener: onViewClickListener? = null

    fun setListener(listener: onViewClickListener) {
        this.mClickListener = listener
    }

    interface onViewClickListener {
        fun onClick(v: View)
    }

    private var mTouchListener = object : View.OnTouchListener {

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {

            if (mStatus == MainViewStatus.NORMAL){
                return false
            }

            when (event?.action) {

                MotionEvent.ACTION_DOWN -> {
                    mStartX = event.getRawX()
                    mStartY = event.getRawY()

                    mX0 = event.getRawX()
                    mY0 = event.getRawY()
                }


                MotionEvent.ACTION_MOVE -> {

                    val x = event.getRawX()
                    val y = event.getRawY()

                    val dx = x - mStartX
                    val dy = y - mStartY

                    mParams?.x = mParams?.x?.plus(dx.toInt())
                    mParams?.y = mParams?.y?.plus(dy.toInt())

                    //刷新位置
                    mWm.updateViewLayout(mMainView, mParams)
                    mStartX = x
                    mStartY = y
                }

                MotionEvent.ACTION_UP -> {
                    val x1 = event.getRawX()
                    val y1 = event.getRawY()
                    if (x1 > getScreenWidth() / 2) {
                        mParams?.x = getScreenWidth() - mWidth
                    } else {
                        mParams?.x = 0
                    }
                    mWm.updateViewLayout(mMainView, mParams)
                    if (Math.abs(x1 - mX0) > 6 || Math.abs(y1 - mY0) > 6) {
                        return true
                    }
                }
            }

            return false
        }
    }

    fun getScreenWidth(): Int {
        val point = Point()
        mWm.defaultDisplay.getSize(point)
        return point.x
    }

    fun getScreenHeight(): Int {
        val point = Point()
        mWm.defaultDisplay.getSize(point)
        return point.y
    }

}