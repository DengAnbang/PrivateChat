package com.hezeyi.privatechat.fragment;


import android.text.TextUtils;
import android.view.View;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.chat.ChatActivity;
import com.hezeyi.privatechat.adapter.ChatListMessageAdapter;
import com.hezeyi.privatechat.base.BaseFragment;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.xhab.chatui.bean.chat.ChatListMessage;
import com.xhab.chatui.dbUtils.ChatDatabaseHelper;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.RxBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by dab on 2021/3/6 14:42
 */
public class ChatFragment extends BaseFragment {

    private List<ChatListMessage> mChatListMessages;

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
        mChatListMessages = ChatDatabaseHelper.get(getActivity(), user_id).chatListDbSelect();
        mChatListMessageAdapter.setListMessages(mChatListMessages);
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
        addDisposable(RxBus.get().register(Const.RxType.TYPE_FRIEND_CHANGE_SHOW, Object.class).subscribe(o -> {
            updateMsgList();
        }));
        Disposable subscribe = RxTextView.textChanges(view.findViewById(R.id.et_search))
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        mChatListMessageAdapter.setListMessages(mChatListMessages);
                    } else {
                        mChatListMessageAdapter.setListMessages(search(charSequence.toString(), mChatListMessages));

                    }
                }, Throwable::printStackTrace);
        addDisposable(subscribe);
    }
    private List<ChatListMessage> search(String key, List<ChatListMessage> projectInfos) {
        List<ChatListMessage> showList = new ArrayList<>();
        for (ChatListMessage userMsgBean : projectInfos) {
            //名称
            if (ChatListMessageAdapter.getShowName(userMsgBean).contains(key)) {
                showList.add(userMsgBean);
                continue;
            }
        }
        return showList;
    }

}
