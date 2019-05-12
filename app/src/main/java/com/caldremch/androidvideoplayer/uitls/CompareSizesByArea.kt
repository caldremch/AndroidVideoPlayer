package com.caldremch.androidvideoplayer.uitls

import android.annotation.TargetApi
import android.os.Build
import android.util.Size
import java.lang.Long.signum

import java.util.Comparator

/**
 * @author Caldremch
 * @date 2019-05-12 18:13
 * @email caldremch@163.com
 * @describe
 */
internal class CompareSizesByArea : Comparator<Size> {

    override fun compare(o1: Size, o2: Size) = signum(o1.width.toLong() * o1.height - o2.width.toLong() * o2.height)

}
