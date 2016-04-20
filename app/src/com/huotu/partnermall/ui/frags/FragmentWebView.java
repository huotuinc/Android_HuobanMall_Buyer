package com.huotu.partnermall.ui.frags;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.HeaderEvent;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.BackEvent;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.model.ShareModel;
import com.huotu.partnermall.receiver.MyBroadcastReceiver;
import com.huotu.partnermall.ui.base.BaseFragment;
import com.huotu.partnermall.ui.nativeui.PageViewActivity;
import com.huotu.partnermall.ui.web.UrlFilterUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.WindowUtils;
import com.huotu.partnermall.widgets.ProgressPopupWindow;
import com.huotu.partnermall.widgets.SharePopupWindow;
import com.tencent.connect.share.QzoneShare;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2016/4/13.
 */
public class FragmentWebView extends BaseFragment
        implements Handler.Callback , PlatformActionListener ,  MyBroadcastReceiver.BroadcastListener{
    //protected String tagName = FragmentWebView.class.getName();

    @Bind(R.id.viewPage)
    PullToRefreshWebView refreshWebView;
    @Bind(R.id.main_pgbar)
    ProgressBar pgBar;

    MyBroadcastReceiver myBroadcastReceiver;
    ProgressPopupWindow progressPopupWindow;
    SharePopupWindow sharePopupWindow;
    Handler mHandler;
    WebView webView;
    String url;

    public FragmentWebView(){
        //tagName = FragmentWebView.class.getName();

        mHandler = new Handler ( this );
    }


    public static FragmentWebView newInstance() {
        FragmentWebView fragment = new FragmentWebView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        //EventBus.getDefault().post(new BackEvent(true,true));
        controlShareButton();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if( bundle.containsKey(Constants.INTENT_URL))  url = bundle.getString ( Constants.INTENT_URL );

        myBroadcastReceiver = new MyBroadcastReceiver(getContext() ,this, MyBroadcastReceiver.ACTION_PAY_SUCCESS);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = refreshWebView.getRefreshableView();
        refreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> pullToRefreshBase) {
                if( webView ==null) return;
                webView.reload();
            }
        });

    }

    private void loadPage(){
        //webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setVerticalScrollBarEnabled(false);
        webView.setClickable(true);
        webView.getSettings().setUseWideViewPort(true);

        String userAgent = webView.getSettings().getUserAgentString();
        if( TextUtils.isEmpty(userAgent) ) {
            userAgent = "mobile";
        }else{
            userAgent +=";mobile";
        }
        webView.getSettings().setUserAgentString(userAgent);

        //是否需要避免页面放大缩小操作
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(false);
        //viewPage.getSettings().setSavePassword(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.addJavascriptInterface(this, "android");

        String tempurl=url;
//        Uri uri = Uri.parse( url);
//        String path = uri.getPath().toLowerCase().trim();
//        if( path.endsWith("view.aspx") ) {
//            tempurl = url;
//        }else {
            //AuthParamUtils paramUtils = new AuthParamUtils(BaseApplication.single, System.currentTimeMillis(), url, this.getActivity());
            //tempurl = paramUtils.obtainUrl();
//        }

        webView.loadUrl(tempurl);

        webView.setWebViewClient(
                new WebViewClient() {
                    //重写此方法，浏览器内部跳转
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        //if (titleText == null) return false;
                        UrlFilterUtils filter = new UrlFilterUtils( FragmentWebView.this.getActivity() , mHandler, BaseApplication.single);
                        return filter.shouldOverrideUrlBySFriend(webView, url);
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

                        if( progressPopupWindow ==null) return;
                        progressPopupWindow.dismissView();
                    }
                }
        );

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

                EventBus.getDefault().post(new HeaderEvent( title ));
