package com.acg.library.network.interceptor;

import android.annotation.SuppressLint;

import com.acg.library.network.INetworkRequiredInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Classname RequestInterceptor
 * @Description 数据发送  拦截器
 * @Version 1.0.0
 * @Date 2023/2/25 23:43
 * @Created by an
 */
public class RequestInterceptor implements Interceptor {

    private final INetworkRequiredInfo mINetworkRequiredInfo;

    public RequestInterceptor(INetworkRequiredInfo iNetworkRequiredInfo) {
        mINetworkRequiredInfo = iNetworkRequiredInfo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 构造器
        Request build = chain.request().newBuilder()
                //添加使用环境
                .addHeader("os", "android")
                //添加版本号
                .addHeader("appVersionCode", mINetworkRequiredInfo.getAppVersionCode())
                //添加版本名
                .addHeader("appVersionName", mINetworkRequiredInfo.getAppVersionName())
                //添加日期时间
                .addHeader("datetime", getNowDateTime()).build();

        return chain.proceed(build);

    }

    private String getNowDateTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
