package com.hezeyi.privatechat.activity.chat;

import android.os.Bundle;

import com.hezeyi.privatechat.Const;
import com.xhab.chatui.activity.BaseChatActivity;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.chatui.bean.chat.MsgType;
import com.xhab.utils.net.RequestHelperAgency;
import com.xhab.utils.net.RequestHelperImp;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.RxBus;

import java.io.File;

import androidx.annotation.Nullable;

/**
 * Created by dab on 2021/3/17 15:24
 * target_name
 * sender_name
 * senderId
 * targetId
 * userId
 * isGroup 是否是群
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

    private ChatStatusListener mChatStatusListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleUser(getIntent().getStringExtra("target_name"));
        mChatStatusListener = new ChatStatusListener(this);
        //修改消息状态
        addDisposable(RxBus.get().register(Const.RxType.TYPE_MSG_UPDATE, ChatMessage.class).subscribe(chatMessage -> {
            updateMsg(chatMessage.getUuid(), chatMessage.getSentStatus());
        }));
        addDisposable(RxBus.get().register(Const.RxType.TYPE_MSG_ADD, ChatMessage.class).subscribe(chatMessage -> {
            addMsg(chatMessage, false);
        }));
        mChatStatusListener.setOnScreenShotListener(imagePath -> {
            File file = new File(imagePath);
            boolean delete = file.delete();
            ChatMessage chatMessage = ChatMessage.getBaseSendMessage(MsgType.SYSTEM, getIntent().getStringExtra("senderId"), getIntent().getStringExtra("targetId"));
            chatMessage.setMsg(getIntent().getStringExtra("sender_name") + "进行了一次截图!");
            RxBus.get().post(Const.RxType.TYPE_MSG_SEND, chatMessage);
            addMsg(chatMessage, true);
            LogUtils.e(delete + "startScreenShotListen*****: " + chatMessage.getMsg());
        });
        mChatStatusListener.setOnBluetoothListener(s -> {
            ChatMessage chatMessage = ChatMessage.getBaseSendMessage(MsgType.SYSTEM, getIntent().getStringExtra("senderId"), getIntent().getStringExtra("targetId"));
            chatMessage.setMsg(getIntent().getStringExtra("sender_name") + s);
            LogUtils.e("onCreate*****: " + chatMessage.getMsg());
            RxBus.get().post(Const.RxType.TYPE_MSG_SEND, chatMessage);
            addMsg(chatMessage, true);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChatStatusListener.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
        mChatStatusListener.onPause();
    }


    @Override
    public RequestHelperAgency initRequestHelper() {
        return new RequestHelperAgency(this);
    }
}
