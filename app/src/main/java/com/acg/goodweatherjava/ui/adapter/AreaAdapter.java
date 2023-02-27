package com.acg.goodweatherjava.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acg.goodweatherjava.databinding.ItemTextRvBinding;
import com.acg.goodweatherjava.db.bean.Province;
import com.acg.goodweatherjava.utils.AdministrativeType;

import java.util.List;

/**
 * @Classname AreaAdapter
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/2/27 23:28
 * @Created by an
 */
public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {

    private final List<Province.City.Area>  mAreas;
    private AdministrativeClickCallback mAdministrativeClickCallback;

    public AreaAdapter(List<Province.City.Area> areas) {
        mAreas = areas;
    }

    public void setAdministrativeClickCallback(AdministrativeClickCallback administrativeClickCallback){
        mAdministrativeClickCallback = administrativeClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTextRvBinding binding = ItemTextRvBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false);
        ViewHolder viewHolder = new ViewHolder(binding);
        binding.getRoot().setOnClickListener(v->{
            if (mAdministrativeClickCallback != null){
                mAdministrativeClickCallback.onAdministrativeItemClick(v,viewHolder.getAdapterPosition(), AdministrativeType.AREA);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItemTextRvBinding.tvText.setText(mAreas.get(position).getAreaName());
    }

    @Override
    public int getItemCount() {
        return mAreas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemTextRvBinding mItemTextRvBinding;
        public ViewHolder(@NonNull ItemTextRvBinding itemTextRvBinding) {
            super(itemTextRvBinding.getRoot());
            mItemTextRvBinding = itemTextRvBinding;
        }
    }
}
