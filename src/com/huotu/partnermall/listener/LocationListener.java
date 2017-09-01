package com.huotu.partnermall.listener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.utils.PreferenceHelper;

/**
 * Created by Administrator on 2017/8/28.
 */
public class LocationListener implements BDLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {

        if (null == location) {
            return;
        }

        //纬度
        double latitude=0;
        //经度
        double Longitude=0;
        //地址
        String address=null;
        //城市
        String city=null;

        int localType = location.getLocType();

        if (BDLocation.TypeGpsLocation == localType) {
            latitude = location.getLatitude();
            Longitude = location.getLongitude();
            city = location.getCity();
            address = location.getAddrStr();
        } else if (BDLocation.TypeNetWorkLocation == localType) {
            latitude = location.getLatitude();
            Longitude = location.getLongitude();
            city = location.getCity();
            address = location.getAddrStr();
        }

        // 将定位信息写入配置文件
        if (null != city) {
            PreferenceHelper.writeString(BaseApplication.single, Constants.LOCATION_INFO, Constants.CITY, city);
        }
        if (null != address) {
            PreferenceHelper.writeString(BaseApplication.single, Constants.LOCATION_INFO, Constants.ADDRESS, address);
        }
        PreferenceHelper.writeString(BaseApplication.single, Constants.LOCATION_INFO, Constants.LATITUDE, String.valueOf(latitude));
        PreferenceHelper.writeString(BaseApplication.single, Constants.LOCATION_INFO, Constants.LONGITUDE, String.valueOf(Longitude));
    }

}
