package com.huotu.partnermall.ui.sis;

/**
 * Created by Administrator on 2015/11/13.
 */
public class SisBaseinfoModel {
    String imgUrl;
    String indexUrl;
    String title;
    Long userId;
    Long sisId;
    boolean enableSis;
    String shareTitle;
    String shareDescription;
    String sisDescription;

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getSisDescription() {
        return sisDescription;
    }

    public void setSisDescription(String sisDescription) {
        this.sisDescription = sisDescription;
    }

    public String getShareDescription() {
        return shareDescription;
    }

    public void setShareDescription(String shareDescription) {
        this.shareDescription = shareDescription;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isEnableSis() {
        return enableSis;
    }

    public void setEnableSis(boolean enableSis) {
        this.enableSis = enableSis;
    }

    public Long getSisId() {
        return sisId;
    }

    public void setSisId(Long sisId) {
        this.sisId = sisId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
