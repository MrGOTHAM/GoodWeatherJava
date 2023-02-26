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
    protected MutableLiveData<String> failed = new MutableLiveData<>();
}
