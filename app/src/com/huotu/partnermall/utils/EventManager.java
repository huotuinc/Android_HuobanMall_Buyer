package com.huotu.partnermall.utils;

import android.content.Context;
import android.content.Intent;

import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.partnermall.ui.ClassActivity;
import com.huotu.partnermall.ui.WebViewActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * Created by jinxiangdong on 2016/1/28.
 */
public class EventManager {
    private Context context;

    public EventManager(Context context){
        this.context = context;
    }

    public void Register(){
        if( !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
    public void UnRegister(){
        if( EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onLinkEvnent(LinkEvent event){
        //Toast.makeText(context, event.getUrl(),Toast.LENGTH_LONG).show();
        if( event.getLinkName()!=null && event.getLinkName().equals("分类") ){
            Intent intent = new Intent(context,ClassActivity.class);
            context.startActivity(intent);
        }else {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", event.getLinkUrl());
            context.startActivity(intent);
        }
    }

}
