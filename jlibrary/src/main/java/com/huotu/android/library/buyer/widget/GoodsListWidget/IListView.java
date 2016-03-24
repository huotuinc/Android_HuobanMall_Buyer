package com.huotu.android.library.buyer.widget.GoodsListWidget;


import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsListBean;
import com.huotu.android.library.buyer.bean.Data.DataItem;

import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
public interface IListView {
     void addItems(GoodsListBean data);

     void asyncGetGoodsData( boolean isRefreshData , int classificationid , String keyword);
}
