package com.acg.goodweatherjava.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.acg.goodweatherjava.api.ApiService;
import com.acg.goodweatherjava.Constant;
import com.acg.goodweatherjava.bean.SearchCityResponse;
import com.acg.library.network.ApiType;
import com.acg.library.network.NetworkApi;
import com.acg.library.network.observer.BaseObserver;

/**
 * @Classname SearchCityRepository
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/26 1:33
 * @Created by an
 */
public class SearchCityRepository {

    private static final String TAG = SearchCityRepository.class.getSimpleName();

    @SuppressLint("CheckResult")
    public void searchCity(MutableLiveData<SearchCityResponse> responseLiveData,
                           MutableLiveData<String> failed, String cityName, boolean isExact) {
        NetworkApi.createService(ApiService.class, ApiType.SEARCH)
                .searchCity(cityName, isExact ? Constant.EXACT : Constant.FUZZY)
                .compose(NetworkApi.applySchedulers(new BaseObserver<SearchCityResponse>() {
                    @Override
                    public void onSuccess(SearchCityResponse searchCityResponse) {
                        if (searchCityResponse == null) {
                            failed.postValue("搜索城市数据为null，请检查城市名是否正确！");
                            return;
                        }
                        //请求接口成功返回数据，失败返回状态码
                        if (Constant.SUCCESS.equals(searchCityResponse.getCode())) {
                            responseLiveData.postValue(searchCityResponse);
                        } else {
                            failed.postValue(searchCityResponse.getCode());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        failed.postValue(e.getMessage());
                    }
                }));

    }
}
