package com.acg.goodweatherjava.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acg.goodweatherjava.bean.DailyWeatherResponse;
import com.acg.goodweatherjava.databinding.ItemDailyRvBinding;
import com.acg.goodweatherjava.utils.EasyDate;
import com.acg.goodweatherjava.utils.WeatherUtil;

import java.util.List;

/**
 * @Classname DailyAdapter
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/27 13:46
 * @Created by an
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {

    private final List<DailyWeatherResponse.DailyBean> mDailyBeans;

    public DailyAdapter(List<DailyWeatherResponse.DailyBean> dailyBeans) {
        mDailyBeans = dailyBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDailyRvBinding itemDailyRvBinding = ItemDailyRvBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ViewHolder(itemDailyRvBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyWeatherResponse.DailyBean dailyBean = mDailyBeans.get(position);

        holder.mBinding.tvDate.setText(EasyDate.dateSplit(dailyBean.getFxDate())+EasyDate.getDayInfo(dailyBean.getFxDate()));
        WeatherUtil.changeIcon(holder.mBinding.ivStatus, Integer.parseInt(dailyBean.getIconDay()));
        holder.mBinding.tvHeight.setText(dailyBean.getTempMax()+"℃");
        holder.mBinding.tvLow.setText(" / "+dailyBean.getTempMin()+"℃");
    }

    @Override
    public int getItemCount() {
        return mDailyBeans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemDailyRvBinding mBinding;

        public ViewHolder(@NonNull ItemDailyRvBinding itemDailyRvBinding) {
            super(itemDailyRvBinding.getRoot());
            mBinding = itemDailyRvBinding;
        }
    }
}
