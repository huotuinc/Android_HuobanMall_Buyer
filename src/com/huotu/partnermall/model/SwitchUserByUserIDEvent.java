package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2016/6/20.
 */
public class SwitchUserByUserIDEvent {
    private String userId;

    public SwitchUserByUserIDEvent(String userId){
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
