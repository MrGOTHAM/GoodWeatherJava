package com.acg.library.network;

import com.acg.library.network.errorHandler.ExceptionHandle;
import com.acg.library.network.errorHandler.HttpErrorHandler;
import com.acg.library.network.interceptor.RequestInterceptor;
import com.acg.library.network.interceptor.ResponseInterceptor;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Classname NetworkApi
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/26 0:05
 * @Created by an
 */
public class NetworkApi {

    // 获取APP运行状态及版本信息，用于日志打印
    private static INetworkRequiredInfo sINetworkRequiredInfo;
    // okhttp客户端
    private static OkHttpClient sOkHttpClient;
    // retrofitHashMap
    private static final HashMap<String, Retrofit> sRetrofitHashMap = new HashMap<>();
    // API访问地址
    private static String sBaseUrl;

    /**
     * 初始化
     */
    public static void init(INetworkRequiredInfo networkRequiredInfo) {
        sINetworkRequiredInfo = networkRequiredInfo;
    }

    /**
     * 创建serviceClass的实例
     */
    public static <T> T createService(Class<T> serviceClass, ApiType apiType) {
        getBaseUrl(apiType);
        return getRetrofit(serviceClass).create(serviceClass);
    }

    /**
     * 配置RxJava 完成线程的切换，如果是Kotlin中完全可以直接使用协程
     *
     * @param observer 这个observer要注意不要使用lifecycle中的Observer
     * @param <T>      泛型
     * @return Observable
     */
    public static <T> ObservableTransformer<T, T> applySchedulers(final Observer<T> observer) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                Observable<T> observable = upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        //判断有没有500的错误，有则进入getAppErrorHandler
                        .map(NetworkApi.<T>getAppErrorHandler())
                        //判断有没有400的错误
                        .onErrorResumeNext(new HttpErrorHandler<T>());
                //这里还少了对异常
                //订阅观察者
                observable.subscribe(observer);
                return observable;

            }
        };
    }

    /**
     * 错误码处理
     */
    private static <T> Function<T, T> getAppErrorHandler() {
        return new Function<T, T>() {
            @Override
            public T apply(T response) {
                //当response返回出现500之类的错误时
                if (response instanceof BaseResponse && ((BaseResponse) response).responseCode >= 500) {
                    //通过这个异常处理，得到用户可以知道的原因
                    ExceptionHandle.ServerException exception = new ExceptionHandle.ServerException();
                    exception.code = ((BaseResponse) response).responseCode;
                    exception.message = ((BaseResponse) response).responseError != null ? ((BaseResponse) response).responseError : "";
                    throw exception;
                }
                return response;
            }
        };
    }

    /**
     * 配置Retrofit
     *
     * @param serviceClass 服务类
     * @return Retrofit
     */
    private static Retrofit getRetrofit(Class serviceClass) {
        if (sRetrofitHashMap.get(sBaseUrl + serviceClass.getName()) != null) {
            //刚才上面定义的Map中键是String，值是Retrofit，当键不为空时，必然有值，有值则直接返回。
            return sRetrofitHashMap.get(sBaseUrl + serviceClass.getName());
        }
        //初始化Retrofit  Retrofit是对OKHttp的封装，通常是对网络请求做处理，也可以处理返回数据。
        //Retrofit构建器
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sBaseUrl)
                .client(getOkHttpClient())  //设置OkHttp客户端，传入上面写好的方法即可获得配置后的OkHttp客户端。
                .addConverterFactory(GsonConverterFactory.create())
                //设置请求回调，使用RxJava 对网络返回进行处理
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        sRetrofitHashMap.put(sBaseUrl + serviceClass.getName(), retrofit);
        return retrofit;
    }

    /**
     * 修改访问地址
     *
     * @param apiType api类型
     */
    private static void getBaseUrl(ApiType apiType) {
        switch (apiType) {
            case SEARCH:
                sBaseUrl = "https://geoapi.qweather.com"; //和风天气搜索城市
                break;
            default:
                break;
        }
    }

    /**
     * 配置OkHttp
     *
     * @return OkHttpClient
     */
    private static OkHttpClient getOkHttpClient() {
        //不为空则说明已经配置过了，直接返回即可。
        if (sOkHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()    //OkHttp构建器
                    //设置缓存大小和缓存地址
                    .cache(new Cache(sINetworkRequiredInfo.getApplicationContext().getCacheDir(), 100 * 1024 * 1024))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(new RequestInterceptor(sINetworkRequiredInfo))
                    .addInterceptor(new ResponseInterceptor());
            if (sINetworkRequiredInfo != null && sINetworkRequiredInfo.isDebug()) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(httpLoggingInterceptor);
            }
            //OkHttp配置完成
            sOkHttpClient = builder.build();
        }
        return sOkHttpClient;
    }


}
