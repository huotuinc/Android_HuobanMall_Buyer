package com.huotu.android.library.buyer.callback;

import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsListBean;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.LoadCompleteEvent;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.widget.GoodsListWidget.ListViewThreeWidget;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/3/30.
 */
public class ListViewThreeCallback implements Callback<BizBaseBean<GoodsListBean>> {
    WeakReference<ListViewThreeWidget> ref;

    public  ListViewThreeCallback(ListViewThreeWidget widget){
        ref = new WeakReference<ListViewThreeWidget>(widget);
    }

    @Override
    public void onResponse(Response<BizBaseBean<GoodsListBean>> response) {
        if( ref.get()==null) {
            EventBus.getDefault().post(new LoadCompleteEvent());
            return;
        }
        if (response == null || response.code() != Constant.REQUEST_SUCCESS
                || response.body() == null || response.body().getData() == null || response.body().getData().getGoods() == null) {
            Logger.e(response.message());
            EventBus.getDefault().post(new LoadCompleteEvent());
            return;
        }

        if (ref.get().goods == null) {
            ref.get().goods = new ArrayList<>();
        }

        ref.get().goods.addAll(response.body().getData().getGoods());

        ref.get().addItems(response.body().getData());
        ref.get().pageIndex = response.body().getData().getPageIndex();

        EventBus.getDefault().post(new LoadCompleteEvent());
    }

    @Override
    public void onFailure(Throwable t) {
        Logger.e(t.getMessage());
        EventBus.getDefault().post(new LoadCompleteEvent());
    }
}