//                if (titleText == null) {
//                    return;
//                }
//                if (title == null) {
//                    return;
//                }
//
//                titleText.setText(title);
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_webview,container ,false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if( webView !=null ){
            webView.setVisibility(View.GONE);
        }
        if( progressPopupWindow !=null){
            progressPopupWindow.dismissView();
        }
        if(sharePopupWindow!=null){
            sharePopupWindow=null;
        }
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public boolean handleMessage ( Message msg ){
        switch ( msg.what ){
            case Constants.SHARE_SUCCESS:{
                //分享成功
                Platform platform = ( Platform ) msg.obj;
                if(WechatMoments.NAME.equals ( platform.getName () )){
                    ToastUtils.showShortToast ( "微信朋友圈分享成功" );
                } else if(Wechat.NAME.equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( "微信分享成功" );
                } else if(QZone.NAME.equals ( platform.getName () )){
                    ToastUtils.showShortToast ( "QQ空间分享成功" );
                } else if(SinaWeibo.NAME.equals ( platform.getName () )){
                    ToastUtils.showShortToast ( "新浪微博分享成功" );
                }
            }
            break;
            case Constants.SHARE_ERROR: {
                //分享失败
                Platform platform = ( Platform ) msg.obj;
                if( WechatMoments.NAME.equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( "微信朋友圈分享失败" );
                }else if( Wechat.NAME.equals ( platform.getName () )){
                    ToastUtils.showShortToast (  "微信分享失败" );
                } else if(QZone.NAME.equals ( platform.getName () )){
                    ToastUtils.showShortToast ( "QQ空间分享失败" );
                } else if( SinaWeibo.NAME.equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( "新浪微博分享失败" );
                }
            }
            break;
            case Constants.SHARE_CANCEL: {
                //分享取消
                Platform platform = ( Platform ) msg.obj;
                if(WechatMoments.NAME.equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( "微信朋友圈分享取消" );
                } else if(Wechat.NAME.equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( "微信分享取消" );
                }
                else if( QZone.NAME.equals ( platform.getName () )) {
                    ToastUtils.showShortToast ( "QQ空间分享取消" );
                }
                else if(SinaWeibo.NAME.equals ( platform.getName () )){
                    ToastUtils.showShortToast ( "新浪微博分享取消" );
                }
            }
            break;
            case Constants.PAY_NET: {
                PayModel payModel = ( PayModel ) msg.obj;
                //调用JS
                webView.loadUrl ( "javascript:utils.Go2Payment("+payModel.getCustomId ()+","+ payModel.getTradeNo ()+","+ payModel.getPaymentType ()+", "
                        + "false);\n" );
            }
            break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (progressPopupWindow == null) progressPopupWindow = new ProgressPopupWindow(this.getActivity());
        if (sharePopupWindow == null) sharePopupWindow = new SharePopupWindow(this.getActivity());

        sharePopupWindow.showShareWindow();
        sharePopupWindow.setPlatformActionListener(this);
        sharePopupWindow.setOnDismissListener(new PoponDismissListener(this.getActivity()));

        loadPage();
    }

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

    @JavascriptInterface
    public void sendShare(final String title, final String desc, final String link, final String img_url) {
        if (this == null) return;
        if (this.sharePopupWindow == null) return;

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                if( progressPopupWindow!=null ){
                    progressPopupWindow.dismissView();
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
                sharePopupWindow.initShareParams(msgModel);
                WindowUtils.backgroundAlpha( FragmentWebView.this.getActivity() , 0.4f);
                sharePopupWindow.showAtLocation( FragmentWebView.this.webView , Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    @Override
    public void onFinishReceiver(MyBroadcastReceiver.ReceiverType type, Object msg) {
        if(type == MyBroadcastReceiver.ReceiverType.wxPaySuccess){
            webView.goBack();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenWeb(LinkEvent event){
        url = event.getLinkUrl();
        webView.loadUrl(url);
    }

    @Override
    public void setUrl( String url ) {
        this.url =  url;
        webView.loadUrl(url);

        controlShareButton();
    }

    protected void controlShareButton(){
        Uri uri = Uri.parse(url);
        String path = uri.getPath().toLowerCase().trim();
        if( path .endsWith( Constant.URL_PERSON_INDEX ) || path.endsWith( Constant.URL_SHOP_CART )){
            EventBus.getDefault().post(new BackEvent(true, false));
        }else{
            EventBus.getDefault().post(new BackEvent(true,true));
        }
    }

    @Override
    public void refreshTitle(){
        String title = webView.getTitle();
        EventBus.getDefault().post( new HeaderEvent( title));
    }

    /**
     * 通过调用javascript代码获得 分享的相关内容
     */
    protected void getShareContentByJS(){
        webView.loadUrl("javascript:__getShareStr();");
    }

    @Override
    public void share(){
        String sourceUrl = webView.getUrl();
        if( !TextUtils.isEmpty( sourceUrl )) {
            Uri u = Uri.parse( sourceUrl );
            String path = u.getPath().toLowerCase().trim();
            int idx = path.lastIndexOf("/");
            String fileName = path.substring(idx + 1);

            if( fileName.equals("view.aspx") ) {//商品详细界面
                progressPopupWindow.showProgress("请稍等...");
                progressPopupWindow.showAtLocation( webView , Gravity.CENTER, 0, 0);
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
        String url = webView.getUrl();
        ShareModel msgModel = new ShareModel ();
        msgModel.setImageUrl(imageurl);
        msgModel.setText(text);
        msgModel.setTitle(title);
        msgModel.setUrl(url);
        sharePopupWindow.initShareParams(msgModel);
        sharePopupWindow.showAtLocation(webView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
