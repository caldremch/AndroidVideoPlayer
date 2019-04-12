package com.caldremch.playercore.player;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.caldremch.playercore.R;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

/**
 * @author Caldremch
 * @date 2019-03-07 15:52
 * @describe
 **/
public class ExoPlayerViewEx extends MyExoPlayerView {


    private boolean mIsFullScreen = false;


    private ViewGroup mWindowViewGroup;

    /**
     * 切换到全屏播放前当前VideoPlayerView的宽度
     */
    private int mVideoWidth;

    /**
     * 切换到全屏播放前当前VideoPlayerView的高度
     */
    private int mVideoHeight;


    /**
     * 切换全屏播放前当前VideoPlayerView的父容器
     */
    private ViewGroup mOldParent;

    /**
     * 切换全屏播放前当前VideoPlayerView的在父容器中的索引
     */
    private int mOldIndex = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper());


    public ExoPlayerViewEx(Context context) {
        super(context);
        init();
    }

    private void init() {
        handleFullVideo();
    }

    public ExoPlayerViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public ExoPlayerViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private ExoPlayerViewEx getSelf(){
        return this;
    }




    private void handleFullVideo() {

        //可自定义 复制PlayerView
        View[] views = getAdOverlayViews();

//        PlayerControlView controllerView = null;
//
//        setFullScreenListener(new FullScreenListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "点击全屏了", Toast.LENGTH_LONG).show();
//                Log.d("caldremch","点击全屏了");
//            }
//        });
//
//        for (View view : views) {
//            if (view instanceof PlayerControlView) {
//                controllerView = (PlayerControlView) view;
//                break;
//            }
//        }
//
//        if (controllerView != null) {
//
//            View fullControllerView = controllerView.findViewById(R.id.btn_full);
//
//            fullControllerView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startFullScreen();
//                }
//            });
//        }
    }

    public void startFullScreen(){

//        getPlayer().setPlayWhenReady(false);

//        setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

        mWindowViewGroup = getActivity(getContext()).findViewById(Window.ID_ANDROID_CONTENT);

        mWindowViewGroup.setBackgroundColor(Color.BLACK);


        mVideoWidth = this.getWidth();
        mVideoHeight = this.getHeight();

        mOldParent = (ViewGroup)this.getParent();
        mOldIndex = mOldParent.indexOfChild(this);

        mOldParent.removeView(getSelf());

        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        lp.setMargins(0,0,0,0);

        mWindowViewGroup.addView(this, lp);

        getActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mIsFullScreen = true;

        getActivity(getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        if (true){
//            return;
//        }
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                mOldParent.removeView(getSelf());
//
//    /*    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        mWindowViewGroup.addView(this, lp);
//
//         getActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        */
//
//
//                mWindowViewGroup.addView(getSelf());
//
//                TransitionManager.beginDelayedTransition(mWindowViewGroup);
//
//                //设置 属性变化---> 动画
//                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                getSelf().setLayoutParams(lp);
//                //设置全屏
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    }
//                }, 200);
//                getPlayer().setPlayWhenReady(true);
//
//            }
//        }, 200);


    }


    public boolean getIsFullScreen(){
        return mIsFullScreen;
    }


    public void exitFullScreen(){
//        getPlayer().setPlayWhenReady(false);

       mWindowViewGroup.removeView(getSelf());

        LayoutParams lp = new LayoutParams(mVideoWidth, mVideoHeight);

        mOldParent.addView(getSelf(), mOldIndex, lp);

        getActivity(getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mOldParent = null;

        mOldIndex = 0;

        mIsFullScreen = false;
//     if (true){
//         return;
//     }
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                mWindowViewGroup.removeView(getSelf());
//
//                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mVideoWidth, mVideoHeight);
//
//                mOldParent.addView(getSelf(), mOldIndex);
//
//                mOldParent = null;
//
//                mOldIndex = 0;
//
//                TransitionManager.beginDelayedTransition(mWindowViewGroup);
//
//                getSelf().setLayoutParams(lp);
//
//               mHandler.postDelayed(new Runnable() {
//                   @Override
//                   public void run() {
//                       getActivity(getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//                   }
//               }, 200);
//
//                getPlayer().setPlayWhenReady(true);
//
//            }
//        }, 200);

    }



    public static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        }

        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }

        return null;
    }

}
