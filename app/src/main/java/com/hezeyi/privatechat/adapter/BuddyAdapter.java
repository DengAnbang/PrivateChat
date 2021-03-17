package com.hezeyi.privatechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.bean.UserMsgBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/3/17 10:10
 */
public class BuddyAdapter extends RecyclerView.Adapter<BuddyAdapter.ViewHolder> {
    private List<UserMsgBean> mUserMsgBeans;

    public List<UserMsgBean> getUserMsgBeans() {
        return mUserMsgBeans;
    }

    public void setUserMsgBeans(List<UserMsgBean> userMsgBeans) {
        mUserMsgBeans = userMsgBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BuddyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buddy, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BuddyAdapter.ViewHolder holder, int position) {
        UserMsgBean userMsgBean = mUserMsgBeans.get(position);
        holder.mTvName.setText(userMsgBean.getUser_name());
    }

    @Override
    public int getItemCount() {
        return mUserMsgBeans == null ? 0 : mUserMsgBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        ImageView mIvHeadPortrait;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mIvHeadPortrait = itemView.findViewById(R.id.iv_head_portrait);
        }
    }
}
