package com.hezeyi.privatechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.utils.inteface.OnItemClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/4/6 19:41
 */
public class FriendReplyAdapter extends RecyclerView.Adapter<FriendReplyAdapter.ViewHolder> {
    private List<UserMsgBean> mUserMsgBeans;
    private OnItemClickListener<UserMsgBean> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<UserMsgBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setUserMsgBeans(List<UserMsgBean> userMsgBeans) {
        mUserMsgBeans = userMsgBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_reply, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserMsgBean userMsgBean = mUserMsgBeans.get(position);
        holder.mTvName.setText(userMsgBean.getShowName());
        GlideUtils.loadHeadPortrait(userMsgBean.getShowPortrait(), holder.mIvHeadPortrait, userMsgBean.getPlaceholder());

        holder.mIvAgree.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, position, userMsgBean);
            }
        });
        holder.mIvRefuse.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, position, userMsgBean);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUserMsgBeans == null ? 0 : mUserMsgBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        ImageView mIvAgree;
        ImageView mIvRefuse;
        ImageView mIvHeadPortrait;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mIvHeadPortrait = itemView.findViewById(R.id.iv_head_portrait);
            mIvAgree = itemView.findViewById(R.id.iv_agree);
            mIvRefuse = itemView.findViewById(R.id.iv_refuse);
        }
    }
}
