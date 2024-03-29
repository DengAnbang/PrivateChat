package com.abxh.chatui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.ToastUtils;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.abxh.chatui.R;
import com.abxh.chatui.adapter.ChatAdapter;
import com.abxh.chatui.bean.chat.ChatMessage;
import com.abxh.chatui.bean.chat.MsgSendStatus;
import com.abxh.chatui.bean.chat.MsgType;
import com.abxh.chatui.dbUtils.ChatDatabaseHelper;
import com.abxh.chatui.inteface.ShowUserImageCallback;
import com.abxh.chatui.utils.ChatUiHelper;
import com.abxh.chatui.utils.FileUtils;
import com.abxh.chatui.utils.LogUtil;
import com.abxh.chatui.utils.PictureFileUtil;
import com.abxh.chatui.widget.MediaManager;
import com.abxh.chatui.widget.RecordButton;
import com.abxh.chatui.widget.StateButton;
import com.abxh.utils.utils.FunUtils;
import com.abxh.utils.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


/**
 * Created by dab on 2021/3/17 13:58
 */
public abstract class BaseChatActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final int REQUEST_CODE_IMAGE = 20;
    public static final int REQUEST_CODE_VEDIO = 1111;
    public static final int REQUEST_CODE_FILE = 2222;

    private ChatAdapter mAdapter;
//    private ChatAdapterNew mAdapterNew=new ChatAdapterNew();

    private RecyclerView mRvChat;
    private SwipeRefreshLayout mSwipeRefresh;//下拉刷新
    private ImageView ivAudio;//录音图片

    public abstract String getSenderId();

    public abstract String getTargetId();

    public abstract boolean isGroup();

    public abstract String getUserId();

    public abstract ShowUserImageCallback getShowImageCallback();

    //发送消息
    public abstract void sendMsg(ChatMessage message);

    public void setTitleUser(String title) {
        TextView textView = findViewById(R.id.common_toolbar_title);
        if (TextUtils.isEmpty(title)) {
            title = "联系人";
        }
        textView.setText(title);
    }


    private String mSenderId;
    private String mTargetId;
    private boolean isGroup;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mTargetId = getTargetId();
        mSenderId = getSenderId();
        isGroup = isGroup();
        mRvChat = findViewById(R.id.rv_chat_list);
        mSwipeRefresh = findViewById(R.id.swipe_chat);
        initContent();
        onRefresh();

    }

    public void initContent() {
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.iv_msg).setVisibility(isGroup ? View.VISIBLE : View.GONE);
        findViewById(R.id.rlLocation).setVisibility(isGroup ? View.GONE : View.VISIBLE);
        findViewById(R.id.rlVideo).setVisibility(View.GONE);
        mAdapter = new ChatAdapter(this, new ArrayList<ChatMessage>());
        mAdapter.setSenderId(mSenderId);
        mAdapter.setShowImageCallback(getShowImageCallback());
        mAdapter.addChildClickViewIds(R.id.rlAudio, R.id.chat_item_fail, R.id.bivPic, R.id.rc_message,R.id.chat_item_header);
        // 先注册需要长按的子控件id（注意，请不要写在convert方法里）
        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            FunUtils.affirm(this, "是否删除?", "删除", aBoolean -> {
                if (aBoolean) {
                    LogUtils.e("是否删除*****: " + position);
                    removedMessage((ChatMessage)adapter.getData().get(position), position);
                }
            });
            return true;
        });

        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        mRvChat.setLayoutManager(mLinearLayout);
        mRvChat.setAdapter(mAdapter);
//        mRvChat.setAdapter(mAdapterNew);
        mSwipeRefresh.setOnRefreshListener(this);
        initChatUi();
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.rlAudio) {
                final boolean isSend = mAdapter.getItem(position).getSenderId().equals(mSenderId);
                if (ivAudio != null) {
                    if (isSend) {
                        ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                    } else {
                        ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
                    }
                    ivAudio = null;
                    MediaManager.reset();
                } else {
                    ivAudio = view.findViewById(R.id.ivAudio);
                    MediaManager.reset();
                    if (isSend) {
                        ivAudio.setBackgroundResource(R.drawable.audio_animation_right_list);
                    } else {
                        ivAudio.setBackgroundResource(R.drawable.audio_animation_left_list);
                    }
                    AnimationDrawable drawable = (AnimationDrawable) ivAudio.getBackground();
                    drawable.start();
                    MediaManager.playSound(BaseChatActivity.this, (mAdapter.getData().get(position)).getLocalPath(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (isSend) {
                                ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                            } else {
                                ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
                            }

                            MediaManager.release();
                        }
                    });
                }
            } else if (view.getId() == R.id.chat_item_fail) {
                ChatMessage message = mAdapter.getData().get(position);
                updateMsg(message.getUuid(), MsgSendStatus.SENDING);
                sendMsg(message);
            } else if (view.getId() == R.id.bivPic) {
                ChatMessage message = mAdapter.getData().get(position);
                Intent intent = new Intent(this, ZoomImageActivity.class);
                intent.putExtra("url", message.getLocalPath());
                startActivity(intent);
//                FunUtils.showPicture(this, message.getLocalPath());
            } else if (view.getId() == R.id.rc_message) {
                ChatMessage message = mAdapter.getData().get(position);
                openFile(message);
            } else if (view.getId() == R.id.chat_item_header) {
                ChatMessage message = mAdapter.getData().get(position);
                clickUser(message);
            }

        });
