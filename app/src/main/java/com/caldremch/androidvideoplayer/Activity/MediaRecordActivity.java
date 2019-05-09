package com.caldremch.androidvideoplayer.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.caldremch.androidvideoplayer.R;
import com.caldremch.androidvideoplayer.uitls.MediaMetadataRetrieverUtils;
import com.caldremch.androidvideoplayer.widget.MediaRecordSurfaceView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

/**
 * @author Caldremch
 * @date 2019-04-29 08:32
 * @email caldremch@163.com
 * @describe
 **/
public class MediaRecordActivity extends AppCompatActivity {


    private int req_android_self_camera = 1;
    private int req_android_media_record = 2;
    private int req_ffmpeg_record = 3;

    private MediaRecordSurfaceView mSurfaceView;
    private Button mButton3;
    private Button mButton5;
    private Chip mChipInGroup21;
    private Chip mChipInGroup22;
    private Chip mChipInGroup23;
    private ChipGroup mChipGroup;
    private ImageView mIv;


    private void assignViews() {
        mSurfaceView = findViewById(R.id.surface_view);
        mButton3 = findViewById(R.id.button3);
        mButton5 = findViewById(R.id.button5);
        mChipInGroup21 = findViewById(R.id.chipInGroup2_1);
        mChipInGroup22 = findViewById(R.id.chipInGroup2_2);
        mChipInGroup23 = findViewById(R.id.chipInGroup2_3);
        mChipGroup =  findViewById(R.id.chipGroup);
        mIv =  findViewById(R.id.iv);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        assignViews();

        mChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                if (i != R.id.chipInGroup2_1 && i != R.id.chipInGroup2_2 && i!=R.id.chipInGroup2_3){
                    mChipInGroup21.setChecked(true);
                }
            }
        });

    }

    public void start(View view) {
        if (mChipGroup.getCheckedChipId() == R.id.chipInGroup2_1){
            Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //1 代表高质量
            i.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);//最大拍摄的秒数
            startActivityForResult(i, req_android_self_camera);
        }
    }

    public void stop(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == req_android_self_camera){
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToNext()){
                    int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                    //视频路径
                    String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                    //获取视频缩略图
//                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
                    mIv.setImageBitmap(MediaMetadataRetrieverUtils.getFrameFromMediaMetadataRetriever(filePath));
                    cursor.close();

                }
            }
        }

    }
}
