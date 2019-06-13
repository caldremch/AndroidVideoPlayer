package com.caldremch.democommom.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.utils.MetricsUtils
import com.caldremch.democommom.R
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_base_test_list.*

/**
 *
 * @author Caldremch
 *
 * @date 2019-06-13 16:06
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/
abstract class BaseTestListActivity : BaseActivity() {

    protected lateinit var adapter: BaseListTestAdapter;
    protected lateinit var datas: MutableList<String>;

    protected fun<T> startAct(clz:Class<T>) {
        startActivity(Intent(this, clz))
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_base_test_list;
    }

    override fun initView() {
        MetricsUtils.compatTitle(this, recyclerView)
        datas = arrayListOf();
        adapter = BaseListTestAdapter(datas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter;
    }

    override fun initEvent() {
        adapter.onItemClickListener = object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

                onItemClick(datas.get(position), position)
            }
        }
    }

    protected fun add(item: String) {
        datas.add(item)
        adapter.notifyDataSetChanged()
    }

    protected fun add(item: MutableList<String>) {
        datas.addAll(item)
        adapter.notifyDataSetChanged()
    }

    abstract fun onItemClick(itemName: String, pos: Int);
}
