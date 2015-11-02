package com.huotu.partnermall.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
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
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.PageInfoModel;
import com.huotu.partnermall.model.PayGoodBean;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.model.ShareModel;
import com.huotu.partnermall.receiver.MyBroadcastReceiver;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.web.KJWebChromeClient;
import com.huotu.partnermall.ui.web.SubUrlFilterUtils;
import com.huotu.partnermall.ui.web.UrlFilterUtils;
import com.huotu.partnermall.utils.AliPayUtil;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.KJSubWebView;
import com.huotu.partnermall.widgets.KJWebView;
import com.huotu.partnermall.widgets.ScrollSwipeRefreshLayout;
import com.huotu.partnermall.widgets.SharePopupWindow;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 单张展示web页面
 */
public
class WebViewActivity extends BaseActivity implements View.OnClickListener, Handler.Callback, MyBroadcastReceiver.BroadcastListener {

    //获取资源文件对象
    private
    Resources       resources;
    private
    Handler         mHandler;
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

    private ImageView titleRightLeftImage;

    private SharePopupWindow share;

    private MyBroadcastReceiver myBroadcastReceiver;

    private ScrollSwipeRefreshLayout swipeRefreshLayout;

    @Override

    protected
    void onCreate ( Bundle savedInstanceState ) {

        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) this.getApplication ( );
        resources = this.getResources ( );
        this.setContentView ( R.layout.new_load_page );
        setImmerseLayout ( findViewById ( R.id.newtitleLayout ) );
        mHandler = new Handler ( this );
        share = new SharePopupWindow ( WebViewActivity.this, WebViewActivity.this, application );
        myBroadcastReceiver = new MyBroadcastReceiver(WebViewActivity.this,this, MyBroadcastReceiver.ACTION_PAY_SUCCESS);
        Bundle bundle = this.getIntent ( ).getExtras ( );
        url = bundle.getString ( Constants.INTENT_URL );
        findViewById ( );
        initView ( );
    }

    @Override
    protected
    void findViewById ( ) {

        homeTitle = ( RelativeLayout ) this.findViewById ( R.id.newtitleLayout );
        //web下拉组件刷新
        swipeRefreshLayout = (ScrollSwipeRefreshLayout) this.findViewById(R.id.pageLoadView);
        titleLeftImage = ( ImageView ) this.findViewById ( R.id.titleLeftImage );
        titleLeftImage.setOnClickListener ( this );
       // titleLeftImage.setClickable ( false );
        titleLeftImage.setVisibility ( View.GONE );
        titleText = ( TextView ) this.findViewById ( R.id.titleText );
        SystemTools.setFontStyle ( titleText, application );
        titleRightImage = ( ImageView ) this.findViewById ( R.id.titleRightImage );
        //titleRightImage.setClickable ( false );
        titleRightImage.setVisibility ( View.GONE );
        titleRightImage.setOnClickListener ( this );
        viewPage = ( KJWebView ) this.findViewById ( R.id.viewPage );
        titleRightLeftImage = ( ImageView ) this.findViewById ( R.id.titleRightLeftImage );
        //titleRightLeftImage.setClickable ( false );
        titleRightLeftImage.setVisibility ( View.GONE );
        titleRightLeftImage.setOnClickListener ( this );
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
        //设置分享图片
        Drawable rightLeftDraw = resources.getDrawable ( R.drawable.home_title_right_share );
        SystemTools.loadBackground ( titleRightLeftImage, rightLeftDraw );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                viewPage.reload();
            }
        });
        loadPage ( );
    }

    private void loadPage()
    {
        viewPage.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        viewPage.setVerticalScrollBarEnabled(false);
        viewPage.setBarHeight(8);
        viewPage.setClickable(true);
        viewPage.setUseWideViewPort(true);
        //是否需要避免页面放大缩小操作

        viewPage.setSupportZoom(true);
        viewPage.setBuiltInZoomControls(true);
        viewPage.setJavaScriptEnabled(true);
        viewPage.setCacheMode(WebSettings.LOAD_DEFAULT);
        viewPage.setSaveFormData(true);
        viewPage.setAllowFileAccess(true);
        viewPage.setLoadWithOverviewMode(false);
        viewPage.setSavePassword(true);
        viewPage.setLoadsImagesAutomatically(true);
        viewPage.loadUrl( url, titleText, null, application, swipeRefreshLayout);
        viewPage.setOnCustomScroolChangeListener(
                new KJSubWebView.ScrollInterface() {

                    @Override
                    public void onSChanged(int l, int t, int oldl, int oldt) {
                        if (viewPage.getWebScrollY() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                }
        );
        viewPage.setWebViewClient(
                new WebViewClient() {

                    //重写此方法，浏览器内部跳转
                    public boolean shouldOverrideUrlLoading(
                            WebView view, String
                            url
                    ) {
                        SubUrlFilterUtils filter = new SubUrlFilterUtils(WebViewActivity.this,
                                WebViewActivity.this,
                                titleText, mHandler,
                                application);
                        return filter.shouldOverrideUrlBySFriend(viewPage, url, swipeRefreshLayout);
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);

                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        //页面加载完成后,读取菜单项
                        // titleRightLeftImage.setClickable ( true );
                        titleLeftImage.setVisibility(View.VISIBLE);
                        titleRightImage.setVisibility(View.GONE);
                        titleRightLeftImage.setVisibility(View.VISIBLE);
                        /*titleLeftImage.setClickable ( true );
                        titleRightImage.setClickable ( true );
                        titleRightLeftImage.setClickable ( true );*/
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
                        //错误页面处理
                        //隐藏菜单栏
                        //bottomMenuLayout.setVisibility ( View.GONE  );
                        /*viewPage.loadUrl ( "file:///android_asset/maintenance.html", titleText, mHandler, application );*/

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
                    if(0 < application.titleStack.size())
                    {
                        application.titleStack.pop ();
                    }

                    //关闭界面
                    WebViewActivity.this.finish ();
                }
            }
            break;
            case R.id.titleRightImage:
            {
                Drawable rightDraw = resources.getDrawable ( R.drawable.main_title_left_refresh_loding );
                SystemTools.loadBackground ( titleRightImage, rightDraw );
                SystemTools.setRotateAnimation(titleRightImage);
                /*PageInfoModel pageInfo = application.titleStack.peek ( );
                viewPage.loadUrl ( pageInfo.getPageUrl (), titleText, null, null );*/
                viewPage.reload ();
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
                else if( TextUtils.isEmpty ( imageurl ))
                {
                    imageurl = Constants.COMMON_SHARE_LOGO;
                }
                String title = application.obtainMerchantName ()+"分享";
                String url = null;
                if(0 == application.titleStack.size ()) {
                    url = application.obtainMerchantUrl ();
                }
                else
                {
                    url = application.titleStack.peek ().getPageUrl ();
                    url = SystemTools.shareUrl ( application, url );
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
                share.setOnDismissListener ( new PoponDismissListener ( WebViewActivity.this ) );
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
            if(viewPage.canGoBack ())
            {
                viewPage.goBack ( titleText, mHandler, application );
            }
            else {
                //清空消息
                if(0 < application.titleStack.size())
                {
                    application.titleStack.pop ();
                }

                //关闭当前页
                closeSelf ( WebViewActivity.this );
            }
            return true;
        }
        // TODO Auto-generated method stub
        return super.dispatchKeyEvent ( event );
    }

    @Override
    public
    boolean handleMessage ( Message msg )
    {
        switch ( msg.what )
        {
            //分享
            case Constants.SHARE_SUCCESS:
            {
                //分享成功
                Platform platform = ( Platform ) msg.obj;
                int action = msg.arg1;
                if("WechatMoments".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( WebViewActivity.this, "微信朋友圈分享成功" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( WebViewActivity.this, "微信分享成功" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( WebViewActivity.this, "QQ空间分享成功" );
                }
                else if("SinaWeibo".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( WebViewActivity.this, "sina微博分享成功" );
                }

            }
            break;
            case Constants.SHARE_ERROR:
            {
                //分享失败
                Platform platform = ( Platform ) msg.obj;
                int action = msg.arg1;
                if("WechatMoments".equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( WebViewActivity.this, "微信朋友圈分享失败" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( WebViewActivity.this, "微信分享失败" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( WebViewActivity.this, "QQ空间分享失败" );
                }
                else if("SinaWeibo".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( WebViewActivity.this, "sina微博分享失败" );
                }
            }
            break;
            case Constants.SHARE_CANCEL:
            {
                //分享取消
                Platform platform = ( Platform ) msg.obj;
                int action = msg.arg1;
                if("WechatMoments".equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( WebViewActivity.this, "微信朋友圈分享取消" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( WebViewActivity.this, "微信分享取消" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( WebViewActivity.this, "QQ空间分享取消" );
                }
                else if("SinaWeibo".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( WebViewActivity.this, "sina微博分享取消" );
                }
            }
            break;
            case AliPayUtil.SDK_PAY_FLAG: {
                PayGoodBean payGoodBean = ( PayGoodBean ) msg.obj;
                String tag = payGoodBean.getTag ( );
                String[] tags = tag.split ( ";" );
                for ( String str:tags )
                {
                    if(str.contains ( "resultStatus" ))
                    {
                        String code = str.substring ( str.indexOf ( "{" )+1, str.indexOf ( "}" ) );
                        if(!"9000".equals ( code ))
                        {
                            //支付宝支付信息提示
                            ToastUtils.showShortToast ( WebViewActivity.this, "支付宝支付失败，code:"+code );
                        }
                    }
                }
            }
            break;
            case Constants.PAY_NET:
            {
                PayModel payModel = ( PayModel ) msg.obj;
                //调用JS
                viewPage.loadUrl ( "javascript:utils.Go2Payment("+payModel.getCustomId ()+","+ payModel.getTradeNo ()+","+ payModel.getPaymentType ()+", "
                                   + "false);\n", titleText, null, application, swipeRefreshLayout );
            }
            default:
                break;
        }
        return false;

    }

    @Override
    protected
    void onDestroy ( ) {
        super.onDestroy ( );
        if( null != myBroadcastReceiver)
        {
            myBroadcastReceiver.unregisterReceiver();
        }
    }

    @Override
    public
    void onFinishReceiver ( MyBroadcastReceiver.ReceiverType type, Object msg ) {
        if(type == MyBroadcastReceiver.ReceiverType.wxPaySuccess)
        {
            viewPage.goBack ( titleText, null, application);
        }
    }

}
