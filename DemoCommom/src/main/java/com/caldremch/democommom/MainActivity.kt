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
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.documentfile.provider.DocumentFile
import com.caldremch.common.annotation.Parceilize
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.utils.MetricsUtils
import com.caldremch.common.utils.permission.FilePermissionDelegate
import com.caldremch.democommom.anim.AnimActivity
import com.caldremch.democommom.anim.WidgetActivity
import com.caldremch.democommom.ui.EmptyActivity
import com.caldremch.democommom.ui.PopWindowDemo
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

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
//        startActivity(Intent(this, WidgetActivity::class.java))

        val logFile = File(Environment.getExternalStorageDirectory().toString() + File.separator + "fuck")
        val path = logFile.absolutePath
        Log.d("tag", "存储路径为 = $path")
        val dir = File(path)
        if (!dir.exists()) {
            Log.d("tag", "创建目录")
            dir.mkdirs()
        }


        var filess = File(path ,"fuck${UUID.randomUUID()}.text")

        if(!filess.exists()){
           var succ =  filess.createNewFile()
            if (succ){
                Log.d("tag", "文件创建成功:$filess")
            }else{
                Log.d("tag", "文件创建失败:$filess")
            }
        }
        Log.d("tag", "新文件路径:"+filess.absolutePath)
        val fos = FileOutputStream(filess.absolutePath)
        fos.write("funck you".toString().toByteArray())
        Log.d("tag", "数据写入完毕:")
        fos.close()

//        val fos = FileOutputStream(path + "fuck.text")
//        fos.write("fuck you".toString().toByteArray())
//        Log.d("tag", "数据写入完毕:")
//        fos.close()
    }


}