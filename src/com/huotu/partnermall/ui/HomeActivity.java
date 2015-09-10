package com.huotu.partnermall.ui;

import android.app.TabActivity;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huotu.partnermall.AppManager;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.BaseBean;
import com.huotu.partnermall.model.MenuBean;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.KJWebView;
import com.mob.tools.utils.UIHandler;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.friends.Wechat;

public class HomeActivity extends BaseActivity implements View.OnClickListener, Handler.Callback, PlatformActionListener {

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
    //侧滑登录按钮
    private
    Button loginButton;

    private long exitTime = 0l;
    //application引用
    public BaseApplication application;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        // TODO Auto-generated method stub
        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) HomeActivity.this.getApplication ( );
        resources = HomeActivity.this.getResources ( );
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
        loginButton = ( Button ) this.findViewById ( R.id.sideslip_login );
        loginButton.setOnClickListener ( this );
        //标题栏文字
        titleText = ( TextView ) this.findViewById ( R.id.titleText );
        viewPage = ( KJWebView ) this.findViewById ( R.id.viewPage );
        mainMenuLayout = ( LinearLayout ) this.findViewById ( R.id.mainMenuLayout );
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
        viewPage.loadUrl("http://www.baidu.com" );
        viewPage.setWebViewClient (
                new WebViewClient ( ) {
                    //重写此方法，浏览器内部跳转
                    public
                    boolean shouldOverrideUrlLoading (
                            WebView view, String
                            url
                                                     ) {
                        return shouldOverrideUrlBySFriend(view, url);
                    }
                }
                                   );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.activity_menu, menu);
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
                                                                                          .LayoutParams.MATCH_PARENT, 70);
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
                                //
                                ToastUtils.showShortToast ( HomeActivity.this, menu.getMenuName (
                                                                                                ) );
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
            return menu01.getMenuGroup ().compareTo ( menu02.getMenuGroup () );
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        // 2秒以内按两次推出程序
        if (event.getKeyCode () == KeyEvent.KEYCODE_BACK
            && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if ((System.currentTimeMillis() - exitTime) > 2000)
            {
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
                //刷新页面
            }
            break;
            case R.id.sideslip_setting:
            {
                //设置
            }
            break;
            case R.id.sideslip_home:
            {
                //home
            }
            break;
            case R.id.sideslip_login:
            {
                //微信登录
                authorize(new Wechat ( this ));
            }
            break;
            default:
                break;
        }
    }

    private void authorize(Platform plat) {
        if(plat.isValid()) {
            String userId = plat.getDb().getUserId();
            if (! TextUtils.isEmpty ( userId )) {
                UIHandler.sendEmptyMessage ( Constants.MSG_USERID_FOUND, this );
                login(plat.getName(), userId, null);
                return;
            }
            else
            {
                UIHandler.sendEmptyMessage ( Constants.MSG_USERID_NO_FOUND, this );
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    private void login(String plat, String userId, HashMap<String, Object> userInfo) {
        Message msg = new Message();
        msg.what = Constants.MSG_LOGIN;
        msg.obj = plat;
        UIHandler.sendMessage ( msg, this );
    }

    @Override
    public
    boolean handleMessage ( Message msg ) {
        return false;
    }

    @Override
    public
    void onComplete ( Platform platform, int action, HashMap< String, Object > hashMap ) {

        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(Constants.MSG_AUTH_COMPLETE, this);
            login(platform.getName(), platform.getDb().getUserId(), hashMap);
        }
        //记录用户信息
        String userName = platform.getDb ().getUserName ();
        String userId = platform.getDb ().getUserId ( );
        String token = platform.getDb ().getToken ();
        application.writeMemberInfo ( userName, userId, token );
    }

    @Override
    public
    void onError ( Platform platform, int action, Throwable throwable ) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage ( Constants.MSG_AUTH_ERROR, this);
        }
    }

    @Override
    public
    void onCancel ( Platform platform, int action ) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(Constants.MSG_AUTH_CANCEL, this);
        }
    }

    /**
     * webview拦截url作相应的处理
     * @param view
     * @param url
     * @return
     */
    protected boolean shouldOverrideUrlBySFriend(WebView view, String url) {
        if(url.contains(Constants.WEB_TAG_NEWFRAME)){
            Intent intentWeb = new Intent(HomeActivity.this, WebViewActivity.class);
            intentWeb.putExtra(Constants.INTENT_URL, url);
            startActivity(intentWeb);
            return true;
        }else if(url.contains(Constants.WEB_TAG_USERINFO)){
            //修改用户信息
            //判断修改信息的类型
            String type = url.substring(url.indexOf("=", 1)+1, url.indexOf("&", 1));
            if(Constants.MODIFY_PSW.equals ( type ))
            {
                //弹出修改密码框

            }
            return true;
        }else if(url.contains(Constants.WEB_TAG_LOGOUT)){
            //处理登出操作

            return true;
        }else if(url.contains(Constants.WEB_TAG_INFO)){
            //处理信息保护
            return true;
        }else if(url.contains(Constants.WEB_TAG_FINISH)){
            if(viewPage.canGoBack())
                viewPage.goBack();
        }
        return false;
    }
}

