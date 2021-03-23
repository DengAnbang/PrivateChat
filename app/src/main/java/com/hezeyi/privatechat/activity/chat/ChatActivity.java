package com.hezeyi.privatechat.activity.chat;

import android.os.Bundle;

import com.hezeyi.privatechat.Const;
import com.xhab.chatui.activity.BaseChatActivity;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.utils.net.RequestHelperAgency;
import com.xhab.utils.net.RequestHelperImp;
import com.xhab.utils.utils.RxBus;

import androidx.annotation.Nullable;

/**
 * Created by dab on 2021/3/17 15:24
 * user_name
 * senderId
 * targetId
 * userId
 */
public class ChatActivity extends BaseChatActivity implements RequestHelperImp {
    @Override
    public String getSenderId() {
        return getIntent().getStringExtra("senderId");
    }

    @Override
    public String getTargetId() {
        return getIntent().getStringExtra("targetId");
    }

    @Override
    public String getUserId() {
        return getIntent().getStringExtra("userId");
    }

    @Override
    public void sendMsg(ChatMessage message) {
        RxBus.get().post(Const.RxType.TYPE_MSG_SEND, message);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleUser(getIntent().getStringExtra("user_name"));
        //修改消息状态
        addDisposable(RxBus.get().register(Const.RxType.TYPE_MSG_UPDATE, ChatMessage.class).subscribe(chatMessage -> {
            updateMsg(chatMessage.getUuid(), chatMessage.getSentStatus());
        }));
        addDisposable(RxBus.get().register(Const.RxType.TYPE_MSG_ADD, ChatMessage.class).subscribe(chatMessage -> {
            addMsg(chatMessage, false);
        }));


    }


    @Override
    public RequestHelperAgency initRequestHelper() {
        return new RequestHelperAgency(this);
    }
}
