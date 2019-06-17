package com.caldremch.androidvideoplayer.Activity

import com.caldremch.democommom.base.BaseTestListActivity

/**
 *
 * @author Caldremch
 *
 * @date 2019-06-17 17:21
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
class CameraListActivity : BaseTestListActivity() {


    override fun initData() {
        add("CameraX")
        add("Camera1")
        add("Camera2")
    }

    override fun onItemClick(itemName: String, pos: Int) {
        when(pos){
            0 -> {
                startAct(CameraActivity::class.java)
            }
        }
    }
}