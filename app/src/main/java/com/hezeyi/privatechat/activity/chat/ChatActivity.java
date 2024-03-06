package com.hezeyi.privatechat.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.account.UserDetailsActivity;
import com.hezeyi.privatechat.bean.ChatGroupBean;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.service.VoiceService;
import com.juphoon.cloud.JCCallItem;
import com.abxh.chatui.activity.BaseChatActivity;
import com.abxh.chatui.bean.chat.ChatMessage;
import com.abxh.chatui.bean.chat.MsgType;
import com.abxh.chatui.dbUtils.ChatDatabaseHelper;
import com.abxh.chatui.inteface.ShowUserImageCallback;
import com.abxh.chatui.utils.FileUtils;
import com.abxh.chatui.utils.GlideUtils;
import com.abxh.chatui.voiceCalls.JuphoonUtils;
import com.abxh.utils.dialog.ProgressDialog;
import com.abxh.utils.net.RequestHelperAgency;
import com.abxh.utils.net.RequestHelperImp;
import com.abxh.utils.utils.FunUtils;
import com.abxh.utils.utils.LogUtils;
import com.abxh.utils.utils.RxBus;
import com.abxh.utils.utils.SPUtils;
import com.abxh.utils.utils.ToastUtil;
import com.xdandroid.hellodaemon.WakeUpReceiver;

import java.io.File;
import java.util.Objects;

import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2021/3/17 15:24
 * targetId
 * isGroup boolean 是否是群
 * msg 是否默认发送消息
 */
public class ChatActivity extends BaseChatActivity implements RequestHelperImp {
    public static final int REQUEST_CODE_CHAT_PWD = 5697;
    private Disposable disposable;

    @Override
    public String getSenderId() {
        return MyApplication.getInstance().getUserMsgBean().getUser_id();
    }

    public static void startChatActivity(Context packageContext, String targetId, boolean isGroup) {
        Intent intent = getStartChatActivity(packageContext, targetId, isGroup);
        packageContext.startActivity(intent);
    }

