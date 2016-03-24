package com.huotu.android.library.buyer.widget.GoodsListWidget;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huotu.android.library.buyer.BizApiService;
import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsListBean;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.LoadCompleteEvent;
import com.huotu.android.library.buyer.bean.Data.StartLoadEvent;
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewOneItemConfig;
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewTwoConfig;
import com.huotu.android.library.buyer.bean.SortBean.SortOneConfig;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.android.library.buyer.widget.SortWidget.SortOneWidget;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商品列表-二方格布局组件
 * Created by jinxiangdong on 2016/1/11.
 */
public class ListViewTwoWidget extends BaseLinearLayoutWidget implements IListView {
    List<LinearLayout> lls=null;
    int count;
    ListViewTwoConfig config;
    LinearLayout llContent;
    int columnCount=2;
    int pageIndex = 0;
    List<GoodsBean> goods;
    SortOneWidget sortOneWidget;

    public ListViewTwoWidget(Context context , ListViewTwoConfig config ) {
        super(context);

        this.count = 0;
        this.config = config;

        if( config.isStyleLayout() ){
            create_PuBuLayout();
        }else{
            create_NormalLayout();
        }
    }

    protected void create_NormalLayout(){
        this.setOrientation(VERTICAL);
        ViewGroup rootView = addSortWidget();
        ((LinearLayout)rootView).setOrientation(VERTICAL);
    }

    protected void create_PuBuLayout(){
        this.setOrientation( config.isOrderRule() ? VERTICAL : HORIZONTAL);
        ViewGroup rootView = addSortWidget();
        ((LinearLayout)rootView).setOrientation(HORIZONTAL);

        lls = new ArrayList<>();

        for( int i=0;i< columnCount ;i++) {
            LinearLayout llItem = new LinearLayout(getContext());
            rootView.addView(llItem);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            int leftPaddingPx = DensityUtils.dip2px(getContext(), i == 0 ? 4 : 2);
            int rightPaddingPx = DensityUtils.dip2px( getContext() , i == columnCount ? 2 : 4 );
            llItem.setPadding(leftPaddingPx,0,rightPaddingPx , 0 );
            llItem.setOrientation(VERTICAL);
            llItem.setLayoutParams(layoutParams);
            lls.add(llItem);
        }
    }

    protected ViewGroup addSortWidget(){

        if( !this.config.isOrderRule()) {
            llContent = this;
            return llContent;
        }

        SortOneConfig sortOneConfig =new SortOneConfig();
        sortOneWidget = new SortOneWidget(getContext(), sortOneConfig);
        this.addView(sortOneWidget);

        llContent = new LinearLayout(getContext());
        this.addView(llContent);

        return llContent;
    }

    private  void addGoodsByPubu(GoodsListBean goods ){
        if( goods ==null|| goods.getGoods()==null|| goods.getGoods().size()<1 ) return;

        int itemWidth = (getWidth()-2*3)/ columnCount;
        for( int i=0;i< goods.getGoods().size();i++) {
            int position = count % columnCount;
            LinearLayout llItem = lls.get(position);
            count++;

            ListViewOneItemConfig listViewOneItemConfig = new ListViewOneItemConfig();
            listViewOneItemConfig.setIsStatic( config.isStatic());
            listViewOneItemConfig.setBindDataID(config.getBindDataID());
            listViewOneItemConfig.setFilterRule(config.isOrderRule());
            listViewOneItemConfig.setGoods_layer(config.getGoods_layer());
            listViewOneItemConfig.setProduct_showname(config.getProduct_showname());
            listViewOneItemConfig.setProduct_showprices(config.getProduct_showprices());
            listViewOneItemConfig.setProduct_userInteger(config.getProduct_userInteger());
            listViewOneItemConfig.setOrderRule(config.isOrderRule());
            listViewOneItemConfig.setPagesize(config.getPagesize());
            listViewOneItemConfig.setBackground(config.getBackground());

            ListViewOneItemWidget oneWidget = new ListViewOneItemWidget( getContext() , listViewOneItemConfig  ,itemWidth);
            llItem.addView( oneWidget );
            oneWidget.addData(goods.getGoods().get(i));
        }
    }

