package com.huotu.android.library.buyer.widget.GoodsListWidget;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.huotu.android.library.buyer.bean.BizBean.GoodsListBean;
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewOneItemConfig;
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewTwoConfig;
import com.huotu.android.library.buyer.bean.SortBean.SortOneConfig;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.widget.SortWidget.SortOneWidget;
import java.util.ArrayList;
import java.util.List;

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




        if(this.config.isOrderRule()==false) {
            llContent = this;
            return llContent;
        }

        SortOneConfig sortOneConfig =new SortOneConfig();
        SortOneWidget sortOneWidget = new SortOneWidget(getContext(), sortOneConfig);
        this.addView(sortOneWidget);

        llContent = new LinearLayout(getContext());
        this.addView(llContent);

        return llContent;
    }

    private  void addGoodsByPubu(GoodsListBean goods ){
        if( goods ==null|| goods.getGoods()==null|| goods.getGoods().size()<1 ) return;

        int itemWidth = getWidth()/ columnCount;
        for( int i=0;i< goods.getGoods().size();i++) {
            int position = count % columnCount;
            LinearLayout llItem = lls.get(position);
            count++;

            ListViewOneItemConfig listViewOneItemConfig = new ListViewOneItemConfig();
            listViewOneItemConfig.setIsStatic( config.isStatic());
            listViewOneItemConfig.setBindDataID(config.getBindDataID());
            listViewOneItemConfig.setFilterRule(config.isOrderRule());
            listViewOneItemConfig.setGoods_layer(config.getGoods_layer());
            listViewOneItemConfig.setProduct_showname(config.isProduct_showname());
            listViewOneItemConfig.setProduct_showprices(config.isProduct_showprices());
            listViewOneItemConfig.setProduct_userInteger(config.isProduct_userInteger());
            listViewOneItemConfig.setOrderRule(config.isOrderRule());
            listViewOneItemConfig.setPagesize(config.getPagesize());
            listViewOneItemConfig.setBackground(config.getBackground());


            ListViewOneItemWidget oneWidget = new ListViewOneItemWidget( getContext() , listViewOneItemConfig  ,itemWidth);
            llItem.addView( oneWidget );
            oneWidget.addData(goods.getGoods().get(i));
        }
    }

    private  void addGoodsByNormal(GoodsListBean goods){
        if( goods ==null|| goods.getGoods()==null|| goods.getGoods().size()<1 ) return;
        int itemWidth = getWidth()/ columnCount;
        int lineCount = goods.getGoods().size()/columnCount;
        lineCount += goods.getGoods().size() % columnCount>0?1:0;

        for( int i=0;i< lineCount;i++) {
            int position = 0;
            LinearLayout llItem = new LinearLayout(getContext());
            llItem.setOrientation(HORIZONTAL);
            count++;

            ListViewOneItemConfig listViewOneItemConfig = new ListViewOneItemConfig();
            listViewOneItemConfig.setIsStatic( config.isStatic());
            listViewOneItemConfig.setBindDataID(config.getBindDataID());
            listViewOneItemConfig.setFilterRule(config.isOrderRule());
            listViewOneItemConfig.setGoods_layer(config.getGoods_layer());
            listViewOneItemConfig.setProduct_showname(config.isProduct_showname());
            listViewOneItemConfig.setProduct_showprices(config.isProduct_showprices());
            listViewOneItemConfig.setProduct_userInteger(config.isProduct_userInteger());
            listViewOneItemConfig.setOrderRule(config.isOrderRule());
            listViewOneItemConfig.setPagesize(config.getPagesize());
            listViewOneItemConfig.setBackground(config.getBackground());

            ListViewOneItemWidget oneWidget = new ListViewOneItemWidget( getContext() , listViewOneItemConfig  ,itemWidth);
            llItem.addView(oneWidget);
            position = i * columnCount;
            oneWidget.addData(goods.getGoods().get(position));

            position = position + 1;
            if( position < goods.getGoods().size() ) {
                oneWidget = new ListViewOneItemWidget(getContext(), listViewOneItemConfig, itemWidth);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f); //(LinearLayout.LayoutParams)oneWidget.getLayoutParams();

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
}
