package com.huotu.partnermall.ui.login;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.model.AccountModel;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;

/**
 * 微信授权实现类
 */
public
class AutnLogin {

    private
    Context context;
    private
    Handler mHandler;

    public AutnLogin(Context context, Handler mHandler)
    {
        this.context = context;
        this.mHandler = mHandler;
    }

    public void authorize(Platform plat)
    {
        if(plat.isValid ()) {
            String userId = plat.getDb().getUserId();
            if (! TextUtils.isEmpty ( userId )) {
                mHandler.sendEmptyMessage ( Constants.MSG_USERID_FOUND );
                login(plat);
                return;
            }
            else
            {
                mHandler.sendEmptyMessage ( Constants.MSG_USERID_NO_FOUND );
                return;
            }
        }
        plat.setPlatformActionListener ( new PlatformActionListener ( ) {
                                             @Override
                                             public
                                             void onComplete ( Platform platform, int action, HashMap< String, Object > hashMap ) {

                                                 if ( action == Platform.ACTION_USER_INFOR ) {
                                                     Message msg = new Message();
                                                     msg.what = Constants.MSG_AUTH_COMPLETE;
                                                     msg.obj = platform;
                                                     mHandler.sendMessage ( msg );
                                                 }
                                             }

                                             @Override
                                             public
                                             void onError ( Platform platform, int action, Throwable throwable ) {
                                                 if (action == Platform.ACTION_USER_INFOR) {
                                                     mHandler.sendEmptyMessage ( Constants.MSG_AUTH_ERROR );
                                                 }
                                             }

                                             @Override
                                             public
                                             void onCancel ( Platform platform, int action ) {
                                                 if (action == Platform.ACTION_USER_INFOR) {
                                                     mHandler.sendEmptyMessage(Constants.MSG_AUTH_CANCEL );
                                                 }
                                             }
                                         } );
        plat.SSOSetting(false);
        plat.showUser ( null );
    }

    private void login(Platform plat) {
        Message msg = new Message();
        msg.what = Constants.MSG_LOGIN;

        PlatformDb   accountDb = plat.getDb ();
        AccountModel account = new AccountModel ();
        account.setAccountId ( accountDb.getUserId () );
        account.setAccountName ( accountDb.getUserName ( ) );
        account.setAccountIcon ( accountDb.getUserIcon ( ) );
        account.setAccountToken ( accountDb.getToken ( ) );

        msg.obj = account;
        mHandler.sendMessage ( msg );
    }
}
