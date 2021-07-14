package com.xhab.chatui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseDelegateMultiAdapter;
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xhab.chatui.R;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.chatui.bean.chat.MsgSendStatus;
import com.xhab.chatui.bean.chat.MsgType;
import com.xhab.chatui.inteface.ShowUserImageCallback;
import com.xhab.chatui.utils.FileUtils;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.chatui.utils.TimeShowUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;


public class ChatAdapter extends BaseDelegateMultiAdapter<ChatMessage, BaseViewHolder> {
    private static final long INTERVALS = 1000 * 60 * 5;//显示聊天信息的间隔时间
    private static final int TYPE_SYSTEM = 0;
    private static final int TYPE_SEND_TEXT = 1;
    private static final int TYPE_RECEIVE_TEXT = 2;
    private static final int TYPE_SEND_IMAGE = 3;
    private static final int TYPE_RECEIVE_IMAGE = 4;
    private static final int TYPE_SEND_VIDEO = 5;
    private static final int TYPE_RECEIVE_VIDEO = 6;
    private static final int TYPE_SEND_FILE = 7;
    private static final int TYPE_RECEIVE_FILE = 8;
    private static final int TYPE_SEND_AUDIO = 9;
    private static final int TYPE_RECEIVE_AUDIO = 10;
    private static final int TYPE_SEND_VOICE_CALLS = 11;
    private static final int TYPE_RECEIVE_VOICE_CALLS = 12;


    private static final int SEND_TEXT = R.layout.item_text_send;
    private static final int RECEIVE_TEXT = R.layout.item_text_receive;
    private static final int SEND_IMAGE = R.layout.item_image_send;
    private static final int RECEIVE_IMAGE = R.layout.item_image_receive;
    private static final int SEND_VIDEO = R.layout.item_video_send;
    private static final int RECEIVE_VIDEO = R.layout.item_video_receive;
    private static final int SEND_FILE = R.layout.item_file_send;
    private static final int RECEIVE_FILE = R.layout.item_file_receive;
    private static final int RECEIVE_AUDIO = R.layout.item_audio_receive;
    private static final int SEND_AUDIO = R.layout.item_audio_send;
    private static final int SEND_RECEIVE_SYSTEM = R.layout.item_send_receive_system;
    private static final int SEND_VOICE_CALLS = R.layout.item_text_send_voice_calls;
    private static final int RECEIVE_VOICE_CALLS = R.layout.item_text_receive_voice_calls;
    /*
    private static final int SEND_LOCATION = R.layout.item_location_send;
    private static final int RECEIVE_LOCATION = R.layout.item_location_receive;*/


    private String mSenderId;
    private ShowUserImageCallback mShowImageCallback;

    public void setShowImageCallback(ShowUserImageCallback showImageCallback) {
        mShowImageCallback = showImageCallback;
    }



    //设置发送的人
    public void setSenderId(String senderId) {
        mSenderId = senderId;
    }

