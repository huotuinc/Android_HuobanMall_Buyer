package com.huotu.android.library.buyer.widget.GoodsListWidget;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsListBean;
import com.huotu.android.library.buyer.bean.Data.DataItem;
import com.huotu.android.library.buyer.bean.GoodsListBean.PubuConfig;
import com.huotu.android.library.buyer.bean.SortBean.SortOneConfig;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.widget.SortWidget.SortOneWidget;
import com.huotu.android.library.buyer.widget.GoodsListWidget.BaseLinearLayoutWidget;
import com.huotu.android.library.buyer.widget.GoodsListWidget.IListView;
import com.huotu.android.library.buyer.widget.GoodsListWidget.ListViewOneItemWidget;
import com.huotu.android.library.buyer.widget.SortWidget.SortOneWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
public class PubuWidget extends BaseLinearLayoutWidget implements IListView {
    List<LinearLayout> lls=null;
    int count;
    PubuConfig pubuConfig;
    LinearLayout llContent;

    public PubuWidget(Context context , PubuConfig pubuConfig ) {
        super(context);

        this.count = 0;
        this.pubuConfig = pubuConfig;
        this.setOrientation(pubuConfig.isOrderRule() ? VERTICAL : HORIZONTAL);

        ViewGroup view = addSortWidget();

        lls = new ArrayList<>();

        for( int i=0;i< pubuConfig.getColumnCount();i++) {
            LinearLayout llItem = new LinearLayout(context);
            view.addView(llItem);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            int leftPaddingPx = DensityUtils.dip2px(getContext(), i == 0 ? 4 : 2);
            int rightPaddingPx = DensityUtils.dip2px( getContext() , i == pubuConfig.getColumnCount() ? 2 : 4 );
            llItem.setPadding(leftPaddingPx,0,rightPaddingPx , 0 );
            llItem.setOrientation(VERTICAL);
            llItem.setLayoutParams(layoutParams);
            lls.add(llItem);
        }
    }

    protected ViewGroup addSortWidget(){
        if(this.pubuConfig.isOrderRule()==false) return this;

        SortOneConfig sortOneConfig =new SortOneConfig();
        SortOneWidget sortOneWidget = new SortOneWidget(getContext(), sortOneConfig);
        this.addView(sortOneWidget);

        llContent = new LinearLayout(getContext());
        this.addView(llContent);

        return llContent;
    }


    public void addItems( GoodsListBean goods ){
        if( goods==null|| goods.getGoods()==null ||goods.getGoods().size()<1 ) return;

        int itemWidth = getWidth()/pubuConfig.getColumnCount();
        for( int i=0;i< goods.getGoods().size();i++) {
            int position = count % pubuConfig.getColumnCount();
            LinearLayout llItem = lls.get(position);
            count++;

            ListViewOneItemWidget oneWidget = new ListViewOneItemWidget( getContext() ,  pubuConfig.getListViewOneItemConfig() ,itemWidth);
            llItem.addView( oneWidget );
            oneWidget.addData( goods.getGoods().get(i) );
        }
    }
}
