package com.caldremch.democommom

import com.caldremch.common.base.BaseApp
import com.caldremch.common.utils.crash.CrashHandler

/**
 *
 * @author Caldremch
 *
 * @date 2019-05-21 19:46
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/

class DemoCommonApp :BaseApp() {
    override fun onCreate() {
        super.onCreate()
        CrashHandler.getInstance().init(this);
    }
}