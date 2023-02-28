package com.acg.goodweatherjava.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * @Classname GoodLocation
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/28 14:56
 * @Created by an
 */
public class GoodLocation {

    public GoodLocation(Context context){
        initLocation(context);
    }

    private static volatile GoodLocation instance = null;
    @SuppressLint("StaticFieldLeak")
    private static LocationClient sLocationClient;
    // 定位监听
    private GoodLocationListener mGoodLocationListener;

    // 定位回调接口，定义成static主要是静态内部类需要使用
    private static LocationCallback sLocationCallback;

    /**
     * 双重检锁单例模式
     * @param context
     * @return
     */
    public static GoodLocation getInstance(Context context){
        if (instance == null){
            synchronized (GoodLocation.class){
                if (instance == null){
                    instance = new GoodLocation(context);
                }
            }
        }
        return instance;
    }

    /**
     * 初始化定位
     */
    private void initLocation(Context context) {
        try {
            mGoodLocationListener = new GoodLocationListener();
            sLocationClient = new LocationClient(context);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (sLocationClient != null) {
            // 注册监听器
            sLocationClient.registerLocationListener(mGoodLocationListener);
            LocationClientOption option = new LocationClientOption();
            // 如果开发者需要获得当前点的地址信息，此处必须为true
            option.setIsNeedAddress(true);
            // 可选，设置是否需要新版本的地址信息，默认不需要，即参数为false
            option.setNeedNewVersionRgc(true);
            // 需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
            sLocationClient.setLocOption(option);
        }
    }

    /**
     * 需要定位的页面调用此方法进行接口回调处理
     * @param locationCallback
     */
    public void setCallback(LocationCallback locationCallback){
        sLocationCallback = locationCallback;
    }

    /**
     * 开始定位
     */
    public void startLocation(){
        if (sLocationClient != null){
            sLocationClient.start();
        }
    }

    /**
     * 请求定位
     */
    public static void requestLocation(){
        if (sLocationClient != null){
            sLocationClient.requestLocation();
        }
    }
    // 获取到定位结果之后就停止定位
    // 否则造成下一次定位无结果返回
    public static void stopLocation(){
        if (sLocationClient != null){
            sLocationClient.stop();
        }
    }

    public static class GoodLocationListener extends BDAbstractLocationListener{

        private final String TAG = GoodLocationListener.class.getSimpleName();

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation ==null) return;
            if (bdLocation.getDistrict() == null){
                Log.e(TAG,"onReceiveLocation: 未获取区/县数据，您可以重新断开连接网络再尝试定位");
                requestLocation();
            }
            // 获取到定位结果之后就停止定位
            // 否则造成下一次定位无结果返回
            stopLocation();
            if (sLocationCallback == null){
                Log.e(TAG,"callback is Null");
                return;
            }
            sLocationCallback.onReceiveLocation(bdLocation);
        }
    }
}
