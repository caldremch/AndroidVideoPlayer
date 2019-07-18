package com.caldremch.democommom.netmonitor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.caldremch.common.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class NetworkUtils {

    public static final int TYPE_UNKNOW = 0;
    public static final int TYPE_MOBILE = 1;
    public static final int TYPE_WIFI = 2;
    private static final String DTEST_HOST = "vcloud.163.com";


    public static int getNetworkType(){
        ConnectivityManager connMgr = (ConnectivityManager) Utils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo!=null){
            if(networkInfo.isConnected()){
                switch (networkInfo.getType()){
                    case ConnectivityManager.TYPE_WIFI:
                        return TYPE_WIFI;
                    case ConnectivityManager.TYPE_MOBILE:
                        return TYPE_MOBILE;
                    default:
                        return TYPE_UNKNOW;
                }
            }else{
                return TYPE_UNKNOW;
            }
        }
        return TYPE_UNKNOW;
    }

    /**
     *
     * @param needReliable 是否需要可靠, true 添加 ping测试, false 直接根据Android系统的结果
     * @return
     */
    public static boolean isNetworkConnected(boolean needReliable){
        ConnectivityManager connMgr = (ConnectivityManager) Utils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo!=null){
            StringBuffer builder = new StringBuffer();

            boolean connect =  networkInfo.isAvailable() && networkInfo.isConnected();
            if(needReliable){
                return connect && ping(DTEST_HOST, 1, builder);
            }else{
                return connect;
            }
        }
        return false;
    }

    //系统检测网络是否连接不可靠,故添加ping方法来测试,网络是否连通.
    public static boolean ping(String host, int pingCount, StringBuffer stringBuffer) {
        //todo 可修改为socket
        String line = null;
        Process process = null;
        BufferedReader successReader = null;
        String command = "ping -c " + pingCount + " -w 2 " + host;
        boolean isSuccess = false;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {

                append(stringBuffer, "ping fail:process is null.");
                return false;
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = successReader.readLine()) != null) {

                append(stringBuffer, line);
            }
            int status = process.waitFor();
            if (status == 0) {

                append(stringBuffer, "exec cmd success:" + command);
                isSuccess = true;
            } else {

                append(stringBuffer, "exec cmd fail.");
                isSuccess = false;
            }

            append(stringBuffer, "exec finished.");
        } catch (IOException e) {

        } catch (InterruptedException e) {

        } finally {

            if (process != null) {
                process.destroy();
            }
            if (successReader != null) {
                try {
                    successReader.close();
                } catch (IOException e) {

                }
            }
        }
        return isSuccess;
    }

    private static void append(StringBuffer stringBuffer, String text) {
        if (stringBuffer != null) {
            stringBuffer.append(text + "\n");
        }
    }

}
