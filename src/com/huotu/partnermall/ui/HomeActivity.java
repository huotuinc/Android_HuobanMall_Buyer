package com.huotu.partnermall.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.libpay.alipayV2.AliPayResultV2;
import com.huotu.android.library.libpay.alipayV2.AliPayUtilV2;
import com.huotu.android.library.libpay.weixin.WeiXinPayResult;
import com.huotu.android.library.libpay.weixin.WeiXinPayUtil;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.FrescoUtils;
import com.huotu.partnermall.image.IDownloadResult;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.AccountModel;
import com.huotu.partnermall.model.AuthMallModel;
import com.huotu.partnermall.model.BindEvent;
import com.huotu.partnermall.model.CloseEvent;
import com.huotu.partnermall.model.GoIndexEvent;
import com.huotu.partnermall.model.LinkEvent;
import com.huotu.partnermall.model.MenuLinkEvent;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.model.PhoneLoginModel;
import com.huotu.partnermall.model.RefreshHttpHeaderEvent;
import com.huotu.partnermall.model.RefreshMenuEvent;
import com.huotu.partnermall.model.RefreshMessageEvent;
import com.huotu.partnermall.model.RefreshPageEvent;
import com.huotu.partnermall.model.ShareModel;
import com.huotu.partnermall.model.SwitchUserByUserIDEvent;
import com.huotu.partnermall.model.SwitchUserModel;
import com.huotu.partnermall.model.WxPaySuccessCallbackModel;
import com.huotu.partnermall.receiver.MyBroadcastReceiver;
import com.huotu.partnermall.receiver.PushProcess;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.login.AutnLogin;
//import com.huotu.partnermall.ui.login.BindPhoneActivity;
import com.huotu.partnermall.ui.login.PhoneLoginActivity;
import com.huotu.partnermall.ui.web.UrlFilterUtils;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.DensityUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.ObtainParamsMap;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.UIUtils;
import com.huotu.partnermall.utils.WindowUtils;
import com.huotu.partnermall.widgets.CustomDialog;
import com.huotu.partnermall.widgets.ProgressPopupWindow;
import com.huotu.partnermall.widgets.SharePopupWindow;
import com.huotu.partnermall.widgets.TipAlertDialog;
import com.huotu.partnermall.widgets.custom.FooterOneWidget;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.huotu.partnermall.inner.R.id.homeAsUp;
import static com.huotu.partnermall.inner.R.id.viewPage;

public class HomeActivity extends BaseActivity
        implements Handler.Callback , ViewTreeObserver.OnGlobalLayoutListener ,
        MyBroadcastReceiver.BroadcastListener , View.OnLongClickListener{
    //获取资源文件对象
    private Resources resources;
    private long exitTime = 0;
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
    private MyBroadcastReceiver myBroadcastReceiver;
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
    //侧滑登录
    @Bind(R.id.loginLayout)
    RelativeLayout loginLayout;
    //主菜单容器
    @Bind(R.id.mainMenuLayout)
    LinearLayout mainMenuLayout;
    //已授权界面
    @Bind(R.id.getAuth)
    RelativeLayout getAuthLayout;
    //用户头像
    @Bind(R.id.accountIcon)
    SimpleDraweeView userLogo;
    //用户名称
    @Bind(R.id.accountName)
    TextView userName;
    @Bind(viewPage)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.layDrag)
    DrawerLayout layDrag;
    @Bind(R.id.main_pgbar)
    ProgressBar pgBar;
    @Bind(R.id.ff1)
    FrameLayout ff1;
    @Bind(R.id.accountTypeList)
    LinearLayout accountTypeList;
    @Bind(R.id.loadMenuView)
    RelativeLayout loadMenuView;

    UrlFilterUtils urlFilterUtils;
    //未读消息数量
    //int unReadMessageCount=0;
    //底部导航栏控件
    FooterOneWidget footerOneWidget;


    CustomDialog customDialog;
    PlatformActionListener platformActionListener= new PlatformActionListener() {
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
    };

    Runnable getMessageRunnable=new Runnable() {
        @Override
        public void run() {
            if(pageWeb==null) return;
            pageWeb.loadUrl("javascript:"+
                    "try{"+
                    "if(window.android){"+
                        //"var count = easemobMessage.getMessageNum();"+
                        "window.android.setUnReadMessageCount( easemobMessage.getMessageNum() );" +
                    "}}catch(err){window.android.setJavascriptError(err);}");

            if(footerOneWidget!=null){
                footerOneWidget.showCircleView(  application.unReadMessageCount>0 );
            }

            mHandler.postDelayed(this,1000);
        }
    };

    /***
     *
     */
