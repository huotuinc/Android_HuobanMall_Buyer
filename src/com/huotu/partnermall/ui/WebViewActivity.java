package com.huotu.partnermall.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.huotu.android.library.libpay.BuildConfig;
import com.huotu.android.library.libpay.alipay.AliPayResult;
import com.huotu.android.library.libpay.alipay.AliPayUtil;
import com.huotu.android.library.libpay.alipay.PayResult;
import com.huotu.android.library.libpay.alipayV2.AliPayResultV2;
import com.huotu.android.library.libpay.alipayV2.AliPayUtilV2;
import com.huotu.android.library.libpay.weixin.WeiXinPayResult;
import com.huotu.android.library.libpay.weixin.WeiXinPayUtil;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.CloseEvent;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.model.RefreshHttpHeaderEvent;
import com.huotu.partnermall.model.RefreshPageEvent;
import com.huotu.partnermall.model.ShareModel;
import com.huotu.partnermall.model.WxPaySuccessCallbackModel;
import com.huotu.partnermall.receiver.MyBroadcastReceiver;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.base.SwipeBackActivity;
import com.huotu.partnermall.ui.web.UrlFilterUtils;
import com.huotu.partnermall.utils.ObtainParamsMap;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.WindowUtils;
import com.huotu.partnermall.widgets.ProgressPopupWindow;
import com.huotu.partnermall.widgets.SharePopupWindow;
import com.huotu.partnermall.widgets.TipAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 单张展示web页面
 */
