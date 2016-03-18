package com.huotu.partnermall.ui.nativeui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huotu.android.library.buyer.Jlibrary;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.Data.LoadCompleteEvent;
import com.huotu.android.library.buyer.bean.Data.StartLoadEvent;
import com.huotu.android.library.buyer.bean.PageConfig;
import com.huotu.android.library.buyer.bean.WidgetConfig;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.GsonUtil;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.widget.FooterWidget.FooterOneWidget;
import com.huotu.android.library.buyer.widget.GoodsListWidget.IListView;
import com.huotu.android.library.buyer.widget.WidgetBuilder;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.config.NativeConstants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.NativeBaseActivity;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NativeActivity
        extends NativeBaseActivity
        implements Handler.Callback , PullToRefreshBase.OnRefreshListener2{
    @Bind(R.id.activity_native_scrollview)
    PullToRefreshScrollView scrollView;
    @Bind(R.id.activity_native_main)
    LinearLayout llMain;
    @Bind(R.id.activity_native_footer)
    RelativeLayout rlFooter;
    @Bind(R.id.activity_native_root)
    RelativeLayout llRoot;
    PageConfig uiConfig;
    Handler mHandler;
    IListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        ButterKnife.bind(this);

        setImmerseLayout(llRoot);

        Register();

        mHandler = new Handler(this);

        Jlibrary.initUserLevelId(BaseApplication.single.readMemberLevelId());

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
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //loadWidgets();
        //scrollView.onRefreshComplete();
        getIndexConfig();
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
        listView.asyncGetGoodsData( isRefresh );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        UnRegister();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    private void getIndexConfig(){
        String rootUrl = PreferenceHelper.readString( BaseApplication.single , NativeConstants.UI_CONFIG_FILE , NativeConstants.UI_CONFIG_SELF_HREF );
        if( !rootUrl.endsWith("/") ) rootUrl+="/";
        String url = rootUrl + NativeConstants.NATIVECODE_URL;
        url +="?platform=Android&version=000000000000000&osVersion=";
        Map<String,String> headers = new HashMap();
        headers.put(NativeConstants.HEADER_USER_KEY, NativeConstants.NATIVIE_KEY());
        String random = String.valueOf(System.currentTimeMillis());
        headers.put(NativeConstants.HEADER_USER_RANDOM, random );
        String secure = SignUtil.getSecure(NativeConstants.NATIVIE_KEY(), NativeConstants.Native_security(), random);
        headers.put(NativeConstants.HEADER_USER_SECURE, secure );

        GsonRequest<PageConfig> request = new GsonRequest<PageConfig>(
                Request.Method.GET,
                url,
                PageConfig.class,
                headers,
                new nativeCodeListener(NativeActivity.this),
                new nativeCodeErrorListener(NativeActivity.this)
        );
        VolleyUtil.getRequestQueue().add(request);
    }

    protected void loadWidgets(){
        llMain.removeAllViews();
        rlFooter.removeAllViews();

        String json = PreferenceHelper.readString(this, NativeConstants.UI_CONFIG_FILE, NativeConstants.UI_CONFIG_SELF_KEY);
        if(TextUtils.isEmpty(json)){
            ToastUtils.showLongToast(this,"配置信息丢失");
            return;
        }

        GsonUtil<PageConfig> gsonUtil = new GsonUtil<>();
        PageConfig pageConfig = new PageConfig();
        pageConfig = gsonUtil.toBean(json , pageConfig);
        //设置 页面的资源根地址
        Jlibrary.initResourceUrl(pageConfig.getMallResourceURL());

        for(WidgetConfig config : pageConfig.getWidgets() ) {
            View view = WidgetBuilder.build( config , this );
            if( view == null) break;
            if( CommonUtil.isInterface(view.getClass(), IListView.class.getName())  ){
                listView = (IListView)view;
            }else if( view instanceof FooterOneWidget){//底部导航栏组件
                rlFooter.removeAllViews();
                rlFooter.addView(view);
                continue;
            }
            llMain.addView(view);
        }
    }

    static class nativeCodeListener implements Response.Listener<PageConfig>{
        WeakReference<NativeActivity> ref;
        public nativeCodeListener(NativeActivity act){
            ref =new WeakReference<>(act);
        }
        @Override
        public void onResponse(PageConfig pageConfig ) {
            if (ref.get() == null) return;
            if (pageConfig == null) {
                ToastUtils.showLongToast(ref.get(), "请求失败！");
                ref.get().scrollView.onRefreshComplete();
                ref.get().dismissProgress();
                return;
            }

            JSONUtil<PageConfig> jsonUtil = new JSONUtil<>();
            String json = jsonUtil.toJson(pageConfig);
            PreferenceHelper.writeString( ref.get() , NativeConstants.UI_CONFIG_FILE , NativeConstants.UI_CONFIG_SELF_KEY , json );

            ref.get().loadWidgets();
            ref.get().scrollView.onRefreshComplete();
            ref.get().dismissProgress();
            ref.get().onRefreshData(new StartLoadEvent());
        }
    }

    static class nativeCodeErrorListener implements Response.ErrorListener {
        WeakReference<NativeActivity> ref;

        public nativeCodeErrorListener(NativeActivity act) {
            ref = new WeakReference<>(act);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (ref.get() == null) return;
            ToastUtils.showLongToast(ref.get(), "请求异常");
            Logger.e("请求异常");
            ref.get().scrollView.onRefreshComplete();
        }
    }

    public void Register(){
        if( !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void UnRegister(){
        if( EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshData(StartLoadEvent event ){
        if( listView !=null) {
            showProgress("正在加载数据...");
        }
        asyncGetGoods(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadComplete(LoadCompleteEvent event){
        scrollView.onRefreshComplete();
        dismissProgress();
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onLinkEvnent(LinkEvent event){
        //Toast.makeText(context, event.getUrl(),Toast.LENGTH_LONG).show();
        if( event.getLinkName()!=null && event.getLinkName().equals("分类") ){
            Intent intent = new Intent(this,ClassActivity.class);
            this.startActivity(intent);
        }else {
            Intent intent = new Intent(this, PageViewActivity.class);
            intent.putExtra(Constants.INTENT_URL, event.getLinkUrl());
            this.startActivity(intent);
        }
    }


}
