package com.huotu.partnermall.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.async.LoadLogoImageAyscTask;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.AccountModel;
import com.huotu.partnermall.model.AuthMallModel;
import com.huotu.partnermall.model.BindEvent;
import com.huotu.partnermall.model.CloseEvent;
import com.huotu.partnermall.model.GoIndexEvent;
import com.huotu.partnermall.model.LinkEvent;
import com.huotu.partnermall.model.MenuBean;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.model.PhoneLoginModel;
import com.huotu.partnermall.model.RefreshHttpHeaderEvent;
import com.huotu.partnermall.model.RefreshMenuEvent;
import com.huotu.partnermall.model.RefreshPageEvent;
import com.huotu.partnermall.model.ShareModel;
import com.huotu.partnermall.model.SwitchUserByUserIDEvent;
import com.huotu.partnermall.model.SwitchUserModel;
import com.huotu.partnermall.model.UpdateLeftInfoModel;
import com.huotu.partnermall.receiver.PushProcess;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.login.AutnLogin;
import com.huotu.partnermall.ui.login.BindPhoneActivity;
import com.huotu.partnermall.ui.login.PhoneLoginActivity;
//import com.huotu.partnermall.ui.sis.GoodManageActivity;
//import com.huotu.partnermall.ui.sis.SisConstant;
import com.huotu.partnermall.ui.web.UrlFilterUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.DensityUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.ObtainParamsMap;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.UIUtils;
import com.huotu.partnermall.utils.Util;
import com.huotu.partnermall.utils.WindowUtils;
import com.huotu.partnermall.widgets.ProgressPopupWindow;
import com.huotu.partnermall.widgets.SharePopupWindow;
import com.huotu.partnermall.widgets.TipAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class HomeActivity extends BaseActivity implements Handler.Callback {
    //获取资源文件对象
    private Resources resources;
    private long exitTime = 0l;
    public Handler mHandler;
    private WindowManager wManager;
    private SharePopupWindow share;
    public ProgressPopupWindow progress;
    public AssetManager am;
    public static ValueCallback< Uri > mUploadMessage;
    public static ValueCallback<Uri[]> mUploadMessages;
    public static final int FILECHOOSER_RESULTCODE = 1;
    public static final int FILECHOOSER_RESULTCODE_5 = 5;
    public static final int BINDPHONE_REQUESTCODE = 1001;
    private AutnLogin autnLogin;

    //标题栏布局对象
    @Bind(R.id.titleLayout)
    RelativeLayout homeTitle;
    //标题栏左侧图标
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    //标题栏标题文字
    @Bind(R.id.titleText)
    TextView titleText;
    //标题栏右侧图标
    @Bind(R.id.titleRightImage)
    ImageView titleRightImage;
    //web视图
    @Bind(R.id.main_webview)
    public WebView pageWeb;
    //单独加载菜单
    @Bind(R.id.menuPage)
    WebView menuView;
    //侧滑登录
    @Bind(R.id.loginLayout)
    RelativeLayout loginLayout;
    //侧滑设置按钮
    @Bind(R.id.sideslip_setting)
    ImageView loginSetting;
    //主菜单容器
    @Bind(R.id.mainMenuLayout)
    LinearLayout mainMenuLayout;
    //已授权界面
    @Bind(R.id.getAuth)
    RelativeLayout getAuthLayout;
    //用户头像
    @Bind(R.id.accountIcon)
    ImageView userLogo;
    //用户名称
    @Bind(R.id.accountName)
    TextView userName;
    //用户类型
    //@Bind(R.id.accountType)
    //TextView userType;
    @Bind(R.id.viewPage)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    //PullToRefreshWebView refreshWebView;
    @Bind(R.id.layDrag)
    DrawerLayout layDrag;
    @Bind(R.id.main_pgbar)
    ProgressBar pgBar;
    @Bind(R.id.ff1)
    FrameLayout ff1;
    @Bind(R.id.accountTypeList)
    LinearLayout accountTypeList;

    UrlFilterUtils urlFilterUtils;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        resources = HomeActivity.this.getResources();
        mHandler = new Handler ( this );
        share = new SharePopupWindow ( HomeActivity.this );
        wManager = this.getWindowManager();
        am = this.getAssets();
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Register();

        //设置沉浸模式
        setImmerseLayout(homeTitle);
        initView();

        initPush(getIntent());

        initRedrectUrl(getIntent());

        progress = new ProgressPopupWindow ( HomeActivity.this );

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAppVersion();
            }
        },500);


    }

    /***
     * 检索app版本
     */
    protected void checkAppVersion(){
        int locaolId= BaseApplication.getAppVersionId();
        int serverid= BaseApplication.readNewAppVersion();
        String appUrl = BaseApplication.readAppUlr();
        if( serverid > locaolId ){
            if( TextUtils.isEmpty(appUrl) ) {
                TipAlertDialog tipAlertDialog = new TipAlertDialog(this);
                tipAlertDialog.show("升级提示", "我们发布了新版本，您可以去应用市场下载", "", R.color.black , false , true );
                return;
            }else{
                TipAlertDialog tipAlertDialog = new TipAlertDialog(this);
                tipAlertDialog.show("升级提示", "我们发布了新版本，是否去应用市场下载？", appUrl );
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUserInfo();
        judgeLoginStatus();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        initPush( intent );

        initRedrectUrl(intent);
    }

    /***
     * 判断 登录以后，是否需要调转
     */
    protected void initRedrectUrl( Intent intent ){
        String redirecturl="";
        if( intent.hasExtra("redirecturl")){
            redirecturl = intent.getStringExtra("redirecturl");
        }
        if( TextUtils.isEmpty(redirecturl) ) return;

        String temp = redirecturl;

        if( !temp.toLowerCase().startsWith("http://")){
            temp = "http://"+temp;
        }

        pageWeb.loadUrl( temp , SignUtil.signHeader() );
    }

    protected void initUserInfo(){
        new LoadLogoImageAyscTask ( resources, userLogo, application.getUserLogo ( ), R.drawable.ic_login_username ).execute();
        //渲染用户名
        userName.setText(application.getUserName());
        userName.setTextColor(resources.getColor(R.color.theme_color));

        //userType.setTextColor(SystemTools.obtainColor(application.obtainMainColor()));
        //userType.setText(application.readMemberLevel());

        showAccountType(application.readMemberLevel());
    }

    protected void showAccountType(String dataArray ){
        if( dataArray ==null || dataArray.isEmpty() ) return;
        String[] data= dataArray.split(","); //new String[]{"普通会员","小伙伴","大伙伴","dasdfs"};
        accountTypeList.removeAllViews();
        //int leftMargin = DensityUtils.dip2px(this,2);
        int leftPadding = DensityUtils.dip2px(this,2);
        int topPadding = DensityUtils.dip2px(this,2);
        for(String item : data ) {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //layoutParams.setMargins(leftMargin,0,0,0);
            TextView tv = new TextView(this);
            tv.setId(item.hashCode());
            tv.setSingleLine();
            tv.setLayoutParams(layoutParams);
            tv.setTextColor(SystemTools.obtainColor(application.obtainMainColor()));
            tv.setText(item);
            tv.setBackgroundResource(R.drawable.member_shape);
            tv.setTextSize( TypedValue.COMPLEX_UNIT_PX , getResources().getDimensionPixelSize( R.dimen.notice_text_size));
            tv.setPadding(leftPadding,topPadding,leftPadding,topPadding);
            accountTypeList.addView(tv);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if( progress !=null ){
            progress.dismissView();
            progress=null;
        }
        if( share !=null){
            share.dismiss();
            share=null;
        }
        if( pageWeb !=null ){
            pageWeb.setVisibility(View.GONE);
        }
        if(menuView !=null){
            menuView.setVisibility(View.GONE);
        }

        UnRegister();

        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void initView ( ) {

        urlFilterUtils = new UrlFilterUtils( this, mHandler, application  );

        //设置title背景
        homeTitle.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        ff1.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        //设置左侧图标
        Drawable leftDraw = ContextCompat.getDrawable( this , R.drawable.main_title_left_sideslip);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        //设置右侧图标
        Drawable rightDraw = ContextCompat.getDrawable( this, R.drawable.home_title_right_share);
        SystemTools.loadBackground(titleRightImage, rightDraw);
        titleRightImage.setVisibility(View.GONE);
        //设置侧滑界面
        loginLayout.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        //设置设置图标
        SystemTools.loadBackground(loginSetting, ContextCompat.getDrawable(this ,R.drawable.switch_white));
        getAuthLayout.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        loginSetting.setVisibility(View.GONE);
        //设置登录界面
        getAuthLayout.setVisibility(View.VISIBLE);
        //动态加载侧滑菜单
        UIUtils ui = new UIUtils(application, HomeActivity.this, resources, mainMenuLayout, mHandler);
        ui.loadMenus();
        //监听web控件
//        pageWeb = refreshWebView.getRefreshableView();
//        refreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<WebView> pullToRefreshBase) {
//                //刷新界面
//                pageWeb.reload();
//            }
//        });

        ptrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrClassicFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                //return false;
                return PtrDefaultHandler.checkContentCanBePulledDown(frame , pageWeb,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageWeb.reload();
            }
        });

        share.setPlatformActionListener(
                new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Message msg = Message.obtain();
                        msg.what = Constants.SHARE_SUCCESS;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Message msg = Message.obtain();
                        msg.what = Constants.SHARE_ERROR;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Message msg = Message.obtain();
                        msg.what = Constants.SHARE_CANCEL;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }
                }
        );
        share.showShareWindow();
        share.setOnDismissListener(new PoponDismissListener(this));

        loadPage();
        loadMainMenu();
    }

    protected void judgeLoginStatus(){
        boolean isLogin = BaseApplication.single.isLogin();
        if(!isLogin){
            userName.setText("未登录");

            //userType.setText("点击登录");
            showAccountType("点击登录");

            userLogo.setImageResource( R.drawable.ic_login_username);
        }else{
        }
    }

    /***
     *  初始化极光推送
     */
    protected void initPush( Intent intent ) {
        if (null == intent || ! intent.hasExtra( Constants.HUOTU_PUSH_KEY )) return;
        Bundle bundle = intent.getBundleExtra( Constants.HUOTU_PUSH_KEY);
        if( bundle==null) return;

        PushProcess.process( this , bundle);
    }

    private void loadMainMenu() {
        menuView.getSettings().setJavaScriptEnabled(true);
        menuView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        menuView.getSettings().setDomStorageEnabled(true);
        menuView.getSettings().setAllowFileAccess(true);
        menuView.getSettings().setAppCacheEnabled(true);
        menuView.getSettings().setDatabaseEnabled(true);

        signHeader( menuView );

        //首页默认为商户站点 + index
        String menuUrl = application.obtainMerchantUrl () + "/bottom.aspx?customerid=" + application.readMerchantId ();
        menuView.loadUrl(menuUrl , SignUtil.signHeader() );

        menuView.setWebViewClient(
                new WebViewClient() {
                    //重写此方法，浏览器内部跳转
                    public boolean shouldOverrideUrlLoading( WebView view, String url ) {
                        if( pageWeb ==null ) return true;
                        titleRightImage.setVisibility(View.GONE);


//                        url="http://olquan.huobanj.cn/Arvato/UserCenter/Index.aspx?customerid=4471";


                        pageWeb.loadUrl(url , SignUtil.signHeader() );
                        return true;
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                    }
                }
        );
    }

    private void signHeader(){
        if( pageWeb==null) return;
        signHeader(pageWeb);
        if(menuView==null) return;
        signHeader(menuView);
    }

    private void signHeader( WebView webView ){
        String userid= application.readMemberId();
        String unionid = application.readUserUnionId();
        String openId = BaseApplication.single.readOpenId();

        String sign = ObtainParamsMap.SignHeaderString(userid, unionid , openId );
        String userAgent = webView.getSettings().getUserAgentString();
        if( TextUtils.isEmpty(userAgent) ) {
            userAgent = "mobile;"+sign;
        }else{
            int idx = userAgent.lastIndexOf(";mobile;hottec:");
            if(idx>=0){
                userAgent = userAgent.substring(0,idx);
            }
            userAgent +=";mobile;"+sign;
        }
        webView.getSettings().setUserAgentString( userAgent );
    }

    private void loadPage(){
        pageWeb.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        pageWeb.setVerticalScrollBarEnabled(false);
        pageWeb.setClickable(true);
        pageWeb.getSettings().setUseWideViewPort(true);
        //pageWeb.getSettings().setSupportZoom(true);
        //pageWeb.getSettings().setBuiltInZoomControls(true);

        pageWeb.getSettings().setJavaScriptEnabled(true);
        pageWeb.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        pageWeb.getSettings().setSaveFormData(true);
        pageWeb.getSettings().setAllowFileAccess(true);
        pageWeb.getSettings().setLoadWithOverviewMode(false);
        pageWeb.getSettings().setSavePassword(true);
        pageWeb.getSettings().setLoadsImagesAutomatically(true);
        pageWeb.getSettings().setDomStorageEnabled(true);
        pageWeb.getSettings().setAppCacheEnabled(true);
        pageWeb.getSettings().setDatabaseEnabled(true);
        String dir = BaseApplication.single.getDir("database", Context.MODE_PRIVATE).getPath();
        pageWeb.getSettings().setGeolocationDatabasePath(dir);
        pageWeb.getSettings().setGeolocationEnabled(true);
        pageWeb.addJavascriptInterface( HomeActivity.this ,"android");

        //设置app标志
        signHeader( pageWeb );
        //首页鉴权
        //AuthParamUtils paramUtils = new AuthParamUtils ( application, System.currentTimeMillis (), application.obtainMerchantUrl ( ), HomeActivity.this );
        //String url = paramUtils.obtainUrl ();
        String url = application.obtainMerchantUrl();

        //首页默认为商户站点 + index
        pageWeb.loadUrl(url , SignUtil.signHeader() );

        pageWeb.setWebViewClient(
                new WebViewClient() {
                    //重写此方法，浏览器内部跳转
                    public boolean shouldOverrideUrlLoading( WebView view, String url ) {
                        //UrlFilterUtils filter = new UrlFilterUtils( HomeActivity.this, mHandler, application  );
                        return urlFilterUtils.shouldOverrideUrlBySFriend(pageWeb, url);
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if( titleText ==null || pageWeb ==null ) return;
                        titleText.setText(view.getTitle());

                        if( UIUtils.isIndexPage( url ) || url.contains ( "&back" ) || url.contains ( "?back" )){
                            mHandler.sendEmptyMessage ( Constants.LEFT_IMG_SIDE );
                        } else {
                            if ( pageWeb.canGoBack ( ) ) {
                                mHandler.sendEmptyMessage ( Constants.LEFT_IMG_BACK );
                            }
                            else {
                                mHandler.sendEmptyMessage ( Constants.LEFT_IMG_SIDE );
                            }
                        }
                    }

                    @Override
                    public void onReceivedError( WebView view, int errorCode, String description,String failingUrl ){
                        super.onReceivedError(view, errorCode, description, failingUrl);
//                        if( refreshWebView ==null) return;
//                        refreshWebView.onRefreshComplete();
                        if(ptrClassicFrameLayout==null)return;
                        ptrClassicFrameLayout.refreshComplete();

                        if( pgBar == null) return;
                        pgBar.setVisibility(View.GONE);
                    }
                }
        );

        pageWeb.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if( titleText ==null) return;
                titleText.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if( ptrClassicFrameLayout ==null || pgBar ==null ) return;

                if(100 == newProgress) {
                    ptrClassicFrameLayout.refreshComplete();

                    pgBar.setVisibility(View.GONE);
                }else {
                    if (pgBar.getVisibility() == View.GONE) pgBar.setVisibility(View.VISIBLE);
                    pgBar.setProgress(newProgress);
                }

                super.onProgressChanged(view, newProgress);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    HomeActivity.mUploadMessage = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    HomeActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), HomeActivity.FILECHOOSER_RESULTCODE);
            }

            public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
                openFileChooser(uploadMsg);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){

                openFileChooser(uploadMsg);

            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke( origin, true, false );
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                HomeActivity.mUploadMessages = filePathCallback;

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "选择图片");

                HomeActivity.this.startActivityForResult( chooserIntent , HomeActivity.FILECHOOSER_RESULTCODE_5);

                return  true;
                //return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }else if( requestCode == BINDPHONE_REQUESTCODE && resultCode == RESULT_OK){
            mainMenuLayout.removeAllViews();
            UIUtils ui = new UIUtils(application, HomeActivity.this, resources, mainMenuLayout, mHandler);
            ui.loadMenus();
        }else if( requestCode == FILECHOOSER_RESULTCODE_5 ){
            if(null == mUploadMessages) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            if( result !=null ) {
                mUploadMessages.onReceiveValue(new Uri[]{result});
            }else{
                mUploadMessages.onReceiveValue(new Uri[]{});
            }
            mUploadMessages=null;
        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        // 2秒以内按两次推出程序
        if (event.getKeyCode () == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(pageWeb.canGoBack () && ! UIUtils.isIndexPage(pageWeb.getUrl())){
                titleRightImage.setVisibility(View.GONE);
                pageWeb.goBack ( );
            }
            else{
                if ( ( System.currentTimeMillis ( ) - exitTime ) > 2000 ) {
                    ToastUtils.showLongToast ( getApplicationContext ( ), "再按一次退出程序" );
                    exitTime = System.currentTimeMillis ( );
                }
                else {
                    try {
                        HomeActivity.this.finish ( );
                        Runtime.getRuntime ( ).exit ( 0 );
                    }
                    catch ( Exception e ) {
                        Runtime.getRuntime ( ).exit ( - 1 );
                    }
                }
            }

            return true;
        }
        return super.dispatchKeyEvent ( event );
    }

    @OnClick(R.id.titleLeftImage)
    void doBackOrMenuClick(){
        if(application.isLeftImg){
            layDrag.openDrawer(Gravity.LEFT);
        } else {
            if( pageWeb.canGoBack() ) {
                titleRightImage.setVisibility(View.GONE);
                pageWeb.goBack();
            }
        }
    }

    @OnClick(R.id.getAuth)
    void doLogin(){
        if(!BaseApplication.single.isLogin()){
            layDrag.closeDrawer(Gravity.LEFT);
            Intent intent=new Intent(this, PhoneLoginActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.sideslip_setting)
    void doSetting(){
        //切换用户
        String url = Constants.getINTERFACE_PREFIX() + "weixin/getuserlist?customerId="+application.readMerchantId ()+"&unionid="+application.readUserUnionId ();
        AuthParamUtils paramUtil = new AuthParamUtils ( application, System.currentTimeMillis (), url, HomeActivity.this );
        final String rootUrls = paramUtil.obtainUrls ( );
        HttpUtil.getInstance().doVolleyObtainUser(
                HomeActivity.this, application,
                rootUrls, titleRightImage, wManager, mHandler
        );

        //隐藏侧滑菜单
        layDrag.closeDrawer(Gravity.LEFT);
        //切换进度条
        progress.showProgress("正在获取用户数据");
        progress.showAtLocation(titleLeftImage, Gravity.CENTER, 0, 0);
    }

    /**
     *
     */
    protected void getShareContentByJS(){
        pageWeb.loadUrl("javascript:__getShareStr();");
    }

    @OnClick(R.id.titleRightImage)
    void doShare(){
        //progress.showProgress("请稍等...");
        //progress.showAtLocation(titleLeftImage, Gravity.CENTER, 0, 0);
        getShareContentByJS();
    }


    @Override
    public boolean handleMessage ( Message msg ) {
        if( layDrag !=null && layDrag.isShown()){
            layDrag.closeDrawer(Gravity.LEFT);
        }

        switch ( msg.what )
        {
            //加载页面
            case Constants.LOAD_PAGE_MESSAGE_TAG:
            {//加载菜单页面

                String url = msg.obj.toString ();
                if( url.toLowerCase().contains("http://www.bindweixin.com") ){
                    //绑定微信
                    callWeixin("");
                }
                else if( url.toLowerCase().trim().contains("http://www.bindphone.com") ){
                    //绑定手机
                    callPhone();
                }
                else if( url.toLowerCase().contains("http://www.dzd.com") ){
                    //openSis();
                }else {
                    pageWeb.loadUrl(url);
                }
            }
            break;
            case Constants.FRESHEN_PAGE_MESSAGE_TAG:
            {
                //刷新界面
                String url = msg.obj.toString ();
                pageWeb.loadUrl(url);
            }
            break;
            //分享
            case Constants.SHARE_SUCCESS:
            {
                //分享成功
                Platform platform = ( Platform ) msg.obj;
                if("WechatMoments".equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( HomeActivity.this, "微信朋友圈分享成功" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( HomeActivity.this, "微信分享成功" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( HomeActivity.this, "QQ空间分享成功" );
                }
                else if( platform.getName ().equals(SinaWeibo.NAME))
                {
                    ToastUtils.showShortToast ( HomeActivity.this, "新浪微博分享成功" );
                }
            }
            break;
            case Constants.SHARE_ERROR:
            {
                //分享失败
                Platform platform = ( Platform ) msg.obj;
                if("WechatMoments".equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( HomeActivity.this, "微信朋友圈分享失败" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( HomeActivity.this, "微信分享失败" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( HomeActivity.this, "QQ空间分享失败" );
                }
                else if(  platform.getName ().equals( SinaWeibo.NAME ))
                {
                    ToastUtils.showShortToast ( HomeActivity.this, "新浪微博分享失败" );
                }
            }
            break;
            case Constants.SHARE_CANCEL:
            {
                //分享取消
                Platform platform = ( Platform ) msg.obj;
                if("WechatMoments".equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( HomeActivity.this, "微信朋友圈分享取消" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( HomeActivity.this, "微信分享取消" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( HomeActivity.this, "QQ空间分享取消" );
                }
                else if("SinaWeibo".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( HomeActivity.this, "新浪微博分享取消" );
                }
            }
            break;
            case Constants.LEFT_IMG_SIDE:
            {
                //设置左侧图标
                Drawable leftDraw = resources.getDrawable ( R.drawable.main_title_left_sideslip );
                SystemTools.loadBackground ( titleLeftImage, leftDraw );
                application.isLeftImg = true;
            }
            break;
            case Constants.LEFT_IMG_BACK:
            {
                //设置左侧图标
                Drawable leftDraw = resources.getDrawable ( R.drawable.main_title_left_back );
                SystemTools.loadBackground ( titleLeftImage, leftDraw );
                application.isLeftImg = false;
            }
            break;
            case Constants.SWITCH_USER_NOTIFY:
            {
                SwitchUserModel.SwitchUser user = ( SwitchUserModel.SwitchUser ) msg.obj;
                //更新userId
                application.writeMemberId(String.valueOf(user.getUserid()));
                //更新昵称
                application.writeUserName(user.getWxNickName());
                application.writeUserIcon(user.getWxHeadImg());
                application.writeUserUnionId( user.getWxUnionId() );
                application.writeMemberLevel(user.getLevelName());

                //记录微信关联类型（0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号）
                application.writeMemberRelatedType(user.getRelatedType());

                //更新界面
                userName.setText(user.getWxNickName());
                //userType.setText(user.getLevelName());
                showAccountType( user.getLevelName() );

                new LoadLogoImageAyscTask ( resources, userLogo, user.getWxHeadImg ( ), R.drawable.ic_login_username ).execute ( );

                //切换用户，需要清空 店中店的 缓存数据
                //SisConstant.SHOPINFO = null;
                //SisConstant.CATEGORY = null;


                mainMenuLayout.removeAllViews();
                //动态加载侧滑菜单
                UIUtils ui = new UIUtils ( application, HomeActivity.this, resources, mainMenuLayout, mHandler );
                ui.loadMenus();



                dealUserid();
            }
            break;
            case Constants.LOAD_SWITCH_USER_OVER:
            {
                progress.dismissView();
            }
            break;
            case Constants.MSG_AUTH_COMPLETE:
            {//提示授权成功
                Platform plat = ( Platform ) msg.obj;
                autnLogin.authorize(plat);
            }
            break;
            case Constants.LOGIN_AUTH_ERROR:
            {//授权登录 失败
                titleLeftImage.setClickable ( true );
                progress.dismissView();
                ToastUtils.showShortToast(this, "授权失败");
            }
            break;
            case Constants.MSG_AUTH_ERROR:
            {//
                titleLeftImage.setClickable ( true );
                progress.dismissView ();
                Throwable throwable = ( Throwable ) msg.obj;
                //if("cn.sharesdk.wechat.utils.WechatClientNotExistException".equals ( throwable.toString () ))
                if( throwable instanceof cn.sharesdk.wechat.utils.WechatClientNotExistException ){
                    ToastUtils.showShortToast(this,"请安装微信客户端，在进行绑定操作");
                }else{
                    ToastUtils.showShortToast(this,"微信绑定失败");
                }
            }
            break;
            case Constants.MSG_AUTH_CANCEL:
            {
                if( progress!=null ){
                    progress.dismissView();
                }
                ToastUtils.showShortToast(this,"你已经取消微信授权，绑定操作失败");
            }
            break;
            case Constants.MSG_USERID_FOUND:
            {
                progress.showProgress ( "已经获取微信的用户信息" );
                progress.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
            break;
            case Constants.MSG_LOGIN:
            {
                progress.dismissView();
                AccountModel account = ( AccountModel ) msg.obj;
                bindWeiXin(account);
            }
            break;
            case Constants.PAY_NET:
            {
                PayModel payModel = ( PayModel ) msg.obj;
                //调用JS
                pageWeb.loadUrl("javascript:utils.Go2Payment(" + payModel.getCustomId() + "," + payModel.getTradeNo() + "," + payModel.getPaymentType() + ", " + "false);\n");
            }
            default:
                break;
        }
        return false;
    }

    protected void dealUserid(){
        pageWeb.clearHistory();
        pageWeb.clearCache(true);
        signHeader(pageWeb);
        signHeader(menuView);

        //AuthParamUtils paramUtils = new AuthParamUtils ( application, System.currentTimeMillis (), application.obtainMerchantUrl ( ), HomeActivity.this );
        //String url = paramUtils.obtainUrl ();

        String  url = application.obtainMerchantUrl();

        //首页默认为商户站点 + index
        pageWeb.loadUrl(url , SignUtil.signHeader() );

        //设置左侧图标
        Drawable leftDraw = resources.getDrawable ( R.drawable.main_title_left_sideslip );
        SystemTools.loadBackground ( titleLeftImage, leftDraw );

        return;
    }

//    private void openSis(){
//        HomeActivity.this.startActivity(new Intent(HomeActivity.this, GoodManageActivity.class));
//    }

    private void refreshLeftMenu(){
        String url = Constants.getINTERFACE_PREFIX() + "weixin/UpdateLeftInfo";
        url +="?customerId="+ application.readMerchantId();
        url +="&userId="+ (TextUtils.isEmpty( application.readMemberId())? "0": application.readMemberId());
        url +="&clientUserType="+ application.readMemberType();
        AuthParamUtils authParamUtils=new AuthParamUtils(application , System.currentTimeMillis() , url , HomeActivity.this);
        url = authParamUtils.obtainUrlName();
        GsonRequest<UpdateLeftInfoModel> request = new GsonRequest<UpdateLeftInfoModel>(
                Request.Method.GET,
                url,
                UpdateLeftInfoModel.class,
                null,
                new MyRefreshMenuListener(HomeActivity.this),
                new MyRefreshMenuErrorListener(HomeActivity.this));

        VolleyUtil.getRequestQueue().add(request);
    }

    static class MyRefreshMenuListener implements Response.Listener<UpdateLeftInfoModel>{
        WeakReference<HomeActivity> ref;
        public MyRefreshMenuListener(HomeActivity aty){
            ref= new WeakReference<>(aty);
        }

        @Override
        public void onResponse(UpdateLeftInfoModel updateLeftInfoModel) {
            if( ref.get() ==null) return;
            if( updateLeftInfoModel==null ) return;

            if( updateLeftInfoModel.getCode() != 200 ){
                ToastUtils.showShortToast( ref.get().application , updateLeftInfoModel.getMsg());
                return;
            }

            if( updateLeftInfoModel.getData()==null ) return;
            if( updateLeftInfoModel.getData().getMenusCode()==0) return;

            ref.get().application.writeMemberLevel(updateLeftInfoModel.getData().getLevelName());

            if( BaseApplication.single.isLogin() ) {
                //ref.get().userType.setText(ref.get().application.readMemberLevel());
                ref.get().showAccountType( ref.get().application.readMemberLevel() );
            }

            //设置侧滑栏菜单
            List<MenuBean > menus = new ArrayList< MenuBean >(  );
            MenuBean menu = null;
            List<UpdateLeftInfoModel.MenuModel > home_menus = updateLeftInfoModel.getData().getHome_menus ();
            for(UpdateLeftInfoModel.MenuModel home_menu:home_menus)
            {
                menu = new MenuBean ();
                menu.setMenuGroup ( String.valueOf ( home_menu.getMenu_group () ) );
                menu.setMenuIcon ( home_menu.getMenu_icon ( ) );
                menu.setMenuName ( home_menu.getMenu_name ( ) );
                menu.setMenuUrl ( home_menu.getMenu_url ( ) );
                menus.add ( menu );
            }
            if(null != menus && !menus.isEmpty ()) {
                ref.get().application.writeMenus(menus);
                //动态加载侧滑菜单
                ref.get().mainMenuLayout.removeAllViews();
                UIUtils ui = new UIUtils (  ref.get().application, ref.get() ,  ref.get().resources,  ref.get().mainMenuLayout,  ref.get().mHandler );
                ui.loadMenus();
            }
        }
    }

    static class MyRefreshMenuErrorListener implements Response.ErrorListener{
        WeakReference<HomeActivity> ref;
        public MyRefreshMenuErrorListener(HomeActivity aty){
            ref = new WeakReference<HomeActivity>(aty);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null) return;
        }
    }
    /*
     * 手机绑定微信
     */
    private void callWeixin( String redirectUrl ) {
        progress.showProgress("正在调用微信授权,请稍等");
        progress.showAtLocation( titleLeftImage , Gravity.CENTER, 0, 0);
        //微信授权登录
        autnLogin = new AutnLogin( mHandler , redirectUrl );
        autnLogin.authorize(new Wechat(HomeActivity.this));
    }

    /*
     *微信绑定手机
     */
    private void callPhone(){
        Intent intent =new Intent(HomeActivity.this , BindPhoneActivity.class);
        HomeActivity.this.startActivityForResult( intent , BINDPHONE_REQUESTCODE);
    }

    private  void bindWeiXin(AccountModel model ){
        String url = Constants.getINTERFACE_PREFIX() + "Account/bindWeixin";
        Map map = new HashMap();
        map.put("userid",  application.readMemberId() );
        map.put("customerid", application.readMerchantId());
        map.put("sex", model.getSex());
        map.put("nickname", model.getNickname());
        map.put("openid", model.getOpenid());
        map.put("city", model.getCity());
        map.put("country",model.getCountry());
        map.put("province",model.getProvince());
        map.put("headimgurl",model.getAccountIcon());
        map.put("unionid",model.getAccountUnionId());
        map.put("refreshToken",model.getAccountToken());
        AuthParamUtils authParamUtils =new AuthParamUtils(application, System.currentTimeMillis(), url , HomeActivity.this);
        Map<String,String> params = authParamUtils.obtainParams(map);

        GsonRequest<PhoneLoginModel> request =new GsonRequest<PhoneLoginModel>(Request.Method.POST,
                url ,
                PhoneLoginModel.class,
                null,
                params,
                new MyBindWeiXinListener(this , model ),
                new MyBindWeiXinErrorListener(this)
                );
        VolleyUtil.getRequestQueue().add(request);

        progress.showProgress("正在绑定微信，请稍等...");
        progress.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    static class MyBindWeiXinListener implements Response.Listener<PhoneLoginModel>{
        WeakReference<HomeActivity> ref;
        AccountModel weixinModel;
        MyBindWeiXinListener(HomeActivity act , AccountModel model ){
            ref=new WeakReference<>(act);
            weixinModel=model;
        }
        @Override
        public void onResponse(PhoneLoginModel phoneLoginModel ) {
            if( ref.get()==null) return;
            if( ref.get().progress!=null){
                ref.get().progress.dismissView();
            }
            if( phoneLoginModel ==null){
                ToastUtils.showShortToast(ref.get(), "绑定微信操作失败");
                return;
            }
            if( phoneLoginModel.getCode() != 200){
                String msg ="绑定微信操作失败";
                if( !TextUtils.isEmpty(phoneLoginModel.getMsg() )){
                    msg = phoneLoginModel.getMsg();
                }
                ToastUtils.showShortToast(ref.get(), msg);
                return;
            }
            if( phoneLoginModel.getData() ==null ){
                ToastUtils.showShortToast(ref.get(),"绑定微信操作失败");
                return;
            }

            ToastUtils.showShortToast(ref.get(), "绑定操作成功");

            ref.get().application.writeMemberInfo (
                    phoneLoginModel.getData().getNickName() , String.valueOf( phoneLoginModel.getData().getUserid() ),
                    phoneLoginModel.getData().getHeadImgUrl() , weixinModel.getAccountToken (),
                    weixinModel.getAccountUnionId () , weixinModel.getOpenid()
            );
            ref.get().application.writeMemberLevel( phoneLoginModel.getData().getLevelName());
            //记录登录类型(1:微信登录，2：手机登录)
            ref.get().application.writeMemberLoginType( 1 );
            ref.get().application.writeMemberRelatedType( phoneLoginModel.getData().getRelatedType() );//重写 关联类型=2 已经绑定
            //动态加载侧滑菜单
            ref.get().mainMenuLayout.removeAllViews();
            UIUtils ui = new UIUtils (  ref.get().application,  ref.get() ,  ref.get().resources,  ref.get().mainMenuLayout,  ref.get().mHandler );
            ui.loadMenus();

            ref.get().initUserInfo();

            ref.get().signHeader();

            //当 重定响 url 不为空 ，则 调转到指定的url
            if( !TextUtils.isEmpty( weixinModel.getRedirecturl())){
                ref.get().pageWeb.loadUrl( weixinModel.getRedirecturl() , SignUtil.signHeader() );
            }else{
                ref.get().pageWeb.reload();
            }
        }
    }

    static class MyBindWeiXinErrorListener implements Response.ErrorListener{
        WeakReference<HomeActivity> ref;
        public MyBindWeiXinErrorListener(HomeActivity act){
            ref=new WeakReference<HomeActivity>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if(ref.get()==null)return;
            if(ref.get().progress!=null ){
                ref.get().progress.dismissView();
            }
            ToastUtils.showShortToast(ref.get(),"绑定微信操作失败。");
        }
    }

    @JavascriptInterface
    public void sendShare(final String title, final String desc, final String link,final String img_url){
        if(  this==null ) return;
        if( this.share ==null ) return;

        //ToastUtils.showShortToast( ref.get() , title+desc+link+img_url);
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                if( HomeActivity.this ==null ) return;

            if( HomeActivity.this.progress !=null ){
                HomeActivity.this.progress.dismissView();
            }

        String sTitle = title;
        if( TextUtils.isEmpty( sTitle ) ){
            sTitle = application.obtainMerchantName ()+"分享";
        }
        String sDesc = desc;
        if( TextUtils.isEmpty( sDesc ) ){
            sDesc = sTitle;
        }
        String imageUrl = img_url; //application.obtainMerchantLogo ();
        if(TextUtils.isEmpty ( imageUrl )) {
            imageUrl = Constants.COMMON_SHARE_LOGO;
        }

        String sLink = link;
        if( TextUtils.isEmpty( sLink ) ){
            sLink = application.obtainMerchantUrl();
        }
        sLink = SystemTools.shareUrl(application, sLink);
        ShareModel msgModel = new ShareModel ();
        msgModel.setImageUrl(imageUrl);
        msgModel.setText(sDesc);
        msgModel.setTitle(sTitle);
        msgModel.setUrl(sLink);
        //msgModel.setImageData( BitmapFactory.decodeResource( resources , R.drawable.ic_launcher ) );
        share.initShareParams(msgModel);
        //share.showShareWindow();
        WindowUtils.backgroundAlpha( HomeActivity.this , 0.4f);
        share.showAtLocation( HomeActivity.this.titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });
    }

//    @JavascriptInterface
//    public void sendSisShare(final String title, final String desc, final String link,final String img_url){
//        if(  this==null ) return;
//        if( this.share ==null ) return;
//
//        //ToastUtils.showShortToast( ref.get() , title+desc+link+img_url);
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//
//                if( HomeActivity.this ==null ) return;
//
//                if( HomeActivity.this.progress !=null ){
//                    HomeActivity.this.progress.dismissView();
//                }
//
//                String sTitle = title;
//                if( TextUtils.isEmpty( sTitle ) ){
//                    sTitle = application.obtainMerchantName ()+"分享";
//                }
//                String sDesc = desc;
//                if( TextUtils.isEmpty( sDesc ) ){
//                    sDesc = sTitle;
//                }
//                String imageUrl = img_url; //application.obtainMerchantLogo ();
//                if(TextUtils.isEmpty ( imageUrl )) {
//                    imageUrl = Constants.COMMON_SHARE_LOGO;
//                }
//
//                String sLink = link;
//                if( TextUtils.isEmpty( sLink ) ){
//                    sLink = application.obtainMerchantUrl();
//                }
//                sLink = SystemTools.shareUrl(application, sLink);
//                ShareModel msgModel = new ShareModel ();
//                msgModel.setImageUrl(imageUrl);
//                msgModel.setText(sDesc);
//                msgModel.setTitle(sTitle);
//                msgModel.setUrl(sLink);
//                //msgModel.setImageData( BitmapFactory.decodeResource( resources , R.drawable.ic_launcher ) );
//                share.initShareParams(msgModel);
//                //share.showShareWindow();
//                WindowUtils.backgroundAlpha( HomeActivity.this , 0.4f);
//                share.showAtLocation( HomeActivity.this.titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//
//            }
//        });
//    }

    @JavascriptInterface
    public void enableShare( String state ){
        if(TextUtils.isEmpty( state ) || state.equals("1")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    titleRightImage.setVisibility(View.VISIBLE);
                }
            });

        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    titleRightImage.setVisibility(View.GONE);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLink(LinkEvent event) {
        if(event==null)return;
        String link = event.getLinkUrl();
        if(TextUtils.isEmpty(link)) return;


        Intent intent=new Intent(HomeActivity.this,WebViewActivity.class);
        intent.putExtra(Constants.INTENT_URL, link);
        HomeActivity.this.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSwitchUser(SwitchUserByUserIDEvent event){
       getUserInfo(event.getUserId());
    }

    protected void getUserInfo(String userId ){

        if(TextUtils.isEmpty(userId)){
            EventBus.getDefault().post(new RefreshPageEvent(false,true));
            ToastUtils.showLongToast("参数userid错误");
            return;
        }
        String suserid = BaseApplication.single.readUserId();
        if(!TextUtils.isEmpty( suserid) && userId.equals( suserid )){
            EventBus.getDefault().post(new RefreshPageEvent(false,true));
            ToastUtils.showLongToast("账号相同，不需要切换。");
            return;
        }



        String url = Constants.getINTERFACE_PREFIX() + "Account/getAppUserInfo";
        Map<String, String> map = new HashMap<>();

        url+="?userid="+userId +"&customerid="+ application.readMerchantId();

        //map.put("userid", application.readMemberId() );
        //map.put("customerid",application.readMerchantId());
        AuthParamUtils authParamUtils =new AuthParamUtils(application, System.currentTimeMillis(), url , HomeActivity.this);
        //Map<String,String> params = authParamUtils.obtainParams(map);

        url = authParamUtils.obtainUrl();

        GsonRequest<AuthMallModel> request =new GsonRequest<>(
                Request.Method.GET,
                url,
                AuthMallModel.class,
                null,
                null,
                new MyGetUserInfoListener(this),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("homeactivity", volleyError.getMessage());
                    }
                }
        );
        VolleyUtil.getRequestQueue().add(request);
    }

    static class MyGetUserInfoListener implements Response.Listener<AuthMallModel>{
        WeakReference<HomeActivity> ref;

        MyGetUserInfoListener(HomeActivity act ){
            ref=new WeakReference<>(act);
        }
        @Override
        public void onResponse(AuthMallModel authMallModel ) {
            if( ref.get()==null) return;
            if( ref.get().progress!=null){
                ref.get().progress.dismissView();
            }
            if( authMallModel ==null){
                ToastUtils.showShortToast( ref.get() , "获取用户数据失败");
                return;
            }
            if( authMallModel.getCode() != 200){
                String msg ="获取用户数据失败";
                if( !TextUtils.isEmpty(authMallModel.getMsg() )){
                    msg = authMallModel.getMsg();
                }
                ToastUtils.showShortToast(ref.get(), msg);
                return;
            }
            if( authMallModel.getData() ==null ){
                ToastUtils.showShortToast(ref.get(),"获取用户数据失败");
                return;
            }

            //ToastUtils.showShortToast(ref.get(), "获取用户数据成功");


            BaseApplication.single.clearAllCookies();


            EventBus.getDefault().post(new CloseEvent() );

            //更新userId
            BaseApplication.single.writeMemberId(String.valueOf(authMallModel.getData().getUserid()));
            //更新昵称
            BaseApplication.single.writeUserName(authMallModel.getData().getNickName());
            BaseApplication.single.writeUserIcon( authMallModel.getData().getHeadImgUrl());
            BaseApplication.single.writeUserUnionId( authMallModel.getData().getUnionId() );
            BaseApplication.single.writeMemberLevel( authMallModel.getData().getLevelName());

            //记录微信关联类型（0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号）
            BaseApplication.single.writeMemberRelatedType(authMallModel.getData().getRelatedType());

            //更新界面
            ref.get().userName.setText(authMallModel.getData().getNickName());
            //ref.getge().userType.setText(authMallModel.getData().getLevelName());
            ref.get().showAccountType(authMallModel.getData().getLevelName());

            String logoUrl = authMallModel.getData().getHeadImgUrl();

            new LoadLogoImageAyscTask ( ref.get().resources , ref.get().userLogo  , authMallModel.getData().getHeadImgUrl(), R.drawable.ic_login_username ).execute();

            //动态加载侧滑菜单
            //UIUtils ui = new UIUtils ( BaseApplication.single , ref.get() , ref.get().resources , ref.get().mainMenuLayout , ref.get().mHandler );
            //ui.loadMenus();


            String usercenterUrl = BaseApplication.single.obtainMerchantUrl() + "/" + Constants.URL_PERSON_INDEX+"?customerid="+BaseApplication.single.readMerchantId();


            ref.get().signHeader();

            //AuthParamUtils authParamUtils =new AuthParamUtils( BaseApplication.single , System.currentTimeMillis(), usercenterUrl , ref.get() );
            //usercenterUrl = authParamUtils.obtainUrl();



            ref.get().pageWeb.loadUrl( usercenterUrl , SignUtil.signHeader() );

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshLeftMenu(RefreshMenuEvent event){
        mainMenuLayout.removeAllViews();
        UIUtils ui = new UIUtils ( BaseApplication.single , this , resources ,  mainMenuLayout , mHandler );
        ui.loadMenus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshHttpHeader(RefreshHttpHeaderEvent event){
        if( pageWeb ==null) return;
        signHeader(pageWeb);
        if(menuView==null) return;
        signHeader(menuView);
        String menuUrl = application.obtainMerchantUrl () + "/bottom.aspx?customerid=" + application.readMerchantId ();
        menuView.loadUrl(menuUrl , SignUtil.signHeader() );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGoIndex(final GoIndexEvent event) {
        if (pageWeb == null) return;


        String url = application.obtainMerchantUrl().toLowerCase();
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }

        //mHandler.sendEmptyMessage( Constants.LEFT_IMG_SIDE);

        pageWeb.clearHistory();
        pageWeb.loadUrl(url, SignUtil.signHeader());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBind(BindEvent event){
        if( event.isBindWeiXin()){
            callWeixin( event.getRedirectUrl() );
        }else {
            callPhone();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshPage(RefreshPageEvent event){
        if( !event.isRefreshMainUI() ) return;

        pageWeb.reload();
    }

}

