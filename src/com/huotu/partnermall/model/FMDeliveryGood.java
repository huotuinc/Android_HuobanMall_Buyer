package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2015/9/19.
 */
public
class FMDeliveryGood extends BaseBean {

    /**
     * @field:serialVersionUID:TODO
     * @since
     */
    private static final long serialVersionUID = 1L;


    public InnerClass getResultData() {
        return resultData;
    }

    public void setResultData(InnerClass resultData) {
        this.resultData = resultData;
    }

    private InnerClass resultData;

    class InnerClass{

    }

}
