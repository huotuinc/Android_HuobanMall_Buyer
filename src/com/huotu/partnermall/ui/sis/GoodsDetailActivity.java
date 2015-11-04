package com.huotu.partnermall.ui.sis;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huotu.partnermall.inner.R;

public class GoodsDetailActivity extends Activity implements View.OnClickListener {
    WebView webview;
    ProgressBar progressBar;
    String url ="";
    TextView back;
    RelativeLayout rlOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_goods_detail);

        back = (TextView)findViewById(R.id.sis_goodsdetail_back);
        back.setOnClickListener(this);
        rlOn =(RelativeLayout)findViewById(R.id.sis_goodsdetail_botton);
        rlOn.setOnClickListener(this);
        webview =(WebView) findViewById(R.id.sis_goodsdetail_webview);
        webview.setWebChromeClient(new MyChromeClient());
        webview.setWebViewClient(new MyWebClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);

        progressBar = (ProgressBar)findViewById(R.id.sis_goodsdetail_pgb);

        if( getIntent() !=null && getIntent().hasExtra("url") ){
            url = getIntent().getStringExtra("url");
            if(!TextUtils.isEmpty( url )) {
                webview.loadUrl(url);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.sis_goodsdetail_back){
            this.finish();
        }else if( v.getId()==R.id.sis_goodsdetail_botton){

        }
    }

    class MyWebClient extends WebViewClient{
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

    class MyChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100){
                progressBar.setVisibility(View.GONE);
            }
            else {
                if (progressBar.getVisibility() == View.GONE)  progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }


    }
}