    private void addGoodsByNormal(GoodsListBean goods){
        if( goods ==null|| goods.getGoods()==null|| goods.getGoods().size()<1 ) return;
        int itemWidth = getWidth()/ columnCount;
        int lineCount = goods.getGoods().size()/columnCount;
        lineCount += goods.getGoods().size() % columnCount>0?1:0;
        int recordConut = goods.getGoods().size();

        for( int i=0;i< lineCount;i++) {
            int position = 0;
            LinearLayout llItem = new LinearLayout(getContext());
            llItem.setOrientation(HORIZONTAL);
            count++;

            ListViewOneItemConfig listViewOneItemConfig = new ListViewOneItemConfig();
            listViewOneItemConfig.setIsStatic(config.isStatic());
            listViewOneItemConfig.setBindDataID(config.getBindDataID());
            listViewOneItemConfig.setFilterRule(config.isFilterRule());
            listViewOneItemConfig.setGoods_layer(config.getGoods_layer());
            listViewOneItemConfig.setProduct_showname(config.getProduct_showname());
            listViewOneItemConfig.setProduct_showprices(config.getProduct_showprices());
            listViewOneItemConfig.setProduct_userInteger(config.getProduct_userInteger());
            listViewOneItemConfig.setOrderRule(config.isOrderRule());
            listViewOneItemConfig.setPagesize(config.getPagesize());
            listViewOneItemConfig.setBackground(config.getBackground());

            ListViewOneItemWidget oneWidget = new ListViewOneItemWidget( getContext() , listViewOneItemConfig  ,itemWidth);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1.0f);
            int bottomPx = 2;
            int topPx=1;
            if( i == 0){ bottomPx=1;topPx = 2;}
            else if( i == (lineCount-1) ){ bottomPx=2;}
            else{topPx=1;bottomPx=1;}
            layoutParams.setMargins(2, topPx , 1 ,bottomPx);
            oneWidget.setLayoutParams(layoutParams);

            llItem.addView(oneWidget);
            position = i * columnCount;
            oneWidget.addData(goods.getGoods().get(position));

            position = position + 1;
            if( position < goods.getGoods().size() ) {
                oneWidget = new ListViewOneItemWidget(getContext(), listViewOneItemConfig, itemWidth);
                layoutParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f); //(LinearLayout.LayoutParams)oneWidget.getLayoutParams();
                topPx = 1;
                bottomPx=2;
                if(i==0){topPx=2;bottomPx=1;}
                else if(i==(lineCount-1)){bottomPx=2;}
                else{topPx=1;bottomPx=1;}
                layoutParams.setMargins(1,topPx,2,bottomPx);
                oneWidget.setLayoutParams(layoutParams);

                llItem.addView(oneWidget);
                oneWidget.addData(goods.getGoods().get(position));
            }

            llContent.addView(llItem);
        }
    }

    public void addItems( GoodsListBean goods ){
        if(config.isStyleLayout()){
            addGoodsByPubu( goods );
        }else{
            addGoodsByNormal( goods );
        }
    }

    @Override
    public void asyncGetGoodsData( boolean isRefreshData , int classid ,String keyword ) {
        if( isRefreshData ){
            pageIndex = 0;
            count=0;
            if( goods !=null) goods.clear();
            if( config.isStyleLayout() ){
                if(lls!=null){
                    for(int i=0;i<lls.size();i++){
                        lls.get(i).removeAllViews();
                    }
                }
            }else {
                if (llContent != null) llContent.removeAllViews();
            }
        }

        BizApiService bizApiService = RetrofitUtil.getBizRetroftInstance(Variable.BizRootUrl).create(BizApiService.class);
        int customerId= Variable.CustomerId;
        int catid = classid;
        int userlevelid = Variable.userLevelId;
        String sortRule = "0:desc";
        String filter= "";
        String searchKey = keyword;
        int pIndex = pageIndex+1;
        if( config.isOrderRule() ){
            sortRule = sortOneWidget.getSortRule();
            filter = sortOneWidget.getFilter();
        }
        int pageSize = config.getPagesize();

        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);


        Call<BizBaseBean<GoodsListBean>> call = bizApiService.getGoodsList(
            key, random,secure, customerId , catid,userlevelid,sortRule,searchKey,filter, pIndex , pageSize
        );

        call.enqueue(new Callback<BizBaseBean<GoodsListBean>>() {
            @Override
            public void onResponse(Response<BizBaseBean<GoodsListBean>> response) {
                if( response ==null || response.code() != Constant.REQUEST_SUCCESS ){
                    Logger.e( response.message());
                    EventBus.getDefault().post(new LoadCompleteEvent());
                    return;
                }
                if( response.body()==null || response.body().getData()==null){
                    EventBus.getDefault().post(new LoadCompleteEvent());
                    return;
                }

                if( goods ==null ){
                    goods=new ArrayList<GoodsBean>();
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
