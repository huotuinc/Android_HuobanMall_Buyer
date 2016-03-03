package com.huotu.android.library.buyer.bean;

/**
 * Created by Administrator on 2016/1/7.
 */
public enum AlignEnum {
    LEFT(1,"LEFT"),
    MIDDLE(2,"MIDDLE"),
    RIGHT(3,"RIGHT");

    private int index;
    private String name;
    private AlignEnum(int index , String name){
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return String.valueOf( this.getIndex());
    }
}
