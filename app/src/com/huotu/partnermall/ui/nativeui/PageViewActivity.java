package com.huotu.partnermall.ui.nativeui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.model.ShareModel;
import com.huotu.partnermall.receiver.MyBroadcastReceiver;
import com.huotu.partnermall.ui.base.NativeBaseActivity;
import com.huotu.partnermall.ui.web.UrlFilterUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.WindowUtils;
import com.huotu.partnermall.widgets.ProgressPopupWindow;
import com.huotu.partnermall.widgets.SharePopupWindow;
import java.util.HashMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * web页面
 */
public class PageViewActivity
        extends NativeBaseActivity
        implements Handler.Callback, MyBroadcastReceiver.BroadcastListener {
    private Handler  mHandler;
    private WebView viewPage;
    private String url;
    private SharePopupWindow share;
    private MyBroadcastReceiver myBroadcastReceiver;
    @Bind(R.id.activity_pageview_titlelayout)
    RelativeLayout titleLayout;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView  titleText;
    @Bind(R.id.titleRightImage)
    ImageView titleRightImage;
    @Bind(R.id.viewPage)
    PullToRefreshWebView refreshWebView;
    @Bind(R.id.main_pgbar)
    ProgressBar pgBar;
    ProgressPopupWindow progress;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_pageview);
        ButterKnife.bind(this);
        setImmerseLayout(titleLayout);
        mHandler = new Handler ( this );
        progress = new ProgressPopupWindow ( PageViewActivity.this );
        share = new SharePopupWindow ( PageViewActivity.this );
        myBroadcastReceiver = new MyBroadcastReceiver(PageViewActivity.this,this, MyBroadcastReceiver.ACTION_PAY_SUCCESS);
        Bundle bundle = this.getIntent().getExtras();
        url = bundle.getString ( Constants.INTENT_URL );
        initView();
    }

    protected void initView() {
        //设置title背景
        titleLayout.setBackgroundColor(SystemTools.obtainColor(BaseApplication.single.obtainMainColor()));
        //设置左侧图标
        Drawable leftDraw = ContextCompat.getDrawable( this ,R.drawable.main_title_left_back);// resources.getDrawable ( R.drawable.main_title_left_back );
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        //设置右侧图标
        Drawable rightDraw = ContextCompat.getDrawable(this, R.drawable.home_title_right_share ); //resources.getDrawable ( R.drawable.home_title_right_share );
        SystemTools.loadBackground(titleRightImage, rightDraw);
        viewPage = refreshWebView.getRefreshableView();
        refreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> pullToRefreshBase) {
                if( viewPage ==null) return;
                viewPage.reload();
            }
        });
        loadPage();

        share.showShareWindow();
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
                    }});
        share.setOnDismissListener(new PoponDismissListener(PageViewActivity.this));
    }

    private void loadPage(){
        viewPage.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        viewPage.setVerticalScrollBarEnabled(false);
        viewPage.setClickable(true);
        viewPage.getSettings().setUseWideViewPort(true);

        String userAgent = viewPage.getSettings().getUserAgentString();
        if( TextUtils.isEmpty(userAgent) ) {
            userAgent = "mobile";
        }else{
            userAgent +=";mobile";
        }
        viewPage.getSettings().setUserAgentString(userAgent);

        //是否需要避免页面放大缩小操作
        viewPage.getSettings().setSupportZoom(true);
        viewPage.getSettings().setBuiltInZoomControls(true);
        viewPage.getSettings().setJavaScriptEnabled(true);
        viewPage.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        viewPage.getSettings().setSaveFormData(true);
        viewPage.getSettings().setAllowFileAccess(true);
        viewPage.getSettings().setLoadWithOverviewMode(false);
        //viewPage.getSettings().setSavePassword(true);
        viewPage.getSettings().setLoadsImagesAutomatically(true);
        viewPage.addJavascriptInterface(this, "android");

        AuthParamUtils paramUtils = new AuthParamUtils ( BaseApplication.single , System.currentTimeMillis (), url , PageViewActivity.this );
        String url = paramUtils.obtainUrl ();

        viewPage.loadUrl(url);

        viewPage.setWebViewClient(
                new WebViewClient() {
                    //重写此方法，浏览器内部跳转
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (titleText == null) return false;
                        UrlFilterUtils filter = new UrlFilterUtils(PageViewActivity.this, mHandler, BaseApplication.single);
                        return filter.shouldOverrideUrlBySFriend(viewPage, url);
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                        if (refreshWebView == null) return;
                        refreshWebView.onRefreshComplete();

                        if (pgBar == null) return;
                        pgBar.setVisibility(View.GONE);

                        if( progress ==null) return;
                        progress.dismissView();
                    }
                }
        );

        viewPage.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

                if (titleText == null) {
                    return;
                }
                if (title == null) {
                    return;
                }

                titleText.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (refreshWebView == null || pgBar == null) return;
                if (100 == newProgress) {
                    refreshWebView.onRefreshComplete();
                    pgBar.setVisibility(View.GONE);
                } else {
                    if (pgBar.getVisibility() == View.GONE) pgBar.setVisibility(View.VISIBLE);
                    pgBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @OnClick(R.id.titleLeftImage)
    void doBack(){
        PageViewActivity.this.finish ();
    }

    /**
     * 通过调用javascript代码获得 分享的相关内容
     */
    protected void getShareContentByJS(){
        viewPage.loadUrl("javascript:__getShareStr();");
    }

    @OnClick(R.id.titleRightImage)
    void doShare(){
        String sourceUrl = viewPage.getUrl();
        if( !TextUtils.isEmpty( sourceUrl )) {
            Uri u = Uri.parse( sourceUrl );
            String path = u.getPath().toLowerCase().trim();
            int idx = path.lastIndexOf("/");
            String fileName = path.substring(idx + 1);

            if( fileName.equals("view.aspx") ) {//商品详细界面
                progress.showProgress("请稍等...");
                progress.showAtLocation( getWindow().getDecorView() , Gravity.CENTER, 0, 0);
                getShareContentByJS();
                return;
            }
        }

        String text = BaseApplication.single.obtainMerchantName ()+"分享";
        String imageurl = BaseApplication.single.obtainMerchantLogo ();
        if( !TextUtils.isEmpty(imageurl) && !imageurl.contains ( "http://" ))
        {
            //加上域名
            imageurl = BaseApplication.single.obtainMerchantUrl () + imageurl;
        }
        else if( TextUtils.isEmpty ( imageurl ))
        {
            imageurl = Constants.COMMON_SHARE_LOGO;
        }
        String title = BaseApplication.single.obtainMerchantName ()+"分享";
        String url = null;
        url = viewPage.getUrl();
        ShareModel msgModel = new ShareModel ();
        msgModel.setImageUrl(imageurl);
        msgModel.setText(text);
        msgModel.setTitle(title);
        msgModel.setUrl(url);
        share.initShareParams(msgModel);
        share.showAtLocation(titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getKeyCode () == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            finish();
            return true;
        }
        return super.dispatchKeyEvent ( event );
    }

    @Override
    public boolean handleMessage ( Message msg ){
        switch ( msg.what )
        {
            //分享
            case Constants.SHARE_SUCCESS:
            {
                //分享成功
                Platform platform = ( Platform ) msg.obj;
                if("WechatMoments".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( PageViewActivity.this, "微信朋友圈分享成功" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( PageViewActivity.this, "微信分享成功" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( PageViewActivity.this, "QQ空间分享成功" );
                }
                else if("SinaWeibo".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( PageViewActivity.this, "新浪微博分享成功" );
                }

            }
            break;
            case Constants.SHARE_ERROR:
            {
                //分享失败
                Platform platform = ( Platform ) msg.obj;
                if("WechatMoments".equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( PageViewActivity.this, "微信朋友圈分享失败" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( PageViewActivity.this, "微信分享失败" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( PageViewActivity.this, "QQ空间分享失败" );
                }
                else if("SinaWeibo".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( PageViewActivity.this, "新浪微博分享失败" );
                }
            }
            break;
            case Constants.SHARE_CANCEL:
            {
                //分享取消
                Platform platform = ( Platform ) msg.obj;
                if("WechatMoments".equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( PageViewActivity.this, "微信朋友圈分享取消" );
                }
                else if("Wechat".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( PageViewActivity.this, "微信分享取消" );
                }
                else if("QZone".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( PageViewActivity.this, "QQ空间分享取消" );
                }
                else if("SinaWeibo".equals ( platform.getName () ))
                {
                    ToastUtils.showShortToast ( PageViewActivity.this, "新浪微博分享取消" );
                }
            }
            break;
//            case AliPayUtil.SDK_PAY_FLAG: {
//                PayGoodBean payGoodBean = ( PayGoodBean ) msg.obj;
//                String tag = payGoodBean.getTag ( );
//                String[] tags = tag.split ( ";" );
//                for ( String str:tags )
//                {
//                    if(str.contains ( "resultStatus" ))
//                    {
//                        String code = str.substring ( str.indexOf ( "{" )+1, str.indexOf ( "}" ) );
//                        if(!"9000".equals ( code ))
//                        {
//                            //支付宝支付信息提示
//                            ToastUtils.showShortToast ( WebViewActivity.this, "支付宝支付失败，code:"+code );
//                        }
//                    }
//                }
//            }
//            break;
            case Constants.PAY_NET:
            {
                PayModel payModel = ( PayModel ) msg.obj;
                //调用JS
                viewPage.loadUrl ( "javascript:utils.Go2Payment("+payModel.getCustomId ()+","+ payModel.getTradeNo ()+","+ payModel.getPaymentType ()+", "
                                   + "false);\n" );
            }
            break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ( );
        ButterKnife.unbind(this);
        if( null != myBroadcastReceiver){
            myBroadcastReceiver.unregisterReceiver();
        }
        if( viewPage !=null ){
            viewPage.setVisibility(View.GONE);
        }
        if( progress !=null){
            progress.dismissView();
        }
    }

    @Override
    public void onFinishReceiver ( MyBroadcastReceiver.ReceiverType type, Object msg ) {
        if(type == MyBroadcastReceiver.ReceiverType.wxPaySuccess){
            viewPage.goBack();
        }
    }

    @JavascriptInterface
    public void sendShare(final String title, final String desc, final String link, final String img_url) {
        if (this == null) return;
        if (this.share == null) return;

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {

                if( PageViewActivity.this ==null ) return;
                if( progress!=null ){
                    progress.dismissView();
                }

                String sTitle = title;
                if( TextUtils.isEmpty( sTitle ) ){
                    sTitle = BaseApplication.single.obtainMerchantName ()+"分享";
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
                    sLink = BaseApplication.single.obtainMerchantUrl();
                }
                ShareModel msgModel = new ShareModel ();
                msgModel.setImageUrl(imageUrl);
                msgModel.setText(sDesc);
                msgModel.setTitle(sTitle);
                msgModel.setUrl(sLink);
                //msgModel.setImageData(BitmapFactory.decodeResource( resources , R.drawable.ic_launcher ));
                share.initShareParams(msgModel);
                WindowUtils.backgroundAlpha( PageViewActivity.this , 0.4f);
                share.showAtLocation( PageViewActivity.this.titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });
    }
}
