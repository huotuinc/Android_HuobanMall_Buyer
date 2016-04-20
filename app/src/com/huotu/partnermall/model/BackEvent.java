package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2016/4/14.
 */
public class BackEvent {
    private boolean isShowBack=false;
    private boolean isShowShare =false;
    public BackEvent(boolean isShowBack, boolean isShowShare){
        this.isShowBack = isShowBack;
        this.isShowShare = isShowShare;
    }

    public boolean isShowBack() {
        return isShowBack;
    }

    public void setShowBack(boolean showBack) {
        isShowBack = showBack;
    }

    public boolean isShowShare() {
        return isShowShare;
    }

    public void setShowShare(boolean showShare) {
        isShowShare = showShare;
    }
}
