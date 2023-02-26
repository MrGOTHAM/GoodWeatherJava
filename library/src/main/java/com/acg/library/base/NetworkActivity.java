package com.acg.library.base;

import android.view.View;

import androidx.viewbinding.ViewBinding;

/**
 * @Classname NetworkActivity
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/26 1:17
 * @Created by an
 */
// BaseVBActivity必须要加<VB> ，否则导致实现他的Activity无法通过binding找到控件
public abstract class NetworkActivity<VB extends ViewBinding> extends BaseVBActivity<VB>{

    @Override
    protected void initData() {
        onCreate();
        onObserverData();
    }

    protected abstract void onCreate();

//    onObserveData()就是在使用LiveData的时候有一个观察数据返回的地方
//    ，为此我写了一个抽象方法，这属于MVVM框架的一部分，但并不是那么严格，
//    在这个类中我们实现了BaseVBActivity类的initData()抽象方法，
//    那么如果有一个类继承自NetworkActivity，就不需要重复实现了，
//    只需要实现onCreate()和onObserveData()即可。
    protected abstract void onObserverData();
}
