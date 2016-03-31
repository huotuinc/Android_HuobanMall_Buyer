package com.huotu.android.library.buyer.bean.GroupBean;

import com.huotu.android.library.buyer.bean.BaseConfig;

import java.util.List;

/**
 *  分类分组系列组件
 * Created by Administrator on 2016/3/25.
 */
public class ClassGroupConfig extends BaseConfig {
    /**
     * 分类列表集合
     */
    private List<GroupBean> Groups;

    public List<GroupBean> getGroups() {
        return Groups;
    }

    public void setGroups(List<GroupBean> groups) {
        Groups = groups;
    }
}
