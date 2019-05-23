package com.caldremch.democommom.ui

import android.app.Activity
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.caldremch.common.utils.DensityUtil
import com.caldremch.common.utils.MetricsUtils
import com.caldremch.democommom.R

/**
 *
 * @author Caldremch
 *
 * @date 2019-05-21 19:59
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/

class PopWindowDemo(context: Context) : PopupWindow(context) {

    private  lateinit var context: Context

    init {
        initView(context);
    }

    private fun initView(context: Context) {
        this.context = context
        var root = LayoutInflater.from(context).inflate(R.layout.pop_demo, null)
        contentView = root
//        isFocusable = true
        isOutsideTouchable = true
//        setBackgroundDrawable(BitmapDrawable())



        width = DensityUtil.getScreenWidth(context)
        height = DensityUtil.getScreenHeight(context)
    }


    fun show(view: View) {
        height = DensityUtil.getScreenHeight(context) - view.bottom + MetricsUtils.getStatusBarHeight(context)
//        if (context is Activity){
//            var lp = (context as Activity).window?.attributes;
//            lp?.alpha = 0.4f;
//            (context as Activity).window.attributes = lp;
//        }
        showAsDropDown(view)
    }


}