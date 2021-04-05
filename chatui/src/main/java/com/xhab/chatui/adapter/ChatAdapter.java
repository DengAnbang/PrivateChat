package com.xhab.chatui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.xhab.chatui.R;
import com.xhab.chatui.bean.chat.ChatMessage;
import com.xhab.chatui.bean.chat.MsgSendStatus;
import com.xhab.chatui.bean.chat.MsgType;
import com.xhab.chatui.inteface.ShowUserImageCallback;
import com.xhab.chatui.utils.GlideUtils;

import java.io.File;
import java.util.List;


public class ChatAdapter extends BaseQuickAdapter<ChatMessage, BaseViewHolder> {

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
        setMultiTypeDelegate(new MultiTypeDelegate<ChatMessage>() {
            @Override
            protected int getItemType(ChatMessage entity) {
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
                }
                return 0;
            }
        });
        getMultiTypeDelegate().registerItemType(TYPE_SEND_TEXT, SEND_TEXT)
                .registerItemType(TYPE_RECEIVE_TEXT, RECEIVE_TEXT)
                .registerItemType(TYPE_SEND_IMAGE, SEND_IMAGE)
                .registerItemType(TYPE_RECEIVE_IMAGE, RECEIVE_IMAGE)
                .registerItemType(TYPE_SEND_VIDEO, SEND_VIDEO)
                .registerItemType(TYPE_RECEIVE_VIDEO, RECEIVE_VIDEO)
                .registerItemType(TYPE_SEND_FILE, SEND_FILE)
                .registerItemType(TYPE_RECEIVE_FILE, RECEIVE_FILE)
                .registerItemType(TYPE_SEND_AUDIO, SEND_AUDIO)
                .registerItemType(TYPE_RECEIVE_AUDIO, RECEIVE_AUDIO)
                .registerItemType(TYPE_SYSTEM, SEND_RECEIVE_SYSTEM);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatMessage item) {
        setContent(helper, item);
        setStatus(helper, item);
        setOnClick(helper, item);

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
                if (sentStatus == MsgSendStatus.SENDING) {
                    helper.setVisible(R.id.chat_item_progress, true).setVisible(R.id.chat_item_fail, false);
                } else if (sentStatus == MsgSendStatus.FAILED) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, true);
                } else if (sentStatus == MsgSendStatus.SENT) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
                } else if (sentStatus == MsgSendStatus.RECEIVE) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
                }
            }
        }


    }

    private void setContent(BaseViewHolder helper, ChatMessage item) {
        if (item.getMsgType() == (MsgType.TEXT)) {
            helper.setText(R.id.chat_item_content_text, item.getMsg());
        } else if (item.getMsgType() == (MsgType.IMAGE)) {
            if (TextUtils.isEmpty(item.getLocalPath())) {
                GlideUtils.loadChatImage(mContext, item.getRemoteUrl(), (ImageView) helper.getView(R.id.bivPic));
            } else {
                File file = new File(item.getLocalPath());
                if (file.exists()) {
                    GlideUtils.loadChatImage(mContext, item.getLocalPath(), (ImageView) helper.getView(R.id.bivPic));
                } else {
                    GlideUtils.loadChatImage(mContext, item.getRemoteUrl(), (ImageView) helper.getView(R.id.bivPic));
                }
            }
        } else if (item.getMsgType() == (MsgType.VIDEO)) {
            File file = new File(item.getLocalPath());
            if (file.exists()) {
                GlideUtils.loadChatImage(mContext, item.getLocalPath(), (ImageView) helper.getView(R.id.bivPic));
            } else {
                GlideUtils.loadChatImage(mContext, item.getLocalPath(), (ImageView) helper.getView(R.id.bivPic));
            }
        } else if (item.getMsgType() == (MsgType.FILE)) {

            helper.setText(R.id.msg_tv_file_name, item.getDisplayName());
            helper.setText(R.id.msg_tv_file_size, item.getSize() + "B");
        } else if (item.getMsgType() == (MsgType.AUDIO)) {
            helper.setText(R.id.tvDuration, item.getDuration() + "\"");
        } else if (item.getMsgType() == (MsgType.SYSTEM)) {
            helper.setText(R.id.chat_item_content_text, item.getMsg());
        }
        if (mShowImageCallback != null) {
            mShowImageCallback.showImage(item, helper.getView(R.id.chat_item_header));
        }


    }


    private void setOnClick(BaseViewHolder helper, ChatMessage item) {
        if (item.getMsgType() == MsgType.AUDIO) {
            helper.addOnClickListener(R.id.rlAudio);
        }
        if (item.getSentStatus() == (MsgSendStatus.FAILED)) {
            helper.addOnClickListener(R.id.chat_item_fail);
            helper.getView(R.id.chat_item_fail).setTag(item);
        }
        if (item.getMsgType() == (MsgType.IMAGE)) {
            helper.addOnClickListener(R.id.bivPic);
            helper.getView(R.id.bivPic).setTag(R.id.bivPic, item);
        }
        if (item.getMsgType() == (MsgType.FILE)) {
            helper.addOnClickListener(R.id.rc_message);
            helper.getView(R.id.rc_message).setTag(R.id.rc_message, item);
        }

    }

}
