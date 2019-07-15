package com.caldremch.common.utils.download;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author Caldremch
 * @date 2019-07-09 09:36
 * @email caldremch@163.com
 * @describe
 **/
public class DownLoadManager implements IDownLoad {

    private static final String TAG = DownLoadManager.class.getSimpleName();
    private Retrofit retrofit;
    private DownLoadApiService apiService;
    private Context context;
    private String url;
    private int threadNum = 1;
    private DownLoadOption option;
    private IDownLoadListener downLoadListener;
    private Disposable disposable;
    private boolean isCancel = false;

    private static HandlerThread sIoThread = new HandlerThread("io_thread");
    static {
        //就绪
        sIoThread.start();
    }
    private static Handler sIoHandler = new Handler(sIoThread.getLooper());
    private static Handler sUIHandler = new Handler(Looper.getMainLooper());

    public void setDownLoadListener(IDownLoadListener downLoadListener) {
        this.downLoadListener = downLoadListener;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    private DownLoadManager(Context context) {
        this.context = context;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HttpLoggingInterceptor());
        OkHttpClient okHttpClient = builder.build();
        Retrofit.Builder retrofitBuild = new Retrofit.Builder()
                .baseUrl("https://qiniu.jpark.vip/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient);
        retrofit = retrofitBuild.build();
        apiService = retrofit.create(DownLoadApiService.class);

        //创建默认配置
        option = new DownLoadOption();
        option.fileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Caldremch/download";
    }


    static final ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return (upstream).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    @Override
    public void start() {
        isCancel = false;
        if (!TextUtils.isEmpty(url)) {

            if (TextUtils.isEmpty(option.fileName)) {
                //1.先获取链接中的文件名
                //2.生成一个文件名
                option.fileName = getFileName(url);
            }

            //子线程下载并处理
            apiService.downLoad(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                            if (downLoadListener != null) {
                                downLoadListener.onStart();
                            }
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {

                            runOnIOThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (isCancel){
                                        Log.d(TAG, "run: 取消了");
                                        cancel();
                                        return;
                                    }

                                    float contentLength = responseBody.contentLength();
                                    float blockSize = contentLength / threadNum;

                                    Log.d(TAG, "onNext: blockSize=" + blockSize);


                                    


                                    InputStream inputStream = null;
                                    OutputStream outputStream = null;

                                    try {
                                        //目录创建
                                        File fileDir = new File(option.fileDir);
                                        if (!fileDir.exists()) {
                                            boolean ret =  fileDir.mkdirs();
                                            if (!ret){
                                                handleError();
                                                return;
                                            }
                                        }

                                        //文件创建
                                        File downLoadFile = new File(option.fileDir + File.separator + option.fileName);
                                        if (!downLoadFile.exists()) {
                                            boolean ret = downLoadFile.createNewFile();
                                            if (!ret){
                                                handleError();
                                                return;
                                            }
                                        }

                                        inputStream = responseBody.byteStream();
                                        outputStream = new FileOutputStream(downLoadFile);

                                        byte[] buf = new byte[1024];
                                        int len = 0;
                                        int hasDown = 0;
                                        while ((len = inputStream.read(buf)) != -1) {

                                            if (isCancel){
                                                cancel();
                                                break;
                                            }

                                            hasDown += len;
                                            int finalHasDown = hasDown;
                                            sUIHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (downLoadListener != null) {
                                                        downLoadListener.onProgress((finalHasDown / contentLength));
                                                    }
                                                }
                                            });
                                            outputStream.write(buf, 0, len);
                                        }

                                        sUIHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (downLoadListener != null) {
                                                    downLoadListener.onSuccess();
                                                }
                                            }
                                        });

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        handleError();
                                    } finally {
                                        if (inputStream != null) {
                                            try {
                                                inputStream.close();
                                                Log.d(TAG, "run: 关闭inputStream");
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                        if (outputStream != null) {
                                            try {
                                                outputStream.close();
                                                Log.d(TAG, "run: 关闭outputStream");
                                            } catch (IOException e2) {
                                                e2.printStackTrace();
                                            }
                                        }
                                    }


                                }
                            });
                        }

                        @Override
                        public void onError(Throwable e) {
                            handleError();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });


        }

    }

    private void handleError() {
        if (downLoadListener != null) {
            downLoadListener.onError();
        }
    }

    private String getFileName(String url) {

        if (TextUtils.isEmpty(url)) {
            //无后缀名
            return System.currentTimeMillis() + "";
        }

        try {
            int start = url.lastIndexOf('/');
            int end = url.length();
            String fileName = url.substring(start, end);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return System.currentTimeMillis() + "";
    }

    @Override
    public void pause() {

    }

    @Override
    public void cancel() {

        //如果还在请求中, 则取消请求
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }

        //子线程中还在处理文件, 取消线程操作
        isCancel = true;

        if (downLoadListener != null){
            downLoadListener.onCancel();
        }
    }

    @Override
    public void continueStart() {

    }

    public static class Builder {

        private Context context;
        private String url;
        private int threadNum = 1;
        DownLoadManager downLoadManager;
        private DownLoadOption option;
        private IDownLoadListener downLoadListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder with(String url) {
            this.url = url;
            return this;
        }

        public Builder threadNum(int num) {
            this.threadNum = num;
            return this;
        }

        public Builder apply(DownLoadOption option) {
            this.option = option;
            return this;
        }

        public DownLoadManager build() {
            DownLoadManager downLoadManager = new DownLoadManager(context);
            downLoadManager.setUrl(url);
            downLoadManager.setThreadNum(threadNum);
            downLoadManager.setDownLoadListener(downLoadListener);
            return downLoadManager;
        }

        public Builder listener(IDownLoadListener downLoadListener) {
            this.downLoadListener = downLoadListener;
            return this;
        }
    }

    private void runOnIOThread(Runnable runnable){
        if (runnable == null) return;
        sIoHandler.post(runnable);
    }
}
