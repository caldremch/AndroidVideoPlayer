package com.caldremch.ffmpegcore;

/**
 * @author Caldremch
 * @date 2019-04-14 17:54
 * @email caldremch@163.com
 * @describe
 **/
public class FFmpegCore {

    /**
     * 1.视频录制
     * 2.视频压缩
     * 3.视频裁剪
     * 4.视频合成
     * 5.水印
     * 6.贴图,贴字
     * 7.播放器
     */


    static {
        System.loadLibrary("FFmpegCore");//需要放在首位， 不然无法识别FFmpegCore的java方法
        System.loadLibrary("avutil");
//        System.loadLibrary("fdk-aac");
        System.loadLibrary("avcodec");
        System.loadLibrary("avformat");
        System.loadLibrary("swscale");
        System.loadLibrary("swresample");
        System.loadLibrary("avfilter");
    }

    public native static int execCmd(String[] cmds);
    public native static byte[] getFrameAt(String path, int timeUnit, int option);


    //视频录制功能
    

}
