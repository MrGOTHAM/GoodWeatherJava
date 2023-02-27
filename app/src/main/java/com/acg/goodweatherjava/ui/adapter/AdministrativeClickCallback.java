package com.acg.goodweatherjava.ui.adapter;

import android.view.View;

import com.acg.goodweatherjava.utils.AdministrativeType;

/**
 * @Classname AdministrativeClickCallback
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/27 23:18
 * @Created by an
 */
public interface AdministrativeClickCallback {

    /**
     * 行政区 点击事件
     *
     * @param view     点击视图
     * @param position 点击位置
     * @param type     行政区类型
     */
    void onAdministrativeItemClick(View view, int position, AdministrativeType type);

}
