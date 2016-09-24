package com.huotu.partnermall.model;

/**
 * 会员等级
 */
public class MemberModel {

    private int code;
    private String msg;
    private MemberInfo data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MemberInfo getData() {
        return data;
    }

    public void setData(MemberInfo data) {
        this.data = data;
    }

    public class MemberInfo {
        private String levelName;

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }
    }
}
