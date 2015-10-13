package com.huotu.partnermall.model;

import java.io.Serializable;

/**
 * 绑定微信账户信息
 */
public
class AccountModel implements Serializable {

    //微信用户ID
    private String accountId;
    //微信用户名称
    private String accountName;
    //微信用户头像
    private String accountIcon;
    //用户Token
    private String accountToken;
    //用户unionid
    private String accountUnionId;

    public
    String getAccountUnionId ( ) {
        return accountUnionId;
    }

    public
    void setAccountUnionId ( String accountUnionId ) {
        this.accountUnionId = accountUnionId;
    }

    public
    String getAccountToken ( ) {
        return accountToken;
    }

    public
    void setAccountToken ( String accountToken ) {
        this.accountToken = accountToken;
    }

    public
    String getAccountId ( ) {
        return accountId;
    }

    public
    void setAccountId ( String accountId ) {
        this.accountId = accountId;
    }

    public
    String getAccountName ( ) {
        return accountName;
    }

    public
    void setAccountName ( String accountName ) {
        this.accountName = accountName;
    }

    public
    String getAccountIcon ( ) {
        return accountIcon;
    }

    public
    void setAccountIcon ( String accountIcon ) {
        this.accountIcon = accountIcon;
    }
}
