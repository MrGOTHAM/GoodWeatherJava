package com.acg.goodweatherjava.api;

import static com.acg.goodweatherjava.Constant.API_KEY;

import com.acg.goodweatherjava.db.bean.BingResponse;
import com.acg.goodweatherjava.db.bean.DailyWeatherResponse;
import com.acg.goodweatherjava.db.bean.HourlyResponse;
import com.acg.goodweatherjava.db.bean.LifestyleResponse;
import com.acg.goodweatherjava.db.bean.NowResponse;
import com.acg.goodweatherjava.db.bean.SearchCityResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @Classname ApiService
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/26 1:29
 * @Created by an
 */
public interface ApiService {
    /**
     * 搜索城市  模糊搜索，国内范围 返回10条数据
     *
     * @param location 城市名
     * @return NewSearchCityResponse 搜索城市数据返回
     */
    @GET("/v2/city/lookup?key=" + API_KEY + "&range=cn")
    Observable<SearchCityResponse> searchCity(@Query("location")String location);

    /**
     * 实况天气
     * @param location 地区id
     * @return
     */
    @GET("/v7/weather/now?key=" + API_KEY)
    Observable<NowResponse> nowWeather(@Query("location") String location);

    /**
     * 每日天气，默认预测7天
     * @param location 地区id
     * @return
     */
    @GET("v7/weather/7d?key="+API_KEY)
    Observable<DailyWeatherResponse> dailyWeather(@Query("location") String location);

    /**
     * 生活指数
     * @param types     默认1，2，3，4，5，6，7；每个数字对应一个类型，拼接在一起代表多个指数都查
     * @param cityId    地区ID
     * @return
     */
    @GET("v7/indices/1d?key="+API_KEY)
    Observable<LifestyleResponse> lifestyle(@Query("type") String types,@Query("location") String cityId);

    /**
     * 逐小时天气预报
     * @param location  地区ID，可以通过searchCity()接口的得到
     * @return
     */
    @GET("v7/weather/24h?key="+API_KEY)
    Observable<HourlyResponse> hourly(@Query("location")String location);

    /**
     * 必应图片
     * @return
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    Observable<BingResponse> bing();
}
