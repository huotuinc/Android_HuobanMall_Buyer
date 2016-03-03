package com.huotu.android.library.buyer.bean.GoodsListBean;

/**
 * Created by Administrator on 2016/1/11.
 */
public class PubuConfig extends  BaseListConfig {
    private ListViewOneItemConfig listViewOneItemConfig;
    private int columnCount = 2;

    public ListViewOneItemConfig getListViewOneItemConfig() {
        return listViewOneItemConfig;
    }

    public void setListViewOneItemConfig(ListViewOneItemConfig listViewOneItemConfig) {
        this.listViewOneItemConfig = listViewOneItemConfig;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }
}
