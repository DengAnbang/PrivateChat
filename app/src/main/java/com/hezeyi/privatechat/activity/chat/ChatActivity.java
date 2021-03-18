package com.hezeyi.privatechat.activity.chat;

import android.os.Bundle;

import com.google.gson.Gson;
import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.xhab.chatui.activity.BaseChatActivity;
import com.xhab.chatui.bean.chat.Message;
import com.xhab.chatui.bean.chat.MsgSendStatus;
import com.xhab.chatui.bean.chat.TextMsgBody;
import com.xhab.utils.utils.RxBus;

import androidx.annotation.Nullable;
import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2021/3/17 15:24
 * user_name
 * senderId
 * targetId
 */
public class ChatActivity extends BaseChatActivity {
    @Override
    public String getSenderId() {
        return getIntent().getStringExtra("senderId");
    }

    @Override
    public String getTargetId() {
        return getIntent().getStringExtra("targetId");
    }

    @Override
    public void sendMsg(Message message) {
        MyApplication.getInstance().sendSendMsgBean(message);

//        int i = new Random().nextInt(2);
//        new Handler().postDelayed(() -> updateMsg(message.getUuid(), i != 1 ? MsgSendStatus.FAILED : MsgSendStatus.SENT), 2000);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleUser(getIntent().getStringExtra("user_name"));
        Disposable subscribe = RxBus.get().register(Const.RxType.TYPE_MSG_TEXT, String.class).subscribe(s -> {
            Message message = new Gson().fromJson(s, TextMsgBody.class);
            addMsg(message);
            MyApplication.getInstance().msgSend(message.getSenderId(), message.getUuid());
        });
        Disposable subscribe1 = RxBus.get().register(Const.RxType.TYPE_MSG_STATUS_SEND, String.class).subscribe(s -> {
            updateMsg(s, MsgSendStatus.SENT);
        });

    }

}
