package com.huotu.partnermall.adapter;

import android.view.View;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.AuthorizeAdapter;

/**
 *本demo将在授权页面底部显示一个“关注官方微博”的提示框，
 *用户可以在授权期间对这个提示进行控制，选择关注或者不关
 *注，如果用户最后确定关注此平台官方微博，会在授权结束以
 *后执行关注的方法。
 */
public
class ShareAdapter extends AuthorizeAdapter implements View.OnClickListener, PlatformActionListener {


    @Override
    public
    void onClick ( View v ) {

    }

    @Override
    public
    void onComplete ( Platform platform, int i, HashMap< String, Object > hashMap ) {

    }

    @Override
    public
    void onError ( Platform platform, int i, Throwable throwable ) {

    }

    @Override
    public
    void onCancel ( Platform platform, int i ) {

    }
}