    public ChatAdapter(Context context, List<ChatMessage> data) {
        super(data);
        setMultiTypeDelegate(new BaseMultiTypeDelegate<ChatMessage>() {
            @Override
            public int getItemType(@NotNull List<? extends ChatMessage> list, int i) {
                ChatMessage entity = list.get(i);
                boolean isSend = entity.getSenderId().equals(mSenderId);
                if (MsgType.TEXT == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_TEXT : TYPE_RECEIVE_TEXT;
                } else if (MsgType.IMAGE == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_IMAGE : TYPE_RECEIVE_IMAGE;
                } else if (MsgType.VIDEO == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_VIDEO : TYPE_RECEIVE_VIDEO;
                } else if (MsgType.FILE == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_FILE : TYPE_RECEIVE_FILE;
                } else if (MsgType.AUDIO == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_AUDIO : TYPE_RECEIVE_AUDIO;
                } else if (MsgType.SYSTEM == entity.getMsgType()) {
                    return isSend ? TYPE_SYSTEM : TYPE_SYSTEM;
                } else if (MsgType.VOICE_CALLS == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_VOICE_CALLS : TYPE_RECEIVE_VOICE_CALLS;
                }
                return 0;
            }
        });

        // 第二部，绑定 item 类型
        getMultiTypeDelegate()
                .addItemType(TYPE_SEND_TEXT, SEND_TEXT)
                .addItemType(TYPE_RECEIVE_TEXT, RECEIVE_TEXT)
                .addItemType(TYPE_SEND_IMAGE, SEND_IMAGE)
                .addItemType(TYPE_RECEIVE_IMAGE, RECEIVE_IMAGE)
                .addItemType(TYPE_SEND_VIDEO, SEND_VIDEO)
                .addItemType(TYPE_RECEIVE_VIDEO, RECEIVE_VIDEO)
                .addItemType(TYPE_SEND_FILE, SEND_FILE)
                .addItemType(TYPE_RECEIVE_FILE, RECEIVE_FILE)
                .addItemType(TYPE_SEND_AUDIO, SEND_AUDIO)
                .addItemType(TYPE_RECEIVE_AUDIO, RECEIVE_AUDIO)
                .addItemType(TYPE_SYSTEM, SEND_RECEIVE_SYSTEM)
                .addItemType(TYPE_SEND_VOICE_CALLS, SEND_VOICE_CALLS)
                .addItemType(TYPE_RECEIVE_VOICE_CALLS, RECEIVE_VOICE_CALLS);
//        getMultiTypeDelegate().registerItemType(TYPE_SEND_TEXT, SEND_TEXT)
//                .registerItemType(TYPE_RECEIVE_TEXT, RECEIVE_TEXT)
//                .registerItemType(TYPE_SEND_IMAGE, SEND_IMAGE)
//                .registerItemType(TYPE_RECEIVE_IMAGE, RECEIVE_IMAGE)
//                .registerItemType(TYPE_SEND_VIDEO, SEND_VIDEO)
//                .registerItemType(TYPE_RECEIVE_VIDEO, RECEIVE_VIDEO)
//                .registerItemType(TYPE_SEND_FILE, SEND_FILE)
//                .registerItemType(TYPE_RECEIVE_FILE, RECEIVE_FILE)
//                .registerItemType(TYPE_SEND_AUDIO, SEND_AUDIO)
//                .registerItemType(TYPE_RECEIVE_AUDIO, RECEIVE_AUDIO)
//                .registerItemType(TYPE_SYSTEM, SEND_RECEIVE_SYSTEM)
//                .registerItemType(TYPE_SEND_VOICE_CALLS, SEND_VOICE_CALLS)
//                .registerItemType(TYPE_RECEIVE_VOICE_CALLS, RECEIVE_VOICE_CALLS);

//        setMultiTypeDelegate(new MultiTypeDelegate<ChatMessage>() {
//            @Override
//            protected int getItemType(ChatMessage entity) {
//                boolean isSend = entity.getSenderId().equals(mSenderId);
//                if (MsgType.TEXT == entity.getMsgType()) {
//                    return isSend ? TYPE_SEND_TEXT : TYPE_RECEIVE_TEXT;
//                } else if (MsgType.IMAGE == entity.getMsgType()) {
//                    return isSend ? TYPE_SEND_IMAGE : TYPE_RECEIVE_IMAGE;
//                } else if (MsgType.VIDEO == entity.getMsgType()) {
//                    return isSend ? TYPE_SEND_VIDEO : TYPE_RECEIVE_VIDEO;
//                } else if (MsgType.FILE == entity.getMsgType()) {
//                    return isSend ? TYPE_SEND_FILE : TYPE_RECEIVE_FILE;
//                } else if (MsgType.AUDIO == entity.getMsgType()) {
//                    return isSend ? TYPE_SEND_AUDIO : TYPE_RECEIVE_AUDIO;
//                } else if (MsgType.SYSTEM == entity.getMsgType()) {
//                    return isSend ? TYPE_SYSTEM : TYPE_SYSTEM;
//                } else if (MsgType.VOICE_CALLS == entity.getMsgType()) {
//                    return isSend ? TYPE_SEND_VOICE_CALLS : TYPE_RECEIVE_VOICE_CALLS;
//                }
//                return 0;
//            }
//        });
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatMessage item) {
        setContent(helper, item);
        setStatus(helper, item);
    }


