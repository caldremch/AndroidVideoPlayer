package com.caldremch.androidvideoplayer.flowplay

import android.os.Parcel
import android.os.Parcelable

/**
 * @author Caldremch
 *
 * @date 2019-05-25 18:58
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/

class VideoWindowData() : Parcelable{

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoWindowData> {
        override fun createFromParcel(parcel: Parcel): VideoWindowData {
            return VideoWindowData(parcel)
        }

        override fun newArray(size: Int): Array<VideoWindowData?> {
            return arrayOfNulls(size)
        }
    }
}