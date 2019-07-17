package com.caldremch.common.utils.download;

/**
 * @author Caldremch
 * @date 2019-07-09 09:27
 * @email caldremch@163.com
 * @describe
 **/
public interface IDownLoad {

    /**
     * 开始下载( 从 0 开始下载)
     */
    void start();

    /**
     * 暂停下载
     */
    void pause();

    /**
     * 取消下载
     */
    void cancel();


    /**
     * 断点继续下载
     */
    void continueStart();

}