public class WebViewActivity extends SwipeBackActivity
        implements Handler.Callback, MyBroadcastReceiver.BroadcastListener {
    private Resources  resources;
    private Handler  mHandler;
    @Bind(R.id.main_webview)
    WebView viewPage;
    private String url;
    private SharePopupWindow share;
    private MyBroadcastReceiver myBroadcastReceiver;
    @Bind(R.id.newtitleLayout)
    RelativeLayout newtitleLayout;
    //标题栏左侧图标
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    //标题栏标题文字
    @Bind(R.id.titleText)
    TextView  titleText;
    //标题栏右侧图标
    @Bind(R.id.titleRightImage)
    ImageView titleRightImage;
    @Bind(R.id.viewPage)
    PtrClassicFrameLayout ptrClassicFrameLayout;

    ProgressPopupWindow progress;

    @Bind(R.id.main_pgbar)
    ProgressBar pgBar;
    @Bind(R.id.statuslayout)
    RelativeLayout statuslayout;

    UrlFilterUtils urlFilterUtils;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        EventBus.getDefault().register(this);

        resources = this.getResources ( );
        this.setContentView(R.layout.new_load_page);
        ButterKnife.bind(this);
        setImmerseLayout(newtitleLayout);
        mHandler = new Handler ( this );
        progress = new ProgressPopupWindow ( WebViewActivity.this );
        share = new SharePopupWindow ( WebViewActivity.this );
        myBroadcastReceiver = new MyBroadcastReceiver(WebViewActivity.this,this,
                MyBroadcastReceiver.ACTION_PAY_SUCCESS  , MyBroadcastReceiver.ACTION_WX_PAY_CANCEL_CALLBACK , MyBroadcastReceiver.ACTION_WX_PAY_ERROR_CALLBACK);
        urlFilterUtils = new UrlFilterUtils(WebViewActivity.this, mHandler, application);
        urlFilterUtils.setOpenKeFuInNewPage(true);

        Bundle bundle = this.getIntent().getExtras();
        url = bundle.getString ( Constants.INTENT_URL );
        initView();
    }

    @Override
    protected void initView() {
        //设置title背景
        newtitleLayout.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        statuslayout.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        //设置左侧图标
        Drawable leftDraw = resources.getDrawable ( R.drawable.main_title_left_back );
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        //设置右侧图标
        Drawable rightDraw = resources.getDrawable ( R.drawable.home_title_right_share );
        SystemTools.loadBackground(titleRightImage, rightDraw);
        titleRightImage.setVisibility(View.GONE);

        ptrClassicFrameLayout.disableWhenHorizontalMove(true);
        ptrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrClassicFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                String url = viewPage.getUrl();
                if(url!=null && !url.isEmpty() && (url.toLowerCase().contains(Constants.URL_KEFU_2) || url.toLowerCase().contains(Constants.URL_KEFU_3) )){
                    //解决客服页面滚动事件与下拉刷新冲突问题
                    return false;
                }
                if(url!=null && !url.isEmpty() && url.toLowerCase().contains( Constants.URL_SubmitOrder.toLowerCase() )){
                    //解决 提交订单页面中弹出添加地址框中的滚动事件与下拉刷新冲突的问题
                    return false;
                }

                return PtrDefaultHandler.checkContentCanBePulledDown(frame , viewPage,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if(viewPage==null)return;
                viewPage.reload();
            }
        });


        //ptrClassicFrameLayout.setEnabled(false);


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
                    }
                });
        share.setOnDismissListener(new PoponDismissListener(WebViewActivity.this));
    }

    private void signHeader( WebView webView ){
//        String userid= application.readMemberId();
//        String unionid = application.readUserUnionId();
//        String openId = BaseApplication.single.readOpenId();
//        String sign = ObtainParamsMap.SignHeaderString(userid, unionid , openId);
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
//        webView.getSettings().setUserAgentString(userAgent);

        String userAgent = SignUtil.signHeader( webView.getSettings().getUserAgentString() );
        webView.getSettings().setUserAgentString( userAgent );

    }

    private void loadPage(){
        viewPage.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        viewPage.setVerticalScrollBarEnabled(false);
        viewPage.setHorizontalScrollBarEnabled(false);
        viewPage.setClickable(true);
        viewPage.getSettings().setUseWideViewPort(true);
        //是否需要避免页面放大缩小操作
        //viewPage.getSettings().setSupportZoom(true);
        //viewPage.getSettings().setBuiltInZoomControls(true);
        viewPage.getSettings().setJavaScriptEnabled(true);
        viewPage.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        viewPage.getSettings().setSaveFormData(true);
        viewPage.getSettings().setAllowFileAccess(true);
        viewPage.getSettings().setLoadWithOverviewMode(false);
        //viewPage.getSettings().setSavePassword(true);
        viewPage.getSettings().setLoadsImagesAutomatically(true);
        viewPage.getSettings().setDomStorageEnabled(true);
        viewPage.getSettings().setAppCacheEnabled(true);
        viewPage.getSettings().setDatabaseEnabled(true);
        String dir = BaseApplication.single.getDir("database", Context.MODE_PRIVATE).getPath();
        viewPage.getSettings().setGeolocationDatabasePath(dir);
        viewPage.getSettings().setGeolocationEnabled(true);
        viewPage.addJavascriptInterface(this, "android");
        String appCacheDir= BaseApplication.single.getDir("cache",Context.MODE_PRIVATE).getPath();
        viewPage.getSettings().setAppCachePath(appCacheDir);

        if(BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ){
            WebView.setWebContentsDebuggingEnabled(true);
        }

        signHeader( viewPage );

        viewPage.loadUrl(url, SignUtil.signHeader());

        viewPage.setWebViewClient(
                new WebViewClient() {
                    //重写此方法，浏览器内部跳转
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (titleText == null) return false;
                        //UrlFilterUtils filter = new UrlFilterUtils(WebViewActivity.this, mHandler, application);
                        return urlFilterUtils.shouldOverrideUrlBySFriend(viewPage, url);
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                        if( titleRightImage !=null  ) {
//                            titleRightImage.setVisibility(View.GONE);
//                        }

                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (titleText == null) return;
                        titleText.setText(view.getTitle());
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                        if (ptrClassicFrameLayout == null) return;
                        ptrClassicFrameLayout.refreshComplete();

                        if (pgBar == null) return;
                        pgBar.setVisibility(View.GONE);

                        if (progress == null) return;
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
                if (ptrClassicFrameLayout == null || pgBar == null) return;
                if (100 == newProgress) {
                    ptrClassicFrameLayout.refreshComplete();
                    pgBar.setVisibility(View.GONE);
                } else {
                    if (pgBar.getVisibility() == View.GONE) pgBar.setVisibility(View.VISIBLE);
                    pgBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                HomeActivity.mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebViewActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), HomeActivity.FILECHOOSER_RESULTCODE);
            }

            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                openFileChooser(uploadMsg);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke( origin , true ,false );
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                //return super.onJsConfirm(view, url, message, result);
                if(view ==null || view.getContext() ==null) return true;

                final TipAlertDialog tipAlertDialog = new TipAlertDialog(view.getContext() , false );
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
        });


    }



    @OnClick(R.id.titleLeftImage)
    void doBack(){
        WebViewActivity.this.finish();
    }

    /**
     * 通过调用javascript代码获得 分享的相关内容
     */
    protected void getShareContentByJS(){
        viewPage.loadUrl("javascript:__getShareStr();");
    }

    @OnClick(R.id.titleRightImage)
    void doShare(){
        //progress.showProgress("请稍等...");
        //progress.showAtLocation( getWindow().getDecorView() , Gravity.CENTER, 0, 0);
        getShareContentByJS();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getKeyCode () == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            closeSelf(WebViewActivity.this);
            return true;
        }
        return super.dispatchKeyEvent ( event );
    }

    @Override
    public boolean handleMessage ( Message msg ) {
        switch (msg.what) {
            //分享
            case Constants.SHARE_SUCCESS: {
                //分享成功
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信朋友圈分享成功");
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信分享成功");
                } else if ("QZone".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "QQ空间分享成功");
                } else if ("SinaWeibo".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "新浪微博分享成功");
                }else if(WechatFavorite.NAME.equals(platform.getName())){
                    ToastUtils.showShortToast("微信收藏成功");
                }
            }
            break;
            case Constants.SHARE_ERROR: {
                //分享失败
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信朋友圈分享失败");
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信分享失败");
                } else if ("QZone".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "QQ空间分享失败");
                } else if ("SinaWeibo".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "新浪微博分享失败");
                }else if(WechatFavorite.NAME.equals(platform.getName())){
                    ToastUtils.showShortToast("微信收藏失败");
                }
            }
            break;
            case Constants.SHARE_CANCEL: {
                //分享取消
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信朋友圈分享取消");
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "微信分享取消");
                } else if ("QZone".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "QQ空间分享取消");
                } else if ("SinaWeibo".equals(platform.getName())) {
                    ToastUtils.showShortToast(WebViewActivity.this, "新浪微博分享取消");
                }else if(WechatFavorite.NAME.equals(platform.getName())){
                    ToastUtils.showShortToast("微信收藏取消");
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
            case Constants.PAY_NET: {
                PayModel payModel = (PayModel) msg.obj;
                //调用JS
                viewPage.loadUrl("javascript:utils.Go2Payment(" + payModel.getCustomId() + ", '" + payModel.getTradeNo() + "' ," + payModel.getPaymentType() + ", "
                        + "false);\n");
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
            default:
                break;
        }
        return false;
    }

    protected void gotoOrderList(){
        if(viewPage!=null){
            String urlstr = String.format( Constants.URL_WaitPayOrderList, application.obtainMerchantUrl(), application.readMerchantId());
            viewPage.loadUrl( urlstr , SignUtil.signHeader() );
        }
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
        if(share !=null){
            share.dismiss();
        }

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFinishReceiver ( MyBroadcastReceiver.ReceiverType type, Object msg ) {
        if(type == MyBroadcastReceiver.ReceiverType.wxPaySuccess){
            //viewPage.goBack();
            if( msg ==null) return;
            Bundle bundle = (Bundle) msg;
            if( bundle ==null) return;
            WxPaySuccessCallbackModel data = (WxPaySuccessCallbackModel) bundle.getSerializable( Constants.HUOTU_PAY_CALLBACK_KEY);
            if( data ==null)  return;
            String orderNo = data.getOrderNo();

            if(viewPage !=null) {
                String urlString = String.format( Constants.URL_PaySuccess , application.obtainMerchantUrl(), application.readMerchantId() , orderNo );
                viewPage.loadUrl(urlString , SignUtil.signHeader() );
            }
        }else if( type == MyBroadcastReceiver.ReceiverType.wxPayCancel || type == MyBroadcastReceiver.ReceiverType.wxPayError){
            if(mHandler==null)return;
            Message uiMessage = mHandler.obtainMessage(Constants.Message_GotoOrderList);
            mHandler.sendMessage(uiMessage);
        }
    }

    @JavascriptInterface
    public void sendShare(final String title, final String desc, final String link, final String img_url) {
        if (this == null) return;
        if (this.share == null) return;

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {

                if( WebViewActivity.this ==null ) return;
                if( progress!=null ){
                    progress.dismissView();
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
                ShareModel msgModel = new ShareModel ();
                msgModel.setImageUrl(imageUrl);
                msgModel.setText(sDesc);
                msgModel.setTitle(sTitle);
                msgModel.setUrl(sLink);
                //msgModel.setImageData(BitmapFactory.decodeResource( resources , R.drawable.ic_launcher ));
                share.initShareParams(msgModel);
                //WindowUtils.backgroundAlpha( WebViewActivity.this , 0.4f);
                share.showAtLocation( WebViewActivity.this.titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });
    }

    @JavascriptInterface
    public void sendSisShare(final String title, final String desc, final String link, final String img_url) {
        if (this == null) return;
        if (this.share == null) return;

        //ToastUtils.showLongToast( getApplicationContext() , "title:"+title +", desc:"+ desc );

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {

                if( WebViewActivity.this ==null ) return;
                if( progress!=null ){
                    progress.dismissView();
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
                ShareModel msgModel = new ShareModel ();
                msgModel.setImageUrl(imageUrl);
                msgModel.setText(sDesc);
                msgModel.setTitle(sTitle);
                msgModel.setUrl(sLink);
                //msgModel.setImageData(BitmapFactory.decodeResource( resources , R.drawable.ic_launcher ));
                share.initShareParams(msgModel);
                //WindowUtils.backgroundAlpha( WebViewActivity.this , 0.4f);
                share.showAtLocation( WebViewActivity.this.titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

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
                    if(titleRightImage==null)return;
                    titleRightImage.setVisibility(View.GONE);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventClose(CloseEvent event){
        this.finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshHttpHeader(RefreshHttpHeaderEvent event){
        if( viewPage==null) return;
        signHeader(viewPage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshPage(RefreshPageEvent event){
        try {
            viewPage.reload();
        }catch (Exception ex){
            ex.printStackTrace();
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
            Toast.makeText(WebViewActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            String orderNo = result.getAliOrderInfo().getOrderNo();
            String urlString = String.format( Constants.URL_PaySuccess , application.obtainMerchantUrl(), application.readMerchantId() , orderNo );
            viewPage.loadUrl(urlString , SignUtil.signHeader() );
        } else {
            // 判断resultStatus 为非"9000"则代表可能支付失败
            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                Toast.makeText(WebViewActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                gotoOrderList();
            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                Toast.makeText(WebViewActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
//            if(viewPage!=null) {
//                String orderNo = result.getOrderInfo().getOrderNo();
//                String urlString = String.format( Constants.URL_PaySuccess , application.obtainMerchantUrl(), application.readMerchantId() , orderNo );
//                viewPage.loadUrl(urlString);
//            }
        }
    }
}
