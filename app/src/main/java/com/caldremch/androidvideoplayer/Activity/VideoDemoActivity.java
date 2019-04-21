package com.caldremch.androidvideoplayer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.caldremch.androidvideoplayer.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);

    }

    public void handleAndroidMedia(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MOVIE);
        startActivityForResult(intent, REQ_MEDIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQ_MEDIA){





        }

    }
}
