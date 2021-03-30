package com.hezeyi.privatechat.activity.chat;

import android.content.Intent;
import android.os.Bundle;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
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
 * targetId
 * isGroup boolean 是否是群
 */
public class ChatActivity extends BaseChatActivity implements RequestHelperImp {
    @Override
    public String getSenderId() {
        return MyApplication.getInstance().getUserMsgBean().getUser_id();
    }

    @Override
    public String getTargetId() {
        return getIntent().getStringExtra("targetId");
    }

    @Override
    public boolean isGroup() {
        return getIntent().getBooleanExtra("isGroup", false);
    }

    @Override
    public String getUserId() {
        return MyApplication.getInstance().getUserMsgBean().getUser_id();
    }

    @Override
    public void sendMsg(ChatMessage message) {
        RxBus.get().post(Const.RxType.TYPE_MSG_SEND, message);
    }

    private ChatStatusListener mChatStatusListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String targetId = getIntent().getStringExtra("targetId");
        boolean isGroup = getIntent().getBooleanExtra("isGroup", false);
        String target_name;
        if (isGroup) {
            target_name = MyApplication.getInstance().getChatGroupBeanById(targetId).getGroup_name();
        } else {
            target_name = MyApplication.getInstance().getUserMsgBeanById(targetId).getUser_name();
        }
        setTitleUser(target_name);
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
            ChatMessage chatMessage = ChatMessage.getBaseSendMessage(MsgType.SYSTEM, MyApplication.getInstance().getUserMsgBean().getUser_id(), getIntent().getStringExtra("targetId"), getIntent().getBooleanExtra("isGroup", false));
            chatMessage.setMsg(MyApplication.getInstance().getUserMsgBean().getUser_name() + "进行了一次截图!");
            RxBus.get().post(Const.RxType.TYPE_MSG_SEND, chatMessage);
            addMsg(chatMessage, true);
            LogUtils.e(delete + "startScreenShotListen*****: " + chatMessage.getMsg());
        });
        mChatStatusListener.setOnBluetoothListener(s -> {
            ChatMessage chatMessage = ChatMessage.getBaseSendMessage(MsgType.SYSTEM, MyApplication.getInstance().getUserMsgBean().getUser_id(), getIntent().getStringExtra("targetId"), getIntent().getBooleanExtra("isGroup", false));
            chatMessage.setMsg(MyApplication.getInstance().getUserMsgBean().getUser_name() + s);
            LogUtils.e("onCreate*****: " + chatMessage.getMsg());
            RxBus.get().post(Const.RxType.TYPE_MSG_SEND, chatMessage);
            addMsg(chatMessage, true);
        });
        //群详情
        findViewById(R.id.iv_msg).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatGroupMsgActivity.class);
            intent.putExtra("group_id", getIntent().getStringExtra("targetId"));
            boolean isGroupAdmin = false;
            if (isGroup) {
                isGroupAdmin = MyApplication.getInstance().getChatGroupBeanById(targetId).getUser_type().equals("1");
            }
            intent.putExtra("isGroupAdmin", isGroupAdmin);
            startActivity(intent);
        });
        findViewById(com.xhab.chatui.R.id.rlLocation).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatVoiceActivity.class);
            intent.putExtra("isCall", true);
            intent.putExtra("targetId", getIntent().getStringExtra("targetId"));
            intent.putExtra("target_name", target_name);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
