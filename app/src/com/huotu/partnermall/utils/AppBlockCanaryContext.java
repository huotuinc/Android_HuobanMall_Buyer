package com.huotu.partnermall.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.BuildConfig;

/**
 * 监控环境的上下文实现
 * Created by Administrator on 2016/4/19.
 */
public class AppBlockCanaryContext extends BlockCanaryContext {
    private static final String TAG = AppBlockCanaryContext.class.getName();

    /**
     * 标示符，可以唯一标示该安装版本号，如版本+渠道名+编译平台
     *
     * @return apk唯一标示符
     */
    @Override
    public String getQualifier() {
        String qualifier = "";
        try {
            PackageInfo info = BaseApplication.single.getPackageManager()
                    .getPackageInfo(BaseApplication.single.getPackageName(), 0);
            qualifier += info.versionCode + "_" + info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getQualifier exception", e);
        }
        return qualifier;
    }

    @Override
    public String getUid() {
        return super.getUid();
    }

    @Override
    public String getNetworkType() {
        return super.getNetworkType();
    }

    @Override
    public int getConfigDuration() {
        return super.getConfigDuration();
    }

    @Override
    public int getConfigBlockThreshold() {
        return super.getConfigBlockThreshold();
    }

    @Override
    public boolean isNeedDisplay() {
        return BuildConfig.DEBUG;
    }

    @Override
    public String getLogPath() {
        return super.getLogPath();
    }
}
