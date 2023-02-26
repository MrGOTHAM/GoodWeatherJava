package com.acg.library.base;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Classname BaseVBActivity
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/26 1:04
 * @Created by an
 */
public abstract class BaseVBActivity<VB extends ViewBinding> extends BaseActivity{

    protected VB mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onRegister();
        super.onCreate(savedInstanceState);
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            try {
                Class<VB> clazz = (Class<VB>) ((ParameterizedType) type).getActualTypeArguments()[0];
                //反射
                Method method = clazz.getMethod("inflate", LayoutInflater.class);
                mBinding = (VB) method.invoke(null, getLayoutInflater());
            } catch (Exception e) {
                e.printStackTrace();
            }
            setContentView(mBinding.getRoot());
        }
        initData();
    }

    protected abstract void initData();

    //后面如果我们需要使用意图去出去，就可以在子类中直接重写父类的方法达到同样的效果。
    protected void onRegister(){

    }
}
