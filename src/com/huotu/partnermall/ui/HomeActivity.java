package com.huotu.partnermall.ui;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.toolbox.NetworkImageView;
import com.huotu.partnermall.AppManager;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.BitmapLoader;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.AccountModel;
import com.huotu.partnermall.model.ShareMsgModel;
import com.huotu.partnermall.model.UserSelectData;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.login.AutnLogin;
import com.huotu.partnermall.ui.web.UrlFilterUtils;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.UIUtils;
import com.huotu.partnermall.widgets.CropperView;
import com.huotu.partnermall.widgets.KJWebView;
import com.huotu.partnermall.widgets.OneKeyShareUtils;
import com.huotu.partnermall.widgets.PhotoSelectView;
import com.huotu.partnermall.widgets.PopTimeView;
import com.huotu.partnermall.widgets.UserInfoView;

import java.util.Calendar;

import cn.sharesdk.framework.Platform;
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

    //侧滑登录
    private RelativeLayout loginLayout;
    //侧滑设置按钮
    private ImageView      loginSetting;
    //侧滑主页按钮
    private ImageView      loginHome;
    //主菜单容器
    private LinearLayout   mainMenuLayout;
    //未登陆界面
    private RelativeLayout noAuthLayout;
    //侧滑登录按钮
    private
    Button loginButton;
    //已授权界面
    private RelativeLayout getAuthLayout;
    //用户头像
    private
    NetworkImageView userLogo;
    //用户名称
    private TextView userName;


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

    private String[] YEAR;
    private PopTimeView
                     popTimeView;

    private
    AutnLogin login;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        // TODO Auto-generated method stub
        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) HomeActivity.this.getApplication ( );
        resources = HomeActivity.this.getResources ( );
        mHandler = new Handler ( this );

        wManager = this.getWindowManager ( );
        AppManager.getInstance ( ).addActivity ( this );
        setContentView ( R.layout.activity_home );
        //设置沉浸模式
        setImmerseLayout ( findViewById ( R.id.titleLayout ) );
        findViewById ( );
        initView ( );

        //初始化修改信息组件
        userInfoView = new UserInfoView ( this, application );
        userInfoView.setOnUserInfoBackListener ( this );
    }

    @Override
    protected
    void findViewById ( ) {
        //标题栏对象
        homeTitle = ( RelativeLayout ) this.findViewById ( R.id.titleLayout );
        //构建标题左侧图标，点击事件
        titleLeftImage = ( ImageView ) this.findViewById ( R.id.titleLeftImage );
        titleLeftImage.setOnClickListener ( this );
        //构建标题右侧图标，点击事件
        titleRightImage = ( ImageView ) this.findViewById ( R.id.titleRightImage );
        titleRightImage.setOnClickListener ( this );
        loginLayout = ( RelativeLayout ) this.findViewById ( R.id.loginLayout );
        loginSetting = ( ImageView ) this.findViewById ( R.id.sideslip_setting );
        loginSetting.setOnClickListener ( this );
        loginHome = ( ImageView ) this.findViewById ( R.id.sideslip_home );
        loginHome.setOnClickListener ( this );

        //标题栏文字
        titleText = ( TextView ) this.findViewById ( R.id.titleText );
        viewPage = ( KJWebView ) this.findViewById ( R.id.viewPage );
        mainMenuLayout = ( LinearLayout ) this.findViewById ( R.id.mainMenuLayout );

        //登录界面区分未得到微信授权、已得到微信授权
        //未得到授权界面
        noAuthLayout = ( RelativeLayout ) this.findViewById ( R.id.noAuth );
        loginButton = ( Button ) this.findViewById ( R.id.sideslip_login );

        //已得到授权界面
        getAuthLayout = ( RelativeLayout ) this.findViewById ( R.id.getAuth );
        userLogo = ( NetworkImageView ) this.findViewById ( R.id.accountIcon );
        userName = ( TextView ) this.findViewById ( R.id.accountName );
    }

    @Override
    protected
    void onResume ( ) {
        super.onResume ( );
        if ( application.isLogin ( ) ) {

            noAuthLayout.setVisibility ( View.GONE );
            getAuthLayout.setVisibility ( View.VISIBLE );
            //渲染logo
            BitmapLoader.create ( ).displayUrl ( HomeActivity.this, userLogo, application.getUserLogo (), R.drawable.ic_login_username, R.drawable.ic_login_username );
            //渲染用户名
            userName.setText ( application.getUserName () );
            userName.setTextColor ( resources.getColor ( R.color.theme_color ) );
        }
        else
        {
            noAuthLayout.setVisibility ( View.VISIBLE );
            getAuthLayout.setVisibility ( View.GONE );
            loginButton.setTextColor ( resources.getColor ( R.color.theme_color ) );
            loginButton.setOnClickListener ( this );
        }

    }

    @Override
    protected
    void initView ( ) {
        //获取系统标题栏高度
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            int statusBarHeight = getStatusBarHeight ( HomeActivity.this );
            loginLayout.setPadding ( 0, statusBarHeight, 0, 0 );
        }

        //初始化侧滑菜单面板
        application.layDrag = ( DrawerLayout ) this.findViewById ( R.id.layDrag );
        //设置title背景
        homeTitle.setBackgroundColor ( resources.getColor ( R.color.home_title_bg ) );
        //设置左侧图标
        Drawable leftDraw = resources.getDrawable ( R.drawable.sideslip_image );
        SystemTools.loadBackground ( titleLeftImage, leftDraw );
        //设置右侧图标
        Drawable rightDraw = resources.getDrawable ( R.drawable.refresh_right );
        SystemTools.loadBackground ( titleRightImage, rightDraw );

        //设置侧滑界面
        loginLayout.setBackgroundColor ( resources.getColor ( R.color.home_title_bg ) );
        //设置设置图标
        SystemTools.loadBackground (
                loginSetting, resources.getDrawable (
                        R.drawable
                                .sideslip_login_setting
                                                    )
                                   );
        //设置Home图标
        SystemTools.loadBackground ( loginHome, resources.getDrawable ( R.drawable

                                                                                .sideslip_login_home ) );
        //设置登录界面背景
        noAuthLayout.setBackgroundColor ( resources.getColor ( R.color.home_title_bg ) );
        getAuthLayout.setBackgroundColor ( resources.getColor ( R.color.home_title_bg ) );


        //设置登录界面
        if(application.isLogin())
        {
            noAuthLayout.setVisibility ( View.GONE );
            getAuthLayout.setVisibility ( View.VISIBLE );
            //渲染logo
            BitmapLoader.create ( ).displayUrl ( HomeActivity.this, userLogo, application.getUserLogo (), R.drawable.ic_login_username, R.drawable.ic_login_username );
            //渲染用户名
            userName.setText ( application.getUserName () );
            userName.setTextColor ( resources.getColor ( R.color.theme_color ) );
        }
        else
        {
            noAuthLayout.setVisibility ( View.VISIBLE );
            getAuthLayout.setVisibility ( View.GONE );
            loginButton.setTextColor ( resources.getColor ( R.color.theme_color ) );
            loginButton.setOnClickListener ( this );
        }

        //动态加载侧滑菜单
        UIUtils ui = new UIUtils ( application, HomeActivity.this, resources, mainMenuLayout, wManager, mHandler);
        ui.loadMenus ();
        //加载页面
        titleText.setText ( "买家版" );

        viewPage.setBarHeight ( 8 );
        viewPage.setClickable ( true );
        viewPage.setUseWideViewPort ( true );
        viewPage.setSupportZoom ( true );
        viewPage.setBuiltInZoomControls ( true );
        viewPage.setJavaScriptEnabled ( true );
        viewPage.setCacheMode ( WebSettings.LOAD_NO_CACHE );
        viewPage.loadUrl ( Constants.HOME_PAGE_URL );
        viewPage.setWebViewClient (
                new WebViewClient ( ) {
                    //重写此方法，浏览器内部跳转
                    public
                    boolean shouldOverrideUrlLoading (
                            WebView view, String
                            url
                                                     ) {
                        UrlFilterUtils filter = new UrlFilterUtils ( HomeActivity.this, viewPage );
                        return filter.shouldOverrideUrlBySFriend ( view, url );
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
            if ((System.currentTimeMillis() - exitTime) > 2000 ) {
                ToastUtils.showLongToast ( getApplicationContext ( ), "再按一次退出程序" );
                exitTime = System.currentTimeMillis();
            } else
            {
                try
                {

                    HomeActivity.this.finish();
                    Runtime.getRuntime().exit(0);
                } catch (Exception e)
                {
                    Runtime.getRuntime().exit(-1);
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
                //切换出侧滑界面
                application.layDrag.openDrawer ( Gravity.LEFT );
            }
            break;
            case R.id.titleRightImage:
            {
                //当前的url
                String url = application.readCurrentUrl ( );
                //刷新页面
                Message msg = mHandler.obtainMessage ( Constants.FRESHEN_PAGE_MESSAGE_TAG, url);
                mHandler.sendMessage ( msg );
            }
            break;
            case R.id.sideslip_setting:
            {
                //设置界面
                //设置界面的url
                //String settingUrl = application.createUrl(Constants.PAGE_TYPE_SETTING);
                /*String settingUrl = "http://www.baidu.com";
                Message msg = mHandler.obtainMessage ( Constants.LOAD_PAGE_MESSAGE_TAG, settingUrl);
                mHandler.sendMessage ( msg );*/
                /*MsgPopWindow popWindow = new MsgPopWindow ( HomeActivity.this,  null, "弹出框测试", "系统出错啦，请关闭系统");
                popWindow.showAtLocation ( HomeActivity.this.findViewById ( R.id.sideslip_home ), Gravity.CENTER, 0,0 );
                popWindow.setOnDismissListener ( new poponDismissListener (HomeActivity.this) );*/
                //测试弹出选择图片
                /*if(null == photo)
                {
                    photo = new PhotoSelectView ( this,this );
                }
                photo.show ();*/
                //测试修改用户名
                userInfoView.show( UserInfoView.Type.Name, null, "方小开");
                //测试弹出时间控件
                /*if (YEAR == null)
                    initYears();
                if (popTimeView == null) {
                    popTimeView = new PopTimeView (this, application);
                    popTimeView.setOnDateBackListener(this);
                }*/

                // mainZoomOut(layAll);
                //popTimeView.show("1987-09-21");
                //隐藏侧滑菜单
                application.layDrag.closeDrawer ( Gravity.LEFT );
            }
            break;
            case R.id.sideslip_home: {

                //模拟分享
                ShareMsgModel msgModel = new ShareMsgModel ();
                msgModel.setShareText ( "分享的一个测试" );
                msgModel.setShareTitleUrl ( "http://www.baidu.com" );
                msgModel.setShareTitle ( "分享01" );
                msgModel.setShareUrl ( "http://www.baidu.com" );

                OneKeyShareUtils oks = new OneKeyShareUtils ( msgModel, HomeActivity.this );
                oks.shareShow (null, true);
                //home
                /*String homeUrl = "http://www.baidu.com";
                Message msg = mHandler.obtainMessage ( Constants.LOAD_PAGE_MESSAGE_TAG, homeUrl);
                mHandler.sendMessage ( msg );*/
                //模拟弹出框
               /* MsgPopWindow popWindow = new MsgPopWindow ( HomeActivity.this,  null, "弹出框测试", "系统出错啦，请关闭系统");
                popWindow.showAtLocation ( HomeActivity.this.findViewById ( R.id.sideslip_home ), Gravity.CENTER, 0,0 );*/
                //模拟弹出支付界面
                /*PayPopWindow payPopWindow = new PayPopWindow ( HomeActivity.this, null, null );
                payPopWindow.showAtLocation ( HomeActivity.this.findViewById ( R.id.sideslip_home ), Gravity.BOTTOM, 0, 0 );
                payPopWindow.setOnDismissListener ( new poponDismissListener ( HomeActivity.this ) );*/
                //隐藏侧滑菜单
                application.layDrag.closeDrawer ( Gravity.LEFT );
            }
            break;
            case R.id.sideslip_login:
            {
                //微信登录
                /*ToastUtils.showShortToast ( HomeActivity.this, application );*/
                //Platform wechat = ShareSDK.getPlatform ( HomeActivity.this, Wechat.NAME );
                login = new AutnLogin ( HomeActivity.this, mHandler );
                login.authorize ( new Wechat ( HomeActivity.this ) );

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
            case Constants.LOAD_PAGE_MESSAGE_TAG:
            {
                //加载菜单页面
                String url = msg.obj.toString ();
                viewPage.loadUrl ( url );
            }
            break;
            case Constants.FRESHEN_PAGE_MESSAGE_TAG:
            {
                //刷新界面
                String url = msg.obj.toString ();
                viewPage.loadUrl ( url );
            }
            break;
            case Constants.MSG_AUTH_COMPLETE:
            {
                //提示授权成功
                Platform plat = ( Platform ) msg.obj;
                ToastUtils.showShortToast ( HomeActivity.this, "微信授权成功，登陆中" );
                login.authorize ( plat );
            }
            break;
            case Constants.MSG_AUTH_ERROR:
            {
                //提示授权失败
                ToastUtils.showShortToast ( HomeActivity.this, "微信授权失败" );
            }
            break;
            case Constants.MSG_AUTH_CANCEL:
            {
                //提示取消授权
                ToastUtils.showShortToast ( HomeActivity.this, "微信授权被取消" );
            }
            break;
            case Constants.MSG_USERID_FOUND:
            {
                //提示授权成功
                ToastUtils.showShortToast ( HomeActivity.this, "已经获取用户信息" );
            }
            break;
            case Constants.MSG_LOGIN:
            {
                //提示授权成功
                ToastUtils.showShortToast ( HomeActivity.this, "登录成功" );
                //登录后更新界面
                AccountModel account = ( AccountModel ) msg.obj;
                application.writeMemberInfo ( account.getAccountName (), account.getAccountId (), account.getAccountIcon (), account.getAccountToken () );
                noAuthLayout.setVisibility ( View.GONE );
                getAuthLayout.setVisibility ( View.VISIBLE );
                BitmapLoader.create ( ).displayUrl ( HomeActivity.this, userLogo, application
                                                             .getUserLogo ( ), R.drawable
                                                             .ic_login_username, R.drawable
                                                             .ic_login_username );
                //渲染用户名
                userName.setText ( application.getUserName ( ) );
                userName.setTextColor ( resources.getColor ( R.color.theme_color ) );
            }
            break;
            case Constants.MSG_USERID_NO_FOUND:
            {
                //提示授权成功
                ToastUtils.showShortToast ( HomeActivity.this, "获取用户信息失败" );
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
}

