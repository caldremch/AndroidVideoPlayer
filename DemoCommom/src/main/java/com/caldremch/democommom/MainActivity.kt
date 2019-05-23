package com.caldremch.democommom

import android.os.Bundle
import android.view.View
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.utils.MetricsUtils
import com.caldremch.democommom.ui.PopWindowDemo
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 * @author Caldremch
 *
 * @date 2019-05-21 19:50
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/

class MainActivity : BaseActivity() {

    private lateinit var win:PopWindowDemo;

    override fun getLayoutId(): Int {
        return R.layout.activity_main;
    }

    override fun initView() {
        MetricsUtils.compatTitle(mContext, textView)
        win = PopWindowDemo(mContext)
        win.setOnDismissListener {
            var lp =  window.attributes
            lp.alpha = 1f;
            window.attributes = lp
        }
    }

    fun dropDown(view: View) {
        win.show(view)
    }



}