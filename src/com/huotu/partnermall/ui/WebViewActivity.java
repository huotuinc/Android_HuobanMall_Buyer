package com.huotu.partnermall.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.PageInfoModel;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.web.SubUrlFilterUtils;
import com.huotu.partnermall.ui.web.UrlFilterUtils;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.KJWebView;

/**
 * 单张展示web页面
 */
public
class WebViewActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {

    //获取资源文件对象
    private
    Resources resources;
    private
    Handler mHandler;
    //application
    private
    BaseApplication application;
    //title
    //tilte组件
    private RelativeLayout homeTitle;
    //title左边图标
    //标题栏左侧图标
    private
    ImageView titleLeftImage;
    //标题栏标题文字
    private
    TextView  titleText;
    //标题栏右侧图标
    private ImageView titleRightImage;
    //web视图
    private
    KJWebView viewPage;
    private String url;

    @Override

    protected
    void onCreate ( Bundle savedInstanceState ) {

        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) this.getApplication ();
        resources = this.getResources ();
        this.setContentView ( R.layout.new_load_page );
        setImmerseLayout ( findViewById ( R.id.newtitleLayout ) );
        mHandler = new Handler ( this );

        Bundle bundle = this.getIntent ().getExtras ();
        url = bundle.getString ( Constants.INTENT_URL );
        findViewById (  );
        initView ();
    }

    @Override
    protected
    void findViewById ( ) {

        homeTitle = ( RelativeLayout ) this.findViewById ( R.id.newtitleLayout );
        titleLeftImage = ( ImageView ) this.findViewById ( R.id.titleLeftImage );
        titleLeftImage.setOnClickListener ( this );
        titleText = ( TextView ) this.findViewById ( R.id.titleText );
        titleRightImage = ( ImageView ) this.findViewById ( R.id.titleRightImage );
        titleRightImage.setOnClickListener ( this );
        viewPage = ( KJWebView ) this.findViewById ( R.id.viewPage );
    }

    @Override
    protected
    void initView ( ) {

        //设置title背景
        homeTitle.setBackgroundColor ( SystemTools.obtainColor ( application.obtainMainColor ( ) ) );
        //设置左侧图标
        Drawable leftDraw = resources.getDrawable ( R.drawable.main_title_left_back );
        SystemTools.loadBackground ( titleLeftImage, leftDraw );
        //设置右侧图标
        Drawable rightDraw = resources.getDrawable ( R.drawable.main_title_left_refresh );
        SystemTools.loadBackground ( titleRightImage, rightDraw );

        loadPage ( );
    }

    private void loadPage()
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
        viewPage.setCacheMode ( WebSettings.LOAD_NO_CACHE );
        viewPage.setSaveFormData ( true );
        viewPage.setAllowFileAccess ( true );
        viewPage.setLoadWithOverviewMode ( false );
        viewPage.setSavePassword ( true );
        viewPage.setLoadsImagesAutomatically ( true );
        viewPage.loadUrl ( url, titleText, null, application );

        viewPage.setWebViewClient (
                new WebViewClient ( ) {

                    //重写此方法，浏览器内部跳转
                    public
                    boolean shouldOverrideUrlLoading (
                            WebView view, String
                            url
                                                     ) {
                        SubUrlFilterUtils filter = new SubUrlFilterUtils ( WebViewActivity.this, WebViewActivity.this, titleText, mHandler, application );
                        return filter.shouldOverrideUrlBySFriend ( viewPage, url );
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
                        //页面加载完成后,读取菜单项

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
                        viewPage.loadUrl ( "file:///android_asset/maintenance.html", titleText, mHandler, application );

                    }

                }
                                  );

    }

    @Override
    public
    void onClick ( View v ) {

        switch ( v.getId () )
        {
            case R.id.titleLeftImage:
            {
                if(viewPage.canGoBack ())
                {
                    viewPage.goBack ( titleText, null, application);
                }
                else
                {
                    //清空消息
                    application.titleStack.clear ();
                    //关闭界面
                    WebViewActivity.this.finish ();
                }
            }
            break;
            case R.id.titleRightImage:
            {
                PageInfoModel pageInfo = application.titleStack.peek ();
                viewPage.loadUrl ( pageInfo.getPageUrl (), titleText, mHandler, application );
            }
            break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        // 2秒以内按两次推出程序
        if (event.getKeyCode () == KeyEvent.KEYCODE_BACK
            && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            //禁止返回按钮
            return true;
        }
        // TODO Auto-generated method stub
        return super.dispatchKeyEvent ( event );
    }

    @Override
    public
    boolean handleMessage ( Message msg ) {
        return false;
    }
}
