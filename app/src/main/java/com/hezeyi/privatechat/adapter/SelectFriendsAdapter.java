package com.hezeyi.privatechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.abxh.chatui.utils.GlideUtils;
import com.abxh.utils.inteface.OnItemClickListener;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/3/27 16:42
 */
public class SelectFriendsAdapter extends RecyclerView.Adapter<SelectFriendsAdapter.ViewHolder> {
    private List<UserMsgBean> mDataList;
    private OnItemClickListener<UserMsgBean> mOnItemClickListener;


    public List<UserMsgBean> getDataList() {
        return mDataList;
    }

    public void setDataList(List<UserMsgBean> dataList) {
        mDataList = dataList;
        Collections.sort(mDataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectFriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buddy_select, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectFriendsAdapter.ViewHolder holder, int position) {
        UserMsgBean t = mDataList.get(position);
        holder.mTvName.setText(t.getShowName());
        holder.mView.setOnClickListener(view -> {
            t.choose();
            GlideUtils.loadChatImage(t.isChoose() ? R.mipmap.ic_checkbox_checked : R.mipmap.ic_checkbox_uncheck, holder.ivSelect);
        });

    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        ImageView mIvHeadPortrait;
        ImageView ivSelect;
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mTvName = itemView.findViewById(R.id.tv_name);
            mIvHeadPortrait = itemView.findViewById(R.id.iv_head_portrait);
            ivSelect = itemView.findViewById(R.id.iv_select);
        }
    }
}
