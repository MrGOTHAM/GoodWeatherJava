package com.acg.goodweatherjava;

import android.app.Application;

import com.acg.library.network.INetworkRequiredInfo;

/**
 * @Classname NetworkRequiredInfo
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/26 1:23
 * @Created by an
 */
public class NetworkRequiredInfo implements INetworkRequiredInfo {
    private final Application mApplication;

    public NetworkRequiredInfo(Application application) {
        mApplication = application;
    }

    @Override
    public String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getAppVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public Application getApplicationContext() {
        return mApplication;
    }
}
