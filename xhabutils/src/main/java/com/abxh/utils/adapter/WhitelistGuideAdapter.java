package com.abxh.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abxh.utils.R;
import com.abxh.utils.bean.WhitelistGuideBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/4/22 10:39
 */
public class WhitelistGuideAdapter extends RecyclerView.Adapter<WhitelistGuideAdapter.ViewHolder> {
    private List<WhitelistGuideBean> mWhitelistGuideBeans;

    public void setWhitelistGuideBeans(List<WhitelistGuideBean> whitelistGuideBeans) {
        mWhitelistGuideBeans = whitelistGuideBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WhitelistGuideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_whitelist_guide, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WhitelistGuideAdapter.ViewHolder holder, int position) {
        WhitelistGuideBean whitelistGuideBean = mWhitelistGuideBeans.get(position);
        holder.description.setText(whitelistGuideBean.getDescription());
        holder.img.setImageDrawable(ContextCompat.getDrawable(holder.img.getContext(), whitelistGuideBean.getResId()));
    }

    @Override
    public int getItemCount() {
        return mWhitelistGuideBeans == null ? 0 : mWhitelistGuideBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_img);
            description = itemView.findViewById(R.id.tv_description);
        }
    }
}
