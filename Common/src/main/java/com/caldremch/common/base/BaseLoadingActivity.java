package com.caldremch.common.base;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import com.caldremch.common.R;
import com.caldremch.common.utils.DensityUtil;
import com.caldremch.common.utils.MetricsUtils;
import com.caldremch.common.widget.EasyTitleBar;
import com.caldremch.common.widget.MultiStateView;

/**
 * @author Caldremch
 * @date 2019-04-24 11:13
 * @email caldremch@163.com
 * @describe
 **/
public class BaseLoadingActivity extends BaseActivity {


    private MultiStateView mStateView;
    private ConstraintLayout mRootLayout;
    private ViewGroup mRealContentView;

    /**
     * 在BaseLoadingActivity子类中使用已经失效,无用设置
     *
     * @return
     */
    @Deprecated
    @Override
    public final int getLayoutId() {
        return 0;
    }


    protected int getContentViewId() {
        return 0;
    }

    @Override
    public View getLayoutView() {


        mRootLayout = new ConstraintLayout(mContext);

        //加载View 要在 TitleView 的底下, 不能遮拦 TitleView
        //1.默认设置一个 TitleView,
        //2.使用自定义的 TitleView
        //3.规定开发在布局中使用固定的 id 作为 TitleView 的 id

        //加载
        if (getContentViewId() != 0) {
            ViewGroup childRootView = (ViewGroup) LayoutInflater.from(mContext).inflate(getContentViewId(), null);
            mRealContentView = childRootView;
            if (childRootView != null) {

                final View titleView = childRootView.findViewById(R.id.android_common_title_view_id);

                if (titleView == null) {
//
//                    if (getInnerLayoutTitleViewId() != 0) {
//
//
//
//                    } else
//
                    if (getTitleViewId() == 0) {

                        //默认 titleView
//                        EasyTitleBar defaultTitleView = new EasyTitleBar(mContext);
//                        mRootLayout.addView(defaultTitleView);
//                        ConstraintLayout.LayoutParams titleParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        titleParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
//                        titleParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
//                        titleParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
//                        defaultTitleView.setLayoutParams(titleParams);
//                        defaultTitleView.setTitle("默认标题");
//                        defaultTitleView.setId(R.id.default_android_common_title_view_id);
//                        initContentView(mRootLayout, childRootView, R.id.default_android_common_title_view_id);
                        initContentView(mRootLayout, childRootView, ConstraintLayout.LayoutParams.PARENT_ID);

                    } else {
                        //自定义 titleView
                        View autoAddTitleView = LayoutInflater.from(mContext).inflate(getTitleViewId(), mRootLayout, false);
                        ConstraintLayout.LayoutParams titleParams = (ConstraintLayout.LayoutParams) autoAddTitleView.getLayoutParams();
                        //内部做适配处理, 根据自己项目定制
                        autoAddTitleView.setPadding(0, MetricsUtils.getStatusBarHeight(mContext), 0, 0);
                        mRootLayout.addView(autoAddTitleView);
                        titleParams.width = 0;
                        titleParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                        titleParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                        titleParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                        autoAddTitleView.setLayoutParams(titleParams);
                        if (autoAddTitleView.getId() == View.NO_ID) {
                            autoAddTitleView.setId(R.id.default_android_common_title_view_id);
                        }
                        initContentView(mRootLayout, childRootView, autoAddTitleView.getId());

                    }
                } else {

                    // TODO: 2019-04-24 此种情况下, childRootView的跟布局必须是ConstraintLayout, 后期再优化 , 如何解决这种情况
                    mRootLayout.addView(childRootView);

                    //建立一个空的 View
                    View placeHolderView = new TextView(mContext);
                    placeHolderView.setBackgroundColor(Color.RED);
                    mStateView = new MultiStateView(mContext);
                    mStateView.addView(placeHolderView);

                    //设置 ChildView 的布局参数
                    ConstraintLayout.LayoutParams childRootViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    childRootViewLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                    childRootViewLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                    childRootViewLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                    childRootViewLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                    childRootView.setLayoutParams(childRootViewLayoutParams);


                    //添加loadding到titleview的地下
                    //设置 stateView 的布局参数
                    ConstraintLayout.LayoutParams stateViewLayoutParams = new ConstraintLayout.LayoutParams(0, 0);
                    stateViewLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                    stateViewLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                    stateViewLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                    stateViewLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                    mStateView.setLayoutParams(stateViewLayoutParams);


                    mStateView.setViewForState(R.layout.common_layout_status_empty, MultiStateView.VIEW_STATE_EMPTY);
                    mStateView.setViewForState(R.layout.common_layout_status_loading, MultiStateView.VIEW_STATE_LOADING);
                    mStateView.setViewForState(R.layout.common_layout_status_error, MultiStateView.VIEW_STATE_ERROR);


                    //根据titleView+状态栏 高度, 设置为loadingView的 topMargin, 为了让LoadingView不遮住titleView

//                    stateViewLayoutParams.topMargin = titleView.getBottom();
//                    stateViewLayoutParams.topMargin = DensityUtil.dp2px(80);
                    Log.d("BASE", "DensityUtil.dp2px(80)-->" + DensityUtil.dp2px(80));
                    childRootView.post(new Runnable() {
                        @Override
                        public void run() {
                            int select = (titleView.getBottom() + MetricsUtils.getStatusBarHeight(mContext));
                            Log.d("BASE", "titleView.getBottom()-->" + select);
                            stateViewLayoutParams.topMargin = select;

                        }
                    });

                    mRootLayout.addView(mStateView);

                    //设置 ContentView
                    FrameLayout.LayoutParams placeHolderViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    placeHolderView.setLayoutParams(placeHolderViewParams);
                }


            }
        } else {
            //添加一个默认的布局
            TextView noLayoutHolder = new TextView(mContext);
//            mRealContentView = noLayoutHolder;
            ConstraintLayout.LayoutParams childRootViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            childRootViewLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            childRootViewLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            childRootViewLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            childRootViewLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            noLayoutHolder.setLayoutParams(childRootViewLayoutParams);
            noLayoutHolder.setPadding(40, 40, 40, 40);
            noLayoutHolder.setText("due, just override the getContentViewId() and do something, ok?");
            noLayoutHolder.setGravity(Gravity.CENTER);

            mRootLayout.addView(noLayoutHolder);


        }

        return mRootLayout;
    }

