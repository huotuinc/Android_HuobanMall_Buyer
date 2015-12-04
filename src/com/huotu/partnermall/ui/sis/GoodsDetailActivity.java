package com.huotu.partnermall.ui.sis;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.Util;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class GoodsDetailActivity extends Activity implements View.OnClickListener {
    WebView webview;
    ProgressBar progressBar;
    String url = "";
    TextView back;
    RelativeLayout rlOn;
    RelativeLayout header;
    RelativeLayout botton;
    RelativeLayout rlcd;
    int tabtype;
    BaseApplication app;
    ProgressPopupWindow progressPopupWindow;
    TextView tvOn;
    SisGoodsModel goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_goods_detail);

        if (getIntent() != null && getIntent().hasExtra("goods")) {
            goods = (SisGoodsModel)getIntent().getSerializableExtra("goods");
        }
        if (getIntent() != null && getIntent().hasExtra("state")) {
            tabtype = getIntent().getIntExtra("state", 0);
        }

        app = (BaseApplication) this.getApplication();
        rlcd = (RelativeLayout) findViewById(R.id.sis_goodsdetail_cd);
        rlcd.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) GoodsDetailActivity.this.getApplication()).obtainMainColor()));

        header = (RelativeLayout) findViewById(R.id.sis_goodsdetail_header);
        header.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) GoodsDetailActivity.this.getApplication()).obtainMainColor()));
        botton = (RelativeLayout) findViewById(R.id.sis_goodsdetail_botton);
        botton.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) GoodsDetailActivity.this.getApplication()).obtainMainColor()));
        botton.setOnClickListener(this);
        tvOn = (TextView) findViewById(R.id.sis_goodsdetail_on);
        tvOn.setText(tabtype == 0 ? "下架" : "上架");

        back = (TextView) findViewById(R.id.sis_goodsdetail_back);
        back.setOnClickListener(this);
        rlOn = (RelativeLayout) findViewById(R.id.sis_goodsdetail_botton);
        rlOn.setOnClickListener(this);
        webview = (WebView) findViewById(R.id.sis_goodsdetail_webview);
        webview.setWebChromeClient(new MyChromeClient());
        webview.setWebViewClient(new MyWebClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);

        progressBar = (ProgressBar) findViewById(R.id.sis_goodsdetail_pgb);

        setImmerseLayout();

        loadUrl();
    }

    protected void loadUrl(){
        if( goods!=null && goods.getDetailsUrl() !=null && !TextUtils.isEmpty( goods.getDetailsUrl() )){
            url = goods.getDetailsUrl();
            progressBar.setVisibility(View.VISIBLE);
            webview.loadUrl(url);
        }else{
            ToastUtils.showShortToast(this,"url地址空");
        }
    }

    public void setImmerseLayout() {
        if (((BaseApplication) this.getApplication()).isKITKAT()) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            int statusBarHeight;
            int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = this.getResources().getDimensionPixelSize(resourceId);
                rlcd.setPadding(0, statusBarHeight, 0, 0);
                rlcd.getLayoutParams().height += statusBarHeight;
                rlcd.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) this.getApplication()).obtainMainColor()));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sis_goodsdetail_back) {
            this.finish();
        } else if (v.getId() == R.id.sis_goodsdetail_botton) {
            online();
        }
    }

    protected void online() {
        if (false == Util.isConnect(this)) {
            if (progressPopupWindow != null && progressPopupWindow.isShowing()) {
                progressPopupWindow.dismissView();
            }
            ToastUtils.showLongToast(this, "无网络");
            return;
        }

        //if (goodsid < 0) return;
        if( goods==null  )return;

        String url = SisConstant.INTERFACE_operGoods;
        AuthParamUtils authParamUtils = new AuthParamUtils(app,
                System.currentTimeMillis(), url, this);
        Map para = new HashMap();
        para.put("userId", app.readMemberId());
        para.put("goodsId", goods.getGoodsId() );
        para.put("opertype", tabtype);
        Map maps = authParamUtils.obtainParams(para);

        GsonRequest<BaseModel> request = new GsonRequest<BaseModel>(
                Request.Method.POST, url, BaseModel.class, null, maps,
                new MyOperateListener(this),
                new ErrorListener(this, null, progressPopupWindow)
        );

        if (progressPopupWindow == null) {
            progressPopupWindow = new ProgressPopupWindow(this, this, getWindowManager());
        }
        progressPopupWindow.showProgress("请稍等...");
        progressPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        VolleyUtil.getRequestQueue().add(request);
    }

    class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            progressBar.setVisibility(View.GONE);
        }
    }

    class MyChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                if (progressBar.getVisibility() == View.GONE)
                    progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }


    }

    static class MyOperateListener implements Response.Listener<BaseModel> {
        WeakReference<GoodsDetailActivity> ref;

        public MyOperateListener(GoodsDetailActivity act) {
            ref = new WeakReference<>(act);
        }

        @Override
        public void onResponse(BaseModel baseModel) {
            if (ref.get() == null) return;

            if (ref.get().progressPopupWindow != null) {
                ref.get().progressPopupWindow.dismissView();
            }

            if (!validateData(ref.get(), baseModel)) {
                return;
            }

            ref.get().setResult(RESULT_OK);
            ToastUtils.showShortToast(ref.get(), ref.get().tabtype == 0 ? "下架成功" : "上架成功");
            ref.get().tabtype = ref.get().tabtype == 0 ? 1 : 0;
            ref.get().tvOn.setText(ref.get().tabtype == 0 ? "下架" : "上架");
        }
    }

    protected static boolean validateData(Context context, BaseModel data) {

        if (null == data) {
            ToastUtils.showLongToast(context, "请求失败");
            return false;
        } else if (data.getSystemResultCode() != 1) {
            ToastUtils.showLongToast(context, data.getSystemResultDescription());
            return false;
        } else if (data.getResultCode() != 1) {
            ToastUtils.showLongToast(context, data.getResultDescription());
            return false;
        }
        return true;
    }

}
