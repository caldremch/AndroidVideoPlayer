package com.caldremch.democommom

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.utils.MetricsUtils
import com.caldremch.common.utils.permission.FilePermissionDelegate
import com.caldremch.democommom.anim.AnimActivity
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
        startActivity(Intent(this, AnimActivity::class.java))
    }


}