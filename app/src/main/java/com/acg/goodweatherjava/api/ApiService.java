package com.acg.goodweatherjava.api;

import static com.acg.goodweatherjava.Constant.API_KEY;

import com.acg.goodweatherjava.bean.SearchCityResponse;

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
     * @param mode     exact 精准搜索  fuzzy 模糊搜索
     * @return NewSearchCityResponse 搜索城市数据返回
     */
    @GET("/v2/city/lookup?key=" + API_KEY + "&range=cn")
    Observable<SearchCityResponse> searchCity(@Query("location")String location,@Query("mode")String mode);
}
