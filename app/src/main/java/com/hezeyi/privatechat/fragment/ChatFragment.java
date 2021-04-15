package com.hezeyi.privatechat.fragment;


import android.view.View;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.chat.ChatActivity;
import com.hezeyi.privatechat.adapter.ChatListMessageAdapter;
import com.hezeyi.privatechat.base.BaseFragment;
import com.xhab.chatui.bean.chat.ChatListMessage;
import com.xhab.chatui.dbUtils.ChatDatabaseHelper;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.RxBus;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by dab on 2021/3/6 14:42
 */
public class ChatFragment extends BaseFragment {

    @Override
    public int viewLayoutID() {
        return R.layout.fragment_chat;
    }

    private ChatListMessageAdapter mChatListMessageAdapter = new ChatListMessageAdapter();

    @Override
    public void onVisibleToUser() {
        super.onVisibleToUser();
        updateMsgList();

    }

    private void updateMsgList() {
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        List<ChatListMessage> chatListMessages = ChatDatabaseHelper.get(getActivity(), user_id).chatListDbSelect();
        mChatListMessageAdapter.setListMessages(chatListMessages);
    }

    @Override
    public void onInvisibleToUser() {
        super.onInvisibleToUser();
    }

    @Override
    public void onFirstVisibleToUser(View view) {
        super.onFirstVisibleToUser(view);
        RecyclerView recyclerView = view.findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mChatListMessageAdapter);
        mChatListMessageAdapter.setItemClickListener((view1, position, chatListMessage) -> {
            String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
            ChatActivity.startChatActivity(getActivity(), chatListMessage.getAnotherId(user_id), chatListMessage.getIs_group() == 1);
        });
        mChatListMessageAdapter.setItemLongClickListener((view1, position, chatListMessage) -> {
            FunUtils.affirm(getActivity(), "是否删除?", "删除", aBoolean -> {
                if (aBoolean) {
                    ChatDatabaseHelper.get(getActivity(), MyApplication.getInstance().getUserMsgBean().getUser_id()).chatListDelete(chatListMessage.getAnotherId(MyApplication.getInstance().getUserMsgBean().getUser_id()) + "");
                    updateMsgList();
                }
            });
            return true;
        });
        updateMsgList();
        addDisposable(RxBus.get().register(Const.RxType.TYPE_SHOW_LIST, Object.class).subscribe(o -> {
            updateMsgList();
        }));

    }

}
