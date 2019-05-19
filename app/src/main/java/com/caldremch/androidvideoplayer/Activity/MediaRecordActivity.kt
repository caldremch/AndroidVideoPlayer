package com.caldremch.androidvideoplayer.Activity

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.uitls.MediaMetadataRetrieverUtils
import com.caldremch.androidvideoplayer.widget.MediaRecordSurfaceView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * @author Caldremch
 * @date 2019-04-29 08:32
 * @email caldremch@163.com
 * @describe
 */
class MediaRecordActivity : AppCompatActivity() {


    private val req_android_self_camera = 1
    private val req_android_media_record = 2
    private val req_ffmpeg_record = 3

    private var mSurfaceView: MediaRecordSurfaceView? = null
    private var mButton3: Button? = null
    private var mButton5: Button? = null
    private var mChipInGroup21: Chip? = null
    private var mChipInGroup22: Chip? = null
    private var mChipInGroup23: Chip? = null
    private var mChipGroup: ChipGroup? = null
    private var mIv: ImageView? = null


    private fun assignViews() {
        mSurfaceView = findViewById(R.id.surface_view)
        mButton3 = findViewById(R.id.button3)
        mButton5 = findViewById(R.id.button5)
        mChipInGroup21 = findViewById(R.id.chipInGroup2_1)
        mChipInGroup22 = findViewById(R.id.chipInGroup2_2)
        mChipInGroup23 = findViewById(R.id.chipInGroup2_3)
        mChipGroup = findViewById(R.id.chipGroup)
        mIv = findViewById(R.id.iv)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_record)
        assignViews()


        //全品

        mChipGroup!!.setOnCheckedChangeListener { chipGroup, i ->
            if (i != R.id.chipInGroup2_1 && i != R.id.chipInGroup2_2 && i != R.id.chipInGroup2_3) {
                mChipInGroup21!!.isChecked = true
            }
        }


    }

    fun start(view: View) {
        if (mChipGroup!!.checkedChipId == R.id.chipInGroup2_1) {
            val i = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1) //1 代表高质量
            i.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15)//最大拍摄的秒数
            startActivityForResult(i, req_android_self_camera)
        }
    }

    fun stop(view: View) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == req_android_self_camera) {
                val uri = data!!.data
                val cursor = contentResolver.query(uri!!, null, null, null, null)
                if (cursor != null && cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID))
                    //视频路径
                    val filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA))
                    //获取视频缩略图
                    //                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
                    mIv!!.setImageBitmap(MediaMetadataRetrieverUtils.getFrameFromMediaMetadataRetriever(filePath))
                    cursor.close()

                }
            }
        }

    }
}
