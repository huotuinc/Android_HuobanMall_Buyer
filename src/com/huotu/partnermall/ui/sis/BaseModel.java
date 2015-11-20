package com.huotu.partnermall.ui.sis;

import android.content.pm.LauncherActivityInfo;

/**
 * Created by Administrator on 2015/8/31.
 */
public class BaseModel {
    /**
     * 系统状态返回：1，成功;0，失败
      */
    private int systemResultCode;
    /**
     * 成功/失败描述
     */
    private String systemResultDescription;
    /**
     * 逻辑状态返回 ：1成功,0 失败
     */
    private int resultCode;
    /**
     *  逻辑状态描述
     */
    private String resultDescription;

    public void setSystemResultCode(int systemResultCode) {
        this.systemResultCode = systemResultCode;
    }

    public void setSystemResultDescription(String systemResultDescription) {
        this.systemResultDescription = systemResultDescription;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public int getSystemResultCode() {
        return systemResultCode;
    }

    public String getSystemResultDescription() {
        return systemResultDescription;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }


}
