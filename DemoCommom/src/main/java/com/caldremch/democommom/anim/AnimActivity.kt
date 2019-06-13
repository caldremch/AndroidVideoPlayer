package com.caldremch.democommom.anim

import com.caldremch.democommom.base.BaseTestListActivity
import com.caldremch.democommom.ui.LoginActivity

/**
 *
 * @author Caldremch
 *
 * @date 2019-06-13 16:24
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
class AnimActivity : BaseTestListActivity() {



    override fun initData() {
        add("登录界面-底部弹出")
    }

    override fun onItemClick(itemName: String, pos: Int) {
        when(pos){
            0 -> {
                startAct(LoginActivity::class.java)
            }
        }
    }
}