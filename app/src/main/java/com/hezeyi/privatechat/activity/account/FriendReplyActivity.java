package com.hezeyi.privatechat.activity.account;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.FriendReplyAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.net.HttpManager;

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
        });
    }

    @Override
    public void initEvent() {
        super.initEvent();
        mFriendReplyAdapter.setOnItemClickListener((view, position, userMsgBean) -> {
            switch (view.getId()) {
                case R.id.tv_agree:
                    HttpManager.addFriend(userMsgBean.getUser_id(), MyApplication.getInstance().getUserMsgBean().getUser_id(), "1", this, o -> {
                        initData();
                    });
                    break;
                case R.id.tv_refuse:
                    HttpManager.addFriend(userMsgBean.getUser_id(), MyApplication.getInstance().getUserMsgBean().getUser_id(), "3", this, o -> {
                        initData();
                    });
                    break;
            }
        });
    }
}
