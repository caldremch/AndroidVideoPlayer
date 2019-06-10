package com.caldremch.democommom

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.utils.MetricsUtils
import com.caldremch.democommom.ui.EmptyActivity
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

    override fun getLayoutId(): Int {
        return R.layout.activity_main;
    }

    override fun initView() {

    }

    override fun isPrintLifeCycle(): Boolean {
        return true
    }

    override fun isAlwaysPortrait(): Boolean {
        return false
    }

    fun oriChange(view: View) {
        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
    }

    fun jump(view: View) {
//        startActivity(Intent(this, EmptyActivity::class.java))

        val intent = Intent()
        val name = ComponentName(this, "com.caldremch.democommom.ui.EmptyActivity")
        intent.component = name

        val resolveInfos = packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (resolveInfos != null && resolveInfos.isNotEmpty()) {
            startActivity(intent)
        }

    }


}