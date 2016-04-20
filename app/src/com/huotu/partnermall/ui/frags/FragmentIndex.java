package com.huotu.partnermall.ui.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huotu.android.library.buyer.ConfigApiService;
import com.huotu.android.library.buyer.Jlibrary;
import com.huotu.android.library.buyer.bean.Data.BottomNavEvent;
import com.huotu.android.library.buyer.bean.Data.FooterEvent;
import com.huotu.android.library.buyer.bean.Data.HeaderEvent;
import com.huotu.android.library.buyer.bean.Data.LoadCompleteEvent;
import com.huotu.android.library.buyer.bean.Data.QuitEvent;
import com.huotu.android.library.buyer.bean.Data.StartLoadEvent;
import com.huotu.android.library.buyer.bean.PageConfig;
import com.huotu.android.library.buyer.bean.WidgetConfig;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.GsonUtil;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.android.library.buyer.widget.FooterWidget.FooterOneWidget;
import com.huotu.android.library.buyer.widget.GoodsListWidget.IListView;
import com.huotu.android.library.buyer.widget.SearchWidget.ISearch;
import com.huotu.android.library.buyer.widget.WidgetBuilder;
import com.huotu.partnermall.config.NativeConstants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.BackEvent;
import com.huotu.partnermall.ui.base.BaseFragment;
import com.huotu.partnermall.ui.base.NativeBaseActivity;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.MsgPopWindow;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class FragmentIndex extends BaseFragment implements PullToRefreshScrollView.OnRefreshListener2{
    protected String tagName = FragmentIndex.class.getName();

    @Bind(R.id.fragment_index_scrollview)
    PullToRefreshScrollView pullToRefreshScrollView;
    @Bind(R.id.fragment_index_main)
    LinearLayout llMain;

    String smartuiConfigUrl;
    IListView listView;
    ISearch searchWidget;
    //搜索关键字
    String keyword;
    //商品分类id
    int classid;
    MsgPopWindow msgPopWindow;
    PageConfig pageConfig;

    public FragmentIndex() {}

    /**
     **/
    public static FragmentIndex newInstance() {
        FragmentIndex fragment = new FragmentIndex();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i( tagName +">>>>>>>>>>>>onCreate");

        if(null==getArguments())return;

        if( getArguments().containsKey( NativeConstants.KEY_SMARTUICONFIGURL) ) {
            smartuiConfigUrl = getArguments().getString(NativeConstants.KEY_SMARTUICONFIGURL);
            smartuiConfigUrl = smartuiConfigUrl.endsWith("/") ? smartuiConfigUrl : smartuiConfigUrl + "/";
        }
        if (getArguments().containsKey(NativeConstants.KEY_CLASSID)) {
            classid = getArguments().getInt(NativeConstants.KEY_CLASSID, 0);
        }
        if (getArguments().containsKey(NativeConstants.KEY_SEARCH)) {
            keyword = getArguments().getString(NativeConstants.KEY_SEARCH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.i(tagName+">>>>>>>>>>>>onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_index, container, false);
        ButterKnife.bind( this , rootView);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Logger.i(tagName+">>>>>>>>>>>>>>>>setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public boolean getUserVisibleHint() {
        Logger.i(tagName+">>>>>>>>>>>>>>>>getUserVisibleHint");
        return super.getUserVisibleHint();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.i(tagName+">>>>>>>>>>>>>>>>onViewCreated");

        pullToRefreshScrollView.setOnRefreshListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logger.i(tagName+">>>>>>>>>>>>>>>>onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.i(tagName+">>>>>>>>>>>>>>>>onStart");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i(tagName+">>>>>>>>>>>>>>>>onResume");
        Register();

        EventBus.getDefault().post(new BackEvent(false,false));
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.i(tagName+">>>>>>>>>>>>>>>>onPause");
        UnRegister();
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.i(tagName+">>>>>>>>>>>>>>>>onStop");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.i(tagName+">>>>>>>>>>>>>>>>onActivityCreated");

        getSmartUIConfig();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.i(tagName+">>>>>>>>>>>>>>>>onDestroyView");

        ButterKnife.unbind(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        getSmartUIConfig();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        asyncGetGoods(false, classid);
    }

    protected void getSmartUIConfig() {
        String key = NativeConstants.NATIVIE_KEY();
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(NativeConstants.NATIVIE_KEY(), NativeConstants.Native_security(), random);

        ConfigApiService configApiService = RetrofitUtil.getTempRetroftClient(smartuiConfigUrl).create(ConfigApiService.class);
        String platForm = "Android";
        String version = String.valueOf(NativeConstants.Version());
        String osVersion = "";
        Call<PageConfig> call = configApiService.nativeCode(key, random, secure, platForm, version, osVersion);
        call.enqueue(new Callback<PageConfig>() {
            @Override
            public void onResponse(Call<PageConfig> call, Response<PageConfig> response) {
                if (response == null || response.body()==null ) {
                    ToastUtils.showLongToast( "请求失败！");
                    pullToRefreshScrollView.onRefreshComplete();
                    //ref.get().dismissProgress();
                    return;
                }
                boolean isUpdate = judgeSmartUIVersion(response.body());
                if(!isUpdate) {
                    pullToRefreshScrollView.onRefreshComplete();
                    //ref.get().dismissProgress();
                    return;
                }
                JSONUtil<PageConfig> jsonUtil = new JSONUtil<>();
                String json =  jsonUtil.toJson( response.body() );
                PreferenceHelper.writeString( FragmentIndex.this.getActivity() , NativeConstants.UI_CONFIG_FILE, smartuiConfigUrl , json);

                pullToRefreshScrollView.onRefreshComplete();
                //ref.get().dismissProgress();
                loadWidgets( smartuiConfigUrl );
            }

            @Override
            public void onFailure(Call<PageConfig> call, Throwable t) {
                ToastUtils.showLongToast( "请求异常");
                Logger.e("请求异常"+t.getMessage() );
                pullToRefreshScrollView.onRefreshComplete();
                //ref.get().dismissProgress();
            }
        });
    }

    /**
     *  判断 是否需要升级APP
     */
    protected boolean judgeSmartUIVersion(PageConfig pageConfig) {
        if (pageConfig == null || pageConfig.getWidgets() == null || pageConfig.getWidgets().size() < 1) return true;
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
                msgPopWindow = new MsgPopWindow(this.getActivity() , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //forceUpdateApp();
                        msgPopWindow.dismiss();
                        //NativeActivity.this.finish();
                        EventBus.getDefault().post(new QuitEvent());
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        msgPopWindow.dismiss();
                        loadWidgets(smartuiConfigUrl);
                    }
                }, "版本升级提示", "当前版本低于服务器版本，无法正常显示，建议你到Android应用市场升级App", false);
                msgPopWindow.setWindowsStyle();
            }
            msgPopWindow.showAtLocation(this.getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
        return isPass;
    }

    /**
     *
     * @param url
     */
    protected void loadWidgets(String url) {
        llMain.removeAllViews();
        //rlFooter.removeAllViews();

        String json = PreferenceHelper.readString(this.getActivity() , NativeConstants.UI_CONFIG_FILE, url);
        if (TextUtils.isEmpty(json)) {
            ToastUtils.showLongToast("配置信息丢失,建议您升级最新版本的App");
            return;
        }

        GsonUtil<PageConfig> gsonUtil = new GsonUtil<>();
        pageConfig = new PageConfig();
        pageConfig = gsonUtil.toBean(json, pageConfig);
        if(null==pageConfig) return;
        //设置 页面的资源根地址
        Jlibrary.initResourceUrl(pageConfig.getMallResourceURL());

        EventBus.getDefault().post(new HeaderEvent(pageConfig.getTitle()));

        for (WidgetConfig config : pageConfig.getWidgets()) {
            View view = WidgetBuilder.build(config, this.getActivity() );
            if (view == null) break;
            if (CommonUtil.isInterface(view.getClass(), IListView.class.getName())) {
                listView = (IListView) view;
            } else if (view instanceof FooterOneWidget) {//底部导航栏组件
                if(isMainUI) {//
                    EventBus.getDefault().post(new BottomNavEvent(config));
                }
                continue;
            } else if (CommonUtil.isInterface(view.getClass(), ISearch.class.getName())) {
                searchWidget = (ISearch) view;
                searchWidget.setKeyWord(keyword);
                searchWidget.setIsMainUi(isMainUI );
            }
            llMain.addView(view);
        }

        if (listView == null) {
            pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        }
        //pullToRefreshScrollView.setOnRefreshListener(this);

        if (listView != null) ( (NativeBaseActivity) this.getActivity()).showProgress("正在加载数据...");
        asyncGetGoods(true, classid);
    }

    /**
     * 异步获得商品列表
     */
    protected void asyncGetGoods(boolean isRefresh, int classId) {
        if (listView == null) return;
        if (searchWidget != null) {
            keyword = searchWidget.getKeyword();
        } else {
            keyword = "";
        }
        listView.asyncGetGoodsData(isRefresh, classId, keyword);
    }

    @Override
    public void share(){
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadComplete(LoadCompleteEvent event) {
        pullToRefreshScrollView.onRefreshComplete();
        ((NativeBaseActivity)this.getActivity()).dismissProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshData(StartLoadEvent event) {
        if (listView != null) {
            //showProgress("正在加载数据...");
        }
        asyncGetGoods(true, classid);
    }

    @Override
    public void setUrl(String url) {
        super.setUrl(url);

        if( pageConfig==null) return;
        EventBus.getDefault().post(new HeaderEvent(pageConfig.getTitle()));
        EventBus.getDefault().post(new BackEvent(false,false));
    }

    @Override
    public void refreshTitle(){
        if( pageConfig==null) return;
        EventBus.getDefault().post( new HeaderEvent( pageConfig.getTitle() ));
        EventBus.getDefault().post(new BackEvent( false, false));
        EventBus.getDefault().post(new FooterEvent(true));
    }
}
