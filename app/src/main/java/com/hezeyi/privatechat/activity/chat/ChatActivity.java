package com.hezeyi.privatechat.activity.chat;

import android.content.Intent;
import android.os.Bundle;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.account.UserDetailsActivity;
import com.hezeyi.privatechat.bean.ChatGroupBean;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.chatui.activity.BaseChatActivity;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.chatui.bean.chat.MsgType;
import com.xhab.chatui.inteface.ShowUserImageCallback;
import com.xhab.chatui.utils.FileUtils;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.utils.net.RequestHelperAgency;
import com.xhab.utils.net.RequestHelperImp;
import com.xhab.utils.utils.FunUtils;
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
    public ShowUserImageCallback getShowImageCallback() {
        return (item, imageView) -> {
            UserMsgBean userMsgBeanById = MyApplication.getInstance().getUserMsgBeanById(item.getSenderId());
            if (userMsgBeanById == null) {
                HttpManager.userSelectById(item.getSenderId(), false, this, userMsgBean -> {
                    MyApplication.getInstance().addUserMsgBeanById(userMsgBean);
                    GlideUtils.loadHeadPortrait(userMsgBean.getShowPortrait(), imageView, userMsgBean.getPlaceholder());
                });
            } else {
                GlideUtils.loadHeadPortrait(userMsgBeanById.getShowPortrait(), imageView, userMsgBeanById.getPlaceholder());
            }
        };
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
        String target_name = "";
        if (isGroup) {
            ChatGroupBean chatGroupBeanById = MyApplication.getInstance().getChatGroupBeanById(targetId);
            if (chatGroupBeanById != null) {
                target_name = chatGroupBeanById.getGroup_name();
            } else {
                target_name = targetId;
            }
        } else {
            UserMsgBean userMsgBeanById = MyApplication.getInstance().getUserMsgBeanById(targetId);
            target_name = userMsgBeanById.getUser_name();
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
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }

    @Override
    public void clickUser(ChatMessage message) {
        super.clickUser(message);
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra("user_id", message.getSenderId());
        startActivity(intent);
    }

    @Override
    public void openFile(ChatMessage message) {
        super.openFile(message);
        String completePath = Const.FilePath.chatFileLocalPath + message.getRemoteUrl();
        File file = new File(message.getLocalPath());
        File file1 = new File(completePath);
        if ((file.exists()) || file1.exists()) {
            FileUtils.openFile(completePath, this);
        } else {
            FunUtils.affirm(this, "确定下载?", "确定", aBoolean -> {
                if (aBoolean) {
                    addDisposable(HttpManager.downloadFileNew(Const.Api.API_HOST + message.getRemoteUrl(), completePath, aBoolean1 -> {
                        message.setLocalPath(completePath);
                        FileUtils.openFile(completePath, this);
                    }));
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChatStatusListener.onResume();
        MyApplication.getInstance().setAnotherId(getTargetId());
    }


    @Override
    protected void onPause() {
        super.onPause();
        mChatStatusListener.onPause();
        MyApplication.getInstance().setAnotherId("");

    }


    @Override
    public RequestHelperAgency initRequestHelper() {
        return new RequestHelperAgency(this);
    }
}
