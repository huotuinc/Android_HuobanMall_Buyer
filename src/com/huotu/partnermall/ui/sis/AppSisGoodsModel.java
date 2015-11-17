package com.huotu.partnermall.ui.sis;

import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */
public class AppSisGoodsModel extends BaseModel{
    public InnerClass getResultData() {
        return resultData;
    }

    public void setResultData(InnerClass resultData) {
        this.resultData = resultData;
    }

    public InnerClass resultData;


    public class InnerClass{
        private List<SisGoodsModel> list;
        private Integer rPageNo;

        public List<SisGoodsModel> getList() {
            return list;
        }

        public void setList(List<SisGoodsModel> list) {
            this.list = list;
        }

        public Integer getrPageNo() {
            return rPageNo;
        }

        public void setrPageNo(Integer rPageNo) {
            this.rPageNo = rPageNo;
        }
    }

}
