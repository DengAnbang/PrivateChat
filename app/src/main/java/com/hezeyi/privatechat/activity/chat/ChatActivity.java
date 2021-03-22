package com.hezeyi.privatechat.activity.chat;

import android.os.Bundle;

import com.google.gson.Gson;
import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.chatui.activity.BaseChatActivity;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.chatui.bean.chat.MsgSendStatus;
import com.xhab.chatui.bean.chat.MsgType;
import com.xhab.utils.net.RequestHelperAgency;
import com.xhab.utils.net.RequestHelperImp;
import com.xhab.utils.utils.RxBus;
import com.xhab.utils.utils.RxUtils;

import androidx.annotation.Nullable;

/**
 * Created by dab on 2021/3/17 15:24
 * user_name
 * senderId
 * targetId
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
    public void sendMsg(ChatMessage message) {
        if (message.getMsgType() == MsgType.TEXT || message.getMsgType() == MsgType.SYSTEM) {
            MyApplication.getInstance().sendSendMsgBean(message, Const.RxType.TYPE_MSG_TEXT);
            updateMsg(message.getUuid(), MsgSendStatus.SENT);
        } else {
            String localPath = (message).getLocalPath();
            HttpManager.fileUpload(Const.FilePath.chatFileType, localPath, this, url -> {
                message.setRemoteUrl(url);
                MyApplication.getInstance().sendSendMsgBean(message, Const.RxType.TYPE_MSG_TEXT);
                updateMsg(message.getUuid(), MsgSendStatus.SENT);
            });
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleUser(getIntent().getStringExtra("user_name"));
        //收到文本消息
        addDisposable(RxBus.get().register(Const.RxType.TYPE_MSG_TEXT, String.class).subscribe(s -> {
            ChatMessage message = new Gson().fromJson(s, ChatMessage.class);
            downloadMsgFile(message);
            MyApplication.getInstance().msgSend(message.getSenderId(), message.getUuid());
        }));
        //对方收到消息
        addDisposable(RxBus.get().register(Const.RxType.TYPE_MSG_STATUS_SEND, String.class).subscribe(s -> {
            updateMsg(s, MsgSendStatus.RECEIVE);
        }));
    }

    private void downloadMsgFile(ChatMessage message) {
        if (message.getMsgType() == MsgType.TEXT
                || message.getMsgType() == MsgType.SYSTEM
                || message.getMsgType() == MsgType.FILE
        ) {
            addMsg(message);
            return;
        }
        String completePath = Const.FilePath.chatFileLocalPath + message.getRemoteUrl();
        HttpManager.downloadFileNew(Const.Api.API_HOST + message.getRemoteUrl(), completePath, this, aBoolean -> {
            message.setLocalPath(completePath);
            RxUtils.runOnUiThread(() -> addMsg(message));

        });

    }

    @Override
    public RequestHelperAgency initRequestHelper() {
        return new RequestHelperAgency(this);
    }
}
