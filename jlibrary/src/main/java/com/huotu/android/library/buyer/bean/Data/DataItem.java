package com.huotu.android.library.buyer.bean.Data;

import android.view.View;

/**
 * Created by Administrator on 2015/12/28.
 */
public class DataItem {
    private int id;
    private String imageurl;
    private String title;
    private String spec;
    private Double price;
    private double zPrice;
    private String detailurl;
    private double rebate;
    private int widgetType;
    private View view;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDetailurl() {
        return detailurl;
    }

    public void setDetailurl(String detailurl) {
        this.detailurl = detailurl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(int widgetType) {
        this.widgetType = widgetType;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRebate() {
        return rebate;
    }

    public void setRebate(double rebate) {
        this.rebate = rebate;
    }

    public double getzPrice() {
        return zPrice;
    }

    public void setzPrice(double zPrice) {
        this.zPrice = zPrice;
    }
}
