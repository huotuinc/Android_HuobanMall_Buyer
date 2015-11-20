package com.huotu.partnermall.ui.sis;

import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */


public class AppSisSortModel extends BaseModel {
    public InnerClass getResultData() {
        return resultData;
    }

    public void setResultData(InnerClass resultData) {
        this.resultData = resultData;
    }

    private InnerClass resultData;

    public class InnerClass{
        public List<SisSortModel> getList() {
            return list;
        }

        public void setList(List<SisSortModel> list) {
            this.list = list;
        }

        List<SisSortModel> list;
    }
}
