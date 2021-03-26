package com.hezeyi.privatechat.fragment;

import android.content.Intent;
import android.view.View;

import com.hezeyi.privatechat.DataInMemory;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.chat.ChatActivity;
import com.hezeyi.privatechat.activity.chat.ChatGroupActivity;
import com.hezeyi.privatechat.adapter.BuddyAdapter;
import com.hezeyi.privatechat.base.BaseFragment;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.view.SuspendDecoration;
import com.xhab.utils.view.WaveSideBar;

import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by dab on 2021/3/9 09:54
 * 通讯录
 */
public class BuddyFragment extends BaseFragment {
    @Override
    public int viewLayoutID() {
        return R.layout.fragment_buddy;
    }

    private BuddyAdapter<UserMsgBean> mBuddyAdapter = new BuddyAdapter<>();
    private boolean isChange;

    @Override
    public void onVisibleToUser() {
        super.onVisibleToUser();
        if (isChange) {
            getUserList();
        }
    }

    @Override
    public void onInvisibleToUser() {
        super.onInvisibleToUser();

    }

    @Override
    public void onFirstVisibleToUser(View view) {
        setTitleString("通讯录");
        RecyclerView recyclerView = view.findViewById(R.id.rv_content);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mBuddyAdapter);
        recyclerView.addItemDecoration(new SuspendDecoration(getActivity()) {
            @Override
            public boolean isSameGroup(int priorGroupId, int nowGroupId) throws Exception {
                return Objects.equals(mBuddyAdapter.getDataList().get(priorGroupId).getSortableString(), mBuddyAdapter.getDataList().get(nowGroupId).getSortableString());
            }

            @Override
            public String showTitle(int position) throws Exception {
                return mBuddyAdapter.getDataList().get(position).getInitial();
            }
        });
        WaveSideBar waveSideBar = view.findViewById(R.id.side_bar);
        waveSideBar.setOnSelectIndexItemListener(index -> {
            for (int i = 0; i < mBuddyAdapter.getDataList().size(); i++) {
                if (mBuddyAdapter.getDataList().get(i).getSortableString().equals(index)) {
                    layoutManager.scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
        });
        mBuddyAdapter.setOnItemClickListener((view1, position, userMsgBean) -> {
            String user_id = DataInMemory.getInstance().getUserMsgBean().getUser_id();
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("target_name", userMsgBean.getUser_name());
            intent.putExtra("sender_name", DataInMemory.getInstance().getUserMsgBean().getUser_name());
            intent.putExtra("userId", userMsgBean.getUser_id());
            intent.putExtra("senderId", user_id);
            intent.putExtra("targetId", userMsgBean.getUser_id());
            startActivity(intent);
        });
        click(R.id.tv_group, v -> {
            String user_id = DataInMemory.getInstance().getUserMsgBean().getUser_id();
            Intent intent = new Intent(getActivity(), ChatGroupActivity.class);
            intent.putExtra("userId", user_id);
            startActivity(intent);
        });
        getUserList();
    }

    private void getUserList() {
        String user_id = DataInMemory.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, this, userMsgBeans -> {
            mBuddyAdapter.setDataList(userMsgBeans);
        });
    }
}
