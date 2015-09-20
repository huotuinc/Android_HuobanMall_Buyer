package com.huotu.partnermall.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huotu.partnermall.AppManager;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.BitmapLoader;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.AliPayListener;
import com.huotu.partnermall.listener.WXPayListener;
import com.huotu.partnermall.listener.poponDismissListener;
import com.huotu.partnermall.model.AccountModel;
import com.huotu.partnermall.model.MenuBean;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.login.WXLoginCallback;
import com.huotu.partnermall.ui.web.UrlFilterUtils;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.KJWebView;
import com.huotu.partnermall.widgets.MsgPopWindow;
import com.huotu.partnermall.widgets.OneKeyShareUtils;
import com.huotu.partnermall.widgets.PayPopWindow;
import com.mob.tools.utils.UIHandler;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class HomeActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {

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

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        // TODO Auto-generated method stub
        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) HomeActivity.this.getApplication ( );
        resources = HomeActivity.this.getResources ( );
        mHandler = new Handler ( this );

        wManager = this.getWindowManager ();
        AppManager.getInstance ( ).addActivity ( this );
        setContentView ( R.layout.activity_home );
        findViewById ( );
        initView ( );
    }

    @Override
     protected
     void findViewById ( ) {
        //标题栏对象
        homeTitle = ( RelativeLayout ) this.findViewById ( R.id.homeTitle );
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

    }

    @Override
    protected
    void initView ( ) {
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
        loadMenus ( );

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate ( R.menu.activity_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * 动态加载菜单栏
     */
    private void loadMenus()
    {

        String menuStr = application.readMenus ();
        //json格式转成list
        Gson gson = new Gson ();
        List<MenuBean> menuList = gson.fromJson ( menuStr, new TypeToken<List<MenuBean>> (){}.getType () );
        //按分组排序
        menuSort(menuList);
        //
        if(null == menuList || menuList.isEmpty ())
        {

            KJLoger.e ( "未加载商家定义的菜单" );
        }
        else
        {
            int size = menuList.size ();
            for(int i=0; i<size; i++)
            {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup
                                                                                          .LayoutParams.MATCH_PARENT, (wManager.getDefaultDisplay ().getHeight ()/15));
                //取出分组
                String menuGroup = menuList.get ( i ).getMenuGroup ();
                RelativeLayout menuLayout = ( RelativeLayout ) LayoutInflater.from ( this ).inflate ( R.layout.main_menu, null );
                menuLayout.setBackgroundColor ( resources.getColor ( R.color.theme_color ) );
                if(i<(size-1))
                {
                    if(0 == i)
                    {
                        lp.setMargins ( 0, 20, 0, 0 );
                    }
                    String menuGroupNext = menuList.get ( i+1 ).getMenuGroup ();
                    if(!menuGroupNext.equals ( menuGroup ))
                    {
                        menuLayout.setBackgroundDrawable ( resources.getDrawable ( R.drawable.main_menu_no_bottom ) );
                        lp.setMargins ( 0, 0, 0, 20 );
                    }
                    else
                    {
                        menuLayout.setBackgroundDrawable ( resources.getDrawable ( R.drawable.main_menu_bottom ) );
                    }
                }
                else
                {
                    //最后一个
                    menuLayout.setBackgroundDrawable ( resources.getDrawable ( R.drawable.main_menu_no_bottom ) );
                    lp.setMargins ( 0, 0, 0, 20 );
                }

                final MenuBean menu = menuList.get ( i );
                //设置ID
                menuLayout.setId ( i );
                //设置图标
                ImageView menuIcon = ( ImageView ) menuLayout.findViewById ( R.id.menuIcon );
                int iconId = resources.getIdentifier ( menu.getMenuIcon (), "drawable", "com.huotu.partnermall.inner" );
                Drawable menuIconDraw = resources.getDrawable ( iconId );
                SystemTools.loadBackground ( menuIcon,  menuIconDraw);
                //设置文本
                TextView menuText = ( TextView ) menuLayout.findViewById ( R.id.menuText );
                menuText.setText ( menu.getMenuName ( ) );
                menuLayout.setOnClickListener (
                        new View.OnClickListener ( ) {
                            @Override
                            public
                            void onClick ( View v ) {
                                //加载具体的页面
                                Message msg = mHandler.obtainMessage ( Constants.LOAD_PAGE_MESSAGE_TAG, menu.getMenuUrl () );
                                mHandler.sendMessage ( msg );

                                //隐藏侧滑菜单
                                application.layDrag.closeDrawer ( Gravity.LEFT );

                            }
                        }
                                              );

                menuLayout.setLayoutParams ( lp );

                mainMenuLayout.addView ( menuLayout );
            }
        }

    }

    /**
     * 按分组排序
     * @param menus
     * @return
     */
    private void menuSort(List<MenuBean> menus)
    {

        ComparatorMenu comparatorMenu = new ComparatorMenu ();
        Collections.sort ( menus, comparatorMenu );
    }

    public class ComparatorMenu implements Comparator {

        @Override
        public
        int compare ( Object lhs, Object rhs ) {

            MenuBean menu01 = ( MenuBean ) lhs;
            MenuBean menu02 = ( MenuBean ) rhs;
            //比较分组序号
            return menu01.getMenuGroup ( ).compareTo ( menu02.getMenuGroup () );
        }
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
                MsgPopWindow popWindow = new MsgPopWindow ( HomeActivity.this,  null, "弹出框测试", "系统出错啦，请关闭系统");
                popWindow.showAtLocation ( HomeActivity.this.findViewById ( R.id.sideslip_home ), Gravity.CENTER, 0,0 );
                popWindow.setOnDismissListener ( new poponDismissListener (HomeActivity.this) );
                //隐藏侧滑菜单
                application.layDrag.closeDrawer ( Gravity.LEFT );
            }
            break;
            case R.id.sideslip_home: {

                //模拟分享
                /*String shareTitle = "分享01";
                String shareText = "分享的一个测试";
                String shareUrl = "http://www.baidu.com";

                OneKeyShareUtils oks = new OneKeyShareUtils ( shareTitle, null, shareText, null, shareUrl, null, null, null, HomeActivity.this );
                oks.shareShow (null, true);*/
                //home
                /*String homeUrl = "http://www.baidu.com";
                Message msg = mHandler.obtainMessage ( Constants.LOAD_PAGE_MESSAGE_TAG, homeUrl);
                mHandler.sendMessage ( msg );*/
                //模拟弹出框
               /* MsgPopWindow popWindow = new MsgPopWindow ( HomeActivity.this,  null, "弹出框测试", "系统出错啦，请关闭系统");
                popWindow.showAtLocation ( HomeActivity.this.findViewById ( R.id.sideslip_home ), Gravity.CENTER, 0,0 );*/
                //模拟弹出支付界面
                PayPopWindow payPopWindow = new PayPopWindow ( HomeActivity.this, null, null );
                payPopWindow.showAtLocation ( HomeActivity.this.findViewById ( R.id.sideslip_home ), Gravity.BOTTOM, 0, 0 );
                payPopWindow.setOnDismissListener ( new poponDismissListener ( HomeActivity.this ) );
                //隐藏侧滑菜单
                application.layDrag.closeDrawer ( Gravity.LEFT );
            }
            break;
            case R.id.sideslip_login:
            {
                //微信登录
                /*ToastUtils.showShortToast ( HomeActivity.this, application );*/
                //Platform wechat = ShareSDK.getPlatform ( HomeActivity.this, Wechat.NAME );
                authorize ( new Wechat ( HomeActivity.this ) );

            }
            break;
            default:
                break;
        }
    }

    private void authorize(Platform plat) {
        if(plat.isValid ()) {
            String userId = plat.getDb().getUserId();
            if (! TextUtils.isEmpty ( userId )) {
                mHandler.sendEmptyMessage ( Constants.MSG_USERID_FOUND );
                login(plat);
                return;
            }
            else
            {
                mHandler.sendEmptyMessage ( Constants.MSG_USERID_NO_FOUND );
                return;
            }
        }
        plat.setPlatformActionListener ( new PlatformActionListener ( ) {
                                             @Override
                                             public
                                             void onComplete ( Platform platform, int action, HashMap< String, Object > hashMap ) {

                                                 if ( action == Platform.ACTION_USER_INFOR ) {
                                                     Message msg = new Message();
                                                     msg.what = Constants.MSG_AUTH_COMPLETE;
                                                     msg.obj = platform;
                                                     mHandler.sendMessage ( msg );
                                                 }
                                             }

                                             @Override
                                             public
                                             void onError ( Platform platform, int action, Throwable throwable ) {
                                                 if (action == Platform.ACTION_USER_INFOR) {
                                                     mHandler.sendEmptyMessage ( Constants.MSG_AUTH_ERROR );
                                                 }
                                             }

                                             @Override
                                             public
                                             void onCancel ( Platform platform, int action ) {
                                                 if (action == Platform.ACTION_USER_INFOR) {
                                                     mHandler.sendEmptyMessage(Constants.MSG_AUTH_CANCEL );
                                                 }
                                             }
                                         } );
        plat.SSOSetting(false);
        plat.showUser ( null );
    }

    private void login(Platform plat) {
        Message msg = new Message();
        msg.what = Constants.MSG_LOGIN;

        PlatformDb accountDb = plat.getDb ();
        AccountModel account = new AccountModel ();
        account.setAccountId ( accountDb.getUserId () );
        account.setAccountName ( accountDb.getUserName ( ) );
        account.setAccountIcon ( accountDb.getUserIcon ( ) );
        account.setAccountToken ( accountDb.getToken () );

        msg.obj = account;
        mHandler.sendMessage ( msg );
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
                authorize ( plat );
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
}

