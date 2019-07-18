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

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null){
            timer.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null){
            timer.cancel();
        }
    }

    private long lastLikeTime = 0;
    //人类一秒钟能点击点赞画面10 次, 因为点击过快, 所以不需要触发条件速度 设置为 5
    private int speedClick = 0;
    public void startLike(View view){
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(60*60*1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                long currentTime = System.currentTimeMillis();
                //1秒内有效点击为 2 次
                if (currentTime - lastLikeTime > 500 || speedClick == 4){
                    liveThumbView.addThumbImage();
                    speedClick = 0;
                }else {
                    speedClick++;
                }
                lastLikeTime = currentTime;
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
