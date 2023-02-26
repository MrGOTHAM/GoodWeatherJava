package com.acg.goodweatherjava.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.acg.goodweatherjava.Constant;
import com.acg.goodweatherjava.api.ApiService;
import com.acg.goodweatherjava.bean.NowResponse;
import com.acg.library.network.ApiType;
import com.acg.library.network.NetworkApi;
import com.acg.library.network.observer.BaseObserver;

/**
 * @Classname WeatherRepository
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/26 15:44
 * @Created by an
 */
@SuppressLint("CheckResult")
public class WeatherRepository {

    private static final String TAG = WeatherRepository.class.getSimpleName();

    /**
     * 实况天气
     *
     * @param responseLiveData 成功数据
     * @param failed           错误信息
     * @param cityId           城市ID
     */
    public void nowWeather(MutableLiveData<NowResponse> responseLiveData,
                           MutableLiveData<String> failed, String cityId) {
        String type = "实时天气-->";
        NetworkApi.createService(ApiService.class, ApiType.WEATHER).nowWeather(cityId)
                .compose(NetworkApi.applySchedulers(new BaseObserver<NowResponse>() {
                    @Override
                    public void onSuccess(NowResponse nowResponse) {
                        if (nowResponse == null) {
                            failed.postValue("实况天气数据为null，请检查城市ID是否正确。");
                            return;
                        }
                        //请求接口成功返回数据，失败返回状态码
                        if (Constant.SUCCESS.equals(nowResponse.getCode())) {
                            responseLiveData.postValue(nowResponse);
                        } else {
                            failed.postValue(type + nowResponse.getCode());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        failed.postValue(type + e.getMessage());
                    }
                }));
    }
}

