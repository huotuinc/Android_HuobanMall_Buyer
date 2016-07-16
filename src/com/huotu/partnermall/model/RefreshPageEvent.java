package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2016/6/20.
 */
public class RefreshPageEvent {
    private boolean isRefreshMainUI;
    private boolean isRefreshPageViewActivity;

    public RefreshPageEvent(boolean isRefreshMainUI , boolean isRefreshPageViewActivity){
        this.isRefreshMainUI = isRefreshMainUI;
        this.isRefreshPageViewActivity=isRefreshPageViewActivity;
    }

    public boolean isRefreshMainUI() {
        return isRefreshMainUI;
    }

    public void setRefreshMainUI(boolean refreshMainUI) {
        isRefreshMainUI = refreshMainUI;
    }

    public boolean isRefreshPageViewActivity() {
        return isRefreshPageViewActivity;
    }

    public void setRefreshPageViewActivity(boolean refreshPageViewActivity) {
        isRefreshPageViewActivity = refreshPageViewActivity;
    }
}
