package com.huotu.partnermall.model;

public
class AuthMallModel  {

    private int code;
    private String msg;
    private AuthMall data;

    public
    int getCode ( ) {
        return code;
    }

    public
    void setCode ( int code ) {
        this.code = code;
    }

    public
    String getMsg ( ) {
        return msg;
    }

    public
    void setMsg ( String msg ) {
        this.msg = msg;
    }

    public
    AuthMall getData ( ) {
        return data;
    }

    public
    void setData ( AuthMall data ) {
        this.data = data;
    }

    public class AuthMall
    {
        private int userid;
        private String levelName;

        public
        String getLevelName ( ) {
            return levelName;
        }

        public
        void setLevelName ( String levelName ) {
            this.levelName = levelName;
        }

        public
        int getUserid ( ) {
            return userid;
        }

        public
        void setUserid ( int userid ) {
            this.userid = userid;
        }
    }
}