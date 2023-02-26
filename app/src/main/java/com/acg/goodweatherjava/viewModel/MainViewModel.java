package com.acg.goodweatherjava.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acg.goodweatherjava.bean.NowResponse;
import com.acg.goodweatherjava.bean.SearchCityResponse;
import com.acg.goodweatherjava.repository.SearchCityRepository;
import com.acg.goodweatherjava.repository.WeatherRepository;
import com.acg.library.base.BaseViewModel;

/**
 * @Classname MainViewModel
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/26 1:43
 * @Created by an
 */
public class MainViewModel extends BaseViewModel {

    public MutableLiveData<SearchCityResponse> searchCityResponseMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<NowResponse> nowResponseMutableLiveData = new MutableLiveData<>();

    /**
     * 搜索城市
     *
     * @param cityName 城市名称
     */
    public void searchCity(String cityName) {
        new SearchCityRepository().searchCity(searchCityResponseMutableLiveData, failed, cityName);
    }

    /**
     * 实况天气
     *
     * @param cityId 城市ID
     */
    public void nowWeather(String cityId) {
        new WeatherRepository().nowWeather(nowResponseMutableLiveData, failed, cityId);
    }
}
