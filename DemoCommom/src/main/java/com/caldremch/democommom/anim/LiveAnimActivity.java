package com.caldremch.democommom.anim;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.caldremch.common.base.BaseActivity;
import com.caldremch.democommom.R;
import com.caldremch.democommom.widget.LiveThumbView;

/**
 * @author Caldremch
 * @date 2019-07-15 16:39
 * @email caldremch@163.com
 * @describe
 **/
public class LiveAnimActivity extends BaseActivity {

    private static final String TAG = LiveAnimActivity.class.getSimpleName();
    private LiveThumbView liveThumbView;
    private CountDownTimer timer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_like_anim;
    }

    @Override
    public void initView() {
        liveThumbView = findViewById(R.id.likeView);
    }

    public void startLike(View view){
        timer = new CountDownTimer(60*60*1000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                liveThumbView.addThumbImage();
            }

            @Override
            public void onFinish() {
                cancel();
            }
        };
        timer.start();
    }

    public void stopLike(View view) {
        if (timer != null){
            timer.cancel();
            liveThumbView.cancel();
            Log.d(TAG, "stopLike: 前 "+liveThumbView.getChildCount());
            liveThumbView.removeAllViews();
            Log.d(TAG, "stopLike: 后 "+liveThumbView.getChildCount());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
        }

        liveThumbView.cancel();
        Log.d(TAG, "stopLike: 前 "+liveThumbView.getChildCount());
        liveThumbView.removeAllViews();
        Log.d(TAG, "stopLike: 后 "+liveThumbView.getChildCount());
    }
}
