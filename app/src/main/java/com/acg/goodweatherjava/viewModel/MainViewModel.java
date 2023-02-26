package com.acg.goodweatherjava.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.acg.goodweatherjava.bean.SearchCityResponse;
import com.acg.goodweatherjava.repository.SearchCityRepository;
import com.acg.library.base.BaseViewModel;

/**
 * @Classname MainViewModel
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/26 1:43
 * @Created by an
 */
public class MainViewModel extends BaseViewModel {

    public MutableLiveData<SearchCityResponse> mSearchCityRepositoryMutableLiveData
            = new MutableLiveData<>();

    /**
     * 搜索成功
     * @param cityName 城市名称
     * @param isExact 是否精准搜索
     */
    public void searchCity(String cityName,boolean isExact){
        new SearchCityRepository().searchCity(mSearchCityRepositoryMutableLiveData,failed,cityName,isExact);
    }

}
