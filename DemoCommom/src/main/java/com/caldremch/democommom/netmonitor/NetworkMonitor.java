package com.caldremch.democommom.netmonitor;

import android.os.CountDownTimer;

/**
 * @author Caldremch
 * @date 2019-07-17 10:53
 * @email caldremch@163.com
 * @describe 设置每隔多久就获取一次网络状态
 **/
public class NetworkMonitor implements INetworkMonitor {

    private CountDownTimer countDownTimer;
    private NetworkListener listener;
    private int currentNetworkType = NetworkUtils.TYPE_UNKNOW;
    private boolean currentConnected = false;

    public NetworkMonitor(NetworkListener listener) {
        this.listener = listener;
    }

    @Override
    public void init() {


    }

    @Override
    public void start(long checkTotalTime, long checkDuration) {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(checkTotalTime, checkDuration) {
            @Override
            public void onTick(long millisUntilFinished) {
                int newType = NetworkUtils.getNetworkType();
                boolean newConnection = NetworkUtils.isNetworkConnected(true);
                //if(newConnection != currentConnected){
                {
                    if (listener != null) {
                        listener.onNetworkChanged(NetworkMonitor.this, newConnection, newType);
                    }
                    currentConnected = newConnection;
                    currentNetworkType = newType;
                }
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();
    }

    @Override
    public void stop() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public interface NetworkListener {
        void onNetworkChanged(NetworkMonitor monitor, boolean connected, int newType);
    }

}
