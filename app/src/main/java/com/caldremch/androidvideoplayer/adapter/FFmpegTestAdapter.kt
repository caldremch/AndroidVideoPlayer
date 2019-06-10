package com.caldremch.androidvideoplayer.adapter

import com.caldremch.androidvideoplayer.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author Caldremch
 * @date 2019-06-10 17:46
 * @email caldremch@163.com
 * @describe
 */
class FFmpegTestAdapter( data: MutableList<String>?) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.ffmpeg_test_item, data) {

    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.btn, item)
    }
}
