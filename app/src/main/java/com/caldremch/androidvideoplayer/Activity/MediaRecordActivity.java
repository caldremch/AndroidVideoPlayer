package com.caldremch.androidvideoplayer.Activity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.caldremch.androidvideoplayer.R;
import com.caldremch.androidvideoplayer.widget.MediaRecordSurfaceView;

/**
 * @author Caldremch
 * @date 2019-04-29 08:32
 * @email caldremch@163.com
 * @describe
 **/
public class MediaRecordActivity extends AppCompatActivity {

    private MediaRecordSurfaceView mSurfaceView;
    private Button mButton3;
    private Button mButton5;

    private void assignViews() {
        mSurfaceView = findViewById(R.id.surface_view);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton5 = (Button) findViewById(R.id.button5);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        assignViews();

    }

    public void start(View view) {

    }

    public void stop(View view) {

    }
}
