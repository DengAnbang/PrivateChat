package com.xhab.chatui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xhab.chatui.R;
import com.xhab.chatui.bean.chat.ChatMessage;

import java.util.List;
import java.util.Objects;

/**
 * Created by dab on 2021/5/6 16:27
 */

public class ChatAdapterNew extends RecyclerView.Adapter<ChatAdapterNew.ViewHolder> {
    private static final long INTERVALS = 1000 * 60 * 5;//显示聊天信息的间隔时间
    private List<ChatMessage> chatMessages;
    private String myUserId;

    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(viewType == 0 ? R.layout.item_chat_send : R.layout.item_chat_send, parent, false);
//        ViewStub viewStub = inflate.findViewById(R.id.vs_content);
//        if (viewStub.getParent()!=null) {
//            viewStub.setLayoutResource(R.layout.item_chat_msg_text);
//            viewStub.inflate();
//        }
        return new ViewHolder(inflate);
    }

    @Override
    public int getItemViewType(int position) {
        return Objects.equals(chatMessages.get(position).getSenderId(), myUserId) ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        if (holder.viewStub.getParent() != null) {
            holder.viewStub.setLayoutResource(chatMessage.getSpecialRes());
            holder.viewStub.inflate();
        }
        chatMessage.showSpecialRes(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return chatMessages == null ? 0 : chatMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewStub viewStub;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewStub = itemView.findViewById(R.id.vs_content);
        }
    }
}
