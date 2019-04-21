package com.caldremch.androidvideoplayer.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.caldremch.androidvideoplayer.Constant;
import com.caldremch.androidvideoplayer.R;
import com.caldremch.playercore.player.FullScreenListener;
import com.caldremch.playercore.player.MyExoPlayerView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

/**
 * @author Caldremch
 * @date 2019-04-21 11:13
 * @email caldremch@163.com
 * @describe
 **/
public class PlayerDemoActivity extends AppCompatActivity {


    SimpleExoPlayer simpleExoPlayer;
    MyExoPlayerView playerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_demo);


        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        playerView = findViewById(R.id.playerView);
        playerView.setPlayer(simpleExoPlayer);

        DataSource.Factory dataFactory = new DefaultDataSourceFactory(this, "caldremch");
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataFactory).createMediaSource(Uri.parse(Constant.MP4_TEST_URL));
        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.setPlayWhenReady(true);
        playerView.setFullScreenListener(new FullScreenListener() {
            @Override
            public void onClick(View view) {
                Log.d("caldremch", "点击全屏了");
            }
        });

//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri uri = Uri.parse(Constant.MP4_TEST_URL4);
//        intent.setData(uri);
//        startActivity(intent);
    }
}
