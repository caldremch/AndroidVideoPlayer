package com.caldremch.common.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Caldremch
 * @date 2019-06-10 15:13
 * @email caldremch@163.com
 * @describe
 **/
public class LifeCycleLogActivity extends AppCompatActivity {

    private static final String TAG = "LifeCycleLogActivity";

    protected boolean isPrintLifeCycle(){
        return false;
    }

    private void d(String log){
        if (isPrintLifeCycle()){
            Log.d(TAG, this.getClass().getSimpleName()+"-->"+log);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        d("onCreate: ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        d("onConfigurationChanged: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        d("onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        d("onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        d("onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        d("onPause: ");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        d("onNewIntent: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        d("onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        d("onRestart: ");
    }

}
