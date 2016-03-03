package com.huotu.partnermall.ui.sis;

import java.util.List;

/**
 * Created by Administrator on 2015/11/19.
 */
public class AppSisTemplateListModel extends BaseModel {
    private InnerClass resultData;

    public InnerClass getResultData() {
        return resultData;
    }

    public void setResultData(InnerClass resultData) {
        this.resultData = resultData;
    }

    public class  InnerClass {
        List<SisTemplateListModel> list;
        Long templateid;

        public List<SisTemplateListModel> getList() {
            return list;
        }

        public void setList(List<SisTemplateListModel> list) {
            this.list = list;
        }

        public Long getTemplateid() {
            return templateid;
        }

        public void setTemplateid(Long templateid) {
            this.templateid = templateid;
        }
    }
}
