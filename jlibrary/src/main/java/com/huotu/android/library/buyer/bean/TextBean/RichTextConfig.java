package com.huotu.android.library.buyer.bean.TextBean;

/**
 * 富文本组件
 * Created by jinxiangdong on 2016/1/7.
 */
public class RichTextConfig extends BaseTextConfig {
    /**
     * 富文本内容(包含html)
     */
    private String content;
    /**
     * 左右距离
     */
    private  int aroundDistance;
    /**
     * 上下距离
     */
    private int vertialDistance;
    /**
     * 背景颜色 #FFFFF格式(Hex color)
     */
    private String widgetBackColor;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAroundDistance() {
        return aroundDistance;
    }

    public void setAroundDistance(int aroundDistance) {
        this.aroundDistance = aroundDistance;
    }

    public int getVertialDistance() {
        return vertialDistance;
    }

    public void setVertialDistance(int vertialDistance) {
        this.vertialDistance = vertialDistance;
    }

    public String getWidgetBackColor() {
        return widgetBackColor;
    }

    public void setWidgetBackColor(String widgetBackColor) {
        this.widgetBackColor = widgetBackColor;
    }
}
