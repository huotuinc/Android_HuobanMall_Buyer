package com.huotu.partnermall.model;

import java.io.Serializable;
import java.util.List;

/**
 * 开机广告类
 * Created by Administrator on 2017/1/10.
 */
public class AdvertiseModel extends DataBase implements Serializable {
    private List<Advertise> data;

    public List<Advertise> getData() {
        return data;
    }

    public void setData(List<Advertise> data) {
        this.data = data;
    }

    public class Advertise implements Serializable{
        private int id;
        private String name;
        private String images;
        private String linkUrl;
        private String customerId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }
    }
}
