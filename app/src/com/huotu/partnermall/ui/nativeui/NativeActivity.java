package com.huotu.partnermall.ui.nativeui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huotu.android.library.buyer.Jlibrary;
import com.huotu.android.library.buyer.bean.Data.ClassEvent;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.Data.LoadCompleteEvent;
import com.huotu.android.library.buyer.bean.Data.SearchEvent;
import com.huotu.android.library.buyer.bean.Data.SmartUiEvent;
import com.huotu.android.library.buyer.bean.Data.StartLoadEvent;
import com.huotu.android.library.buyer.bean.PageConfig;
import com.huotu.android.library.buyer.bean.WidgetConfig;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.GsonUtil;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.widget.FooterWidget.FooterOneWidget;
import com.huotu.android.library.buyer.widget.GoodsListWidget.IListView;
import com.huotu.android.library.buyer.widget.SearchWidget.ISearch;
import com.huotu.android.library.buyer.widget.WidgetBuilder;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.config.NativeConstants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.AppUpdateActivity;
import com.huotu.partnermall.ui.base.NativeBaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.MsgPopWindow;
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
        implements PullToRefreshBase.OnRefreshListener2 {
    @Bind(R.id.activity_native_scrollview)
    PullToRefreshScrollView scrollView;
    @Bind(R.id.activity_native_main)
    LinearLayout llMain;
    @Bind(R.id.activity_native_footer)
    RelativeLayout rlFooter;
    @Bind(R.id.activity_native_root)
    RelativeLayout llRoot;
    @Bind(R.id.activity_native_header)
    LinearLayout llHeader;

    IListView listView;
    ISearch searchWidget;
    String smartuiConfigUrl;
    //商品分类id
    int classid;
    //搜索关键字
    String keyword;
    MsgPopWindow msgPopWindow;

    public String getConfigUrl(){
        String temp=smartuiConfigUrl;
        if (!temp.endsWith("/")) temp += "/";
        String url = temp + NativeConstants.NATIVECODE_URL;
        url += "?platform=Android&version=0&osVersion=";
        return url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        if (getIntent().hasExtra(NativeConstants.KEY_SMARTUICONFIGURL)) {
            smartuiConfigUrl = getIntent().getStringExtra(NativeConstants.KEY_SMARTUICONFIGURL);
        }
        if (getIntent().hasExtra(NativeConstants.KEY_CLASSID)) {
            classid = getIntent().getIntExtra(NativeConstants.KEY_CLASSID, 0);
        }
        if (getIntent().hasExtra(NativeConstants.KEY_SEARCH)) {
            keyword = getIntent().getStringExtra(NativeConstants.KEY_SEARCH);
        }

        if (getIntent().hasExtra(NativeConstants.KEY_ISMAINUI)) {
            //如果是主界面，则设置相关参数
            isMainUI = getIntent().getBooleanExtra(NativeConstants.KEY_ISMAINUI, false);
            Jlibrary.initMainUIConfigUrl(smartuiConfigUrl);
        }

        ButterKnife.bind(this);

        //llRoot.setBackgroundColor(SystemTools.obtainColor(BaseApplication.single.obtainMainColor()));
        setImmerseLayout(llHeader);

        Jlibrary.initUserLevelId(BaseApplication.single.readMemberLevelId());

        getIndexConfig();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //loadWidgets();
        //scrollView.onRefreshComplete();
        //String rootUrl = PreferenceHelper.readString( BaseApplication.single , NativeConstants.UI_CONFIG_FILE , NativeConstants.UI_CONFIG_SELF_HREF );

        getIndexConfig();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        asyncGetGoods(false, classid);
    }

    /**
     * 异步获得商品列表
     */
    protected void asyncGetGoods(boolean isRefresh, int classid) {
        if (listView == null) return;
        if (searchWidget != null) {
            keyword = searchWidget.getKeyword();
        } else {
            keyword = "";
        }
        listView.asyncGetGoodsData(isRefresh, classid, keyword);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Register();

//        if(getIntent().hasExtra(NativeConstants.KEY_NEEDREFRESHUI )){
//            getIndexConfig();
//            getIntent().removeExtra(NativeConstants.KEY_NEEDREFRESHUI);
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.hasExtra(NativeConstants.KEY_NEEDREFRESHUI )){
            getIndexConfig();
            intent.removeExtra(NativeConstants.KEY_NEEDREFRESHUI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UnRegister();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void getIndexConfig() {
        String url = getConfigUrl();

        Map<String, String> headers = new HashMap();
        headers.put(NativeConstants.HEADER_USER_KEY, NativeConstants.NATIVIE_KEY());
        String random = String.valueOf(System.currentTimeMillis());
        headers.put(NativeConstants.HEADER_USER_RANDOM, random);
        String secure = SignUtil.getSecure(NativeConstants.NATIVIE_KEY(), NativeConstants.Native_security(), random);
        headers.put(NativeConstants.HEADER_USER_SECURE, secure);

        GsonRequest<PageConfig> request = new GsonRequest<PageConfig>(
                Request.Method.GET,
                url,
                PageConfig.class,
                headers,
                new nativeCodeListener(NativeActivity.this, url),
                new nativeCodeErrorListener(NativeActivity.this)
        );
        VolleyUtil.getRequestQueue().add(request);
    }

    /**
     *  判断 是否需要升级APP
     */
    protected boolean judgeVersion(PageConfig pageConfig) {
        if (pageConfig == null || pageConfig.getWidgets() == null || pageConfig.getWidgets().size() < 1)
            return true;
        int localVersion = NativeConstants.Version();
        boolean isPass = true;
        for (WidgetConfig item : pageConfig.getWidgets()) {
            int version = item.getVersion();
            if (version > localVersion) {
                isPass = false;
                break;
            }
        }
        if (!isPass) {
            if( msgPopWindow==null) {
                msgPopWindow = new MsgPopWindow(NativeActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //forceUpdateApp();
                        msgPopWindow.dismiss();
                        NativeActivity.this.finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        msgPopWindow.dismiss();
                        //NativeActivity.this.finish();
                        loadWidgets(getConfigUrl());
                    }
                }, "版本升级提示", "当前版本低于服务器版本，无法正常显示，建议你到Android应用市场升级App", false);
                msgPopWindow.setWindowsStyle();
            }
            msgPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
        return isPass;
    }

    /**
     * 强制APP升级
     */
    protected void forceUpdateApp() {
        boolean isForce = true;
        AppUpdateActivity.UpdateType type = com.huotu.partnermall.ui.AppUpdateActivity.UpdateType.FullUpate;
        String md5 = "";//model.getUpdateMD5();
        String url = ""; //model.getUpdateUrl();
        String tips = "";//model.getUpdateTips();

        Intent intent = new Intent(NativeActivity.this, com.huotu.partnermall.ui.AppUpdateActivity.class);
        intent.putExtra("isForce", isForce);
        intent.putExtra("type", type);
        intent.putExtra("md5", md5);
        intent.putExtra("url", url);
        intent.putExtra("tips", tips);
        //startActivityForResult(intent, NativeConstants.REQUEST_CODE_CLIENT_DOWNLOAD);
        startActivity(intent);
    }

    protected void loadWidgets(String url) {
        llMain.removeAllViews();
        rlFooter.removeAllViews();

        String json = PreferenceHelper.readString(this, NativeConstants.UI_CONFIG_FILE, url);
        if (TextUtils.isEmpty(json)) {
            Toast.makeText(this,"配置信息丢失,建议您升级最新版本的App",Toast.LENGTH_LONG).show();
            return;
        }

        GsonUtil<PageConfig> gsonUtil = new GsonUtil<>();
        PageConfig pageConfig = new PageConfig();
        pageConfig = gsonUtil.toBean(json, pageConfig);
        //设置 页面的资源根地址
        Jlibrary.initResourceUrl(pageConfig.getMallResourceURL());

        for (WidgetConfig config : pageConfig.getWidgets()) {
            View view = WidgetBuilder.build(config, this);
            if (view == null) break;
            if (CommonUtil.isInterface(view.getClass(), IListView.class.getName())) {
                listView = (IListView) view;
            } else if (view instanceof FooterOneWidget) {//底部导航栏组件
                rlFooter.removeAllViews();
                rlFooter.addView(view);
                continue;
            } else if (CommonUtil.isInterface(view.getClass(), ISearch.class.getName())) {
                searchWidget = (ISearch) view;
                searchWidget.setKeyWord(keyword);
                searchWidget.setIsMainUi(isMainUI);
            }
            llMain.addView(view);
        }

        if (listView == null) {
            scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        }
        scrollView.setOnRefreshListener(this);

        if (listView != null) showProgress("正在加载数据...");
        asyncGetGoods(true, classid);
    }

    static class nativeCodeListener implements Response.Listener<PageConfig> {
        WeakReference<NativeActivity> ref;
        String url;

        public nativeCodeListener(NativeActivity act, String url) {
            ref = new WeakReference<>(act);
            this.url = url;
        }

        @Override
        public void onResponse(PageConfig pageConfig) {
            if (ref.get() == null) return;
            if (pageConfig == null) {
                //ToastUtils.showLongToast(ref.get(), "请求失败！");
                Toast.makeText(ref.get(),"请求失败！",Toast.LENGTH_LONG).show();
                ref.get().scrollView.onRefreshComplete();
                ref.get().dismissProgress();
                return;
            }

            boolean isUpdate = ref.get().judgeVersion(pageConfig);
            if(!isUpdate) {
                ref.get().scrollView.onRefreshComplete();
                ref.get().dismissProgress();
                return;
            }

            JSONUtil<PageConfig> jsonUtil = new JSONUtil<>();
            String json = jsonUtil.toJson(pageConfig);
            PreferenceHelper.writeString(ref.get(), NativeConstants.UI_CONFIG_FILE, url, json);

            ref.get().scrollView.onRefreshComplete();
            ref.get().dismissProgress();
            ref.get().loadWidgets(url);
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
            Toast.makeText(ref.get(), "请求异常",Toast.LENGTH_LONG).show();
            Logger.e("请求异常"+volleyError.getMessage());
            ref.get().scrollView.onRefreshComplete();
            ref.get().dismissProgress();
        }
    }

    public void Register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void UnRegister() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshData(StartLoadEvent event) {
        if (listView != null) {
            showProgress("正在加载数据...");
        }
        asyncGetGoods(true, classid);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadComplete(LoadCompleteEvent event) {
        scrollView.onRefreshComplete();
        dismissProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLinkEvnent(LinkEvent event) {
        Intent intent = new Intent(this, PageViewActivity.class);
        intent.putExtra(Constants.INTENT_URL, event.getLinkUrl());
        this.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmartUiEvent(SmartUiEvent event) {

        String smartUrl = event.getConfigUrl();
        Bundle bd = new Bundle();
        bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, smartUrl);
        if( event.ismainUi() ){
            bd.putBoolean(NativeConstants.KEY_NEEDREFRESHUI,true);
            ActivityUtils.getInstance().showActivity(NativeActivity.this, NativeActivity.class, bd);
        }else {
            ActivityUtils.getInstance().showActivity(NativeActivity.this, SmartUIActivity.class, bd);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmartUiClassEvent(ClassEvent event) {
        //if(!isMainUI)return;

        String smartUrl = event.getConfigUrl();
        Bundle bd = new Bundle();
        bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, smartUrl);
        bd.putInt(NativeConstants.KEY_CLASSID, event.getClassId());
        //if(){
            ActivityUtils.getInstance().showActivity(NativeActivity.this, SmartUIActivity.class, bd);
        //}else {
        //    ActivityUtils.getInstance().skipActivity(NativeActivity.this, NativeActivity.class, bd);
        //}
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchUIEvent(SearchEvent event) {
        String smartUrl = event.getConfigUrl();
        Bundle bd = new Bundle();
        bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, smartUrl);
        bd.putString(NativeConstants.KEY_SEARCH, event.getKeyword());
        //if(isMainUI){
            ActivityUtils.getInstance().showActivity(NativeActivity.this, SmartUIActivity.class, bd);
        //}else {
        //    ActivityUtils.getInstance().skipActivity(NativeActivity.this, NativeActivity.class, bd);
        //}
    }
}