//    void getMessageRun(){
//        mHandler.postDelayed( getMessageRunnable ,1000);
//    }

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        resources = HomeActivity.this.getResources();
        mHandler = new Handler ( this );
        wManager = this.getWindowManager();
        am = this.getAssets();
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        share = new SharePopupWindow ( HomeActivity.this );
        myBroadcastReceiver = new MyBroadcastReceiver(this,this,
                MyBroadcastReceiver.ACTION_PAY_SUCCESS , MyBroadcastReceiver.ACTION_WX_PAY_CANCEL_CALLBACK , MyBroadcastReceiver.ACTION_WX_PAY_ERROR_CALLBACK);

        Register();

        //设置沉浸模式
        setImmerseLayout(homeTitle);

        initView();

        initPush(getIntent());

        initAdLinkUrl(getIntent());

        initRedrectUrl(getIntent());

        progress = new ProgressPopupWindow ( HomeActivity.this );

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAppVersion();
            }
        },500);

        //定时获取未读消息

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //当android系统版本>=4.4时，才开启定时读取未读消息js代码
            //低版本有bug，暂时不启用
            mHandler.postDelayed(getMessageRunnable, 500);
        }
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
                TipAlertDialog tipAlertDialog = new TipAlertDialog(this,true);
                tipAlertDialog.show("升级提示", "我们发布了新版本，您可以去应用市场下载", "", R.color.black , false , true );
                return;
            }else{
                TipAlertDialog tipAlertDialog = new TipAlertDialog(this , true );
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
        //new LoadLogoImageAyscTask ( resources, userLogo, application.getUserLogo ( ), R.drawable.ic_login_username ).execute();
        userLogo.setImageURI( Uri.parse( application.getUserLogo() ));

        //渲染用户名
        userName.setText(application.getUserName());
        userName.setTextColor(resources.getColor(R.color.theme_color));

        //userType.setTextColor(SystemTools.obtainColor(application.obtainMainColor()));
        //userType.setText(application.readMemberLevel());

        showAccountType(application.readMemberLevel());
    }

    protected void showAccountType(String dataArray ){
        if( dataArray ==null || dataArray.isEmpty() ) return;
        String[] data= dataArray.split("&");
        accountTypeList.removeAllViews();
        int leftMargin = DensityUtils.dip2px(this,3);
        int leftPadding = DensityUtils.dip2px(this,3);
        int topPadding = DensityUtils.dip2px(this,2);
        int i=0;
        for(String item : data ) {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i>0){ layoutParams.setMargins(leftMargin,0,0,0);}
            i++;
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
            pageWeb.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }

        if( null != myBroadcastReceiver){
            myBroadcastReceiver.unregisterReceiver();
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
        String leftMenuShowBgPicture = getString(R.string.left_menu_show_bg_picture);
        boolean leftmenushowpicture = Boolean.parseBoolean(leftMenuShowBgPicture);
        if(leftmenushowpicture){
            loginLayout.setBackgroundResource(R.drawable.menu_bg);
        }else {
            loginLayout.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        }
        //设置设置图标
        //SystemTools.loadBackground(loginSetting, ContextCompat.getDrawable(this ,R.drawable.switch_white));
        //getAuthLayout.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        //loginSetting.setVisibility(View.GONE);
        //设置登录界面
        getAuthLayout.setVisibility(View.VISIBLE);
        //动态加载侧滑菜单
        UIUtils ui = new UIUtils(application, HomeActivity.this, resources, mainMenuLayout, mHandler);
        ui.loadMenus();

        ptrClassicFrameLayout.disableWhenHorizontalMove(true);
        ptrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrClassicFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                //return false;

                String url = pageWeb.getUrl();
                if(url!=null && !url.isEmpty() && ( url.toLowerCase().contains(Constants.URL_KEFU_2) || url.toLowerCase().contains( Constants.URL_KEFU_3 ) )){
                    //解决客服页面滚动事件与下拉刷新冲突问题
                    return false;
                }
                if(url!=null && !url.isEmpty() && url.toLowerCase().contains( Constants.URL_SubmitOrder.toLowerCase() )){
                    //解决 提交订单页面中弹出添加地址框中的滚动事件与下拉刷新冲突的问题
                    return false;
                }

                return PtrDefaultHandler.checkContentCanBePulledDown(frame , pageWeb,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageWeb.reload();
            }
        });

        share.setPlatformActionListener(platformActionListener );
        share.showShareWindow();
        share.setOnDismissListener(new PoponDismissListener(this));

        loadPage();
        loadMainMenu();

        initSoftKeyboard();
    }

    protected void initSoftKeyboard(){
    }

    protected void judgeLoginStatus(){
        boolean isLogin = BaseApplication.single.isLogin();
        if(!isLogin){
            userName.setText("未登录");

            //userType.setText("点击登录");
            showAccountType("点击登录");

            //userLogo.setImageResource( R.drawable.ic_login_username);
            userLogo.setImageURI( "res:///"+ R.drawable.ic_login_username);
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

    /***
     * 处理 广告页面
     * @param intent
     */
    protected void initAdLinkUrl(Intent intent){
        if (null == intent || ! intent.hasExtra( Constants.HUOTU_AD_URL_KEY )) return;
        String adLinkUrl = intent.getStringExtra( Constants.HUOTU_AD_URL_KEY);
        if( adLinkUrl==null || adLinkUrl.isEmpty() ) return;

        String urlStr = adLinkUrl;
        Bundle bundle = new Bundle();
        bundle.putString ( Constants.INTENT_URL, urlStr );
        ActivityUtils.getInstance ( ).showActivity ( this , WebViewActivity.class, bundle );
    }

    private void loadMainMenu(){
        loadMenuView.removeAllViews();

        footerOneWidget=new FooterOneWidget(this);
        int heightPx = DensityUtils.dip2px(this , 50);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
        footerOneWidget.setLayoutParams(layoutParams);
        loadMenuView.addView(footerOneWidget);
    }

    private void signHeader(){
        if( pageWeb==null) return;
        signHeader(pageWeb);
        //if(menuView==null) return;
        //signHeader(menuView);
    }

    private void signHeader( WebView webView ){
//        String userid= application.readMemberId();
//        String unionid = application.readUserUnionId();
//        String openId = BaseApplication.single.readOpenId();
//
//        String sign = ObtainParamsMap.SignHeaderString(userid, unionid , openId );
//        String userAgent = webView.getSettings().getUserAgentString();
//        if( TextUtils.isEmpty(userAgent) ) {
//            userAgent = "mobile;"+sign;
//        }else{
//            int idx = userAgent.lastIndexOf(";mobile;hottec:");
//            if(idx>=0){
//                userAgent = userAgent.substring(0,idx);
//            }
//            userAgent +=";mobile;"+sign;
//        }
//
//        webView.getSettings().setUserAgentString( userAgent );

        String userAgent = SignUtil.signHeader( webView.getSettings().getUserAgentString() );
        webView.getSettings().setUserAgentString( userAgent );

    }

    private void loadPage(){
        pageWeb.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        pageWeb.setVerticalScrollBarEnabled(false);
        pageWeb.setHorizontalScrollBarEnabled(false);
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

        pageWeb.setOnLongClickListener(this);

        if(com.huotu.partnermall.inner.BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ){
            WebView.setWebContentsDebuggingEnabled(true);
        }

        //设置app标志
        signHeader( pageWeb );

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
                        //titleText.setText(view.getTitle());

                        if( UIUtils.isIndexPage( url ) || url.contains ( "&back" ) || url.contains ( "?back" ) || url.contains("easemob/im.html")){
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
                if( ptrClassicFrameLayout ==null || pgBar ==null || titleText ==null ) return;

                if(100 == newProgress) {
                    ptrClassicFrameLayout.refreshComplete();
                    pgBar.setVisibility(View.GONE);
                    titleText.setText( view.getTitle() );
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

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                //return super.onJsConfirm(view, url, message, result);
                if(view ==null || view.getContext() ==null) return true;

                final TipAlertDialog tipAlertDialog = new TipAlertDialog(view.getContext(),false);
                tipAlertDialog.show("询问", message, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipAlertDialog.dismiss();
                        result.cancel();
                    }
                },new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        tipAlertDialog.dismiss();
                        result.confirm();
                    }
                });

                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });

        pageWeb.getViewTreeObserver().addOnGlobalLayoutListener(this);

        String url = application.obtainMerchantUrl();
        //首页默认为商户站点 + index
        if( url !=null && !url.isEmpty()) {
            pageWeb.loadUrl(url, SignUtil.signHeader());
        }
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
            if(pageWeb.canGoBack () && !UIUtils.isIndexPage(pageWeb.getUrl()) && !UIUtils.isKefuPage(pageWeb.getUrl())){
                titleRightImage.setVisibility(View.GONE);
                pageWeb.goBack();
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
        AuthParamUtils paramUtil = new AuthParamUtils ( application , System.currentTimeMillis (), url );
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
                    //callPhone();
                }
                else if( url.toLowerCase().contains("http://www.dzd.com") ){
                    //openSis();
                }else {
                    pageWeb.loadUrl(url , SignUtil.signHeader() );
                }
            }
            break;
            case Constants.FRESHEN_PAGE_MESSAGE_TAG:
            {
                //刷新界面
                String url = msg.obj.toString ();
                pageWeb.loadUrl(url , SignUtil.signHeader() );
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
                }else if(platform.getName().equals(WechatFavorite.NAME)){
                    ToastUtils.showShortToast("微信收藏成功");
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
                }else if(platform.getName().equals(WechatFavorite.NAME)){
                    ToastUtils.showShortToast("微信收藏失败");
                }
            }
            break;
            case Constants.SHARE_CANCEL: {
                //分享取消
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast(HomeActivity.this, "微信朋友圈分享取消");
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast(HomeActivity.this, "微信分享取消");
                } else if ("QZone".equals(platform.getName())) {
                    ToastUtils.showShortToast(HomeActivity.this, "QQ空间分享取消");
                } else if ("SinaWeibo".equals(platform.getName())) {
                    ToastUtils.showShortToast(HomeActivity.this, "新浪微博分享取消");
                } else if (WechatFavorite.NAME.equals(platform.getName())) {
                    ToastUtils.showShortToast("微信收藏取消");
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

                //new LoadLogoImageAyscTask ( resources, userLogo, user.getWxHeadImg ( ), R.drawable.ic_login_username ).execute ( );
                userLogo.setImageURI(  user.getWxHeadImg()==null ? "res:///"+R.drawable.ic_login_username : user.getWxHeadImg()  );

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
                pageWeb.loadUrl("javascript:utils.Go2Payment(" + payModel.getCustomId() + ", '" + payModel.getTradeNo() + "' ," + payModel.getPaymentType() + ", " + "false);\n");
            }
            break;
            case WeiXinPayUtil.SDK_WX_PAY_FLAG :{
                dealWeiXinPayResult(msg);
            }
            break;
            case AliPayUtilV2.SDK_Ali_PAY_V2_FLAG:{
                dealAliPayResult(msg);
            }
            break;
            case Constants.Message_GotoOrderList:{//跳转到待支付订单列表页面
                gotoOrderList();
            }
            break;
            case Constants.MESSAGE_DOWNLOADIMAGE_SUCCESS:{//下载图片完成消息
                String path = msg.obj.toString();
                saveImageToGallery(path);
                ToastUtils.showLongToast(path);
                break;
            }
            case Constants.MESSAGE_DOWNLOADIMAGE_FAIL:{//
                ToastUtils.showLongToast("下载图片失败");
                break;
            }
            default:
                break;
        }
        return false;
    }

    protected void gotoOrderList(){
        if(pageWeb!=null){
            String urlstr = String.format( Constants.URL_WaitPayOrderList, application.obtainMerchantUrl(), application.readMerchantId());
            pageWeb.loadUrl( urlstr , SignUtil.signHeader() );
        }
    }

    protected void dealUserid(){
        pageWeb.clearHistory();
        pageWeb.clearCache(true);
        signHeader(pageWeb);
        //signHeader(menuView);

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
//    private void callPhone(){
//        Intent intent =new Intent(HomeActivity.this , BindPhoneActivity.class);
//        HomeActivity.this.startActivityForResult( intent , BINDPHONE_REQUESTCODE);
//    }

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
        AuthParamUtils authParamUtils =new AuthParamUtils( application, System.currentTimeMillis(), url );
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
    public void sendShare(final String title, final String desc, final String link,final String img_url) {
        if (this == null) return;
        if (this.share == null) return;

        //ToastUtils.showShortToast( ref.get() , title+desc+link+img_url);
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                if (HomeActivity.this == null) return;

                if (HomeActivity.this.progress != null) {
                    HomeActivity.this.progress.dismissView();
                }

                String sTitle = title;
                if (TextUtils.isEmpty(sTitle)) {
                    sTitle = application.obtainMerchantName() + "分享";
                }
                String sDesc = desc;
                if (TextUtils.isEmpty(sDesc)) {
                    sDesc = sTitle;
                }
                String imageUrl = img_url; //application.obtainMerchantLogo ();
                if (TextUtils.isEmpty(imageUrl)) {
                    imageUrl = Constants.COMMON_SHARE_LOGO;
                }

                String sLink = link;
                if (TextUtils.isEmpty(sLink)) {
                    sLink = application.obtainMerchantUrl();
                }
                sLink = SystemTools.shareUrl(application, sLink);
                ShareModel msgModel = new ShareModel();
                msgModel.setImageUrl(imageUrl);
                msgModel.setText(sDesc);
                msgModel.setTitle(sTitle);
                msgModel.setUrl(sLink);
                //msgModel.setImageData( BitmapFactory.decodeResource( resources , R.drawable.ic_launcher ) );
                share.initShareParams(msgModel);
                //share.showShareWindow();
                //WindowUtils.backgroundAlpha( HomeActivity.this , 0.4f);
                share.showAtLocation(HomeActivity.this.titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });
    }

    @JavascriptInterface
    public void enableShare( String state ){
        if(TextUtils.isEmpty( state ) || state.equals("1")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(titleRightImage==null) return;
                    titleRightImage.setVisibility(View.VISIBLE);
                }
            });

        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(titleRightImage==null) return;
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

        if( event.isOpenUrlByBrowser()) {
            //Intent intent=new Intent(HomeActivity.this,WebViewActivity.class);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            //intent.putExtra(Constants.INTENT_URL, link);
            HomeActivity.this.startActivity(intent);
        }else {
            Intent intent=new Intent(HomeActivity.this,WebViewActivity.class);
            intent.putExtra(Constants.INTENT_URL, link);
            HomeActivity.this.startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMenuLink(MenuLinkEvent event){
        if(event==null)return;
        String link = event.getLinkUrl();
        if(TextUtils.isEmpty(link)) return;

        if(pageWeb==null) return;

        titleRightImage.setVisibility(View.GONE);

        link = dealUrl(link);

        pageWeb.loadUrl(link ,  SignUtil.signHeader() );
    }

    /**
     * 获得当前页面的 goodid参数值
     * @return
     */
    private String getGoodIdFromUrl(){
        if( pageWeb==null ) return "";
        String url = pageWeb.getUrl();
        if( TextUtils.isEmpty( url ) )return "";
        String goodsidString ="goodsid";
        Uri uri = Uri.parse( url );
        String goodid = uri.getQueryParameter("goodsid");
        if( TextUtils.isEmpty( goodid )) goodid="";
        return  goodid;
    }

    /**
     * 获得当前页面的 orderid 参数值
     * @return
     */
    private String getOrderIdFromUrl(){
        if( pageWeb==null ) return "";
        String url = pageWeb.getUrl();
        if( TextUtils.isEmpty( url ) )return "";
        String orderidString ="orderid";
        Uri uri = Uri.parse( url );
        String orderid = uri.getQueryParameter("orderid");
        if( TextUtils.isEmpty(orderid) ) orderid="";
        return orderid;
    }


    protected String dealUrl(String url){

        String useridString ="{userid}";
        String userid = BaseApplication.single.readMemberId();
        if( !TextUtils.isEmpty(url) && url.contains( useridString )){
            url = url.replace( useridString , userid );
        }
        String goodidString = "{goodid}";
        String goodid = getGoodIdFromUrl();
        if( !TextUtils.isEmpty(url) && url.contains( goodidString )){
            url = url.replace( goodidString , goodid );
        }
        String orderidString ="{orderid}";
        String orderid = getOrderIdFromUrl();
        if( !TextUtils.isEmpty(url) && url.contains( orderidString )){
            url= url.replace( orderidString , orderid );
        }
        return url;
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
        AuthParamUtils authParamUtils =new AuthParamUtils(application, System.currentTimeMillis(), url );
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
                        Log.e("homeactivity", volleyError.getMessage()==null? "error" :  volleyError.getMessage());
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

            //new LoadLogoImageAyscTask ( ref.get().resources , ref.get().userLogo  , authMallModel.getData().getHeadImgUrl(), R.drawable.ic_login_username ).execute();
            ref.get().userLogo.setImageURI( authMallModel.getData().getHeadImgUrl() );

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
        //if(menuView==null) return;
        //signHeader(menuView);
        //String menuUrl = application.obtainMerchantUrl () + "/bottom.aspx?customerid=" + application.readMerchantId ();
        //menuView.loadUrl(menuUrl , SignUtil.signHeader() );
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
            //callPhone();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshPage(RefreshPageEvent event){
        if( !event.isRefreshMainUI() ) return;

        pageWeb.reload();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefresMessage(final RefreshMessageEvent event){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(footerOneWidget==null)return;
                BaseApplication.single.unReadMessageCount= event.isHasMessage()?1:0;
                footerOneWidget.showCircleView(event.isHasMessage());
            }
        },1500);
    }

    @Override
    public void onGlobalLayout() {
        try {
            final int softKeyboardHeight = 100;
            Rect r = new Rect();
            View rootView = pageWeb.getRootView();
            rootView.getWindowVisibleDisplayFrame(r);
            DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
            int heightDiff = rootView.getBottom() - r.bottom;
            boolean isShow = heightDiff > softKeyboardHeight * dm.density;
            if (isShow) {
                loadMenuView.setVisibility(View.GONE);
            } else {
                loadMenuView.setVisibility(View.VISIBLE);
            }
        }catch (Exception ex){
            loadMenuView.setVisibility(View.VISIBLE);
        }
    }


    void dealAliPayResult( Message msg ){
        AliPayResultV2 result = (AliPayResultV2) msg.obj;
        /**
         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
         * docType=1) 建议商户依赖异步通知
         */
        String resultInfo = result.getResult();// 同步返回需要验证的信息
        String resultStatus = result.getResultStatus();
        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(resultStatus, "9000")) {
            Toast.makeText(HomeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            String orderNo = result.getAliOrderInfo().getOrderNo();
            String urlString = String.format( Constants.URL_PaySuccess , application.obtainMerchantUrl(), application.readMerchantId() , orderNo );
            pageWeb.loadUrl(urlString);
        } else {
            // 判断resultStatus 为非"9000"则代表可能支付失败
            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                Toast.makeText(HomeActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                gotoOrderList();
            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                Toast.makeText(HomeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                gotoOrderList();
            }
        }
    }

    void dealWeiXinPayResult(Message msg ){
        WeiXinPayResult result = (WeiXinPayResult) msg.obj;
        if ( result !=null && result.getCode() == WeiXinPayUtil.FAIL) {
            Toast.makeText(getApplication(), result.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }else if( result !=null ){
//            if(pageWeb !=null) {
//                String orderNo = result.getOrderInfo().getOrderNo();
//                String urlString = String.format( Constants.URL_PaySuccess , application.obtainMerchantUrl(), application.readMerchantId() , orderNo );
//                pageWeb.loadUrl(urlString);
//            }
        }
    }

    /***
     * js调用android的方法，设置未读消息数量
     * @param msgCount
     */
    @JavascriptInterface
    public void setUnReadMessageCount(int msgCount){
        BaseApplication.single.unReadMessageCount=msgCount;
    }

    @JavascriptInterface
    public void setJavascriptError(String error){
        Log.e("HomeActivity", error);
        //ToastUtils.showLongToast(error);
    }


    @Override
    public void onFinishReceiver(MyBroadcastReceiver.ReceiverType type, Object msg) {
        if(type == MyBroadcastReceiver.ReceiverType.wxPaySuccess){
            //viewPage.goBack();
            if( msg ==null) return;
            Bundle bundle = (Bundle) msg;
            if( bundle ==null) return;
            WxPaySuccessCallbackModel data = (WxPaySuccessCallbackModel) bundle.getSerializable( Constants.HUOTU_PAY_CALLBACK_KEY);
            if( data ==null)  return;
            String orderNo = data.getOrderNo();
            if( pageWeb !=null) {
                String urlString = String.format( Constants.URL_PaySuccess , application.obtainMerchantUrl(), application.readMerchantId() , orderNo );
                pageWeb.loadUrl(urlString , SignUtil.signHeader() );
            }
        }else if( type == MyBroadcastReceiver.ReceiverType.wxPayCancel || type == MyBroadcastReceiver.ReceiverType.wxPayError ){
            if(mHandler==null)return;
            Message uiMessage = mHandler.obtainMessage(Constants.Message_GotoOrderList);
            mHandler.sendMessage(uiMessage);
        }
    }


    @Override
    public boolean onLongClick(View v) {

        final WebView.HitTestResult htr = pageWeb.getHitTestResult();//获取所点击的内容
        if( htr==null) return false;
        if (htr.getType() == WebView.HitTestResult.IMAGE_TYPE || htr.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE ) {//判断被点击的类型为图片
            String url = htr.getExtra();
            showDialog(url);
        }

        return false;
    }


    /**
     * 显示Dialog
     * param v
     */
    private void  showDialog(final String url) {

        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.custom_item_dialog);
        //adapter.add("发送给朋友");
        adapter.add("保存到手机");


        customDialog = new CustomDialog(this) {
            @Override
            public void initViews() {
                // 初始CustomDialog化控件
                ListView mListView = (ListView) findViewById(R.id.lv_dialog);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 点击事件
                        switch (position) {
                            case 0:
                                downloadImage(HomeActivity.this, url);
                                closeDialog();
                                //sendToFriends( HomeActivity.this , url );//把图片发送给好友
                                //closeDialog();
                                break;
                            //case 1:
                            //saveImageToGallery(MainActivity.this);

                            //break;
//                            case 2:
//                                Toast.makeText(MainActivity.this, "已收藏", Toast.LENGTH_LONG).show();
//                                closeDialog();
//                                break;
//                            case 3:
//                                goIntent();
//                                closeDialog();
//                                break;
                        }

                    }
                });
            }
        };

        customDialog.show();
    }

    protected void downloadImage( Context context , String url){
            FrescoUtils.downloadImage( context ,  url , new IDownloadResult(context ){
                @Override
                public void onResult(String filePath) {
                    if( filePath==null ){
                        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DOWNLOADIMAGE_FAIL);
                        mHandler.sendMessage(msg);
                    }else {
                        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DOWNLOADIMAGE_SUCCESS);
                        msg.obj = filePath;
                        mHandler.sendMessage(msg);
                    }
                }
            } );
    }

        /**
         * 先保存到本地再广播到图库
         * */
        public  void saveImageToGallery(String imagePath ) {
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(BaseApplication.single.getContentResolver(), imagePath  , "code", null);
                // 最后通知图库更新
                BaseApplication.single.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imagePath)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


}

