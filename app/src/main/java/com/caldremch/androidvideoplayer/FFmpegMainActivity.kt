package com.caldremch.androidvideoplayer

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.caldremch.androidvideoplayer.adapter.FFmpegTestAdapter
import com.caldremch.androidvideoplayer.ffmpeg.ListStringConstant.TO_TS
import com.caldremch.androidvideoplayer.uitls.asset.AssetUtils
import com.caldremch.common.base.BaseActivity
import com.caldremch.common.utils.MetricsUtils
import com.caldremch.ffmpegcore.utils.VedioEditUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_ffmpeg_main.*

/**
 * @author Caldremch
 * @date 2019-04-17 08:46
 * @email caldremch@163.com
 * @describe
 */
class FFmpegMainActivity : BaseActivity() {


    private lateinit var adapter: FFmpegTestAdapter;
    private lateinit var datas: MutableList<String>;

    override fun getLayoutId(): Int {
        return R.layout.activity_ffmpeg_main;
    }

    override fun initView() {
        MetricsUtils.compatTitle(this, recyclerView)
        datas = arrayListOf();
        adapter = FFmpegTestAdapter(datas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter;
    }

    override fun initData() {

        datas.add(TO_TS)

    }

    override fun initEvent() {
        adapter.onItemClickListener = object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

                when (datas.get(position)) {

                    TO_TS -> {
                        Log.d("tag", "转ts")

                        var file = AssetUtils.getAssetFile(mContext, "test2.mp4")
                        VedioEditUtils.mp4TransTs(file?.absolutePath)

                    }

                }

            }

        }
    }

}
