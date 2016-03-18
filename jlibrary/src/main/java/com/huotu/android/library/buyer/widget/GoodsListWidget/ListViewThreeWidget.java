package com.huotu.android.library.buyer.widget.GoodsListWidget;

import android.content.Context;
import android.widget.Toast;

import com.huotu.android.library.buyer.BizApiService;
import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsListBean;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.DataItem;
import com.huotu.android.library.buyer.bean.Data.LoadCompleteEvent;
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewThreeConfig;
import com.huotu.android.library.buyer.bean.SortBean.SortOneConfig;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.android.library.buyer.widget.SortWidget.SortOneWidget;
import com.huotu.android.library.buyer.widget.GoodsListWidget.BaseLinearLayoutWidget;
import com.huotu.android.library.buyer.widget.GoodsListWidget.IListView;
import com.huotu.android.library.buyer.widget.GoodsListWidget.ListViewThreeItemWidget;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商品列表-三方格组件
 * Created by jinxiangdong on 2016/1/11.
 */
public class ListViewThreeWidget extends BaseLinearLayoutWidget implements IListView {
    ListViewThreeConfig listViewThreeConfig;
    int pageIndex = 0;
    SortOneWidget sortOneWidget;
    List<GoodsBean> goods;

    public ListViewThreeWidget(Context context , ListViewThreeConfig listViewThreeConfig) {
        super(context);

        this.listViewThreeConfig = listViewThreeConfig;
        this.setOrientation( VERTICAL);

        addSortWidget();
    }

    protected void addSortWidget(){
        if( !this.listViewThreeConfig.isOrderRule() ) return;

        SortOneConfig sortOneConfig =new SortOneConfig();
        sortOneConfig.setFilterRule(listViewThreeConfig.isFilterRule());
        sortOneWidget = new SortOneWidget(getContext(), sortOneConfig);
        this.addView(sortOneWidget);
    }

    public void addItems( GoodsListBean goods ){
        if( goods ==null || goods.getGoods() ==null || goods.getGoods().size()<1) return;

        int total = goods.getGoods().size();
        int itemCount = total /3;
        itemCount += total %3 > 0 ? 1:0;

        for(int i=0;i<itemCount;i++) {
            ListViewThreeItemWidget widget = new ListViewThreeItemWidget(getContext(),listViewThreeConfig);
            List<GoodsBean> child = new ArrayList<>();
            for(int k = 0; k<3; k++) {
                int pos = i * 3 + k;
                if( (pos +1 ) >= total ) break;
                child.add(goods.getGoods().get(pos));
            }
            widget.addItem(child);
            this.addView(widget);
        }
    }

    @Override
    public void asyncGetGoodsData( boolean isRefreshData ) {
        if( isRefreshData) {
            pageIndex= 0;
            if( goods !=null) goods.clear();
            int count = this.getChildCount();
            int startidx = 0;
            if( sortOneWidget!=null) {
                startidx = this.indexOfChild(sortOneWidget)+1;
                count--;
            }
            if( count >0 )  this.removeViews(startidx, count);
        }

        BizApiService bizApiService = RetrofitUtil.getBizRetroftInstance(Variable.BizRootUrl).create(BizApiService.class);
        int customerid= Variable.CustomerId;
        int catid = 0;
        int userlevelid = Variable.userLevelId;
        String sortRule ="0:desc";
        String filter= "";
        String searchKey = "";
        int pIndex = pageIndex+1;
        if( listViewThreeConfig.isOrderRule() ){
            sortRule = sortOneWidget.getSortRule();
            filter = sortOneWidget.getFilter();
        }
        int pageSize = listViewThreeConfig.getPagesize();

        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);

        Call<BizBaseBean<GoodsListBean>> call = bizApiService.getGoodsList(
                key, random, secure, customerid, catid, userlevelid, sortRule, searchKey, filter, pIndex, pageSize
        );

        call.enqueue(new Callback<BizBaseBean<GoodsListBean>>() {
            @Override
            public void onResponse(Response<BizBaseBean<GoodsListBean>> response) {
                if( response ==null || response.code() != Constant.REQUEST_SUCCESS
                        || response.body() ==null || response.body().getData()==null || response.body().getData().getGoods()==null ){
                    Logger.e(response.message());
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(new LoadCompleteEvent());
                    return;
                }

                if( goods ==null ){
                    goods=new ArrayList<>();
                }

                goods.addAll( response.body().getData().getGoods() );

                addItems(response.body().getData());
                pageIndex = response.body().getData().getPageIndex();

                EventBus.getDefault().post(new LoadCompleteEvent());
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t.getMessage());
                EventBus.getDefault().post(new LoadCompleteEvent());
            }
        });
    }
}
