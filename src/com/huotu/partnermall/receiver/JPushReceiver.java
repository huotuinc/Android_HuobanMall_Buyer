package com.huotu.partnermall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huotu.partnermall.BaseApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2015/8/28.
 */
public
class JPushReceiver extends BroadcastReceiver
{
    private static final String TAG = "JPush";

    public BaseApplication application;

    private String imei = null;

    private String regId = null;

    @Override
    public
    void onReceive ( final Context context, Intent intent ) {

        Bundle bundle = intent.getExtras ( );
        if ( JPushInterface.ACTION_REGISTRATION_ID.equals ( intent.getAction ( ) ) ) {
            // 注册后获取极光全局ID
            regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            imei = application.getPhoneIMEI(context);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                                                                         .getAction()))
        {
            // 处理接受的自定义消息
            int notifactionId = bundle
                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                                                                              .getAction()))
        {
            // 接收到了自定义的通知
            // 通知ID

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                                                                            .getAction()))
        {
            // 接受点击通知事件

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                                                                          .getAction()))
        {
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..
            // 接受富文本框
            int notifactionId = bundle
                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                                                                          .getAction()))
        {
            boolean connected = intent.getBooleanExtra(
                    JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            // 处理网络变更事件

        } else
        {

        }
    }
}

