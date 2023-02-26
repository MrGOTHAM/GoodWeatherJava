package com.acg.library.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @Classname BaseViewModel
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/26 1:21
 * @Created by an
 */
public class BaseViewModel extends ViewModel {
    // 这里我增加了一个case，用于判断接口传进来的Api类型，从而设置不同的地址头，
    // 因为每一个接口都需要有成功和失败的请求回调，
    // 那么我们修改一下BaseViewModel中的failed变量的作用域为public
    public MutableLiveData<String> failed = new MutableLiveData<>();
}
