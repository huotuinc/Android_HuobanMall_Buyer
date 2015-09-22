package com.huotu.partnermall.ui.web;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

/**
 * chromeClient 自定义类
 */
public
class KJWebChromeClient extends android.webkit.WebChromeClient {

    private WebChromeClient mWrappedClient;

    public
    KJWebChromeClient ( WebChromeClient mWrappedClient ) {
        this.mWrappedClient = mWrappedClient;
    }

    /** {@inheritDoc} */
    @Override
    public
    void onShowCustomView ( View view, CustomViewCallback callback ) {
        mWrappedClient.onShowCustomView ( view, callback );
    }

    /** {@inheritDoc} */
    @Override
    public
    void onHideCustomView ( ) {
        mWrappedClient.onHideCustomView ( );
    }

    /** {@inheritDoc} */
    @Override
    public
    boolean onCreateWindow (
            WebView view, boolean dialog, boolean userGesture,
                                  Message resultMsg)
    {
        return mWrappedClient.onCreateWindow(view, dialog, userGesture, resultMsg);
    }

    /** {@inheritDoc} */
    @Override
    public void onRequestFocus(WebView view)
    {
        mWrappedClient.onRequestFocus(view);
    }

    /** {@inheritDoc} */
    @Override
    public void onCloseWindow(WebView window)
    {
        mWrappedClient.onCloseWindow(window);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result)
    {
        return mWrappedClient.onJsAlert(view, url, message, result);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result)
    {
        return mWrappedClient.onJsConfirm(view, url, message, result);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result)
    {
        return mWrappedClient.onJsPrompt(view, url, message, defaultValue, result);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message,
                                    JsResult result)
    {
        return mWrappedClient.onJsBeforeUnload(view, url, message, result);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("deprecation")
    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                        long currentQuota, long estimatedSize, long totalUsedQuota,
                                        @SuppressWarnings("deprecation") WebStorage.QuotaUpdater quotaUpdater)
    {
        mWrappedClient.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota,
                                               estimatedSize, totalUsedQuota, quotaUpdater);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("deprecation")
    @Override
    public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota,
                                         @SuppressWarnings("deprecation") WebStorage.QuotaUpdater quotaUpdater)
    {
        mWrappedClient
                .onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota, quotaUpdater);
    }

    /** {@inheritDoc} */
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,
                                                   GeolocationPermissions.Callback callback)
    {
        mWrappedClient.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    /** {@inheritDoc} */
    @Override
    public void onGeolocationPermissionsHidePrompt()
    {
        mWrappedClient.onGeolocationPermissionsHidePrompt();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("deprecation")
    @Override
    public boolean onJsTimeout()
    {
        return mWrappedClient.onJsTimeout();
    }

    /** {@inheritDoc} */
    @Override
    @Deprecated
    public void onConsoleMessage(String message, int lineNumber, String sourceID){
        mWrappedClient.onConsoleMessage(message, lineNumber, sourceID);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage){
        return mWrappedClient.onConsoleMessage(consoleMessage);
    }

    /** {@inheritDoc} */
    @Override
    public
    Bitmap getDefaultVideoPoster()
    {
        return mWrappedClient.getDefaultVideoPoster();
    }

    /** {@inheritDoc} */
    @Override
    public View getVideoLoadingProgressView()
    {
        return mWrappedClient.getVideoLoadingProgressView();
    }

    /** {@inheritDoc} */
    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback)
    {
        mWrappedClient.getVisitedHistory(callback);
    }

    /** {@inheritDoc} <3.0*/

    public void openFileChooser(ValueCallback<Uri > uploadFile)
    {
        ((KJWebChromeClient ) mWrappedClient).openFileChooser(uploadFile);
    }
    /** {@inheritDoc} >3.0*/

    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType)
    {
        ((KJWebChromeClient ) mWrappedClient).openFileChooser ( uploadFile, acceptType );
    }

    /** {@inheritDoc} >4.1.1以上*/

    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture)
    {
        ((KJWebChromeClient) mWrappedClient).openFileChooser(uploadFile, acceptType, capture);
    }
}
