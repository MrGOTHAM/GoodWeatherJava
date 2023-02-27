package com.acg.goodweatherjava.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.acg.goodweatherjava.WeatherApp;
import com.acg.goodweatherjava.db.bean.Province;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * @Classname CityRepository
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/27 22:44
 * @Created by an
 */
public class CityRepository {

    private static final String TAG = CityRepository.class.getSimpleName();

    private static final class CityRepositoryHolder {
        private static final CityRepository mInstance = new CityRepository();
    }

    public static CityRepository getInstance() {
        return CityRepository.CityRepositoryHolder.mInstance;
    }

    /**
     * 添加城市数据
     */
    public void addCityData(List<Province> cityList) {
        Province[] provinceArray = cityList.toArray(new Province[0]);
        Completable insertAll = WeatherApp.getDb().provinceDao().insertAll(provinceArray);
        CustomDisposable.addDisposable(insertAll, () -> Log.d(TAG, "addCityData: 插入数据成功。"));
    }

    /**
     * 获取城市数据
     * listMutableLiveData::postValue代表一个新值将发布到livedata中
     */
    public void getCityData(MutableLiveData<List<Province>> listMutableLiveData) {
        Flowable<List<Province>> listFlowable = WeatherApp.getDb().provinceDao().getAll();
        CustomDisposable.addDisposable(listFlowable, listMutableLiveData::postValue);
    }
}
