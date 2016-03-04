package com.huotu.android.library.buyer.bean.TextBean;

import java.util.List;

/**
 * 文本导航组件
 * Created by jinxiangdong on 2016/1/7.
 * Done
 */
public class NavigationConfig {
    /**
     * 多条导航信息。
     */
    private List<LinkConfig> links;

    public List<LinkConfig> getLinks() {
        return links;
    }

    public void setLinks(List<LinkConfig> links) {
        this.links = links;
    }

}
