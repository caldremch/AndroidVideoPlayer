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

class CameraPermissionDelegate {

    private val CAMERA_REQ : Int = 0x11;
    private lateinit var activity :Activity;

    constructor(activity: Activity) {
        this.activity = activity
    }

    fun hasPermission(): Boolean{
        var permissionResult = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)

        return permissionResult == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(){

        ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQ
        )
    }

    fun resultGranted(reqCode:Int, permission:Array<out String>, grantResult:IntArray):Boolean{

        if (reqCode != CAMERA_REQ){
            return false
        }

        if (grantResult.size < 1){
            return false
        }

        if (!(permission[0].equals(Manifest.permission.CAMERA))){
            return false
        }

        if (grantResult[0] == PackageManager.PERMISSION_GRANTED){
            return true
        }

        return false

    }
}