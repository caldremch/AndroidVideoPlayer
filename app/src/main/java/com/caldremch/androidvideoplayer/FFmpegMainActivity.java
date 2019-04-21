package com.caldremch.androidvideoplayer;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.caldremch.androidvideoplayer.uitls.DeviceUtils;
import com.caldremch.ffmpegcore.FFmpegCore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Caldremch
 * @date 2019-04-17 08:46
 * @email caldremch@163.com
 * @describe
 **/
public class FFmpegMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ffmpeg_main);

        TextView textView = findViewById(R.id.sample_text);

        textView.setText(FFmpegCore.invokeTest());

        DeviceUtils.getDeviceUUID();

    }

}
