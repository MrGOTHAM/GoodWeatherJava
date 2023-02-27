package com.acg.goodweatherjava;

import com.acg.goodweatherjava.db.AppDatabase;
import com.acg.goodweatherjava.utils.MVUtils;
import com.acg.library.base.BaseApplication;
import com.acg.library.network.NetworkApi;
import com.baidu.location.LocationClient;
import com.tencent.mmkv.MMKV;

/**
 * @Classname WeatherApp
 * @Description 自定义一个App类放入manifest文件中，避免重复写隐私合规政策
 * @Version 1.0.0
 * @Date 2023/2/25 21:47
 * @Created by an
 */
public class WeatherApp extends BaseApplication {

    // 数据库
    private static AppDatabase db;
    @Override
    public void onCreate() {
        super.onCreate();
        // 使用定位需要同意隐私合规政策
        LocationClient.setAgreePrivacy(true);
        // 初始化网络框架
        NetworkApi.init(new NetworkRequiredInfo(this));
        //  MMKV初始化
        MMKV.initialize(this);
        // 工具类初始化
        MVUtils.getInstance();
        // 初始化Room数据库
        db = AppDatabase.getInstance(this);
    }

    public static AppDatabase getDb(){
        return db;
    }
}
