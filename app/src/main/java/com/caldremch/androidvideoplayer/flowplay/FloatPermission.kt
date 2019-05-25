package com.caldremch.androidvideoplayer.flowplay

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.provider.Settings

import com.caldremch.androidvideoplayer.uitls.Utils

import java.lang.reflect.Method

/**
 * @author Caldremch
 * @date 2019-05-25 20:42
 * @email caldremch@163.com
 * @describe
 */
object FloatPermission {

    fun toAppDetail() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + Utils.getContext().packageName))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        Utils.getContext().startActivity(intent)
    }

    fun isFlowViewPermissionGranted(context: Context): Boolean {
        return isPermissionGranted(context, "24")
    }

    private fun isPermissionGranted(context: Context, permissionCode: String): Boolean {
        try {
            val `object` = context.getSystemService(Context.APP_OPS_SERVICE) ?: return false
            val localClass = `object`.javaClass
            val arrayOfClass = arrayOfNulls<Class<*>>(3)
            arrayOfClass[0] = Integer.TYPE
            arrayOfClass[1] = Integer.TYPE
            arrayOfClass[2] = String::class.java
            val method = localClass.getMethod("checkOp", *arrayOfClass) ?: return false

            val arrayOfObject = arrayOfNulls<Any>(3)
            arrayOfObject[0] = Integer.valueOf(permissionCode)
            arrayOfObject[1] = Integer.valueOf(Binder.getCallingUid())
            arrayOfObject[2] = context.packageName
            val m = (method.invoke(`object`, *arrayOfObject) as Int).toInt()
            return m == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

}
