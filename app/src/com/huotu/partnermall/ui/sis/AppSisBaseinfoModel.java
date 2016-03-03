package com.huotu.partnermall.ui.sis;

/**
 * Created by Administrator on 2015/11/18.
 */
public class AppSisBaseinfoModel extends  BaseModel {
    public InnerClass getResultData() {
        return resultData;
    }

    public void setResultData(InnerClass resultData) {
        this.resultData = resultData;
    }

    InnerClass resultData;

    public class InnerClass{
        SisBaseinfoModel data;

        public SisBaseinfoModel getData() {
            return data;
        }

        public void setData(SisBaseinfoModel data) {
            this.data = data;
        }
    }
}
