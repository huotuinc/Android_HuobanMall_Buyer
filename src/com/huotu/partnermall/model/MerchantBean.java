package com.huotu.partnermall.model;

/**
 * 商户信息bean
 */
public class MerchantBean extends BaseBean {
    //商家ID
    private String merchantId;
    //商户子ID
    private String merchantSubId;
    //商家名称
    private String merchantName;
    //商户私钥(pkcs8)，支付宝商户版获取
    private String alipayKey;
    //商户平台的API密钥
    private String weixinKey;
    //微信财付通账号(PartnerID)
    private String weixinMerchantId;
    //使用微信支付的APPID
    private String merchantWeixinId;
    //商户号(PartnerID)
    private String alipayMerchantId;
    //收款商户帐号
    private String merchantAlipayId;
    //umengkey
    private String umengAppkey;
    //umeng channel
    private String umengChannel;
    //umeng 消息加密
    private String umengMessageSecret;
    //网络请求前缀
    private String httpPrefix;
    //shareSDK
    private String shareSDKKey;
    //QQkey
    private String tencentKey;
    //QQ加密信息
    private String tencentSecret;
    //sinaKEY
    private String sinaKey;
    //sina加密
    private String sinaSecret;
    //sina回调
    private String sinaRedirectUri;
    //微信分享key
    private String weixinShareKey;
    //微信加密
    private String weixinShareSecret;

    public String getWeixinShareSecret() {
        return weixinShareSecret;
    }

    public void setWeixinShareSecret(String weixinShareSecret) {
        this.weixinShareSecret = weixinShareSecret;
    }

    //pushkey
    private String pushKey;
    //location_key
    private String locationKey;

    public String getLocationKey() {
        return locationKey;
    }

    public void setLocationKey(String locationKey) {
        this.locationKey = locationKey;
    }

    public String getWeixinMerchantId() {
        return weixinMerchantId;
    }

    public void setWeixinMerchantId(String weixinMerchantId) {
        this.weixinMerchantId = weixinMerchantId;
    }

    public String getMerchantWeixinId() {
        return merchantWeixinId;
    }

    public void setMerchantWeixinId(String merchantWeixinId) {
        this.merchantWeixinId = merchantWeixinId;
    }

    public String getAlipayMerchantId() {
        return alipayMerchantId;
    }

    public void setAlipayMerchantId(String alipayMerchantId) {
        this.alipayMerchantId = alipayMerchantId;
    }

    public String getMerchantAlipayId() {
        return merchantAlipayId;
    }

    public void setMerchantAlipayId(String merchantAlipayId) {
        this.merchantAlipayId = merchantAlipayId;
    }

    public String getUmengAppkey() {
        return umengAppkey;
    }

    public void setUmengAppkey(String umengAppkey) {
        this.umengAppkey = umengAppkey;
    }

    public String getUmengChannel() {
        return umengChannel;
    }

    public void setUmengChannel(String umengChannel) {
        this.umengChannel = umengChannel;
    }

    public String getUmengMessageSecret() {
        return umengMessageSecret;
    }

    public void setUmengMessageSecret(String umengMessageSecret) {
        this.umengMessageSecret = umengMessageSecret;
    }

    public String getHttpPrefix() {
        return httpPrefix;
    }

    public void setHttpPrefix(String httpPrefix) {
        this.httpPrefix = httpPrefix;
    }

    public String getShareSDKKey() {
        return shareSDKKey;
    }

    public void setShareSDKKey(String shareSDKKey) {
        this.shareSDKKey = shareSDKKey;
    }

    public String getTencentKey() {
        return tencentKey;
    }

    public void setTencentKey(String tencentKey) {
        this.tencentKey = tencentKey;
    }

    public String getTencentSecret() {
        return tencentSecret;
    }

    public void setTencentSecret(String tencentSecret) {
        this.tencentSecret = tencentSecret;
    }

    public String getSinaKey() {
        return sinaKey;
    }

    public void setSinaKey(String sinaKey) {
        this.sinaKey = sinaKey;
    }

    public String getSinaSecret() {
        return sinaSecret;
    }

    public void setSinaSecret(String sinaSecret) {
        this.sinaSecret = sinaSecret;
    }

    public String getSinaRedirectUri() {
        return sinaRedirectUri;
    }

    public void setSinaRedirectUri(String sinaRedirectUri) {
        this.sinaRedirectUri = sinaRedirectUri;
    }

    public String getWeixinShareKey() {
        return weixinShareKey;
    }

    public void setWeixinShareKey(String weixinShareKey) {
        this.weixinShareKey = weixinShareKey;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getAlipayKey() {
        return alipayKey;
    }

    public void setAlipayKey(String alipayKey) {
        this.alipayKey = alipayKey;
    }

    public String getWeixinKey() {
        return weixinKey;
    }

    public void setWeixinKey(String weixinKey) {
        this.weixinKey = weixinKey;
    }

    public String getMerchantSubId() {
        return merchantSubId;
    }

    public void setMerchantSubId(String merchantSubId) {
        this.merchantSubId = merchantSubId;
    }
}