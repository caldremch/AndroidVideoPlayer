package com.caldremch.democommom

import android.content.Intent
import android.content.pm.ActivityInfo
import android.view.View
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.utils.permission.FilePermissionDelegate
import com.caldremch.democommom.anim.LiveAnimActivity

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

    var filePermission = FilePermissionDelegate(this)

    override fun onStart() {
        super.onStart()
        if (!filePermission.hasPermission()) {
            filePermission.requestPermission()
        }
    }

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
        startActivity(Intent(this, LiveAnimActivity::class.java))

    }


}