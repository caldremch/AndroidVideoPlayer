package com.caldremch.common.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.caldremch.common.utils.EventManager;

/**
 * @author Caldremch
 * @date 2019/1/25
 * @Email caldremch@163.com
 * @describe
 **/
public abstract class BaseActivity extends AbsActivity implements BaseInit {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isUseEvent()) {
            EventManager.register(this);
        }

        //support view
        if (getLayoutView() == null){
            setContentView(getLayoutId());
        }else{
            setContentView(getLayoutView());
        }
        initView();
        initData();
        initEvent();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isUseEvent()) {
            EventManager.unregister(this);
        }
    }

    protected boolean isUseEvent() {
        return false;
    }

}
