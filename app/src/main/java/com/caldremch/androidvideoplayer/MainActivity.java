package com.caldremch.androidvideoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

    SimpleExoPlayer simpleExoPlayer;
    MyExoPlayerView playerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       VedioEditUtils.mp4TransTs("/storage/emulated/0/DCIM/Camera/VID_20190414_184208.mp4");
//        startActivity(new Intent(this, FFmpegMainActivity.class));
//        Player

//        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
//        playerView = findViewById(R.id.playerView);
//        playerView.setPlayer(simpleExoPlayer);
//
//        DataSource.Factory dataFactory = new DefaultDataSourceFactory(this, "caldremch");
//        MediaSource videoSource = new ExtractorMediaSource.Factory(dataFactory).createMediaSource(Uri.parse(Constant.MP4_TEST_URL));
//        simpleExoPlayer.prepare(videoSource);
//        simpleExoPlayer.setPlayWhenReady(true);
//        playerView.setFullScreenListener(new FullScreenListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("caldremch", "点击全屏了");
//            }
//        });

//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri uri = Uri.parse(Constant.MP4_TEST_URL4);
//        intent.setData(uri);
//        startActivity(intent);


    }
}
