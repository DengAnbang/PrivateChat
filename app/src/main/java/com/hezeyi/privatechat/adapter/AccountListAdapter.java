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
import com.xhab.utils.utils.TimeUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/4/13 10:07
 */
public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {
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
    public AccountListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountListAdapter.ViewHolder holder, int position) {
        UserMsgBean userMsgBean = mUserMsgBeans.get(position);
        GlideUtils.loadHeadPortrait(userMsgBean.getHead_portrait(), holder.portrait, userMsgBean.getPlaceholder());
        holder.name.setText(userMsgBean.getNickname() + "(账号:" + userMsgBean.getAccount() + ")");
        holder.msg.setText("账号到期时间:" + TimeUtils.toTimeByString(userMsgBean.getVip_time()));
        if (mOnItemClickListener != null) {
            holder.mView.setOnClickListener(v -> {
                mOnItemClickListener.onItemClick(v, position, userMsgBean);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mUserMsgBeans == null ? 0 : mUserMsgBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView portrait;
        View mView;
        TextView name;
        TextView msg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            portrait = itemView.findViewById(R.id.iv_head_portrait);
            name = itemView.findViewById(R.id.tv_name);
            msg = itemView.findViewById(R.id.tv_msg);

        }
    }
}
