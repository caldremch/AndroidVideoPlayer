package com.caldremch.androidvideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.caldremch.androidvideoplayer.Activity.PlayerDemoActivity;
import com.caldremch.androidvideoplayer.Activity.VideoDemoActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void handleVideo(View view) {
        startActivity(new Intent(this, VideoDemoActivity.class));

    }

    public void handleFFmpeg(View view) {
        startActivity(new Intent(this, FFmpegMainActivity.class));
//       VedioEditUtils.mp4TransTs("/storage/emulated/0/DCIM/Camera/VID_20190414_184208.mp4");
    }

    public void videoPlayer(View view) {
        startActivity(new Intent(this, PlayerDemoActivity.class));
    }
}
