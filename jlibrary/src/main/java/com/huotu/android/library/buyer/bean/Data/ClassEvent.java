package com.huotu.android.library.buyer.bean.Data;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ClassEvent {
    private String configUrl;
    private int classId;

    public String getConfigUrl() {
        return configUrl;
    }

    public void setConfigUrl(String configUrl) {
        this.configUrl = configUrl;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public ClassEvent(String configUrl , int classId){
        this.configUrl = configUrl;
        this.classId = classId;
    }
}
