package com.huotu.android.library.buyer.bean.GroupBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

import java.util.List;

/**
 * Created by Administrator on 2016/2/19.
 */
public class GoodsGroupConfig extends BaseConfig{
    /**
     * 布局:
     * card –卡片布局
     * normal –方格布局
     */
    private String girdLayout;
    /**
     * 是否显示积分：
     * show –显示积分
     * hide  -不显示
     */
    private String isShowUserInteger;
    /**
     * 分类列表集合
     */
    private List<GroupBean> groups;

    public String getGirdLayout() {
        return girdLayout;
    }

    public void setGirdLayout(String girdLayout) {
        this.girdLayout = girdLayout;
    }

    public String getIsShowUserInteger() {
        return isShowUserInteger;
    }

    public void setIsShowUserInteger(String isShowUserInteger) {
        this.isShowUserInteger = isShowUserInteger;
    }

    public List<GroupBean> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupBean> groups) {
        this.groups = groups;
    }
}
