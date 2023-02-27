package com.acg.goodweatherjava.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.acg.goodweatherjava.db.bean.Province;
import com.acg.goodweatherjava.repository.CityRepository;
import com.acg.library.base.BaseViewModel;

import java.util.List;

/**
 * @Classname SplashViewModel
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/27 22:58
 * @Created by an
 */
public class SplashViewModel extends BaseViewModel {

    public MutableLiveData<List<Province>> listMutableLiveData = new MutableLiveData<>();

    /**
     * 添加城市数据
     */
    public void addCityData(List<Province> provinceList) {
        CityRepository.getInstance().addCityData(provinceList);
    }

    /**
     * 获取所有城市数据
     */
    public void getAllCityData() {
        CityRepository.getInstance().getCityData(listMutableLiveData);
    }
}
