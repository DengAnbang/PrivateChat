package com.hezeyi.privatechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.bean.ChatGroupBean;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.xhab.chatui.bean.chat.ChatListMessage;
import com.xhab.chatui.utils.TimeShowUtils;
import com.xhab.utils.inteface.OnItemClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/3/29 20:40
 */
public class ChatListMessageAdapter extends RecyclerView.Adapter<ChatListMessageAdapter.ViewHolder> {
    private List<ChatListMessage> mListMessages;
    private OnItemClickListener<ChatListMessage> mItemClickListener;

    public void setItemClickListener(OnItemClickListener<ChatListMessage> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setListMessages(List<ChatListMessage> listMessages) {
        mListMessages = listMessages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatListMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListMessageAdapter.ViewHolder holder, int position) {
        ChatListMessage chatListMessage = mListMessages.get(position);
        holder.msg.setText(chatListMessage.getMsg());
        String group_name = chatListMessage.getTarget_id();
        if (chatListMessage.getIs_group() == 1) {
            ChatGroupBean chatGroupBeanById = MyApplication.getInstance().getChatGroupBeanById(chatListMessage.getTarget_id());
            if (chatGroupBeanById != null) {
                group_name = chatGroupBeanById.getGroup_name();
            }
        } else {
            UserMsgBean userMsgBeanById = MyApplication.getInstance().getUserMsgBeanById(chatListMessage.getTarget_id());
            if (userMsgBeanById != null) {
                group_name = userMsgBeanById.getUser_name();
            }
        }
        holder.name.setText(group_name);
        holder.time.setText(TimeShowUtils.getNewChatTime(chatListMessage.getSentTime()));
        if (mItemClickListener != null) {
            holder.mView.setOnClickListener(v -> {
                mItemClickListener.onItemClick(v,position,chatListMessage);
            });

        }
    }

    @Override
    public int getItemCount() {
        return mListMessages == null ? 0 : mListMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView portrait;
        View mView;
        TextView name;
        TextView msg;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            portrait = itemView.findViewById(R.id.iv_head_portrait);
            name = itemView.findViewById(R.id.tv_name);
            msg = itemView.findViewById(R.id.tv_msg);
            time = itemView.findViewById(R.id.tv_time);
        }
    }
}
