package com.acg.goodweatherjava.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.acg.goodweatherjava.Constant;
import com.acg.goodweatherjava.R;
import com.acg.goodweatherjava.location.GoodLocation;
import com.acg.goodweatherjava.ui.adapter.DailyAdapter;
import com.acg.goodweatherjava.ui.adapter.LifestyleAdapter;
import com.acg.goodweatherjava.db.bean.DailyWeatherResponse;
import com.acg.goodweatherjava.db.bean.LifestyleResponse;
import com.acg.goodweatherjava.db.bean.NowResponse;
import com.acg.goodweatherjava.db.bean.SearchCityResponse;
import com.acg.goodweatherjava.databinding.ActivityMainBinding;
import com.acg.goodweatherjava.location.LocationCallback;
import com.acg.goodweatherjava.utils.CityDialog;
import com.acg.goodweatherjava.utils.GlideUtils;
import com.acg.goodweatherjava.utils.MVUtils;
import com.acg.goodweatherjava.viewModel.MainViewModel;
import com.acg.library.base.NetworkActivity;
import com.baidu.location.BDLocation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NetworkActivity<ActivityMainBinding> implements LocationCallback, CityDialog.SelectedCityCallback {

    private MainViewModel mViewModel;

    //权限数组
    private final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求权限意图
    private ActivityResultLauncher<String[]> requestPermissionIntent;

    // 天气预报相关
    private final List<DailyWeatherResponse.DailyBean> mDailyBeanList = new ArrayList<>();
    private final DailyAdapter mDailyAdapter = new DailyAdapter(mDailyBeanList);

    // 生活指数相关
    private final List<LifestyleResponse.DailyBean> mLifestyleList = new ArrayList<>();
    private final LifestyleAdapter mLifestyleAdapter = new LifestyleAdapter(mLifestyleList);

    //城市弹窗
    private CityDialog cityDialog;

    // 定位服务
    private GoodLocation mGoodLocation;

    // 菜单，如果看的不是本地天气，
    // 则显示定位按钮，否则不显示
    private Menu mMenu;
    // 城市信息来源标识 0：定位，1：切换城市
    private int mCityFlag = 0;

    //城市名称，定位和切换城市都会重新赋值。
    private String mCityName;
    //是否正在刷新
    private boolean isRefresh;



    /**
     * 天气预报
     */
    private void initView() {
        setToolbarMoreIconCustom(mBinding.materialToolbar);
        mBinding.rvDaily.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvDaily.setAdapter(mDailyAdapter);
        mBinding.rvLifestyle.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvLifestyle.setAdapter(mLifestyleAdapter);
        mBinding.layRefresh.setOnRefreshListener(() -> {
            if (mCityName == null){
                mBinding.layRefresh.setRefreshing(false);
                return;
            }
            // 设置正在刷新
            isRefresh = true;
            // 搜索城市
            mViewModel.searchCity(mCityName);
        });
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
        // 获取城市数据
        mViewModel.getAllCity();
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
                // 根据cityFlag设置重新定位菜单项是否显示
                mMenu.findItem(R.id.item_relocation).setVisible(mCityFlag == 1);
                // 检查到正在刷新
                if (isRefresh){
                    showToast("刷新完成！");
                    mBinding.layRefresh.setRefreshing(false);
                    isRefresh = false;
                }
                //获取到城市的ID
                if (id != null) {
                    //通过城市ID查询城市实时天气
                    mViewModel.nowWeather(id);
                    mViewModel.dailyWeather(id);
                    mViewModel.lifestyle(id);
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

                mBinding.tvWindDirection.setText("风向     " + now.getWindDir());//风向
                mBinding.tvWindPower.setText("风力     " + now.getWindScale() + "级");//风力
                mBinding.wwBig.startRotate();//大风车开始转动
                mBinding.wwSmall.startRotate();//小风车开始转动

            }
        });
        // 每日天气返回
        mViewModel.dailyWeatherResponseMutableLiveData.observe(this, response -> {
            List<DailyWeatherResponse.DailyBean> dailyBeans = response.getDaily();
            if (dailyBeans != null) {
                if (mDailyBeanList.size() > 0) {
                    mDailyBeanList.clear();
                }
                mDailyBeanList.addAll(dailyBeans);
                mDailyAdapter.notifyDataSetChanged();
            }
        });
        // 生活指数返回
        mViewModel.lifestyleResponseMutableLiveData.observe(this, lifestyleResponse -> {
            List<LifestyleResponse.DailyBean> dailyList = lifestyleResponse.getDaily();
            if (dailyList != null) {
                if (mLifestyleList.size() > 0) {
                    mLifestyleList.clear();
                }
                mLifestyleList.addAll(dailyList);
                mLifestyleAdapter.notifyDataSetChanged();
            }
        });
        // 城市数据返回
        mViewModel.cityMutableLiveData.observe(this, provinces -> {
            //城市弹窗初始化
            cityDialog = CityDialog.getInstance(MainActivity.this, provinces);
            cityDialog.setSelectedCityCallback(this);
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
        mGoodLocation = GoodLocation.getInstance(this);
        mGoodLocation.setCallback(this);
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        mCityFlag = 0;
        mGoodLocation.startLocation();
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
            mCityName = district;
            mBinding.tvCity.setText(district);
            //搜索城市
            mViewModel.searchCity(district);
        } else {
            Log.e("TAG", "district: " + district);
        }
    }

    /**
     * 设置toolbar图标
     *
     * @param toolbar
     */
    public void setToolbarMoreIconCustom(Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.setTitle("");
            Drawable moreIcon = ContextCompat.getDrawable(toolbar.getContext(), R.drawable.ic_round_add_32);
            if (moreIcon != null) toolbar.setOverflowIcon(moreIcon);
            setSupportActionBar(toolbar);
        }
    }

    /**
     * 设置菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        // 根据cityFlag设置重新定位菜单项是否显示
        mMenu.findItem(R.id.item_relocation).setVisible(mCityFlag == 1);
        // 根据使用必应壁纸的状态，设置item项是否选中
        mMenu.findItem(R.id.item_bing).setChecked(MVUtils.getBoolean(Constant.USED_BING));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_switching_cities:
                if (cityDialog != null) cityDialog.show();
                break;
            case R.id.item_relocation:
                startLocation();    // 重新定位
                break;
            case R.id.item_bing:
                boolean useBing = !item.isChecked();
                item.setChecked(useBing); // 是否选中了必应
                MVUtils.put(Constant.USED_BING, useBing);
                String bingUrl = MVUtils.getString(Constant.BING_URL);
                updateBgImage(useBing, bingUrl); // 更新壁纸
                break;
        }
        return true;
    }

    /**
     * 回调接口，从dialog里选了地点之后，回调到这里，然后重新请求定位城市数据
     * 城市数据的回收接口(来自rxjava的)里面会再请求其它接口
     *
     * @param cityName
     */
    @Override
    public void selectedCity(String cityName) {
        mCityFlag = 1;  // 切换城市
        mCityName = cityName;
        //搜索城市
        mViewModel.searchCity(cityName);
        //显示所选城市
        mBinding.tvCity.setText(cityName);
    }

    /**
     * 更新mainActivity的背景
     *
     * @param usedBing 是否使用必应壁纸
     * @param bingUrl  壁纸的地址
     */
    public void updateBgImage(boolean usedBing, String bingUrl) {
        if (usedBing && !bingUrl.isEmpty()) {
            GlideUtils.loadImg(this, bingUrl, mBinding.layRoot);
        } else {
            mBinding.layRoot.setBackground(ContextCompat.getDrawable(this, R.drawable.main_bg));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBgImage(MVUtils.getBoolean(Constant.USED_BING), MVUtils.getString(Constant.BING_URL));
    }
}