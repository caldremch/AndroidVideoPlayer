package com.caldremch.androidvideoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.caldremch.androidvideoplayer.Activity.PlayerDemoActivity;
import com.caldremch.androidvideoplayer.Activity.VideoDemoActivity;
import com.caldremch.ffmpegcore.FFmpegCore;
import com.caldremch.ffmpegcore.utils.VedioEditUtils;
import com.caldremch.playercore.player.FullScreenListener;
import com.caldremch.playercore.player.MyExoPlayerView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import androidx.appcompat.app.AppCompatActivity;


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
}
