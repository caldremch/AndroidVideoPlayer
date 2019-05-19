package com.caldremch.common.utils;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.ParameterizedType;


/**
 * @author Caldremch
 * @date 2019-02-15 17:27
 * @describe
 **/
public class Utils {

    private static Application sApp;

    public static Application getApp() {
        if (sApp == null) {
            throw new RuntimeException("请在 Application 中初始化...Utils.init(this)");
        }
        return sApp;
    }

    public static void init(Application context) {
        sApp = context;
    }

    public static Context getContext() {
        return getApp().getApplicationContext();
    }



    /**
     * 创建泛型对象
     *
     * @param o   携带泛型的对象
     * @param <T> 具体类型
     * @return
     */
    public static <T> T getInstance(Object o) {

        try {
            //项目特殊原因, 分页的时候, 将会有两个泛型

            return ((Class<T>) (((ParameterizedType) o.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0])).newInstance();


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
