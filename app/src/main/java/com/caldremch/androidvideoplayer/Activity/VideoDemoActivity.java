package com.caldremch.androidvideoplayer.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caldremch.androidvideoplayer.Constant;
import com.caldremch.androidvideoplayer.R;
import com.caldremch.androidvideoplayer.adapter.VideoListAdapter;
import com.caldremch.androidvideoplayer.uitls.CLog;
import com.caldremch.androidvideoplayer.uitls.UriUtils;
import com.caldremch.ffmpegcore.FFmpegCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Caldremch
 * @date 2019-04-21 11:17
 * @email caldremch@163.com
 * @describe
 **/
public class VideoDemoActivity extends AppCompatActivity {


    private static final int REQ_MEDIA = 1;
    private static final String MOVIE = "video/*";
    private static final String PICTURE = "image/*";

    private List<String> videoFirstFrames = new ArrayList<>();

    private Button mButton4;
    private RecyclerView mRv;
    private VideoListAdapter mAdapter;
    private ImageView mIv;

    private void assignViews() {
        mButton4 = findViewById(R.id.button4);
        mRv = findViewById(R.id.rv);
        mIv = findViewById(R.id.iv);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);

        assignViews();
        mAdapter = new VideoListAdapter(videoFirstFrames);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(mAdapter);

        FFmpegCore.getFrameAt(Constant.MP4_TEST_URL, 0, 0);


        //获取网络视频的第一帧
//        mIv.setImageBitmap(getFrameFromMediaMetadataRetriever(Constant.MP4_TEST_URL));

    }

    public void handleAndroidMedia(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PICTURE);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MOVIE);
        startActivityForResult(intent, REQ_MEDIA);

        FFmpegCore.getFrameAt("", 0, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQ_MEDIA && resultCode == RESULT_OK) {

            String realPath = UriUtils.getPath(this, data.getData());

            CLog.d("realPath=" + realPath);

            //1.获取视频的第一帧 ffmpeg
           FFmpegCore.getFrameAt(realPath, 0, 0);

            mIv.setImageURI(Uri.parse("/storage/emulated/0/Android/test.jpg"));

            //2.获取视频的第一帧 android原生
//            mIv.setImageBitmap(getFrameFromMediaMetadataRetriever(realPath));
        }

    }

    private Bitmap getFrameFromMediaMetadataRetriever(String realPath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        Bitmap bitmap = null;
        try {

            if (realPath.startsWith("http://")
                    || realPath.startsWith("https://")
                    || realPath.startsWith("widevine://")) {
                mmr.setDataSource(realPath, new Hashtable<String, String>());
            } else {
                mmr.setDataSource(realPath);

            }

            bitmap = mmr.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }finally {
            mmr.release();
        }


        return bitmap;
    }


    public void mediaRecord(View view) {

        startActivity(new Intent(this, MediaRecordActivity.class));

    }
}
