package com.caldremch.androidvideoplayer.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.caldremch.androidvideoplayer.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author Caldremch
 * @date 2019-04-21 15:41
 * @email caldremch@163.com
 * @describe
 **/
public class VideoListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public VideoListAdapter(@Nullable List<String> data) {
        super(R.layout.video_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext).load(item).into((ImageView) helper.getView(R.id.iv));
    }
}
