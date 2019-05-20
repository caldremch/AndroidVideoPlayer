package com.caldremch.androidvideoplayer.uitls;

import android.util.Size;

import java.util.Comparator;

/**
 * @author Caldremch
 * @date 2019-05-20 11:05
 * @email caldremch@163.com
 * @describe
 **/
public class ClosestComp implements Comparator<Size> {

    private int w;
    private int h;

    public ClosestComp(int w, int h) {
        this.w = w;
        this.h = h;
    }

    @Override
    public int compare(Size o1, Size o2) {
        return Math.abs(o1.getWidth() - o2.getWidth()) + Math.abs(o2.getHeight() - o1.getHeight());
    }
}
