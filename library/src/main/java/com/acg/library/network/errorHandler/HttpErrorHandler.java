package com.acg.library.network.errorHandler;


import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @Classname HttpErrorHandler
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/25 23:59
 * @Created by an
 */
public class HttpErrorHandler<T> implements Function<Throwable, Observable<T>> {
    /**
     * 处理以下两类网络错误：
     * 1、http请求相关的错误，例如：404，403，socket timeout等等；
     * 2、应用数据的错误会抛RuntimeException，最后也会走到这个函数来统一处理；
     */
    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        //通过这个异常处理，得到用户可以知道的原因
        return Observable.error(ExceptionHandle.handleException(throwable));
    }
}
