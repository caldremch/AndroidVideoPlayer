package com.caldremch.androidvideoplayer.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.caldremch.androidvideoplayer.Constant
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.adapter.VideoListAdapter
import com.caldremch.androidvideoplayer.uitls.CLog
import com.caldremch.androidvideoplayer.uitls.CameraPermissionDelegate
import com.caldremch.androidvideoplayer.uitls.UriUtils
import com.caldremch.ffmpegcore.FFmpegCore

import java.util.ArrayList
import java.util.HashMap
import java.util.Hashtable

/**
 * @author Caldremch
 * @date 2019-04-21 11:17
 * @email caldremch@163.com
 * @describe
 */
class VideoDemoActivity : AppCompatActivity() {

    private val videoFirstFrames = ArrayList<String>()

    private var mButton4: Button? = null
    private var mRv: RecyclerView? = null
    private var mAdapter: VideoListAdapter? = null
    private var mIv: ImageView? = null

    private fun assignViews() {
        mButton4 = findViewById(R.id.button4)
        mRv = findViewById(R.id.rv)
        mIv = findViewById(R.id.iv)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_demo)

        assignViews()
        mAdapter = VideoListAdapter(videoFirstFrames)
        mRv!!.layoutManager = LinearLayoutManager(this)
        mRv!!.adapter = mAdapter

        //        FFmpegCore.getFrameAt(Constant.MP4_TEST_URL, 0, 0);


        //获取网络视频的第一帧
        //        mIv.setImageBitmap(getFrameFromMediaMetadataRetriever(Constant.MP4_TEST_URL));

    }

    fun handleAndroidMedia(view: View) {
        val intent = Intent(Intent.ACTION_PICK, null)
        //        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PICTURE);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MOVIE)
        startActivityForResult(intent, REQ_MEDIA)

        FFmpegCore.getFrameAt("", 0, 0)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == REQ_MEDIA && resultCode == Activity.RESULT_OK) {

            val realPath = UriUtils.getPath(this, data!!.data)

            CLog.d("realPath=" + realPath!!)

            //1.获取视频的第一帧 ffmpeg
            FFmpegCore.getFrameAt(realPath, 0, 0)

            mIv!!.setImageURI(Uri.parse("/storage/emulated/0/Android/test.jpg"))

            //2.获取视频的第一帧 android原生
            //  mIv.setImageBitmap(getFrameFromMediaMetadataRetriever(realPath));
        }

    }

    val delegate = CameraPermissionDelegate(this)

    fun mediaRecord(view: View) {

        if (delegate.hasPermission()) {
            startActivity(Intent(this, MediaRecordActivity::class.java))
        } else {
            delegate.requestPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val ret = delegate.resultGranted(
                reqCode = requestCode,
                permission = permissions,
                grantResult = grantResults
        )

        if (ret) {
            startActivity(Intent(this, MediaRecordActivity::class.java))
        }
    }

    companion object {


        private val REQ_MEDIA = 1
        private val MOVIE = "video/*"
        private val PICTURE = "image/*"
    }
}
