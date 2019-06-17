package com.caldremch.common.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.caldremch.common.utils.EventManager

/**
 *
 * @author Caldremch
 *
 * @date 2019-06-17 14:40
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
abstract class BaseFragment : Fragment() {

    protected var mContext: Activity? = null
    protected var mRootView: View? = null
    protected var mIsVisible = false
    private var mIsPrepare = false
    private var mIsFirst = true

    protected fun <T : View> findViewById(@IdRes id: Int): T {
        return mRootView!!.findViewById(id)
    }

    protected fun isUseEvent(): Boolean {
        return false
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (userVisibleHint) {
            mIsVisible = true
            onVisible()
            loadData()
        } else {
            mIsVisible = false
            onInvisible()
        }
    }

    protected fun onInvisible() {

    }

    protected fun onVisible() {

    }

    protected fun lazyLoad() {}


    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        inflaterView(inflater, container)
        return mRootView
    }

    private fun inflaterView(inflater: LayoutInflater, container: ViewGroup?) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false)
        }
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isUseEvent()) {
            EventManager.register(this)
        }
        initView()
        initData()
        initEvent()
        mIsPrepare = true
        mIsFirst = true

        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isUseEvent()) {
            EventManager.unregister(this)
        }
    }

    //加载数据
    protected fun loadData() {
        if (!mIsPrepare || !mIsVisible || !mIsFirst) {
            return
        }
        lazyLoad()
        mIsFirst = false
    }


    protected abstract fun getLayoutId(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    protected fun initEvent() {}

    override fun onDestroy() {
        super.onDestroy()
    }


}