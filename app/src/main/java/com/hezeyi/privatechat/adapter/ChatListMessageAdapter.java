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
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.chatui.utils.TimeShowUtils;
import com.xhab.utils.inteface.OnItemClickListener;
import com.xhab.utils.inteface.OnItemLongClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/3/29 20:40
 */
public class ChatListMessageAdapter extends RecyclerView.Adapter<ChatListMessageAdapter.ViewHolder> {
    private List<ChatListMessage> mListMessages;
    private OnItemClickListener<ChatListMessage> mItemClickListener;
    private OnItemLongClickListener<ChatListMessage> mItemLongClickListener;

    public void setItemLongClickListener(OnItemLongClickListener<ChatListMessage> itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

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

                GlideUtils.loadHeadPortrait(chatGroupBeanById.getGroup_portrait(), holder.portrait, chatGroupBeanById.getPlaceholder());
            }
        } else {
            UserMsgBean userMsgBeanById = MyApplication.getInstance().getUserMsgBeanById(chatListMessage.getAnotherId(MyApplication.getInstance().getUserMsgBean().getUser_id()));
            if (userMsgBeanById != null) {
                group_name = userMsgBeanById.getNickname();
                GlideUtils.loadHeadPortrait(userMsgBeanById.getHead_portrait(), holder.portrait, userMsgBeanById.getPlaceholder());
            }

        }
        holder.prompt.setVisibility(chatListMessage.getUnread() == 0 ? View.GONE : View.VISIBLE);
        int prompt = chatListMessage.getUnread();
        if (prompt > 99) {
            prompt = 99;
        }
        holder.prompt.setText(prompt + "");
        holder.name.setText(group_name);
        holder.time.setText(TimeShowUtils.getNewChatTimeList(chatListMessage.getSentTime()));
        if (mItemClickListener != null) {
            holder.mView.setOnClickListener(v -> {
                mItemClickListener.onItemClick(v, position, chatListMessage);
            });
        }
        if (mItemLongClickListener != null) {
            holder.mView.setOnLongClickListener(v -> mItemLongClickListener.onLongClick(v, position, chatListMessage));
        }

    }

    @Override
    public int getItemCount() {
        return mListMessages == null ? 0 : mListMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prompt;
        ImageView portrait;
        View mView;
        TextView name;
        TextView msg;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            portrait = itemView.findViewById(R.id.iv_head_portrait);
            prompt = itemView.findViewById(R.id.tv_prompt);
            name = itemView.findViewById(R.id.tv_name);
            msg = itemView.findViewById(R.id.tv_msg);
            time = itemView.findViewById(R.id.tv_time);
        }
    }
}
