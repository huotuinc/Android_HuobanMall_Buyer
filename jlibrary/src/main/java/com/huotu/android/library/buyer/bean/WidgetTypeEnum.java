package com.huotu.android.library.buyer.bean;

/**
 * Created by jinxiangdong on 2016/1/7.
 */
public enum  WidgetTypeEnum {
    /**
     *
     */
    LISTVIEW_THREE(150,"LISTVIEW_THREE"),
    /**
     *
     */
    LISTVIEW_TWO(151,"LISTVIEW_TWO"),
    TEXT_TITLE(100,"TEXT_TITLE"),
    TEXT_RICHTEXT(101,"TEXT_RICHTEXT"),
    TEXT_ARTICLETITLE(102,"TEXT_ARTICLETITLE"),
    TEXT_NAVIGATION(103,"NAVIGATION"),
    TEXT_BILLBOARD(104,"BILLBOARD"),
    AD_WINDOW(200,"AD_WINDOW"),
    AD_ADONE(201,"AD_ADONE"),
    AD_ADBANNER(202,"AD_ADBANNER"),
    AD_ADPAGEBANNER(203,"AD_ADPAGEBANNER"),
    AD_ADAVERAGEBANNER(204,"AD_ADAVERAGEBANNER"),
    AD_ADTHREE(205,"AD_ADTHREE"),
    AD_ADFOUR(206,"AD_ADFOUR"),
    ASSIST_GUIDES2(250,"ASSIST_GUIDES2"),
    ASSIST_GUIDES1(251,"ASSIST_GUIDES1"),
    ASSIST_GUIDESSHOP(252,"ASSIST_GUIDESSHOP"),
    ASSIST_BUTTON(253,"ASSIST_BUTTON"),
    SEARCH_ONE(300,"SEARCH_ONE"),
    SEARCH_TWO(301,"SEARCH_TWO"),
    SHOP_TWO(351 , "SHOP_TWO"),
    SHOP_ONE(350,"SHOP_ONE"),
    SHOP_DEFAULT(352,"SHOP_DEFAULT"),
    SORT_ONE(400,"SORT_ONE"),
    FOOTER_ONE(450,"FOOTER_ONE"),
    GOODS_ONE(500,"GOODS_ONE"),
    GOODS_ONE_CARD(501,"GOODS_ONE_CARD"),
    GOODS_TWO(502,"GOODS_TWO"),
    PROMOTION_ONE(550,"PROMOTION_ONE"),
    GROUP_GOODS(600,"GROUP_GOODS"),
    GROUP_Class(601,"GROUP_CLASS");

    private int index;
    private String name;
    private WidgetTypeEnum(int index , String name){
        this.index = index;
        this.name = name;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
