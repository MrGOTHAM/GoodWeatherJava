package com.acg.goodweatherjava;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.acg.goodweatherjava.adapter.DailyAdapter;
import com.acg.goodweatherjava.bean.DailyWeatherResponse;
import com.acg.goodweatherjava.bean.NowResponse;
import com.acg.goodweatherjava.bean.SearchCityResponse;
import com.acg.goodweatherjava.databinding.ActivityMainBinding;
import com.acg.goodweatherjava.location.LocationCallback;
import com.acg.goodweatherjava.location.MyLocationListener;
import com.acg.goodweatherjava.viewModel.MainViewModel;
import com.acg.library.base.NetworkActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NetworkActivity<ActivityMainBinding> implements LocationCallback {
    private LocationClient mLocationClient;
    private final MyLocationListener mMyLocationListener = new MyLocationListener();
    private MainViewModel mViewModel;

    //权限数组
    private final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求权限意图
    private ActivityResultLauncher<String[]> requestPermissionIntent;

    // 天气预报相关
    private final List<DailyWeatherResponse.DailyBean> mDailyBeanList = new ArrayList<>();
    private final DailyAdapter mDailyAdapter = new DailyAdapter(mDailyBeanList);

    /**
     * 天气预报
     */
    private void initView(){
        mBinding.rvDaily.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvDaily.setAdapter(mDailyAdapter);
    }

    /**
     * 注册意图
     */
    @Override
    protected void onRegister() {
        //请求权限意图
        requestPermissionIntent = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            boolean fineLocation = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
            boolean writeStorage = Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE));
            if (fineLocation && writeStorage) {
                //权限已经获取到，开始定位      主要是第一次安装的时候，如果不加这一句会获取不到定位
                startLocation();
            }
        });
    }

    /**
     * 初始化
     */
    @Override
    protected void onCreate() {
        setFullScreenImmersion();       // 全屏沉浸式
        initLocation();
        requestPermission();
        initView();
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    /**
     * 数据观察
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onObserverData() {
        if (mViewModel == null) {
            return;
        }
        //城市数据返回
        mViewModel.searchCityResponseMutableLiveData.observe(this, searchCityResponse -> {
            List<SearchCityResponse.LocationBean> location = searchCityResponse.getLocation();
            if (location != null && location.size() > 0) {
                String id = location.get(0).getId();
                //获取到城市的ID
                if (id != null) {
                    //通过城市ID查询城市实时天气
                    mViewModel.nowWeather(id);
                    mViewModel.dailyWeather(id);
                }
            }
        });
        //实况天气返回
        mViewModel.nowResponseMutableLiveData.observe(this, nowResponse -> {
            NowResponse.NowBean now = nowResponse.getNow();
            if (now != null) {
                mBinding.tvInfo.setText(now.getText());
                mBinding.tvTemp.setText(now.getTemp());
                mBinding.tvUpdateTime.setText("最近更新时间：" + nowResponse.getUpdateTime());
            }
        });
        // 每日天气返回
        mViewModel.dailyWeatherResponseMutableLiveData.observe(this,response->{
            List<DailyWeatherResponse.DailyBean> dailyBeans = response.getDaily();
            if (dailyBeans!=null){
                if (mDailyBeanList.size() >0){
                    mDailyBeanList.clear();
                }
                mDailyBeanList.addAll(dailyBeans);
                mDailyAdapter.notifyDataSetChanged();
            }
        });
        // 错误信息返回
        mViewModel.failed.observe(this, this::showLongToast);

    }

    // 申请权限弹框
    private void requestPermission() {
        //因为项目的最低版本API是23，所以肯定需要动态请求危险权限，只需要判断权限是否拥有即可
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //开始权限请求
            requestPermissionIntent.launch(permissions);
            return;
        }
        //开始定位
        startLocation();
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        try {
            mLocationClient = new LocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mLocationClient != null) {
            mMyLocationListener.setCallback(this);
            // 注册监听器
            mLocationClient.registerLocationListener(mMyLocationListener);
            LocationClientOption option = new LocationClientOption();
            // 如果开发者需要获得当前点的地址信息，此处必须为true
            option.setIsNeedAddress(true);
            // 可选，设置是否需要新版本的地址信息，默认不需要，即参数为false
            option.setNeedNewVersionRgc(true);
            // 需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
            mLocationClient.setLocOption(option);
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        if (mLocationClient != null) {
            mLocationClient.start();
        }
    }

    /**
     * 接收定位信息
     *
     * @param bdLocation 定位数据
     */
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        String city = bdLocation.getCity();         //  获取城市
        String district = bdLocation.getDistrict(); // 获取区县

        if (mViewModel != null && district != null) {
            mBinding.tvCity.setText(district);
            //搜索城市
            mViewModel.searchCity(district);
        } else {
            Log.e("TAG", "district: " + district);
        }
    }
}