package cy.com.partnermall.widgets;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2015/8/25.
 */
public
class KJWebView {
    private Activity aty;
    private WebView  webView;

    public KJWebView(Activity aty)
    {
        this.aty = aty;
    }

    public void initWeb(int resId, String url)
    {
        webView = ( WebView )aty.findViewById ( resId );
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //阻塞图片下载
        settings.setBlockNetworkImage ( true );
        settings.setDomStorageEnabled ( true );
        settings.setUseWideViewPort ( true );
        settings.setLoadWithOverviewMode(true);
        loadPage(url);
    }

    private void loadPage(String url)
    {
        webView.loadUrl(url);

        webView.setWebChromeClient(new ChromeView());
        webView.setWebViewClient(new Client());
    }

    private class Client extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            view.getSettings().setBlockNetworkImage(false);
        }

    }

    private class ChromeView extends WebChromeClient
    {
        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            // TODO Auto-generated method stub
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
            } else {
            }
        }
    }
}