//        mAdapter.setItemLongClickListener((view, position, message) -> {
//            FunUtils.affirm(this, "是否删除?", "删除", aBoolean -> {
//                if (aBoolean) {
//                    LogUtils.e("是否删除*****: " + position);
//                    removedMessage(message, position);
//                }
//            });
//            return true;
//        });

    }

    private void removedMessage(ChatMessage message, int position) {
        ChatMessage lastMessage = getLastMessage(position);
        ChatDatabaseHelper.get(this, getUserId()).chatDbDelete(message, lastMessage);
        mAdapter.getData().remove(message);
        mAdapter.notifyItemRemoved(position);
    }

    private ChatMessage getLastMessage(int position) {
        List<ChatMessage> data = mAdapter.getData();

        //删除的下一条如果有有效消息,则不修改
        for (int i = position + 1; i < data.size(); i++) {
            ChatMessage chatMessage = data.get(i);
            if (chatMessage.isMessage()) {
                return null;
            }
        }
        //删除的上一条有效消息,加入外面的列表
        for (int i = position - 1; i >= 0; i--) {
            ChatMessage chatMessage = data.get(i);
            if (chatMessage.isMessage()) {
                return chatMessage;
            }
        }
        return null;
    }

    public void clickUser(ChatMessage message) {

    }

    public void openFile(ChatMessage message) {

    }

    private StateButton mBtnSend;//发送按钮
    private EditText mEtContent;
    private RelativeLayout mRlBottomLayout;//表情,添加底部布局
    private ImageView mIvAdd;
    private ImageView mIvEmo;
    private ImageView mIvAudio;//录音图片
    private RecordButton mBtnAudio;//录音按钮
    private LinearLayout mLlAdd;//添加布局
    private LinearLayout mLlEmotion;//表情布局

    @SuppressLint("ClickableViewAccessibility")
    private void initChatUi() {
        mBtnSend = findViewById(R.id.btn_send);
        mEtContent = findViewById(R.id.et_content);
        mRlBottomLayout = findViewById(R.id.bottom_layout);
        mLlEmotion = findViewById(R.id.rlEmotion);
        mLlAdd = findViewById(R.id.llAdd);
        mBtnAudio = findViewById(R.id.btnAudio);
        mIvAudio = findViewById(R.id.ivAudio);
        mIvEmo = findViewById(R.id.ivEmo);
        mIvAdd = findViewById(R.id.ivAdd);
        final ChatUiHelper mUiHelper = ChatUiHelper.with(this);
        mUiHelper.bindContentLayout(findViewById(R.id.llContent))
                .bindttToSendButton(mBtnSend)
                .bindEditText(mEtContent)
                .bindBottomLayout(mRlBottomLayout)
                .bindEmojiLayout(mLlEmotion)
                .bindAddLayout(mLlAdd)
                .bindToAddButton(mIvAdd)
                .bindToEmojiButton(mIvEmo)
                .bindAudioBtn(mBtnAudio)
                .bindAudioIv(mIvAudio)
                .bindEmojiData();
        //底部布局弹出,聊天列表上滑
        mRvChat.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                mRvChat.post(() -> {
                    if (mAdapter.getItemCount() > 0) {
                        mRvChat.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                    }
                });
            }
        });
        //点击空白区域关闭键盘
        mRvChat.setOnTouchListener((view, motionEvent) -> {
            mUiHelper.hideBottomLayout(false);
            mUiHelper.hideSoftInput();
            mEtContent.clearFocus();
            mIvEmo.setImageResource(R.mipmap.ic_emoji);
            return false;
        });

        //
        mBtnAudio.setOnFinishedRecordListener((audioPath, time) -> {
            LogUtil.d("录音结束回调");
            File file = new File(audioPath);
            if (file.exists()) {
                sendAudioMessage(audioPath, time);
            }
        });
        findViewById(R.id.btn_send).setOnClickListener(v -> {
            sendTextMsg(mEtContent.getText().toString());
            mEtContent.setText("");
        });
        findViewById(R.id.rlPhoto).setOnClickListener(v -> {
            PictureFileUtil.openGalleryPic(this, REQUEST_CODE_IMAGE);
        });

        findViewById(R.id.rlVideo).setOnClickListener(v -> {
            PictureFileUtil.openGalleryAudio(this, REQUEST_CODE_VEDIO);
        });
        findViewById(R.id.rlFile).setOnClickListener(v -> {
            PictureFileUtil.openFile(this, REQUEST_CODE_FILE);
        });
        findViewById(R.id.rlLocation).setOnClickListener(v -> {
            ToastUtils.s(this, "语音通话按钮");
        });


    }


    //文件消息
    private void sendFileMessage(final String path) {
        ChatMessage mFileMsgBody = ChatMessage.getBaseSendMessage(MsgType.FILE, mSenderId, mTargetId, isGroup);
        mFileMsgBody.setLocalPath(path);
        mFileMsgBody.setDisplayName(FileUtils.getFileName(path));
        mFileMsgBody.setSize(FileUtils.getFileLength(path) + "");
        //开始发送
        addMsg(mFileMsgBody, true);
        sendMsg(mFileMsgBody);
    }

    //语音消息
    private void sendAudioMessage(final String path, int time) {
        ChatMessage mFileMsgBody = ChatMessage.getBaseSendMessage(MsgType.AUDIO, mSenderId, mTargetId, isGroup);
        mFileMsgBody.setLocalPath(path);
        mFileMsgBody.setDuration(time + "");
        //开始发送
        addMsg(mFileMsgBody, true);
        sendMsg(mFileMsgBody);
    }


    //文本消息
    public void sendTextMsg(String hello) {
        ChatMessage mTextMsgBody = ChatMessage.getBaseSendMessage(MsgType.TEXT, mSenderId, mTargetId, isGroup);
        mTextMsgBody.setMsg(hello);
        //开始发送
        addMsg(mTextMsgBody, true);
        sendMsg(mTextMsgBody);
    }


    //图片消息
    private void sendImageMessage(final LocalMedia media) {
        ChatMessage mImageMsgBody = ChatMessage.getBaseSendMessage(MsgType.IMAGE, mSenderId, mTargetId, isGroup);
        mImageMsgBody.setLocalPath(media.getCompressPath());
        //开始发送
        addMsg(mImageMsgBody, true);
        sendMsg(mImageMsgBody);
    }


    //视频消息
    private void sendVideoMessage(final LocalMedia media) {
        try {
            ChatMessage mImageMsgBody = ChatMessage.getBaseSendMessage(MsgType.VIDEO, mSenderId, mTargetId, isGroup);
            //生成缩略图路径
            String vedioPath = media.getRealPath() == null ? media.getPath() : media.getRealPath();
//            String vedioPath = media.getPath();
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//            mediaMetadataRetriever.setDataSource(vedioPath);
            mediaMetadataRetriever.setDataSource(this, Uri.parse(vedioPath));
            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime();
            String imgname = System.currentTimeMillis() + ".jpg";
            String urlpath = Environment.getExternalStorageDirectory() + "/" + imgname;
            File f = new File(urlpath);
            try {
                if (f.exists()) {
                    f.delete();
                }
                FileOutputStream out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                LogUtil.d("视频缩略图路径获取失败：" + e.toString());
                e.printStackTrace();
            }
            mImageMsgBody.setExtra(vedioPath);
            mImageMsgBody.setLocalPath(urlpath);

            //开始发送
            addMsg(mImageMsgBody, true);
            sendMsg(mImageMsgBody);
        } catch (Exception e) {
            LogUtil.d("视频缩略图路径获取失败：" + e.toString());
            e.printStackTrace();
        }


    }


    public void updateMsg(String uuid, @MsgSendStatus int msgSendStatus) {
        int position = 0;
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            ChatMessage mAdapterMessage = mAdapter.getData().get(i);
            if (uuid.equals(mAdapterMessage.getUuid())) {
                position = i;
                mAdapterMessage.setSentStatus(msgSendStatus);
                mAdapter.notifyItemChanged(position);
                return;
            }
        }
    }

    public void addMsg(ChatMessage message, boolean isAddDb) {
        if (message.getTargetId().equals(mTargetId)//我发出去的当前消息(群聊和单聊)
                || (message.getTargetId().equals(mSenderId) && message.getSenderId().equals(mTargetId))//单聊的情况下,当前发给我的消息
        ) {
            mAdapter.addData(message);
            mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
        }
        if (isAddDb) {
            ChatDatabaseHelper.get(this, getUserId()).chatDbInsert(message);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FILE:
                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    LogUtil.d("获取到的文件路径:" + filePath);
                    sendFileMessage(filePath);
                    break;
                case REQUEST_CODE_IMAGE:
                    // 图片选择结果回调
                    List<LocalMedia> selectListPic = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectListPic) {
                        LogUtil.d("获取图片路径成功:" + media.getPath());
                        sendImageMessage(media);
                    }
                    break;
                case REQUEST_CODE_VEDIO:
                    // 视频选择结果回调
                    List<LocalMedia> selectListVideo = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectListVideo) {
                        LogUtil.d("获取视频路径成功:" + media.getPath());
                        sendVideoMessage(media);
                    }
                    break;
            }
        }
    }

    @Override
    public void onRefresh() {
        new Thread(() -> {
            String targetId = getTargetId();
            List<ChatMessage> chatMessages = ChatDatabaseHelper.get(this, getUserId()).chatMsgSelect(mAdapter.getData(), targetId);
            mSwipeRefresh.post(() -> {
                mAdapter.addData(0, chatMessages);
//                mAdapterNew.setChatMessages(chatMessages);
                mSwipeRefresh.setRefreshing(false);
                mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
            });
        }).start();

    }

}
