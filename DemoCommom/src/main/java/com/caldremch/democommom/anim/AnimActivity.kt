package com.caldremch.democommom.anim

import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.caldremch.democommom.R
import com.caldremch.democommom.base.BaseTestListActivity
import com.caldremch.democommom.ui.LoginActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_base_test_list.*

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
        add("登录界面-底部弹出2")
        add("登录界面-底部弹出3")

        var lm = GridLayoutManager(this, 2);
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
             var isFooter =   adapter.getItemViewType(position) == BaseQuickAdapter.FOOTER_VIEW

                Toast.makeText(mContext, "提示", Toast.LENGTH_SHORT).show()
               return if (isFooter) 2 else 1
            }
        }
        recyclerView.layoutManager = lm
        Handler().postDelayed({
            var footer = View.inflate(this, R.layout.footer_view, null)
            adapter.addFooterView(footer)
            adapter.notifyDataSetChanged()
        }, 5000)
    }

    override fun onItemClick(itemName: String, pos: Int) {
        when(pos){
            0 -> {
                startAct(LoginActivity::class.java)
            }
        }
    }
}