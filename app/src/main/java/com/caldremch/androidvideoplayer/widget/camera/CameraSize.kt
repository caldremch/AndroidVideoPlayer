package com.caldremch.androidvideoplayer.widget.camera

/**
 *
 * @author Caldremch
 *
 * @date 2019-06-18 10:00
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
class CameraSize(var width: Int, var height: Int) {

    fun fixed():CameraSize{
//        var temp = width
//        width = height
//        height = temp
        return CameraSize(height, width)
    }

}