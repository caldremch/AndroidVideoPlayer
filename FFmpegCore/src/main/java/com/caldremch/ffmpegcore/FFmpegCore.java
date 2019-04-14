package com.caldremch.ffmpegcore;

/**
 * @author Caldremch
 * @date 2019-04-14 17:54
 * @email caldremch@163.com
 * @describe
 **/
public class FFmpegCore {


    static {
        System.loadLibrary("FFmpegCore");//需要放在首位， 不然无法识别FFmpegCore的java方法
        System.loadLibrary("avutil");
        System.loadLibrary("fdk-aac");
        System.loadLibrary("avcodec");
        System.loadLibrary("avformat");
        System.loadLibrary("swscale");
        System.loadLibrary("swresample");
        System.loadLibrary("avfilter");
    }

    public native static void invokeTest2(String[] cmds);
}
