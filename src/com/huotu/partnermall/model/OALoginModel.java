package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2017/8/29.
 */

public class OALoginModel extends DataBase {

    private OALogin data;

    public OALogin getData() {
        return data;
    }

    public void setData(OALogin data) {
        this.data = data;
    }

    public class OALogin {
        private String  username;
        private String password;
        private String customerid;
        private long userid;
        private int levelID;
        private int userType;
        private int relatedType;
        private int  coerceloseefficacy;
        private int belongOne;
        private String wxOpenId;
        private String wxNickName;
        private String wxHeadImg;
        private int IsDelete;
        private String wxUnionId;
        private String levelName;
        private String authorizeCode;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCustomerid() {
            return customerid;
        }

        public void setCustomerid(String customerid) {
            this.customerid = customerid;
        }

        public long getUserid() {
            return userid;
        }

        public void setUserid(long userid) {
            this.userid = userid;
        }

        public int getLevelID() {
            return levelID;
        }

        public void setLevelID(int levelID) {
            this.levelID = levelID;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public int getRelatedType() {
            return relatedType;
        }

        public void setRelatedType(int relatedType) {
            this.relatedType = relatedType;
        }

        public int getCoerceloseefficacy() {
            return coerceloseefficacy;
        }

        public void setCoerceloseefficacy(int coerceloseefficacy) {
            this.coerceloseefficacy = coerceloseefficacy;
        }

        public int getBelongOne() {
            return belongOne;
        }

        public void setBelongOne(int belongOne) {
            this.belongOne = belongOne;
        }

        public String getWxOpenId() {
            return wxOpenId;
        }

        public void setWxOpenId(String wxOpenId) {
            this.wxOpenId = wxOpenId;
        }

        public String getWxNickName() {
            return wxNickName;
        }

        public void setWxNickName(String wxNickName) {
            this.wxNickName = wxNickName;
        }

        public String getWxHeadImg() {
            return wxHeadImg;
        }

        public void setWxHeadImg(String wxHeadImg) {
            this.wxHeadImg = wxHeadImg;
        }

        public int getIsDelete() {
            return IsDelete;
        }

        public void setIsDelete(int isDelete) {
            IsDelete = isDelete;
        }

        public String getWxUnionId() {
            return wxUnionId;
        }

        public void setWxUnionId(String wxUnionId) {
            this.wxUnionId = wxUnionId;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getAuthorizeCode() {
            return authorizeCode;
        }

        public void setAuthorizeCode(String authorizeCode) {
            this.authorizeCode = authorizeCode;
        }
    }
}
