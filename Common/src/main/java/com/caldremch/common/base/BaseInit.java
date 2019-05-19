package com.caldremch.common.base;

import android.view.View;

import androidx.annotation.LayoutRes;

/**
 * @author Caldremch
 * @date 2019/1/24
 * @Email caldremch@163.com
 * @describe
 **/
public interface BaseInit {

    default View getLayoutView(){
        return null;
    }

    default int getLayoutId(){
        return 0;
    }

    default void initView() {
    }

    default void initData() {
    }

    default void initEvent() {
    }


}
