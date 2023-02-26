package com.acg.library.network.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @Classname ResponseInterceptor
 * @Description 返回拦截器ResponseInterceptor
 * @Version 1.0.0
 * @Date 2023/2/25 23:51
 * @Created by an
 */
public class ResponseInterceptor implements Interceptor {

    private static final String TAG = ResponseInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        long requestTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        Log.i(TAG, "requestSpendTime=" + (System.currentTimeMillis() - requestTime) + "ms");
        return response;
    }
}
