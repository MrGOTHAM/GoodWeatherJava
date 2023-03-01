package com.acg.goodweatherjava.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.acg.goodweatherjava.Constant;
import com.acg.goodweatherjava.api.ApiService;
import com.acg.goodweatherjava.db.bean.DailyWeatherResponse;
import com.acg.goodweatherjava.db.bean.HourlyResponse;
import com.acg.goodweatherjava.db.bean.LifestyleResponse;
import com.acg.goodweatherjava.db.bean.NowResponse;
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

    // 单例模式，防止每次使用WeatherRepository都建立一个WeatherRepository
    // 静态内部类方式构建单例
    private final static class WeatherRepositoryHolder {
        private static final WeatherRepository mInstance = new WeatherRepository();
    }

    public static WeatherRepository getInstance() {
        return WeatherRepositoryHolder.mInstance;
    }

    private WeatherRepository() {
    }

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

    /**
     * 每日天气
     *
     * @param responseLiveData 来自viewModel的数据
     * @param failed           失败数据
     * @param cityId           城市ID
     */
    public void dailyWeather(MutableLiveData<DailyWeatherResponse> responseLiveData,
                             MutableLiveData<String> failed, String cityId) {
        String type = "每日天气（未来7天）--->";
        NetworkApi.createService(ApiService.class, ApiType.WEATHER).dailyWeather(cityId)
                .compose(NetworkApi.applySchedulers(new BaseObserver<DailyWeatherResponse>() {
                    @Override
                    public void onSuccess(DailyWeatherResponse dailyWeatherResponse) {
                        if (dailyWeatherResponse == null) {
                            failed.postValue("每日天气数据为null，请检查城市ID是否正确！");
                            return;
                        }
                        //请求接口成功返回数据，失败返回状态码
                        if (Constant.SUCCESS.equals(dailyWeatherResponse.getCode())) {
                            responseLiveData.postValue(dailyWeatherResponse);
                        } else {
                            failed.postValue(type + dailyWeatherResponse.getCode());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        failed.postValue(type + e.getMessage());
                    }
                }));
    }

    /**
     * 生活指数
     *
     * @param responseLiveData 来自viewModel的数据，这里给他更新
     * @param failed           失败的数据
     * @param cityId           城市ID
     */
    public void lifeStyle(MutableLiveData<LifestyleResponse> responseLiveData,
                          MutableLiveData<String> failed, String cityId) {
        String type = "生活指数 -->";
        NetworkApi.createService(ApiService.class, ApiType.WEATHER).lifestyle("1,2,3,4,5,6,7,8,9", cityId)
                .compose(NetworkApi.applySchedulers(new BaseObserver<LifestyleResponse>() {
                    @Override
                    public void onSuccess(LifestyleResponse lifestyleResponse) {
                        if (lifestyleResponse == null) {
                            failed.postValue("生活指数数据为null，请检查城市ID是否正确！");
                            return;
                        }
                        if (Constant.SUCCESS.equals(lifestyleResponse.getCode())) {
                            responseLiveData.postValue(lifestyleResponse);
                        } else {
                            failed.postValue(type + lifestyleResponse.getCode());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        failed.postValue(type + e.getMessage());
                    }
                }));
    }

    /**
     * 逐小时天气预报
     * @param responseLiveData  viewModel里的livedata数据
     * @param failed    失败
     * @param cityId    城市ID
     */
    public void hourly(MutableLiveData<HourlyResponse> responseLiveData,
                       MutableLiveData<String> failed, String cityId) {
        String type = "逐小时天气预报--->";
        NetworkApi.createService(ApiService.class, ApiType.WEATHER).hourly(cityId)
                .compose(NetworkApi.applySchedulers(new BaseObserver<HourlyResponse>() {
                    @Override
                    public void onSuccess(HourlyResponse hourlyResponse) {
                        if (hourlyResponse == null) {
                            failed.postValue("逐小时天气预报为null，请检查城市ID是否正确！");
                            return;
                        }
                        if (Constant.SUCCESS.equals(hourlyResponse.getCode())) {
                            responseLiveData.postValue(hourlyResponse);
                        } else {
                            failed.postValue(type + hourlyResponse.getCode());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        failed.postValue(type + e.getMessage());
                    }
                }));
    }
}

