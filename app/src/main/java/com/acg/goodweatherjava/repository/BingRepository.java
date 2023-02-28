package com.acg.goodweatherjava.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.acg.goodweatherjava.Constant;
import com.acg.goodweatherjava.api.ApiService;
import com.acg.goodweatherjava.db.bean.BingResponse;
import com.acg.goodweatherjava.db.bean.NowResponse;
import com.acg.library.network.ApiType;
import com.acg.library.network.NetworkApi;
import com.acg.library.network.observer.BaseObserver;

/**
 * @Classname BingRepository
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/28 19:09
 * @Created by an
 */
public class BingRepository {
    private static final String TAG = WeatherRepository.class.getSimpleName();

    // 静态内部类 单例模式
    private static final class BingRepositoryHelper {
        private static final BingRepository mInstance = new BingRepository();
    }

    public static BingRepository getInstance() {
        return BingRepositoryHelper.mInstance;
    }

    @SuppressLint("CheckResult")
    public void bing(MutableLiveData<BingResponse> responseLiveData,
                     MutableLiveData<String> failed) {
        String type = "必应壁纸-->";
        NetworkApi.createService(ApiService.class, ApiType.BING).bing()
                .compose(NetworkApi.applySchedulers(new BaseObserver<BingResponse>() {
                    @Override
                    public void onSuccess(BingResponse bingResponse) {
                        if (bingResponse == null) {
                            failed.postValue("必应壁纸数据为null！");
                            return;
                        }

                        responseLiveData.postValue(bingResponse);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        failed.postValue(type + e.getMessage());
                    }
                }));
    }
}
