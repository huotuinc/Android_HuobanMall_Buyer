package com.huotu.partnermall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huotu.android.library.libpush.PushReceiver;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.ui.PushHandlerActivity;

/**
 *
 * Created by Administrator on 2016/4/28.
 */
public class PushBusinessReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if( action.equals(PushReceiver.ACTION_PUSH_BUSINESS)){
            dealMessage(context , intent);
        }
    }

    protected void dealMessage(Context context , Intent intent){
        Bundle bd = intent.getExtras();
        if( bd == null) return;
//        String content = bd.getString(JPushInterface.EXTRA_ALERT);
//        String title = bd.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//        String json = bd.getString(JPushInterface.EXTRA_EXTRA);

//        ToastUtils.showLongToast(title+ " " +content+ " " + json );

        Intent actionIntent = new Intent();
        actionIntent.putExtra(Constants.HUOTU_PUSH_KEY , bd);
        actionIntent.setClass(context , PushHandlerActivity.class);
        //actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(actionIntent);
    }
}
