package com.caldremch.androidvideoplayer.widget.camera

import android.hardware.Camera
import android.util.Size
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.common.utils.DensityUtil
import com.caldremch.common.utils.Utils
import kotlin.math.abs

/**
 *
 * @author Caldremch
 *
 * @date 2019-06-18 09:40
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
class CameraUtils {
    companion object {

        fun getPreViewBestSize(supportSize: MutableList<CameraSize>): CameraSize {
            //全屏预览
            val screenWidth = DensityUtil.getScreenWidth()
            val screenHeight = DensityUtil.getScreenHeight()
            var bestSize = CameraSize(0, 0);
            if (supportSize.isNotEmpty()) {
                var firstSize = supportSize.get(0);
                var diff = abs(firstSize.width - screenWidth) + abs(firstSize.height - screenHeight)
                for (s in supportSize) {
                    var tempDiff = abs(s.width - screenWidth) + abs(s.height - screenHeight);
                    CLog.d("[$screenWidth x $screenHeight]<--->[${s.width} x ${s.height}]每次比较--->:$tempDiff")
                    if (tempDiff < diff) {
                        diff = tempDiff;
                        bestSize = s;
                    }
                }
            }
            return bestSize.fixed();
        }

        fun getVideoBestSize(supportSize: MutableList<Camera.Size>): Camera.Size {
            //全屏预览
            val screenWidth = DensityUtil.getScreenWidth()
            val screenHeight = DensityUtil.getScreenHeight()
            var bestSize:Camera.Size? = null
            if (supportSize.isNotEmpty()) {
                var firstSize = supportSize.get(0);
                var diff = abs(firstSize.width - screenWidth) + abs(firstSize.height - screenHeight)
                for (s in supportSize) {
                    var tempDiff = abs(s.width - screenWidth) + abs(s.height - screenHeight);
                    CLog.d("[$screenWidth x $screenHeight]<--->[${s.width} x ${s.height}]每次比较--->:$tempDiff")
                    if (tempDiff < diff) {
                        diff = tempDiff;
                        bestSize = s;
                    }
                }
            }
            return bestSize!!
        }




        fun transSize(source: MutableList<Camera.Size>, dest: MutableList<CameraSize>) {
            for (s in source) {
                dest.add(CameraSize(s.height, s.width))
            }
        }
    }

}