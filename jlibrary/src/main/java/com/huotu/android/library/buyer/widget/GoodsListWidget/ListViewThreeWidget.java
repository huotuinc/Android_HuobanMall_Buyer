package com.huotu.android.library.buyer.widget.GoodsListWidget;

import android.content.Context;

import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsListBean;
import com.huotu.android.library.buyer.bean.Data.DataItem;
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewThreeConfig;
import com.huotu.android.library.buyer.bean.SortBean.SortOneConfig;
import com.huotu.android.library.buyer.widget.SortWidget.SortOneWidget;
import com.huotu.android.library.buyer.widget.GoodsListWidget.BaseLinearLayoutWidget;
import com.huotu.android.library.buyer.widget.GoodsListWidget.IListView;
import com.huotu.android.library.buyer.widget.GoodsListWidget.ListViewThreeItemWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
public class ListViewThreeWidget extends BaseLinearLayoutWidget implements IListView {
    ListViewThreeConfig listViewThreeConfig;

    public ListViewThreeWidget(Context context , ListViewThreeConfig listViewThreeConfig) {
        super(context);

        this.listViewThreeConfig = listViewThreeConfig;
        this.setOrientation( VERTICAL);

        addSortWidget();
    }

    protected void addSortWidget(){
        if( !this.listViewThreeConfig.isOrderRule() ) return;

        SortOneConfig sortOneConfig =new SortOneConfig();
        sortOneConfig.setFilterRule( listViewThreeConfig.getListViewThreeItemConfig().isFilterRule() );
        SortOneWidget sortOneWidget = new SortOneWidget(getContext(), sortOneConfig);
        this.addView(sortOneWidget);
    }


    public void addItems( GoodsListBean goods ){
        if( goods ==null || goods.getGoods() ==null || goods.getGoods().size()<1) return;

        int total = goods.getGoods().size();
        int itemCount = total /3;
        itemCount += total %3 > 0 ? 1:0;

        for(int i=0;i<itemCount;i++) {
            ListViewThreeItemWidget widget = new ListViewThreeItemWidget(getContext(),listViewThreeConfig.getListViewThreeItemConfig());
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
}
