package com.caldremch.democommom.recycleview;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.caldremch.common.base.BaseActivity;
import com.caldremch.democommom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caldremch
 * @date 2019-07-18 18:54
 * @email caldremch@163.com
 * @describe
 **/
public class SmoothRecyclerViewActivity extends BaseActivity {

    SmoothAdapter adapter;
    RecyclerView recyclerView;
    private List<SmoothData> smoothDataList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_smoothrecyclerview;
    }

    @Override
    public void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        initTestData();
        adapter = new SmoothAdapter(smoothDataList);
        recyclerView.setAdapter(adapter);
    }

    private void initTestData() {

       }
}
