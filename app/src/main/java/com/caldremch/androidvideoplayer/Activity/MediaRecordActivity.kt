package com.caldremch.androidvideoplayer.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.caldremch.androidvideoplayer.R
import com.caldremch.androidvideoplayer.uitls.ClosestComp
import com.caldremch.androidvideoplayer.uitls.MediaMetadataRetrieverUtils
import com.caldremch.androidvideoplayer.widget.MediaRecordSurfaceView
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.utils.MetricsUtils
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*
import kotlin.Comparator
import kotlin.math.abs

/**
 * @author Caldremch
 * @date 2019-04-29 08:32
 * @email caldremch@163.com
 * @describe
 */
class MediaRecordActivity : BaseActivity() {


    private val req_android_self_camera = 1
    private val req_android_media_record = 2
    private val req_ffmpeg_record = 3

    private val CAMERA_REQ = 0x01;

    private var isCameraPermite: Boolean = false;

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

    override fun onStart() {
        super.onStart()
        isCameraPermite = checkPermission()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_REQ && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isCameraPermite = true;
        }

    }

    private fun checkPermission(): Boolean {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQ);
            } else {
                return true
            }
        } else {
            return true
        }

        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_record;
    }

    override fun initView() {
        assignViews()
        MetricsUtils.compatTitle(mContext, mChipGroup)
        //全品

        mChipGroup!!.setOnCheckedChangeListener { chipGroup, i ->
            if (i != R.id.chipInGroup2_1 && i != R.id.chipInGroup2_2 && i != R.id.chipInGroup2_3) {
                mChipInGroup21!!.isChecked = true
            }
        }

        //隐藏底部导航栏
//        var uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.xor(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        window.decorView.systemUiVisibility  = uiOption
        mBar.transparentNavigationBar().init()


    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        setContentView(R.layout.activity_record)
//    }

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
