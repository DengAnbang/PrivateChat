package com.hezeyi.privatechat.activity.chat;

import android.content.Intent;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.ChatGroupMsgAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;

import java.util.ArrayList;
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
        String group_id = getIntent().getStringExtra("group_id");

        HttpManager.groupSelectUserMsg(group_id, this, userMsgBeans -> {
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
                Intent intent = new Intent(this, SelectFriendsActivity.class);
                List<UserMsgBean> dataList = mChatGroupMsgAdapter.getDataList();
                StringBuilder ids = new StringBuilder();
                for (UserMsgBean msgBean : dataList) {
                    ids.append(msgBean.getId());
                    ids.append("#");
                }
                intent.putExtra("ids", ids.toString());
                startActivityForResult(intent, KEY_SELECT);
            }
        });
    }

    private int i = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == KEY_SELECT && data != null) {
            ArrayList<String> select_ids = data.getStringArrayListExtra("select_ids");
            for (String s : select_ids) {
                HttpManager.groupAddUser(s, getIntent().getStringExtra("group_id"), this, o -> {
                    i = i + 1;
                    if (i == select_ids.size()) {
                        initData();
                    }
                });
            }

        }
    }
}
