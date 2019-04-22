package com.caldremch.androidvideoplayer.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caldremch.androidvideoplayer.R;
import com.caldremch.androidvideoplayer.adapter.VideoListAdapter;
import com.caldremch.androidvideoplayer.uitls.UriUtils;

import java.util.ArrayList;
import java.util.HashMap;
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

    private void assignViews() {
        mButton4 = (Button) findViewById(R.id.button4);
        mRv = (RecyclerView) findViewById(R.id.rv);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);

        assignViews();
        mAdapter = new VideoListAdapter(videoFirstFrames);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(mAdapter);

    }

    public void handleAndroidMedia(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PICTURE);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MOVIE);
        startActivityForResult(intent, REQ_MEDIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQ_MEDIA && resultCode == RESULT_OK) {
            String realPath = UriUtils.getPath(this, data.getData());

            //获取视频的第一帧

            MediaMetadataRetriever mmr= new MediaMetadataRetriever();

            mmr.setDataSource(realPath, new HashMap<String, String>());

            Bitmap bitmap = mmr.


        }

    }
}
