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
        private int rpageno;
        private Long sisuptotal;
        private Long sisouttotal;

        public Long getSisuptotal() {
            return sisuptotal;
        }

        public void setSisuptotal(Long sisuptotal) {
            this.sisuptotal = sisuptotal;
        }

        public Long getSisouttotal() {
            return sisouttotal;
        }

        public void setSisouttotal(Long sisouttotal) {
            this.sisouttotal = sisouttotal;
        }

        public List<SisGoodsModel> getList() {
            return list;
        }

        public void setList(List<SisGoodsModel> list) {
            this.list = list;
        }

        public int getRpageno() {
            return rpageno;
        }

        public void setRpageno(int rpageno) {
            this.rpageno = rpageno;
        }
    }

}
