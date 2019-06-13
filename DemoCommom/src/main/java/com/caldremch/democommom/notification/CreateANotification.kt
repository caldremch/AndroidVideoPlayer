package com.caldremch.democommom.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.view.View
import androidx.core.app.NotificationCompat
import com.caldremch.democommom.R

/**
 *
 * @author Caldremch
 *
 * @date 2019-06-13 16:02
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
class CreateANotification {

    @TargetApi(Build.VERSION_CODES.O)
    fun create(context: Context) {

        var manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;

        //创建渠道
        var channel = NotificationChannel("test-channel","测试渠道", NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)


        var build = NotificationCompat.Builder(context, "test-channel")
        build.setContentTitle("test title")
                .setContentText("test content")
                .setSmallIcon(R.drawable.common_app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.common_app_icon))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)

        manager.notify(1, build.build())
    }
}