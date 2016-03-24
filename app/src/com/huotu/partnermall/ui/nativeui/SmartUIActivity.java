package com.huotu.partnermall.ui.nativeui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huotu.android.library.buyer.widget.GoodsListWidget.IListView;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.NativeBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SmartUIActivity extends NativeBaseActivity implements PullToRefreshBase.OnRefreshListener2{
    @Bind(R.id.activity_smartui_scrollview)
    PullToRefreshScrollView scrollView;
    @Bind(R.id.activity_smartui_main)
    LinearLayout llMain;
    @Bind(R.id.activity_smartui_root)
    RelativeLayout llRoot;
    IListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_ui);

        ButterKnife.bind(this);

        setImmerseLayout(llRoot);

        loadWidgets();

        if( listView ==null ){
            scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }else {
            scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        }
        scrollView.setOnRefreshListener(this);

        if( listView!=null) showProgress("正在加载数据...");
        asyncGetGoods(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        asyncGetGoods(false);
    }

    /**
     * 异步获得商品列表
     */
    protected void asyncGetGoods(boolean isRefresh ){
        if( listView==null) return;
        listView.asyncGetGoodsData(isRefresh, 0,"");
    }

    protected void loadWidgets(){

    }
}
