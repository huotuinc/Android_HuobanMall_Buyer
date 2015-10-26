package com.huotu.partnermall.ui;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.huotu.partnermall.AppManager;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.async.LoadLogoImageAyscTask;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.BitmapLoader;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.AccountModel;
import com.huotu.partnermall.model.PageInfoModel;
import com.huotu.partnermall.model.ShareModel;
import com.huotu.partnermall.model.SwitchUserModel;
import com.huotu.partnermall.model.UserSelectData;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.login.AutnLogin;
import com.huotu.partnermall.ui.web.UrlFilterUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.SwitchUserPopWin;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.UIUtils;
import com.huotu.partnermall.widgets.CircleImageDrawable;
import com.huotu.partnermall.widgets.CropperView;
import com.huotu.partnermall.widgets.KJSubWebView;
import com.huotu.partnermall.widgets.KJWebView;
import com.huotu.partnermall.widgets.NetworkImageViewCircle;
import com.huotu.partnermall.widgets.PhotoSelectView;
import com.huotu.partnermall.widgets.PopTimeView;
import com.huotu.partnermall.widgets.ProgressPopupWindow;
import com.huotu.partnermall.widgets.SharePopupWindow;
import com.huotu.partnermall.widgets.UserInfoView;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class HomeActivity extends BaseActivity implements View.OnClickListener,
                                                          Handler.Callback,
                                                          PhotoSelectView.OnPhotoSelectBackListener,
                                                          UserInfoView.OnUserInfoBackListener,
                                                          PopTimeView.OnDateBackListener {

    //获取资源文件对象
    private
    Resources      resources;
    //标题栏布局对象
    private
    RelativeLayout homeTitle;
    //标题栏左侧图标
    private
    ImageView      titleLeftImage;
    //标题栏标题文字
    private
    TextView       titleText;
    //标题栏右侧图标
    private ImageView titleRightImage;
    //web视图
    private
    KJWebView viewPage;
    //单独加载菜单
    private KJWebView menuView;


    //底部菜单
    private RelativeLayout bottomMenuLayout;

    //侧滑登录
    private RelativeLayout loginLayout;
    //侧滑设置按钮
    private ImageView      loginSetting;

    //主菜单容器
    private LinearLayout   mainMenuLayout;
    //未登陆界面
    private RelativeLayout noAuthLayout;
    //侧滑登录按钮
    /*private
    Button loginButton;*/
    //已授权界面
    private RelativeLayout getAuthLayout;
    //用户头像
    private
    ImageView userLogo;
    //用户名称
    private TextView userName;
    //用户类型
    private TextView userType;


    private long exitTime = 0l;
    //application引用
    public BaseApplication application;

    //handler对象
    public Handler mHandler;

    //windows类
    WindowManager wManager;
    private
    PhotoSelectView photo;

    private UserInfoView userInfoView;
    private CropperView  cropperView;

    private String[]         YEAR;
    private PopTimeView
                             popTimeView;
    private SharePopupWindow share;

    private
    AutnLogin login;
    //未登录时的头像
    private ImageView accountLogo;

    private ImageView titleRightLeftImage;

    private SwitchUserPopWin switchUser;

    public
    ProgressPopupWindow progress;

    public
    AssetManager am;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        // TODO Auto-generated method stub
        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) HomeActivity.this.getApplication ( );
        resources = HomeActivity.this.getResources ( );
        mHandler = new Handler ( this );
        share = new SharePopupWindow ( HomeActivity.this, HomeActivity.this, application );
        wManager = this.getWindowManager ( );
        am = this.getAssets ( );
        AppManager.getInstance ( ).addActivity ( this );
        setContentView ( R.layout.activity_home );
        //设置沉浸模式
        setImmerseLayout ( findViewById ( R.id.titleLayout ) );
        findViewById ( );
        initView ( );

        //初始化修改信息组件
        userInfoView = new UserInfoView ( this, application );
        userInfoView.setOnUserInfoBackListener ( this );
        progress = new ProgressPopupWindow ( HomeActivity.this, HomeActivity.this, wManager );
    }

    @Override
    protected
    void findViewById ( ) {
        //标题栏对象
        homeTitle = ( RelativeLayout ) this.findViewById ( R.id.titleLayout );
        //构建标题左侧图标，点击事件
        titleLeftImage = ( ImageView ) this.findViewById ( R.id.titleLeftImage );
        titleLeftImage.setOnClickListener ( this );
        //titleLeftImage.setClickable ( false );
        titleLeftImage.setVisibility ( View.GONE );
        //构建标题右侧图标，点击事件
        titleRightImage = ( ImageView ) this.findViewById ( R.id.titleRightImage );
        //titleRightImage.setClickable ( false );
        titleRightImage.setVisibility ( View.GONE );
        titleRightImage.setOnClickListener ( this );
        loginLayout = ( RelativeLayout ) this.findViewById ( R.id.loginLayout );
        loginSetting = ( ImageView ) this.findViewById ( R.id.sideslip_setting );
        loginSetting.setOnClickListener ( this );

        //标题栏文字
        titleText = ( TextView ) this.findViewById ( R.id.titleText );
        SystemTools.setFontStyle ( titleText, application );
        viewPage = ( KJWebView ) this.findViewById ( R.id.viewPage );
        mainMenuLayout = ( LinearLayout ) this.findViewById ( R.id.mainMenuLayout );

        //登录界面区分未得到微信授权、已得到微信授权
        //未得到授权界面
        noAuthLayout = ( RelativeLayout ) this.findViewById ( R.id.noAuth );
        //loginButton = ( Button ) this.findViewById ( R.id.sideslip_login );
        accountLogo = ( ImageView ) this.findViewById ( R.id.accountLogo );

        //已得到授权界面
        getAuthLayout = ( RelativeLayout ) this.findViewById ( R.id.getAuth );
        userLogo = ( ImageView ) this.findViewById ( R.id.accountIcon );
        userName = ( TextView ) this.findViewById ( R.id.accountName );
        SystemTools.setFontStyle ( userName, application );
        userType = ( TextView ) this.findViewById ( R.id.accountType );
        SystemTools.setFontStyle ( userType, application );

        //初始化底部菜单
        bottomMenuLayout = ( RelativeLayout ) this.findViewById ( R.id.menuL );

        menuView = ( KJWebView ) this.findViewById ( R.id.menuPage );
        titleRightLeftImage = ( ImageView ) this.findViewById ( R.id.titleRightLeftImage );
        //titleRightLeftImage.setClickable ( false );
        titleRightLeftImage.setVisibility ( View.GONE );
        titleRightLeftImage.setOnClickListener ( this );
    }

    @Override
    protected
    void onResume ( ) {
        super.onResume ( );
        if ( application.isLogin ( ) ) {

            noAuthLayout.setVisibility ( View.GONE );
            getAuthLayout.setVisibility ( View.VISIBLE );
            //渲染logo
            //BitmapLoader.create ( ).displayUrl ( HomeActivity.this, userLogo, application.getUserLogo (), R.drawable.ic_login_username, R.drawable.ic_login_username );
            new LoadLogoImageAyscTask ( resources, userLogo, application.getUserLogo ( ), R.drawable.ic_login_username ).execute ( );
            //渲染用户名
            userName.setText ( application.getUserName ( ) );
            userName.setTextColor ( resources.getColor ( R.color.theme_color ) );
            userType.setTextColor (
                    SystemTools.obtainColor (
                            application.obtainMainColor (
                                                        )
                                            )
                                  );
            userType.setText ( application.readMemberLevel ( ) );
        }
        else {
            noAuthLayout.setVisibility ( View.VISIBLE );
            getAuthLayout.setVisibility ( View.GONE );
            /*loginButton.setTextColor ( resources.getColor ( R.color.theme_color ) );
            loginButton.setOnClickListener ( this );*/
            noAuthLayout.setOnClickListener ( this );
        }

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
        homeTitle.setBackgroundColor ( SystemTools.obtainColor ( application.obtainMainColor ( ) ) );
        //设置左侧图标
        Drawable leftDraw = resources.getDrawable ( R.drawable.main_title_left_sideslip );
        SystemTools.loadBackground ( titleLeftImage, leftDraw );
        //设置右侧图标
        Drawable rightDraw = resources.getDrawable ( R.drawable.main_title_left_refresh );
        SystemTools.loadBackground ( titleRightImage, rightDraw );
        //设置分享图片
        Drawable rightLeftDraw = resources.getDrawable ( R.drawable.home_title_right_share );
        SystemTools.loadBackground ( titleRightLeftImage, rightLeftDraw );

        //设置侧滑界面
        loginLayout.setBackgroundColor (
                SystemTools.obtainColor (
                        application.obtainMainColor (
                                                    )
                                        )
                                       );
        //设置登录按钮背景
        /*Drawable loginDrawable = resources.getDrawable ( R.drawable.login_button_draw );
        loginDrawable.setColorFilter (
                new LightingColorFilter (
                        SystemTools.obtainColor (
                                application.obtainMainColor ( )
                                                ), SystemTools.obtainColor (
                        application.obtainMainColor ( )
                                                                           )
                )
                                     );
        SystemTools.loadBackground ( loginButton, loginDrawable );*/

        //设置未登陆按钮
        Bitmap bitmap = BitmapFactory.decodeResource ( resources, R.drawable.sideslip_login_lefttop_logo );
        accountLogo.setImageDrawable ( new CircleImageDrawable ( bitmap ) );

        //设置设置图标
        SystemTools.loadBackground (
                loginSetting, resources.getDrawable (
                        R.drawable
                                .switch_white
                                                    )
                                   );
        //设置登录界面背景
        noAuthLayout.setBackgroundColor (
                SystemTools.obtainColor (
                        application.obtainMainColor (
                                                    )
                                        )
                                        );
        getAuthLayout.setBackgroundColor (
                SystemTools.obtainColor (
                        application.obtainMainColor (
                                                    )
                                        )
                                         );

        //设置登录界面
        if ( application.isLogin ( ) ) {
            noAuthLayout.setVisibility ( View.GONE );
            getAuthLayout.setVisibility ( View.VISIBLE );
            //渲染logo
            new LoadLogoImageAyscTask ( resources, userLogo, application.getUserLogo ( ), R.drawable.ic_login_username ).execute ( );
            /*Bitmap bitmap = BitmapFactory.decodeResource ( resources, R.drawable.ic_launcher );
            userLogo.setImageDrawable ( new CircleImageDrawable ( bitmap ) );*/
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
            AuthParamUtils param = new AuthParamUtils ( application, System.currentTimeMillis (), builder.toString () );
            String nameUrl = param.obtainUrlName ();
            HttpUtil.getInstance ( ).doVolleyName ( HomeActivity.this, application, nameUrl, userType );
        }
        else {
            noAuthLayout.setVisibility ( View.VISIBLE );
            getAuthLayout.setVisibility ( View.GONE );
            /*loginButton.setTextColor ( resources.getColor ( R.color.theme_color ) );
            loginButton.setOnClickListener ( this );*/
            noAuthLayout.setOnClickListener ( this );
        }

        //动态加载侧滑菜单
        UIUtils ui = new UIUtils ( application, HomeActivity.this, resources, mainMenuLayout, wManager, mHandler, am );
        ui.loadMenus ( );
        //加载底部菜单
        //ui.loadMainMenu ( null, bottomMenuLayout );
        //加载页面
        //页面集成，title无需展示
        //titleText.setText ( "买家版" );

        loadPage ( );
        loadMainMenu ( );
    }

    private
    void loadMainMenu ( )
    {
        menuView.setBarHeight ( 0 );
        menuView.setJavaScriptEnabled ( true );
        menuView.setCacheMode ( WebSettings.LOAD_NO_CACHE );

        //首页默认为商户站点 + index
        String menuUrl = application.obtainMerchantUrl () + "/bottom.aspx?customerid=" + application.readMerchantId ();
        menuView.loadUrl ( menuUrl, null, null, null);
        menuView.setOnCustomScroolChangeListener ( new KJSubWebView.ScrollInterface ( ){

                                                       @Override
                                                       public
                                                       void onSChanged ( int l, int t, int oldl, int oldt ) {

                                                       }
                                                   });
        menuView.setWebViewClient (
                new WebViewClient ( ) {

                    //重写此方法，浏览器内部跳转
                    public
                    boolean shouldOverrideUrlLoading (
                            WebView view, String
                            url
                                                     ) {
                        viewPage.loadUrl ( url, titleText, mHandler, application );
                        return true;
                    }

                    @Override
                    public
                    void onPageStarted ( WebView view, String url, Bitmap favicon ) {

                        super.onPageStarted ( view, url, favicon );
                    }

                    @Override
                    public
                    void onPageFinished ( WebView view, String url ) {
                        super.onPageFinished ( view, url );
                    }
                }
                                  );
    }

    private
    void loadPage()
    {
        viewPage.setScrollBarStyle ( View.SCROLLBARS_OUTSIDE_OVERLAY );
        viewPage.setVerticalScrollBarEnabled ( false );
        viewPage.setBarHeight ( 8 );
        viewPage.setClickable ( true );
        viewPage.setUseWideViewPort ( true );
        //是否需要避免页面放大缩小操作

        viewPage.setSupportZoom ( true );
        viewPage.setBuiltInZoomControls ( true );
        viewPage.setJavaScriptEnabled ( true );
        viewPage.setCacheMode ( WebSettings.LOAD_DEFAULT );
        viewPage.setSaveFormData ( true );
        viewPage.setAllowFileAccess ( true );
        viewPage.setLoadWithOverviewMode ( false );
        viewPage.setSavePassword ( true );
        viewPage.setLoadsImagesAutomatically ( true );
        viewPage.setDomStorageEnabled(true);
        //首页鉴权
        AuthParamUtils paramUtils = new AuthParamUtils ( application, System.currentTimeMillis (), application.obtainMerchantUrl ( ) );
        String url = paramUtils.obtainUrl ();
        //首页默认为商户站点 + index
        viewPage.loadUrl ( url, titleText, null, application );

        viewPage.setOnCustomScroolChangeListener ( new KJSubWebView.ScrollInterface ( ) {
                                                       @Override
                                                       public
                                                       void onSChanged ( int l, int t, int oldl, int oldt ) {
                                                           // TODO Auto-generated method stub
                                                       }
                                                   } );

        viewPage.setWebViewClient (
                new WebViewClient ( ) {

                    //重写此方法，浏览器内部跳转
                    public
                    boolean shouldOverrideUrlLoading (
                            WebView view, String
                            url
                                                     ) {
                        UrlFilterUtils filter = new UrlFilterUtils (
                                HomeActivity.this,
                                HomeActivity.this,
                                titleText, mHandler,
                                application,
                                wManager
                        );
                        return filter.shouldOverrideUrlBySFriend ( viewPage, url );
                    }

                    @Override
                    public
                    void onPageStarted ( WebView view, String url, Bitmap favicon ) {
                        super.onPageStarted ( view, url, favicon );
                        /*titleLeftImage.setClickable ( false );
                        titleRightImage.setClickable ( false );
                        titleRightLeftImage.setClickable ( false );*/
                    }

                    @Override
                    public
                    void onPageFinished ( WebView view, String url ) {
                        //页面加载完成后,读取菜单项
                        super.onPageFinished ( view, url );
                        //titleRightLeftImage.setClickable ( true );
                       // titleLeftImage.setVisibility ( View.VISIBLE );
                        //titleLeftImage.setClickable ( true );
                        //titleRightImage.setClickable ( true );
                        //titleRightLeftImage.setClickable ( true );
                        titleLeftImage.setVisibility ( View.VISIBLE );
                        titleRightImage.setVisibility ( View.VISIBLE );
                        titleRightLeftImage.setVisibility ( View.VISIBLE );
                        titleText.setText ( view.getTitle ( ) );
                        //切换背景
                        titleRightImage.clearAnimation ( );
                        Drawable rightDraw = resources.getDrawable ( R.drawable.main_title_left_refresh );
                        SystemTools.loadBackground ( titleRightImage, rightDraw );
                    }

                    @Override
                    public
                    void onReceivedError (
                            WebView view, int errorCode, String description,
                            String failingUrl
                                         ) {
                        super.onReceivedError ( view, errorCode, description, failingUrl );
                        //错误页面处理
                        //隐藏菜单栏
                        //bottomMenuLayout.setVisibility ( View.GONE  );
                        viewPage.loadUrl (
                                "file:///android_asset/maintenance.html", titleText,
                                mHandler, application
                                         );

                    }

                }
                                  );
    }

    @Override
    public
    void onPhotoSelectBack ( PhotoSelectView.SelectType type ) {
        if (null == type)
            return;
        getPhotoByType ( type );
    }

    private void getPhotoByType(PhotoSelectView.SelectType type) {
        switch (type) {
            case Camera:
                //getPhotoByCamera();
                break;
            case File:
                //getPhotoByFile();
                break;

            default:
                break;
        }
    }

    @Override
    public
    void onUserInfoBack ( UserInfoView.Type type, UserSelectData data ) {

    }

    @Override
    public
    void onDateBack ( String date ) {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        // 2秒以内按两次推出程序
        if (event.getKeyCode () == KeyEvent.KEYCODE_BACK
            && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if(viewPage.canGoBack ())
            {
                viewPage.goBack ( titleText, mHandler, application );
            }
            else {
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

    @Override
    public
    void onClick ( View v ) {

        switch ( v.getId () )
        {
            case R.id.titleLeftImage:
            {
                if(application.isLeftImg)
                {
                    //切换出侧滑界面
                    application.layDrag.openDrawer ( Gravity.LEFT );
                } else
                {
                    viewPage .goBack ( titleText, mHandler, application );
                }

            }
            break;
            case R.id.titleRightImage:
            {
                //切换背景
                Drawable rightDraw = resources.getDrawable ( R.drawable.main_title_left_refresh_loding );
                SystemTools.loadBackground ( titleRightImage, rightDraw );
                SystemTools.setRotateAnimation(titleRightImage);
                /*//当前的url
                PageInfoModel pageInfo = application.titleStack.peek ( );
                //刷新页面
                Message msg = mHandler.obtainMessage ( Constants.FRESHEN_PAGE_MESSAGE_TAG, pageInfo.getPageUrl ());
                mHandler.sendMessage ( msg );*/
                viewPage.reload ( );
            }
            break;
            case R.id.sideslip_setting:
            {
                //切换用户
                String url = Constants.INTERFACE_PREFIX + "weixin/getuserlist?customerId="+application.readMerchantId ()+"&unionid="+application.readUserUnionId ();
                AuthParamUtils paramUtil = new AuthParamUtils ( application, System.currentTimeMillis (), url );
                final String rootUrls = paramUtil.obtainUrls ( );
                HttpUtil.getInstance ().doVolleyObtainUser (
                        HomeActivity.this, HomeActivity.this, application,
                        rootUrls, findViewById ( R.id.titleRightLeftImage ), wManager, mHandler
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
            break;
            /*case R.id.sideslip_home: {

                //模拟分享
               *//* String text = "这是我的分享测试数据！~我只是来酱油的！~请不要在意 好不好？？？？？";
                String imageurl = "http://www.wyl.cc/wp-content/uploads/2014/02/10060381306b675f5c5.jpg";
                String title = "江苏华漫";
                String url = "www.baidu.com";
                ShareModel msgModel = new ShareModel ();
                msgModel.setImageUrl ( imageurl);
                msgModel.setText ( text );
                msgModel.setTitle ( title );
                msgModel.setUrl ( url );
                share.initShareParams ( msgModel );
                share.showShareWindow ( );
                share.showAtLocation (
                        findViewById ( R.id.sideslip_home ),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0
                                     );
                share.setPlatformActionListener (
                        new PlatformActionListener ( ) {
                            @Override
                            public
                            void onComplete (
                                    Platform platform, int i, HashMap< String, Object > hashMap
                                            ) {
                                Message msg = Message.obtain ();
                                msg.what = Constants.SHARE_SUCCESS;
                                msg.obj = platform;
                                mHandler.sendMessage ( msg );
                            }

                            @Override
                            public
                            void onError ( Platform platform, int i, Throwable throwable ) {
                                Message msg = Message.obtain ();
                                msg.what = Constants.SHARE_ERROR;
                                msg.obj = platform;
                                mHandler.sendMessage ( msg );
                            }

                            @Override
                            public
                            void onCancel ( Platform platform, int i ) {
                                Message msg = Message.obtain ();
                                msg.what = Constants.SHARE_CANCEL;
                                msg.obj = platform;
                                mHandler.sendMessage ( msg );
                            }
                        }
                                                );
                share.setOnDismissListener ( new PoponDismissListener ( HomeActivity.this ) );*//*
                //home
                String homeUrl = application.obtainMerchantUrl ();
                Message msg = mHandler.obtainMessage ( Constants.LOAD_PAGE_MESSAGE_TAG, homeUrl);
                mHandler.sendMessage ( msg );
                //模拟弹出框
               *//* MsgPopWindow popWindow = new MsgPopWindow ( HomeActivity.this,  null, "弹出框测试", "系统出错啦，请关闭系统");
                popWindow.showAtLocation ( HomeActivity.this.findViewById ( R.id.sideslip_home ), Gravity.CENTER, 0,0 );*//*
                //模拟弹出支付界面
                *//*PayPopWindow payPopWindow = new PayPopWindow ( HomeActivity.this, null, null );
                payPopWindow.showAtLocation ( HomeActivity.this.findViewById ( R.id.sideslip_home ), Gravity.BOTTOM, 0, 0 );
                payPopWindow.setOnDismissListener ( new PoponDismissListener ( HomeActivity.this ) );*//*
                //隐藏侧滑菜单
                application.layDrag.closeDrawer ( Gravity.LEFT );
            }
            break;*/
            case R.id.noAuth:
            {
                //微信登录
                /*ToastUtils.showShortToast ( HomeActivity.this, application );*/
                /*Platform wechat = ShareSDK.getPlatform ( HomeActivity.this, Wechat.NAME );
                login = new AutnLogin ( HomeActivity.this, mHandler, noAuthLayout );
                login.authorize ( new Wechat ( HomeActivity.this ) );
                noAuthLayout.setClickable ( false );*/
            }
            break;
            case R.id.titleRightLeftImage:
            {
                String text = application.obtainMerchantName ()+"分享";
                String imageurl = application.obtainMerchantLogo ();
                if(!imageurl.contains ( "http://" ))
                {
                    //加上域名
                    imageurl = application.obtainMerchantUrl () + imageurl;
                }
                String title = application.obtainMerchantName ()+"分享";
                String url = null;
                if(0 == application.titleStack.size ())
                {
                    url = application.obtainMerchantUrl ();
                }
                else
                {
                    url = application.titleStack.peek ().getPageUrl ();
                }
                ShareModel msgModel = new ShareModel ();
                msgModel.setImageUrl ( imageurl);
                msgModel.setText ( text );
                msgModel.setTitle ( title );
                msgModel.setUrl ( url );
                share.initShareParams ( msgModel );
                share.showShareWindow ( );
                share.showAtLocation (
                        findViewById ( R.id.titleRightLeftImage ),
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
            break;
            default:
                break;
        }
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
                viewPage.loadUrl ( url, titleText, mHandler, application );
            }
            break;
            case Constants.FRESHEN_PAGE_MESSAGE_TAG:
            {
                //刷新界面
                String url = msg.obj.toString ();
                viewPage.loadUrl ( url, titleText, null, null );
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

    private void initYears() {
        YEAR = new String[60];
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR) - 10;
        for (int i = 0; i < 60; i++) {
            YEAR[i] = curYear - i + "";

        }
    }

    public class JSModel
    {
        @JavascriptInterface
        public void obtainMenuStatus(String status)
        {
            application.isMenuHide = Boolean.parseBoolean ( status );
        }
    }
}

