package com.caldremch.common.utils.download;

/**
 * @author Caldremch
 * @date 2019-07-09 09:31
 * @email caldremch@163.com
 * @describe
 **/
public interface IDownLoadListener {

    /**
     * 下载开始
     */
    default void onStart(){}

    /**
     * 下载暂停
     */
    default void onPause(){}

    /**
     * 下载取消
     */
    default void onCancel(){}

    /**
     * 回调进度
     * @param currentPos
     */
    default void onProgress(float currentPos){}

    /**
     * 下载恢复
     */
    default void onResume(){}

    /**
     * 下载成功
     */
    default void onSuccess(){}

    /**
     * 下载失败
     */
    default void onError(){}
}
