package com.hezeyi.privatechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.bean.SortableAndBuddyShowAble;
import com.abxh.chatui.utils.GlideUtils;
import com.abxh.utils.inteface.OnItemClickListener;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/3/17 10:10
 */
public class BuddyAdapter<T extends SortableAndBuddyShowAble> extends RecyclerView.Adapter<BuddyAdapter.ViewHolder> {
    private List<T> mDataList;
    private OnItemClickListener<T> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(List<T> dataList) {
        mDataList = dataList;
        Collections.sort(mDataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BuddyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buddy, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BuddyAdapter.ViewHolder holder, int position) {
        T t = mDataList.get(position);
        holder.mTvName.setText(t.getShowName());
        holder.mTvOnline.setVisibility(t.isGroup() ? View.GONE : View.VISIBLE);
        holder.mTvOnline.setText(t.isOnline()?"在线":"离线");
        GlideUtils.isOnline(holder.mIvHeadPortrait, t.isOnline());
        GlideUtils.loadHeadPortrait(t.getShowPortrait(), holder.mIvHeadPortrait, t.getPlaceholder());
        if (mOnItemClickListener != null) {
            holder.mView.setOnClickListener(view -> {
                mOnItemClickListener.onItemClick(view, position, t);
            });
        }
        if (mOnItemClickListener != null) {
            holder.mIvHeadPortrait.setOnClickListener(view -> {
                mOnItemClickListener.onItemClick(view, position, t);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvOnline;
        TextView mTvName;
        ImageView mIvHeadPortrait;
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mTvName = itemView.findViewById(R.id.tv_name);
            mIvHeadPortrait = itemView.findViewById(R.id.iv_head_portrait);
            mTvOnline = itemView.findViewById(R.id.tv_online);
        }
    }
}
