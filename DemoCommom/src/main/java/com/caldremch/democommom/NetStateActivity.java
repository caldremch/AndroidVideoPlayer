package com.caldremch.democommom;

import android.util.Log;
import android.view.View;

import com.caldremch.common.base.BaseActivity;
import com.caldremch.democommom.netmonitor.NetworkMonitor;

/**
 * @author Caldremch
 * @date 2019-07-17 10:52
 * @email caldremch@163.com
 * @describe
 **/
public class NetStateActivity extends BaseActivity {

    private String TAG = NetStateActivity.class.getSimpleName();
    private NetworkMonitor monitor;

    @Override
    public int getLayoutId() {
        return R.layout.activity_netstate;
    }

    public void startListener(View view) {
        if (monitor != null) {
            monitor.stop();
        }
        monitor = new NetworkMonitor(new NetworkMonitor.NetworkListener() {
            @Override
            public void onNetworkChanged(NetworkMonitor monitor, boolean connected, int newType) {
                Log.d(TAG, "onNetworkChanged: " + connected);
            }
        });
        monitor.start(30000, 2000);
    }

    public void stopListener(View view) {
        if (monitor != null) {
            monitor.stop();
        }
    }
}
