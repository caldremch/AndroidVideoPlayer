package com.caldremch.androidvideoplayer.uitls;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import java.util.Hashtable;

/**
 * @author Caldremch
 * @date 2019-05-09 21:19
 * @email caldremch@163.com
 * @describe
 **/
public class MediaMetadataRetrieverUtils {

    public static Bitmap getFrameFromMediaMetadataRetriever(String realPath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        Bitmap bitmap = null;
        try {

            if (realPath.startsWith("http://")
                    || realPath.startsWith("https://")
                    || realPath.startsWith("widevine://")) {
                mmr.setDataSource(realPath, new Hashtable<String, String>());
            } else {
                mmr.setDataSource(realPath);

            }
            bitmap = mmr.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }finally {
            mmr.release();
        }


        return bitmap;
    }

}
