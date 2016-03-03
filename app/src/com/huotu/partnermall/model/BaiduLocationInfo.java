package com.huotu.partnermall.model;

/**
 * Created by Administrator on 2015/8/25.
 */
public class BaiduLocationInfo {
    public String getCityCode() {
        return cityCode;
    }

    public int getLocalType() {
        return localType;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setLocalType(int localType) {
        this.localType = localType;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    /**
     * 城市码
     */
    public String cityCode;

    public int localType;

    /**
     * 城市
     */
    public String city;

    /**
     * 地址
     */
    public String address;

    /**
     * 纬度
     */
    public double latitude;

    /**
     * 经度
     */
    public double Longitude;
}
