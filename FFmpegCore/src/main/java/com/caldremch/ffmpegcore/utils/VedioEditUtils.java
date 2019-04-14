package com.caldremch.ffmpegcore.utils;

import android.util.Log;

import com.caldremch.ffmpegcore.FFmpegCore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caldremch
 * @date 2019-04-11 15:29
 * @email caldremch@163.com
 * @describe
 **/
public class VedioEditUtils {


    /**
     * 视频裁剪
     * @param srcPath    源媒体路径
     * @param start     起始位置，秒
     * @param duration  时长，秒
     */
    /*public static String videoCut(String srcPath, float start, float duration) {

        String dstFile = FileUtils.createFileInBox("mp4");

        List<String> cmdList = new ArrayList<String>();

        cmdList.add("ffmpeg");

        cmdList.add("-i");
        cmdList.add(srcPath);

        cmdList.add("-ss");
        cmdList.add(String.valueOf(start));

        cmdList.add("-t");
        cmdList.add(String.valueOf(duration));

        cmdList.add("-vcodec");
        cmdList.add("copy");

        cmdList.add("-acodec");
        cmdList.add("copy");

        cmdList.add("-y");
        cmdList.add(dstFile);

        String finalCmd = "";
        String[] command = new String[cmdList.size()];
        for (int i = 0; i < cmdList.size(); i++) {
            command[i] = (String) cmdList.get(i);
            finalCmd+=command[i]+" ";
        }
        int ret = FFmpegBridge.jxFFmpegCMDRun(finalCmd);
        if (ret == 0) {
            return dstFile;
        } else {
            FileUtils.deleteFile(dstFile);
            return null;
        }
    }*/


    /**
     * 将ts流转码成MP4
     * @param tsArray
     * @return
     */
    public static String tsTransMP4(String[] tsArray) {

            String dstPath = FileUtils.createFileInBox(".mp4");
            String concat = "concat:";
            for (int i = 0; i < tsArray.length - 1; i++) {
                concat += tsArray[i];
                concat += "|";
            }
            concat += tsArray[tsArray.length - 1];

            List<String> cmdList = new ArrayList<String>();
            cmdList.add("ffmpeg");

            cmdList.add("-i");
            cmdList.add(concat);

            cmdList.add("-c");
            cmdList.add("copy");

            cmdList.add("-bsf:a");
            cmdList.add("aac_adtstoasc");

            cmdList.add("-y");
            cmdList.add(dstPath);

            String[] command = new String[cmdList.size()];
            for (int i = 0; i < cmdList.size(); i++) {
                command[i] = (String) cmdList.get(i);
            }
            int ret = FFmpegCore.execCmd(command);
            if (ret == 0) {
                return dstPath;
            } else {
                FileUtils.deleteFile(dstPath);
                return null;
            }

}

    /**
     * 将分段录制的视频拼接在一起，视频的帧率、宽高大小等需要保证一致
     * @param mp4Array
     * @return
     */
    public  static String concatMp4(String[] mp4Array) {
        ArrayList<String> tsPathArray = new ArrayList<String>();
        // 1、转码ts
        for (int i = 0; i < mp4Array.length; i++) {
            String segTs1 = mp4TransTs(mp4Array[i]);
            tsPathArray.add(segTs1);
        }

        //2、把ts流转码成mp4
        String[] tsPaths = new String[tsPathArray.size()];
        for (int i = 0; i < tsPathArray.size(); i++) {
            tsPaths[i] = (String) tsPathArray.get(i);
        }
        String dstPath = tsTransMP4(tsPaths);

        //3、删除ts文件.
        for (int i = 0; i < tsPathArray.size(); i++) {
            FileUtils.deleteFile(tsPathArray.get(i));
        }
        return dstPath;
    }
    /**
     * mp4转码成ts流，内部使用
     *
     * @param srcPath
     * @return
     */
    public static String mp4TransTs(String srcPath) {


        List<String> cmdList = new ArrayList<String>();

        String dstTs = FileUtils.createFileInBox("ts");
        cmdList.add("ffmpeg");

        cmdList.add("-i");
        cmdList.add(srcPath);

        cmdList.add("-c");
        cmdList.add("copy");

        cmdList.add("-bsf:v");
        cmdList.add("h264_mp4toannexb");

        cmdList.add("-f");
        cmdList.add("mpegts");

        cmdList.add("-y");
        cmdList.add(dstTs);

        String[] command = new String[cmdList.size()];
        for (int i = 0; i < cmdList.size(); i++) {
            command[i] = (String) cmdList.get(i);
        }
        int ret = FFmpegCore.execCmd(command);

        if (ret == 0) {
            Log.d("caldremch", "转换成功");
            return dstTs;
        } else {
            Log.d("caldremch", "转换失败, 删除文件");
            FileUtils.deleteFile(dstTs);
            return null;
        }
    }
}
