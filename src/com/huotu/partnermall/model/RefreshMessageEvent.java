package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2017/1/11.
 */

public class RefreshMessageEvent  {
    private boolean hasMessage;

    public RefreshMessageEvent(boolean hasMessage){
        this.hasMessage = hasMessage;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }
}
