package com.huotu.partnermall.ui.sis;

/**
 * Created by Administrator on 2015/11/17.
 */
public enum EditSetTypeEnum {
    SHOPNAME("店铺名称",0),
    LOGO("店铺LOGO",1),
    SHARETITLE("分享标题",2),
    SHAREDESCRIPTION("分享描述",3),
    SHOPDESCRIPTION("店铺描述",4);


    private EditSetTypeEnum(String name , int index){
        this.name=name;
        this.index=index;
    }

    public static EditSetTypeEnum valueOf(int value){
        switch (value){
            case 0:
                return EditSetTypeEnum.SHOPNAME;
            case 1:
                return EditSetTypeEnum.LOGO;
            case 2:
                return EditSetTypeEnum.SHARETITLE;
            case 3:
                return EditSetTypeEnum.SHAREDESCRIPTION;
            case 4:
                return EditSetTypeEnum.SHOPDESCRIPTION;
            default:
                return EditSetTypeEnum.SHOPNAME;
        }
    }

    // 普通方法
    public static String getName(int index) {
        for (EditSetTypeEnum c : EditSetTypeEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

}