    public static Intent getStartChatActivity(Context packageContext, String targetId, boolean isGroup) {
        Intent intent = new Intent(packageContext, ChatActivity.class);
        intent.putExtra("isGroup", isGroup);
        intent.putExtra("targetId", targetId);
        return intent;
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
            if (imageView==null)return;
            UserMsgBean userMsgBeanById = MyApplication.getInstance().getUserMsgBeanById(item.getSenderId());
            if (userMsgBeanById == null) {
                HttpManager.userSelectById(item.getSenderId(), MyApplication.getInstance().getUserMsgBean().getUser_id(), false, this, userMsgBean -> {
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
        MyApplication.getInstance().setLock(true);
        String targetId = getIntent().getStringExtra("targetId");
        boolean isGroup = getIntent().getBooleanExtra("isGroup", false);
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();


        String msg = getIntent().getStringExtra("msg");
        if (!TextUtils.isEmpty(msg)) {
            sendTextMsg(msg);
        }

        //清空未读的标记
        ChatDatabaseHelper.get(this, getUserId()).clearChatListUnread(targetId);
        String target_name = "";
        if (isGroup) {
            ChatGroupBean chatGroupBeanById = MyApplication.getInstance().getChatGroupBeanById(targetId);
            if (chatGroupBeanById != null) {
                target_name = chatGroupBeanById.getGroup_name();
            } else {
                target_name = targetId;
                HttpManager.groupSelectList(getUserId(), false, this, chatGroupBeans -> {
                    MyApplication.getInstance().setChatGroupBeans(chatGroupBeans);
                    ChatGroupBean chatGroupBeanById1 = MyApplication.getInstance().getChatGroupBeanById(targetId);
                    setTitleUser(chatGroupBeanById1 == null ? targetId : chatGroupBeanById1.getGroup_name());
                });
            }
        } else {
            UserMsgBean userMsgBeanById = MyApplication.getInstance().getUserMsgBeanById(targetId);
            if (userMsgBeanById == null) {
                target_name = targetId;
                HttpManager.userSelectById(targetId, MyApplication.getInstance().getUserMsgBean().getUser_id(), false, this, userMsgBean -> {
                    MyApplication.getInstance().addUserMsgBeanById(userMsgBean);
                    setTitleUser(userMsgBean.getNickname());
                    verification(user_id, userMsgBean);

                });
            } else {
                target_name = userMsgBeanById.getNickname();
                verification(user_id, userMsgBeanById);
            }
        }
        //如果不是群聊,检查聊天码

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
            chatMessage.setMsg(MyApplication.getInstance().getUserMsgBean().getNickname() + "进行了一次截图!");
            RxBus.get().post(Const.RxType.TYPE_MSG_SEND, chatMessage);
            addMsg(chatMessage, true);
            LogUtils.e(delete + "startScreenShotListen*****: " + chatMessage.getMsg());
        });
        mChatStatusListener.setOnBluetoothListener(s -> {
            ChatMessage chatMessage = ChatMessage.getBaseSendMessage(MsgType.SYSTEM, MyApplication.getInstance().getUserMsgBean().getUser_id(), getIntent().getStringExtra("targetId"), getIntent().getBooleanExtra("isGroup", false));
            chatMessage.setMsg(MyApplication.getInstance().getUserMsgBean().getNickname() + s);
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
        findViewById(com.abxh.chatui.R.id.rlLocation).setOnClickListener(v -> {
            // 1. 获取当前活跃通话
            JCCallItem item = JuphoonUtils.get().getCall().getActiveCallItem();
            if (item != null) {
                ToastUtil.showToast("正在通话中,不能再次拨打电话!");
                return;
            }
            JuphoonUtils.get().call(getIntent().getStringExtra("targetId"), null);
            startService(new Intent(this, VoiceService.class));
            startActivity(new Intent(this, ChatVoiceActivity.class));
        });

    }

    private void verification(String user_id, UserMsgBean userMsgBean) {
        String chat_pwd = userMsgBean.getChat_pwd();
        String sp_key = user_id + "_" + userMsgBean.getUser_id();
        String string = SPUtils.getString(sp_key, "123456");
        if (!Objects.equals(chat_pwd, string)) {
            LogUtils.e("onCreate*****: 聊天码验证失败:" + chat_pwd + "本地:" + string);
            Intent intent = new Intent(this, ChatPwdVerificationActivity.class);
            intent.putExtra("chat_pwd", chat_pwd);
            intent.putExtra("sp_key", sp_key);
            startActivityForResult(intent, REQUEST_CODE_CHAT_PWD);
        } else {
            LogUtils.e("onCreate*****: 聊天码验证成功" + chat_pwd);
        }
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
        if (file.exists()) {
            FileUtils.openFile(message.getLocalPath(), this);
            return;
        }
        File file1 = new File(completePath);
        if (file1.exists()) {
            FileUtils.openFile(completePath, this);
        } else {
            FunUtils.affirm(this, "确定下载?", "确定", aBoolean -> {
                if (aBoolean) {
                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.show();
                    progressDialog.setOnDismissListener(dialog -> {
                        if (disposable != null) {
                            disposable.dispose();
                        }
                    });
                    disposable = HttpManager.downloadFileProgress(Const.Api.API_HOST + message.getRemoteUrl(), completePath, (msg, finish, e) -> {
                        if (e != null) {
                            ToastUtil.showToast(e.toString());
                            progressDialog.dismiss();
                            return;
                        }
                        if (!finish) {
                            progressDialog.setProgress(msg);
                        } else {
                            progressDialog.dismiss();
                            message.setLocalPath(completePath);
                            FileUtils.openFile(completePath, this);
                        }
                    });


                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChatStatusListener.onResume();
        //如果在这个页面,是这个人发来消息,则不显示通知
        MyApplication.getInstance().setAnotherId(getTargetId());
    }


    @Override
    protected void onPause() {
        super.onPause();
        mChatStatusListener.onPause();
        ChatDatabaseHelper.get(this, getUserId()).clearChatListUnread(getTargetId());
        MyApplication.getInstance().setAnotherId("");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHAT_PWD) {
            if (resultCode != RESULT_OK) {
                finish();
            }
        }
    }

    @Override
    public RequestHelperAgency initRequestHelper() {
        return new RequestHelperAgency(this);
    }
}
