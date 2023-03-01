package com.acg.goodweatherjava.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acg.goodweatherjava.R;
import com.acg.goodweatherjava.databinding.ItemHourlyBinding;
import com.acg.goodweatherjava.db.bean.HourlyResponse;
import com.acg.goodweatherjava.utils.EasyDate;
import com.acg.goodweatherjava.utils.WeatherUtil;

import java.util.List;

/**
 * @Classname HourlyAdapter
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/1 18:39
 * @Created by an
 */
public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {

    private final List<HourlyResponse.HourlyBean> mLists;

    public HourlyAdapter(List<HourlyResponse.HourlyBean> lists) {
        mLists = lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHourlyBinding itemHourlyBinding = ItemHourlyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemHourlyBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyResponse.HourlyBean hourlyBean = mLists.get(position);
        String time = EasyDate.updateTime(hourlyBean.getFxTime());
        holder.mItemHourlyBinding.tvTime.setText(String.format("%s%s", EasyDate.showTimeInfo(time), time));
        WeatherUtil.changeIcon(holder.mItemHourlyBinding.ivStatus, Integer.parseInt(hourlyBean.getIcon()));
        holder.mItemHourlyBinding.tvTemp.setText(String.format("%s℃", hourlyBean.getTemp()));
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemHourlyBinding mItemHourlyBinding;

        public ViewHolder(@NonNull ItemHourlyBinding itemHourlyBinding) {
            super(itemHourlyBinding.getRoot());
            mItemHourlyBinding = itemHourlyBinding;
        }
    }
}
