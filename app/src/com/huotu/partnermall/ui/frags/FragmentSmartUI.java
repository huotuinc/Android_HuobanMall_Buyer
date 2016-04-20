package com.huotu.partnermall.ui.frags;

import android.os.Bundle;
import com.huotu.android.library.buyer.bean.Data.HeaderEvent;
import com.huotu.android.library.buyer.bean.Data.StartLoadEvent;
import com.huotu.partnermall.model.BackEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2016/4/13.
 */
public class FragmentSmartUI extends FragmentIndex {
    public FragmentSmartUI(){}

    public static FragmentSmartUI newInstance() {
        FragmentSmartUI fragment = new FragmentSmartUI();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new BackEvent(true,false));
    }

    @Override
    public void setUrl(String url) {
        smartuiConfigUrl = url;
        smartuiConfigUrl = smartuiConfigUrl.endsWith("/")? smartuiConfigUrl: smartuiConfigUrl+"/";
        getSmartUIConfig();
    }

    @Override
    public void setParameter(String smartUrl , int classId , String keyword){
        smartuiConfigUrl = smartUrl;
        smartuiConfigUrl = smartuiConfigUrl.endsWith("/")? smartuiConfigUrl: smartuiConfigUrl+"/";
        this.classid = classId;
        this.keyword = keyword;
        getSmartUIConfig();
    }

    @Override
    public void refreshTitle(){
        if( pageConfig==null) return;
        EventBus.getDefault().post( new HeaderEvent( pageConfig.getTitle() ));
        EventBus.getDefault().post(new BackEvent( true , false ));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshData(StartLoadEvent event) {
        //if (listView != null) {
        //    showProgress("正在加载数据...");
        //}

        asyncGetGoods(true, classid);
    }
}
