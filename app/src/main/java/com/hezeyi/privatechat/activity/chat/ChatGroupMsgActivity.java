package com.hezeyi.privatechat.activity.chat;

import android.content.Intent;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.account.UserDetailsActivity;
import com.hezeyi.privatechat.adapter.ChatGroupMsgAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/3/26 16:40
 * group_id
 * isGroupAdmin
 */
public class ChatGroupMsgActivity extends BaseActivity {

    private String mGroup_id;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_chat_group_msg;
    }

    private static final int KEY_SELECT = 0x45;
    private ChatGroupMsgAdapter<UserMsgBean> mChatGroupMsgAdapter = new ChatGroupMsgAdapter<>();

    @Override
    public void initView() {
        super.initView();
        setTitleString("群成员");
        mChatGroupMsgAdapter.setAdmin(getIntent().getBooleanExtra("isGroupAdmin", false));
    }

    @Override
    public void initData() {
        super.initData();
        mGroup_id = getIntent().getStringExtra("group_id");

        HttpManager.groupSelectUserMsg(mGroup_id, this, userMsgBeans -> {
            mChatGroupMsgAdapter.setDataList(userMsgBeans);
        });
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerView.setAdapter(mChatGroupMsgAdapter);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        mChatGroupMsgAdapter.setItemClickListener((view, position, userMsgBean) -> {
            if (userMsgBean == null) {
                Intent intent = new Intent(this, ChatGroupAddActivity.class);
                intent.putExtra("group_id", mGroup_id);
                List<UserMsgBean> dataList = mChatGroupMsgAdapter.getDataList();
                StringBuilder ids = new StringBuilder();
                for (UserMsgBean msgBean : dataList) {
                    ids.append(msgBean.getId());
                    ids.append("#");
                }
                intent.putExtra("ids", ids.toString());
                startActivityForResult(intent, KEY_SELECT);
            }else {
                Intent intent = new Intent(this, UserDetailsActivity.class);
                intent.putExtra("user_id", userMsgBean.getUser_id());
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == KEY_SELECT) {
//            finish();
            initData();
        }
    }
}
