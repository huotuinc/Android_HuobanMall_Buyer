package com.huotu.partnermall.ui.login;

import android.os.Handler;

import com.huotu.partnermall.config.Constants;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 微信登录回调
 */
public
class WXLoginCallback implements PlatformActionListener {

    private
    Handler handler;
    public WXLoginCallback(Handler handler)
    {
        this.handler = handler;
    }

    @Override
    public
    void onComplete ( Platform platform, int action, HashMap< String, Object > hashMap ) {

        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage( Constants.MSG_AUTH_COMPLETE );
        }
    }

    @Override
    public
    void onError ( Platform platform, int action, Throwable throwable ) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage ( Constants.MSG_AUTH_ERROR);
        }
    }

    @Override
    public
    void onCancel ( Platform platform, int action ) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(Constants.MSG_AUTH_CANCEL);
        }
    }
}
