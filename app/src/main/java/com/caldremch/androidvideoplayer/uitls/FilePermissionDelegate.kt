package com.caldremch.androidvideoplayer.uitls

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 *
 * @author Caldremch
 *
 * @date 2019-05-24 13:44
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/

class FilePermissionDelegate {

    private val REQ : Int = 0x11;
    private lateinit var activity :Activity;
    private val permission = arrayOf( Manifest.permission.READ_EXTERNAL_STORAGE,  Manifest.permission.WRITE_EXTERNAL_STORAGE)

    constructor(activity: Activity) {
        this.activity = activity
    }

    fun hasPermission(): Boolean{

        var permissionResult = ContextCompat.checkSelfPermission(activity, permission[1])

        return permissionResult == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(){

        ActivityCompat.requestPermissions(
                activity,
                permission,
                REQ
        )
    }

    fun resultGranted(reqCode:Int, permission:Array<out String>, grantResult:IntArray):Boolean{

        if (reqCode != REQ){
            return false
        }

        if (grantResult[0] == PackageManager.PERMISSION_GRANTED
                && grantResult[1] == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }
}