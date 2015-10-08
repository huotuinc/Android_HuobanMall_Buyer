package com.huotu.partnermall.model;

import android.webkit.JavascriptInterface;

import com.huotu.partnermall.BaseApplication;

/**
 * js交互对象
 */
public
class JSModel {

    private
    BaseApplication application;

    public JSModel(BaseApplication application)
    {
        this.application = application;
    }

    @JavascriptInterface
    public void obtainMenuStatus(String status)
    {
            application.isMenuHide = Boolean.parseBoolean ( status );
    }
}
