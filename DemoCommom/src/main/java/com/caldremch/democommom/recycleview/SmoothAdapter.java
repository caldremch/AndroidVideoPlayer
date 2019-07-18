package com.caldremch.democommom.recycleview;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.caldremch.democommom.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author Caldremch
 * @date 2019-07-18 18:55
 * @email caldremch@163.com
 * @describe
 **/
public class SmoothAdapter extends BaseQuickAdapter<SmoothData, BaseViewHolder> {

    public SmoothAdapter(@Nullable List<SmoothData> data) {
        super(R.layout.item_smooth, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SmoothData item) {
        ImageView imageView = helper.getView(R.id.iv);
        Glide.with(mContext).load(item.imageUrl).into(imageView);
        helper.setText(R.id.title, item.text);
    }
}
