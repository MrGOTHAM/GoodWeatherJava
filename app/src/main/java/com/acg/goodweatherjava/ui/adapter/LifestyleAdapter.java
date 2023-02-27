package com.acg.goodweatherjava.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acg.goodweatherjava.db.bean.LifestyleResponse;
import com.acg.goodweatherjava.databinding.ItemLifestyleRvBinding;

import java.util.List;

/**
 * @Classname LifestyleAdapter
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/27 14:53
 * @Created by an
 */
public class LifestyleAdapter extends RecyclerView.Adapter<LifestyleAdapter.ViewHolder> {
    private final List<LifestyleResponse.DailyBean> mDailyBeans;

    public LifestyleAdapter(List<LifestyleResponse.DailyBean> dailyBeans) {
        mDailyBeans = dailyBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLifestyleRvBinding binding = ItemLifestyleRvBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LifestyleResponse.DailyBean dailyBean = mDailyBeans.get(position);
        holder.mItemLifestyleRvBinding.tvLifestyle.setText(dailyBean.getName() + "：" + dailyBean.getText());
    }

    @Override
    public int getItemCount() {
        return mDailyBeans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemLifestyleRvBinding mItemLifestyleRvBinding;

        public ViewHolder(@NonNull ItemLifestyleRvBinding itemLifestyleRvBinding) {
            super(itemLifestyleRvBinding.getRoot());
            mItemLifestyleRvBinding = itemLifestyleRvBinding;
        }
    }
}
