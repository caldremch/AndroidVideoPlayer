package com.caldremch.democommom;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.caldremch.common.base.BaseActivity;
import com.caldremch.common.utils.download.DownLoadManager;
import com.caldremch.common.utils.download.IDownLoad;
import com.caldremch.common.utils.download.IDownLoadListener;

/**
 * @author Caldremch
 * @date 2019-07-09 10:54
 * @email caldremch@163.com
 * @describe
 **/
public class DownLoadActivity extends BaseActivity {

    private TextView tvProgress;
    private ProgressBar progressBar;
    private IDownLoad downLoad;

    @Override
    public int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    public void initView() {
        tvProgress = findViewById(R.id.tvProgress);
        progressBar = findViewById(R.id.progressBar);
    }


    public void startDownLoad(View view) {

        downLoad = new DownLoadManager
                .Builder(this)
                .with("https://qiniu.jpark.vip/test/app-release.apk")
                .listener(new IDownLoadListener() {
                    @Override
                    public void onProgress(float currentPos) {
                        int pro = (int) (currentPos * 100);
                        progressBar.setProgress(pro);
                        tvProgress.setText(String.format("进度:%d%%", pro));
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(mContext, "已取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        downLoad.start();

    }

    public void pauseDownLoad(View view) {
    }

    public void resumeDownLoad(View view) {
    }

    public void cancelDownLoad(View view) {

        downLoad.cancel();
    }
}
