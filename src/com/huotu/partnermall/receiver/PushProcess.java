package com.huotu.partnermall.receiver;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.huotu.android.library.libpush.PushMessage;
import com.huotu.android.library.libpush.PushMessageActiveMessage;
import com.huotu.android.library.libpush.PushMessageDownPaySuccess;
import com.huotu.android.library.libpush.PushMessageDownRegisterSuccess;
import com.huotu.android.library.libpush.PushMessageGetRedPackets;
import com.huotu.android.library.libpush.PushMessageGetStock;
import com.huotu.android.library.libpush.PushMessageOrderShip;
import com.huotu.android.library.libpush.PushMessagePaySuccess;
import com.huotu.android.library.libpush.PushMessageSendRedPackets;
import com.huotu.android.library.libpush.PushMessageType;
import com.huotu.android.library.libpush.PushMessageUpgradeDreamPartner;
import com.huotu.android.library.libpush.PushMessageUpgradePartner;
import com.huotu.android.library.libpush.PushMessageWithdrawApply;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.widgets.TipAlertDialog;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/4/28.
 */
public class PushProcess {

    public static void process(Context context , Bundle bundle ){
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Map data=JSONUtil.getGson().fromJson(json, Map.class);
        String type="";
        if(data.containsKey("type")){
            type = data.get("type").toString();
        }
        if(TextUtils.isEmpty(type)){
            type="";
        }

        if( type.equals( PushMessageType.DownPaySuccess.getName())){
            PushMessageDownPaySuccess model = JSONUtil.getGson().fromJson(json, PushMessageDownPaySuccess.class);
            process( context ,title,content,model);
        }else if( type.equals( PushMessageType.DownRegisterSuccess.getName())){
            PushMessageDownRegisterSuccess model = JSONUtil.getGson().fromJson(json,PushMessageDownRegisterSuccess.class);
            process(context ,title,content,model);
        }else if(type.equals(PushMessageType.GetRedPackets.getName())){
            PushMessageGetRedPackets model = JSONUtil.getGson().fromJson(json, PushMessageGetRedPackets.class);
            process(context ,title,content,model);
        }else if(type.equals(  PushMessageType.GetStock.getName())){
            PushMessageGetStock model = JSONUtil.getGson().fromJson(json,PushMessageGetStock.class);
            process(context ,title,content,model);
        }else if(type.equals( PushMessageType.OrderShip.getName() )){
            PushMessageOrderShip model = JSONUtil.getGson().fromJson(json,PushMessageOrderShip.class);
            process(context ,title,content,model);
        }else if(type.equals( PushMessageType.PaySuccess.getName() )){
            PushMessagePaySuccess model = JSONUtil.getGson().fromJson(json,PushMessagePaySuccess.class);
            process(context ,title,content,model);
        }else if( type.equals( PushMessageType.SendRedPackets.getName())){
            PushMessageSendRedPackets model = JSONUtil.getGson().fromJson(json,PushMessageSendRedPackets.class);
            process(context ,title,content,model);
        }else if( type.equals( PushMessageType.UpgradeDreamPartner.getName())){
            PushMessageUpgradeDreamPartner model = JSONUtil.getGson().fromJson(json,PushMessageUpgradeDreamPartner.class);
            process(context ,title,content,model);
        }else if(type.equals( PushMessageType.UpgradePartner.getName())){
            PushMessageUpgradePartner model = JSONUtil.getGson().fromJson(json,PushMessageUpgradePartner.class);
            process(context ,title,content,model);
        }else if( type.equals( PushMessageType.WithdrawApply.getName() )){
            PushMessageWithdrawApply model = JSONUtil.getGson().fromJson(json, PushMessageWithdrawApply.class);
            process(context , title,content, model );
        }else if( type.equals( PushMessageType.ActiveMessage.getName())) {
            PushMessageActiveMessage model = JSONUtil.getGson().fromJson(json, PushMessageActiveMessage.class);
            process(context, title, content, model);
        }else{
            PushMessage model = JSONUtil.getGson().fromJson(json, PushMessage.class);
            process(context,title,content,model);
        }
    }

    protected static void process(Context context , String title , String content , PushMessage msg){
        if(null==msg) return;

        if( !TextUtils.isEmpty( msg.getAlertUrl())){
            TipAlertDialog tipAlertDialog = new TipAlertDialog(context,false);
            tipAlertDialog.show( title , content , msg.getAlertUrl() );
            return;
        }else if(!TextUtils.isEmpty(msg.getUrl())){
            TipAlertDialog tipAlertDialog = new TipAlertDialog(context,false);
            tipAlertDialog.show( title , content , msg.getUrl() );
            return;
        }else {
            TipAlertDialog tipAlertDialog = new TipAlertDialog(context,false);
            tipAlertDialog.show(title , content , "");
            return;
        }
    }

}
