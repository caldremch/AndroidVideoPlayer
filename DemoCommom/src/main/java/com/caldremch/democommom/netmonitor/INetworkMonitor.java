package com.caldremch.democommom.netmonitor;

/**
 * @author Caldremch
 * @date 2019-07-17 10:55
 * @email caldremch@163.com
 * @describe
 **/
public interface INetworkMonitor {
    void init ();
    void start(long checkTotalTime, long checkDuration);
    void stop();
}
