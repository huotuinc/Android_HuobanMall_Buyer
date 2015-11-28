package com.huotu.partnermall.ui;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.huotu.partnermall.AppManager;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.async.LoadLogoImageAyscTask;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.ShareModel;
import com.huotu.partnermall.model.SwitchUserModel;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.web.UrlFilterUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.SwitchUserPopWin;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.UIUtils;
import com.huotu.partnermall.widgets.ProgressPopupWindow;
import com.huotu.partnermall.widgets.SharePopupWindow;
import java.util.HashMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class HomeActivity extends BaseActivity implements Handler.Callback {

    //获取资源文件对象
    private
    Resources      resources;
    private long exitTime = 0l;
    //application引用
    public BaseApplication application;
    //handler对象
    public Handler mHandler;
    //windows类
    WindowManager wManager;
    private SharePopupWindow share;
    private SwitchUserPopWin switchUser;
    public
    ProgressPopupWindow progress;
    public
    AssetManager am;
    public static ValueCallback< Uri > mUploadMessage;
    public static final int FILECHOOSER_RESULTCODE = 1;


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
    public WebView pageWeb;
    //单独加载菜单
    @Bind(R.id.menuPage)
    WebView menuView;
    //底部菜单
    @Bind(R.id.menuL)
    RelativeLayout bottomMenuLayout;
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
    @Bind(R.id.accountType)
    TextView userType;
    @Bind(R.id.viewPage)
    PullToRefreshWebView refreshWebView;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        // TODO Auto-generated method stub
        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) HomeActivity.this.getApplication ( );
        resources = HomeActivity.this.getResources ( );
        mHandler = new Handler ( this );
        share = new SharePopupWindow ( HomeActivity.this, HomeActivity.this, application );
        wManager = this.getWindowManager();
        am = this.getAssets();
        AppManager.getInstance ( ).addActivity(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        //设置沉浸模式
        setImmerseLayout(findViewById(R.id.titleLayout));
        initView();
        progress = new ProgressPopupWindow ( HomeActivity.this, HomeActivity.this, wManager );
    }

    @Override
    protected
    void onResume ( ) {
        super.onResume();
            new LoadLogoImageAyscTask ( resources, userLogo, application.getUserLogo ( ), R.drawable.ic_login_username ).execute();
            //渲染用户名
            userName.setText ( application.getUserName ( ) );
            userName.setTextColor(resources.getColor(R.color.theme_color));
            userType.setTextColor(
                    SystemTools.obtainColor(
                            application.obtainMainColor(
                            )
                    )
            );
            userType.setText(application.readMemberLevel());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected
    void initView ( ) {
        //获取系统标题栏高度
        if ( application.isKITKAT ( ) ) {
            int statusBarHeight = getStatusBarHeight ( HomeActivity.this );
            loginLayout.setPadding ( 0, statusBarHeight, 0, 0 );
        }
        //初始化侧滑菜单面板
        application.layDrag = ( DrawerLayout ) this.findViewById ( R.id.layDrag );
        //设置title背景
        homeTitle.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor ( ) ) );
        //设置左侧图标
        Drawable leftDraw = resources.getDrawable ( R.drawable.main_title_left_sideslip );
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        //设置右侧图标
        Drawable rightDraw = resources.getDrawable ( R.drawable.home_title_right_share );
        SystemTools.loadBackground(titleRightImage, rightDraw );
        //设置侧滑界面
        loginLayout.setBackgroundColor(
                SystemTools.obtainColor(
                        application.obtainMainColor(
                        )
                )
        );

        //设置设置图标
        SystemTools.loadBackground(
                loginSetting, resources.getDrawable(
                        R.drawable
                                .switch_white
                )
        );
        getAuthLayout.setBackgroundColor (
                SystemTools.obtainColor (
                        application.obtainMainColor (
                                                    )
                                        )
                                         );

        //设置登录界面
            getAuthLayout.setVisibility ( View.VISIBLE );
            //渲染logo
            new LoadLogoImageAyscTask ( resources, userLogo, application.getUserLogo ( ), R.drawable.ic_login_username ).execute ( );
            //渲染用户名
            userName.setText ( application.getUserName ( ) );
            userName.setTextColor ( resources.getColor ( R.color.theme_color ) );
            userType.setTextColor ( SystemTools.obtainColor (
                                            application.obtainMainColor (
                                                                        )
                                                            ) );
            //获取用户等级
            StringBuilder builder = new StringBuilder (  );
            builder.append ( Constants.INTERFACE_PREFIX + "Weixin/GetUserLevelName" );
            builder.append ( "?customerId="+application.readMerchantId ( ) );
            builder.append ( "&unionId="+application.readUserUnionId ( ) );
            builder.append ( "&userId=" + application.readMemberId() );
            AuthParamUtils param = new AuthParamUtils ( application, System.currentTimeMillis (), builder.toString (), HomeActivity.this );
            String nameUrl = param.obtainUrlName ();
            HttpUtil.getInstance ( ).doVolleyName(HomeActivity.this, application, nameUrl, userType);

        //动态加载侧滑菜单
        UIUtils ui = new UIUtils ( application, HomeActivity.this, resources, mainMenuLayout, wManager, mHandler, am );
        ui.loadMenus();
        //监听web控件
        pageWeb = refreshWebView.getRefreshableView();
        refreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> pullToRefreshBase) {
                //刷新界面
                pageWeb.reload();
            }
        });

        loadPage();
        loadMainMenu();
    }

    private
    void loadMainMenu ( )
    {

        menuView.getSettings().setJavaScriptEnabled(true);
        menuView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        //首页默认为商户站点 + index
        String menuUrl = application.obtainMerchantUrl () + "/bottom.aspx?customerid=" + application.readMerchantId ();
        menuView.loadUrl( menuUrl );

        menuView.setWebViewClient(
                new WebViewClient() {

                    //重写此方法，浏览器内部跳转
                    public boolean shouldOverrideUrlLoading(
                            WebView view, String
                            url
                    ) {
                        pageWeb.loadUrl( url );
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

    private
    void loadPage()
    {
        pageWeb.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        pageWeb.setVerticalScrollBarEnabled(false);
        pageWeb.setClickable ( true );
        pageWeb.getSettings().setUseWideViewPort(true);
        //是否需要避免页面放大缩小操作

        pageWeb.getSettings().setSupportZoom(true);
        pageWeb.getSettings().setBuiltInZoomControls(true);
        pageWeb.getSettings().setJavaScriptEnabled(true);
        pageWeb.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        pageWeb.getSettings().setSaveFormData(true);
        pageWeb.getSettings().setAllowFileAccess(true);
        pageWeb.getSettings().setLoadWithOverviewMode(false);
        pageWeb.getSettings().setSavePassword(true);
        pageWeb.getSettings().setLoadsImagesAutomatically(true);
        pageWeb.getSettings().setDomStorageEnabled(true);
        //首页鉴权
        AuthParamUtils paramUtils = new AuthParamUtils ( application, System.currentTimeMillis (), application.obtainMerchantUrl ( ), HomeActivity.this );
        String url = paramUtils.obtainUrl ();
        //首页默认为商户站点 + index
        pageWeb.loadUrl(url);

        pageWeb.setWebViewClient(
                new WebViewClient() {

                    //重写此方法，浏览器内部跳转
                    public boolean shouldOverrideUrlLoading(
                            WebView view, String
                            url
                    ) {
                        UrlFilterUtils filter = new UrlFilterUtils(
                                HomeActivity.this,
                                HomeActivity.this,
                                titleText, mHandler,
                                application,
                                wManager
                        );
                        return filter.shouldOverrideUrlBySFriend( pageWeb, url );
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        /*titleLeftImage.setClickable ( false );
                        titleRightImage.setClickable ( false );
                        titleRightLeftImage.setClickable ( false );*/
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        //页面加载完成后,读取菜单项
                        super.onPageFinished(view, url);
                        if (url.contains("&back") || url.contains("?back")) {
                            //application.titleStack.clear ();
                            mHandler.sendEmptyMessage(Constants.LEFT_IMG_SIDE);
                        }
                        //titleRightLeftImage.setClickable ( true );
                        // titleLeftImage.setVisibility ( View.VISIBLE );
                        //titleLeftImage.setClickable ( true );
                        //titleRightImage.setClickable ( true );
                        //titleRightLeftImage.setClickable ( true );
                        titleLeftImage.setVisibility(View.VISIBLE);
                        titleRightImage.setVisibility(View.GONE);
                        titleText.setText(view.getTitle());
                        //切换背景
                        titleRightImage.clearAnimation();
                        Drawable rightDraw = resources.getDrawable(R.drawable
                                .main_title_left_refresh);
                        SystemTools.loadBackground(titleRightImage, rightDraw);
                    }

                    @Override
                    public void onReceivedError(
                            WebView view, int errorCode, String description,
                            String failingUrl
                    ) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                    }

                }
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        // 2秒以内按两次推出程序
        if (event.getKeyCode () == KeyEvent.KEYCODE_BACK
            && event.getAction() == KeyEvent.ACTION_DOWN)
        {

            if(pageWeb.canGoBack ())
            {
                pageWeb.goBack ( );
            }
            else
            {
                if ( ( System.currentTimeMillis ( ) - exitTime ) > 2000 ) {
                    ToastUtils.showLongToast ( getApplicationContext ( ), "再按一次退出程序" );
                    exitTime = System.currentTimeMillis ( );
                }
                else {
                    try {
                        HomeActivity.this.finish ( );
                        application.titleStack.clear ();
                        Runtime.getRuntime ( ).exit ( 0 );
                    }
                    catch ( Exception e ) {
                        Runtime.getRuntime ( ).exit ( - 1 );
                    }
                }
            }

            return true;
        }
        // TODO Auto-generated method stub
        return super.dispatchKeyEvent ( event );
    }

    @OnClick(R.id.titleLeftImage)
    void doBackOrMenuClick()
    {
        if(application.isLeftImg)
        {
            //切换出侧滑界面
            application.layDrag.openDrawer ( Gravity.LEFT );
        } else
        {
            pageWeb .goBack ( );
        }
    }

    @OnClick(R.id.sideslip_setting)
    void doSetting()
    {
        //切换用户
        String url = Constants.INTERFACE_PREFIX + "weixin/getuserlist?customerId="+application.readMerchantId ()+"&unionid="+application.readUserUnionId ();
        AuthParamUtils paramUtil = new AuthParamUtils ( application, System.currentTimeMillis (), url, HomeActivity.this );
        final String rootUrls = paramUtil.obtainUrls ( );
        HttpUtil.getInstance ().doVolleyObtainUser (
                HomeActivity.this, HomeActivity.this, application,
                rootUrls, titleRightImage, wManager, mHandler
        );

        //隐藏侧滑菜单
        application.layDrag.closeDrawer ( Gravity.LEFT );
        //切换进度条
        progress.showProgress ( "正在获取用户数据" );
        progress.showAtLocation (
                findViewById ( R.id.titleLeftImage ),
                Gravity.CENTER, 0, 0
        );
    }

    @OnClick(R.id.titleRightImage)
    void doShare()
    {
        String text = application.obtainMerchantName ()+"分享";
        String imageurl = application.obtainMerchantLogo ();
        if(!imageurl.contains ( "http://" ))
        {
            //加上域名
            imageurl = application.obtainMerchantUrl () + imageurl;
        }
        else if(TextUtils.isEmpty ( imageurl ))
        {
            imageurl = Constants.COMMON_SHARE_LOGO;
        }
        String title = application.obtainMerchantName ()+"分享";
        String url = null;
        if(0 == application.titleStack.size ())
        {
            url = application.obtainMerchantUrl ();
            url = SystemTools.shareUrl ( application, url );
        }
        else
        {
            url = application.titleStack.peek ().getPageUrl ();
            url = SystemTools.shareUrl(application, url);
        }
        ShareModel msgModel = new ShareModel ();
        msgModel.setImageUrl ( imageurl);
        msgModel.setText ( text );
        msgModel.setTitle ( title );
        msgModel.setUrl ( url );
        share.initShareParams ( msgModel );
        share.showShareWindow ( );
        share.showAtLocation (
                titleRightImage,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0
        );
        share.setPlatformActionListener (
                new PlatformActionListener ( ) {
                    @Override
                    public
                    void onComplete (
                            Platform platform, int i, HashMap< String, Object > hashMap
                    ) {
                        Message msg = Message.obtain ( );
                        msg.what = Constants.SHARE_SUCCESS;
                        msg.obj = platform;
                        mHandler.sendMessage ( msg );
                    }

                    @Override
                    public
                    void onError ( Platform platform, int i, Throwable throwable ) {
                        Message msg = Message.obtain ( );
                        msg.what = Constants.SHARE_ERROR;
                        msg.obj = platform;
                        mHandler.sendMessage ( msg );
                    }

                    @Override
                    public
                    void onCancel ( Platform platform, int i ) {
                        Message msg = Message.obtain ( );
                        msg.what = Constants.SHARE_CANCEL;
                        msg.obj = platform;
                        mHandler.sendMessage ( msg );
                    }
                }
        );
        share.setOnDismissListener ( new PoponDismissListener ( HomeActivity.this ) );
    }

    @Override
    public
    boolean handleMessage ( Message msg ) {

        switch ( msg.what )
        {
            //加载页面
            case Constants.LOAD_PAGE_MESSAGE_TAG:
            {
                //加载菜单页面
                String url = msg.obj.toString ();
                pageWeb.loadUrl ( url );
            }
            break;
            case Constants.FRESHEN_PAGE_MESSAGE_TAG:
            {
                //刷新界面
                String url = msg.obj.toString ();
                pageWeb.loadUrl ( url );
            }
            break;
            //分享
            case Constants.SHARE_SUCCESS:
            {
                //分享成功
                Platform platform = ( Platform ) msg.obj;
                int action = msg.arg1;
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
                else if("SinaWeibo".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( HomeActivity.this, "sina微博分享成功" );
                }

            }
            break;
            case Constants.SHARE_ERROR:
            {
                //分享失败
                Platform platform = ( Platform ) msg.obj;
                int action = msg.arg1;
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
                else if("SinaWeibo".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( HomeActivity.this, "sina微博分享失败" );
                }
            }
            break;
            case Constants.SHARE_CANCEL:
            {
                //分享取消
                Platform platform = ( Platform ) msg.obj;
                int action = msg.arg1;
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
                    ToastUtils.showShortToast ( HomeActivity.this, "sina微博分享取消" );
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
                application.writeMemberId ( String.valueOf ( user.getUserid ( ) ) );
                //更新昵称
                application.writeUserName ( user.getWxNickName () );
                application.writeUserIcon ( user.getWxHeadImg ( ) );

                application.writeMemberLevel(user.getLevelName ());

                //更新界面
                userName.setText ( user.getWxNickName () );
                userType.setText ( user.getLevelName ( ) );
                new LoadLogoImageAyscTask ( resources, userLogo, user.getWxHeadImg ( ), R.drawable.ic_login_username ).execute ( );

            }
            break;
            case Constants.LOAD_SWITCH_USER_OVER:
            {
                progress.dismissView ( );
            }
            break;
            default:
                break;
        }
        return false;
    }
}

