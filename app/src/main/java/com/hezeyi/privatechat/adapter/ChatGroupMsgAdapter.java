package com.hezeyi.privatechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.inteface.BuddyShowAble;
import com.abxh.chatui.utils.GlideUtils;
import com.abxh.utils.inteface.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/3/27 09:58
 */
public class ChatGroupMsgAdapter<T extends BuddyShowAble> extends RecyclerView.Adapter<ChatGroupMsgAdapter.ViewHolder> {
    private List<T> mDataList = new ArrayList<>();

    public void setDataList(List<T> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    private OnItemClickListener<T> mItemClickListener;

    public void setItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_group_msg, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatGroupMsgAdapter.ViewHolder holder, int position) {
        if (position == mDataList.size()) {
            holder.name.setText("");
            GlideUtils.loadChatImage(R.mipmap.f28_tianjia, holder.headPortrait);
            if (mItemClickListener != null) {
                holder.headPortrait.setOnClickListener(v -> {
                    mItemClickListener.onItemClick(v, position, null);
                });
            }
        } else {
            T t = mDataList.get(position);
            holder.name.setText(t.getShowName());
            GlideUtils.loadHeadPortrait(t.getShowPortrait(),holder.headPortrait,t.getPlaceholder());
            if (mItemClickListener != null) {
                holder.headPortrait.setOnClickListener(v -> {
                    mItemClickListener.onItemClick(v, position, t);
                });

            }
        }
    }


    @Override
    public int getItemCount() {
        return getCount(mDataList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView headPortrait;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headPortrait = itemView.findViewById(R.id.iv_head_portrait);
            name = itemView.findViewById(R.id.tv_name);
        }
    }


    private int getCount(List list) {
        int count = list == null ? 0 : list.size();
        if (isAdmin) {
            count = count + 1;
        }
        return count;
    }

    private boolean isAdmin = true;

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