    private void setStatus(BaseViewHolder helper, ChatMessage item) {
        if (item.getMsgType() == MsgType.TEXT
                || item.getMsgType() == MsgType.AUDIO
                || item.getMsgType() == MsgType.VIDEO
                || item.getMsgType() == MsgType.FILE
                || item.getMsgType() == MsgType.IMAGE
        ) {
            //只需要设置自己发送的状态
            int sentStatus = item.getSentStatus();
            boolean isSend = item.getSenderId().equals(mSenderId);
            if (isSend) {
                helper.setText(R.id.tv_send_status, item.getMsgSendStatusString());
                if (sentStatus == MsgSendStatus.SENDING) {
                    helper.setVisible(R.id.chat_item_progress, true).setVisible(R.id.chat_item_fail, false);
                } else if (sentStatus == MsgSendStatus.FAILED) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
//                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, true);
                } else if (sentStatus == MsgSendStatus.SENT) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
                } else if (sentStatus == MsgSendStatus.RECEIVE) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
                }
            }
        }


    }

    private void setContent(BaseViewHolder helper, ChatMessage item) {
        int adapterPosition = helper.getBindingAdapterPosition();
        boolean isShowTime;
        if (!item.isMessage()) {
            isShowTime = false;
        } else if (adapterPosition == 0) {
            isShowTime = true;
        } else {
            ChatMessage chatMessage = getData().get(adapterPosition - 1);
            isShowTime = item.getSentTime() - chatMessage.getSentTime() > INTERVALS;
        }
        helper.getView(R.id.item_tv_time).setVisibility(isShowTime ? View.VISIBLE : View.GONE);
        if (isShowTime) {
            helper.setText(R.id.item_tv_time, TimeShowUtils.getNewChatTime(item.getSentTime()));
        }
        if (item.getMsgType() == (MsgType.TEXT)) {
            helper.setText(R.id.chat_item_content_text, item.getMsg());
        } else if (item.getMsgType() == (MsgType.IMAGE)) {
            if (TextUtils.isEmpty(item.getLocalPath())) {
                GlideUtils.loadChatImage(helper.itemView.getContext(), item.getRemoteUrl(), (ImageView) helper.getView(R.id.bivPic));
            } else {
                File file = new File(item.getLocalPath());
                if (file.exists()) {
                    GlideUtils.loadChatImage(helper.itemView.getContext(), item.getLocalPath(), (ImageView) helper.getView(R.id.bivPic));
                } else {
                    GlideUtils.loadChatImage(helper.itemView.getContext(), item.getRemoteUrl(), (ImageView) helper.getView(R.id.bivPic));
                }
            }
        } else if (item.getMsgType() == (MsgType.VIDEO)) {
            File file = new File(item.getLocalPath());
            if (file.exists()) {
                GlideUtils.loadChatImage(helper.itemView.getContext(), item.getLocalPath(), (ImageView) helper.getView(R.id.bivPic));
            } else {
                GlideUtils.loadChatImage(helper.itemView.getContext(), item.getLocalPath(), (ImageView) helper.getView(R.id.bivPic));
            }
        } else if (item.getMsgType() == (MsgType.FILE)) {

            helper.setText(R.id.msg_tv_file_name, item.getDisplayName());
            helper.setText(R.id.msg_tv_file_size, FileUtils.FormatFileSize(item.getSize()));

        } else if (item.getMsgType() == (MsgType.AUDIO)) {
            helper.setText(R.id.tvDuration, item.getDuration() + "\"");
        } else if (item.getMsgType() == (MsgType.SYSTEM)) {
            helper.setText(R.id.chat_item_content_text, item.getMsg());
        } else if (item.getMsgType() == (MsgType.VOICE_CALLS)) {
            helper.setText(R.id.chat_item_content_text, item.getExtra());
        }
        if (mShowImageCallback != null) {
            mShowImageCallback.showImage(item, helper.getViewOrNull(R.id.chat_item_header));
        }


    }



}
