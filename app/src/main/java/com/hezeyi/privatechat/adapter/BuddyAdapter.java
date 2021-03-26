package com.hezeyi.privatechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.inteface.BuddyShowAble;
import com.xhab.utils.inteface.OnItemClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/3/17 10:10
 */
public class BuddyAdapter<T extends BuddyShowAble> extends RecyclerView.Adapter<BuddyAdapter.ViewHolder> {
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
        if (mOnItemClickListener != null) {
            holder.mView.setOnClickListener(view -> {
                mOnItemClickListener.onItemClick(view, position, t);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        ImageView mIvHeadPortrait;
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mTvName = itemView.findViewById(R.id.tv_name);
            mIvHeadPortrait = itemView.findViewById(R.id.iv_head_portrait);
        }
    }
}
