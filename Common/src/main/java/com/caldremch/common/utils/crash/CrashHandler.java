package com.caldremch.common.utils.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Caldremch
 * @date 2019-06-10 10:26
 * @email caldremch@163.com
 * @describe
 **/
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = CrashHandler.class.getSimpleName();
    private static final String FILE_FLAG = "logfiles";
    /**
     * 系统默认UncaughtExceptionHandler处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private static CrashHandler INSTANCE = new CrashHandler();

    private Context mContext;

    /**
     * 存储设备信息和异常信息
     */
    private Map<String, String> mInfos = new HashMap<>();

    private DateFormat mFormater = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA);

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }


    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        if (!handleException(e) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException exception) {

            }

            //退出app
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }


    /**
     * 异常处理, 收集日志, 发送日志等
     *
     * @param e 异常信息
     * @return true: 处理该信息, false: 未处理
     */
    private boolean handleException(Throwable e) {

        if (e == null) {
            return false;
        }

        e.printStackTrace();

        new Thread(mWorkThreadRunable).start();

        //收集设备参数
        handleDeviceInfo(mContext);

        //保存到本地
        saveLocal(e);

        return true;
    }

    private void saveLocal(Throwable e) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : mInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        try {
            long timestamp = System.currentTimeMillis();
            String time = mFormater.format(new Date());
            String fileName =  "-" + time + "-" + timestamp
                    + ".log";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File logFile = new File(Environment.getExternalStorageDirectory() + File.separator + FILE_FLAG);
                String path = logFile.getAbsolutePath();
                Log.d("tag", "存储路径为 = "+path);
                File dir = new File(path);
                if (!dir.exists()) {
                    Log.d("tag", "不存在?");
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                Log.d("tag", "数据写入完毕:"+sb.toString());
                fos.close();
            }
        } catch (Exception exception) {
            Log.d("tag", "写入文件由问题?");
            exception.printStackTrace();
            Log.e(TAG, "an error occured while writing file...", exception);
        }

    }

    private void handleDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                mInfos.put("versionName", versionName);
                mInfos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    private Runnable mWorkThreadRunable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            Toast.makeText(mContext, "出现异常, 正在收集日志, 即将退出", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    };


    public static void main(String[] args) {
        Field[] fields = Build.class.getDeclaredFields();
        Map<String, String> info = new HashMap<>();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }

        System.out.println(new Gson().toJson(info));
    }
}
