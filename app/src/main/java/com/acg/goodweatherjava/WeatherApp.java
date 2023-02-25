package com.acg.goodweatherjava;

import android.app.Application;

import com.baidu.location.LocationClient;

/**
 * @Classname WeatherApp
 * @Description 自定义一个App类放入manifest文件中，避免重复写隐私合规政策
 * @Version 1.0.0
 * @Date 2023/2/25 21:47
 * @Created by an
 */
public class WeatherApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 使用定位需要同意隐私合规政策
        LocationClient.setAgreePrivacy(true);
    }
}
