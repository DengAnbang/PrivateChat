package com.hezeyi.privatechat.activity.chat;

import android.os.Bundle;
import android.os.Handler;

import com.xhab.chatui.activity.BaseChatActivity;
import com.xhab.chatui.bean.chat.Message;
import com.xhab.chatui.bean.chat.MsgSendStatus;

import java.util.Random;

import androidx.annotation.Nullable;

/**
 * Created by dab on 2021/3/17 15:24
 * user_name
 */
public class ChatActivity extends BaseChatActivity {
    @Override
    public String getSenderId() {
        return "right";
    }

    @Override
    public String getTargetId() {
        return "left";
    }

    @Override
    public void sendMsg(Message message) {
        int i = new Random().nextInt(2);
        new Handler().postDelayed(() -> updateMsg(message.getUuid(), i != 1 ? MsgSendStatus.FAILED : MsgSendStatus.SENT), 2000);
    }

    @Override
    public void updateMsg(String uuid, MsgSendStatus msgSendStatus) {
        super.updateMsg(uuid, msgSendStatus);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitleUser(getIntent().getStringExtra("user_name"));
    }

}