    /**
     * Set the 'Z' translation for a view * * @param view {@link View} to set 'Z' translation for * @param translationZ 'Z' translation as float
     */
    public static void setTranslationZ(View view, float translationZ) {
        ViewCompat.setTranslationZ(view, translationZ);
    }

    /**
     * @param rootLayout    根布局
     * @param childRootView 内容布局
     * @param id            内容布局要在哪个布局的底部
     */
    private void initContentView(ConstraintLayout rootLayout, View childRootView, int id) {

        mStateView = new MultiStateView(mContext);
        mStateView.addView(childRootView);
        rootLayout.addView(mStateView);

        mStateView.setViewForState(R.layout.common_layout_status_empty, MultiStateView.VIEW_STATE_EMPTY);
        mStateView.setViewForState(R.layout.common_layout_status_loading, MultiStateView.VIEW_STATE_LOADING);
        mStateView.setViewForState(R.layout.common_layout_status_error, MultiStateView.VIEW_STATE_ERROR);

        ConstraintLayout.LayoutParams stateViewLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.PARENT_ID, ConstraintLayout.LayoutParams.PARENT_ID);
        mStateView.setLayoutParams(stateViewLayoutParams);
        if (id == ConstraintLayout.LayoutParams.PARENT_ID) {
            stateViewLayoutParams.topToTop = id;
        } else {
            stateViewLayoutParams.topToBottom = id;
        }
        stateViewLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        stateViewLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        stateViewLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

        //设置 ContentView
        FrameLayout.LayoutParams childRootViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        childRootView.setLayoutParams(childRootViewParams);


    }

    protected final void onEmptyStatus() {
        if (mStateView != null) {
            mStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    protected final void onErrorStatus() {
        if (mStateView != null) {
            mStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    protected final void onContentStatus() {
        if (mStateView != null) {
            mStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
    }

    protected final void onLoadingStatus() {
        if (mStateView != null) {
            mStateView.bringToFront();
            mStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        }
    }

    /**
     * 不设置在layout布局中的ViewId
     *
     * @return
     */
    public int getTitleViewId() {
        return 0;
    }

    @Deprecated
    public int getInnerLayoutTitleViewId() {
        return 0;
    }

}
