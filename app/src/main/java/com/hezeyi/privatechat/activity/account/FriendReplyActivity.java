package com.hezeyi.privatechat.activity.account;

import android.content.Intent;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.chat.ChatActivity;
import com.hezeyi.privatechat.adapter.FriendReplyAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.utils.RxBus;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/4/6 19:26
 */
public class FriendReplyActivity extends BaseActivity {
    private FriendReplyAdapter mFriendReplyAdapter = new FriendReplyAdapter();

    @Override
    public int getContentViewRes() {
        return R.layout.activity_friend_reply;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("好友申请");
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mFriendReplyAdapter);
    }

    @Override
    public void initData() {
        super.initData();
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, "2", this, userMsgBeans -> {
            mFriendReplyAdapter.setUserMsgBeans(userMsgBeans);
            MyApplication.getInstance().setHasNewFriend(userMsgBeans.size() != 0);
            RxBus.get().post(Const.RxType.TYPE_SHOW_FRIEND_RED_PROMPT, userMsgBeans.size());

        });
    }

    @Override
    public void initEvent() {
        super.initEvent();
        mFriendReplyAdapter.setOnItemClickListener((view, position, userMsgBean) -> {
            switch (view.getId()) {
                case R.id.iv_agree:
                    HttpManager.friendAdd(userMsgBean.getUser_id(), MyApplication.getInstance().getUserMsgBean().getUser_id(), "1", this, o -> {
                        initData();
                        MyApplication.getInstance().addUserMsgBeanById(userMsgBean);
                        Intent intent = ChatActivity.getStartChatActivity(this, userMsgBean.getUser_id(), false);
                        intent.putExtra("msg", "我已经通过你的好友申请啦!");
                        startActivity(intent);
                    });

                    break;
                case R.id.iv_refuse:
                    HttpManager.friendAdd(userMsgBean.getUser_id(), MyApplication.getInstance().getUserMsgBean().getUser_id(), "3", this, o -> {
                        initData();
                    });
                    break;
            }
        });
    }
}
