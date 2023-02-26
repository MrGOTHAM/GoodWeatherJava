package com.acg.library.network;

import android.app.Application;

/**
 * @Classname INetworkRequiredInfo
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/25 23:08
 * @Created by an
 */
public interface INetworkRequiredInfo {
    /**
     * 获取App版本名
     */
    String getAppVersionName();

    /**
     * 获取App版本号
     */
    String getAppVersionCode();

    /**
     * 判断是否为Debug模式
     */
    boolean isDebug();

    /**
     * 获取全局上下文参数
     */
    Application getApplicationContext();

}
