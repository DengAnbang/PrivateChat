package com.hezeyi.privatechat.activity.chat;

import android.content.Intent;
import android.text.TextUtils;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.BuddyAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.ChatGroupBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.xhab.utils.utils.ToastUtil;
import com.xhab.utils.view.SuspendDecoration;
import com.xhab.utils.view.WaveSideBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2021/3/25 23:05
 * userId
 */
public class ChatGroupActivity extends BaseActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_chat_group;
    }

    private BuddyAdapter<ChatGroupBean> mBuddyAdapter = new BuddyAdapter<>();
    private List<ChatGroupBean> mChatGroupBeans=new ArrayList<>();
    @Override
    public void initData() {
        super.initData();
        String userId = getIntent().getStringExtra("userId");
        HttpManager.groupSelectList(userId, this, chatGroupBeans -> {
            mChatGroupBeans = chatGroupBeans;
            mBuddyAdapter.setDataList(chatGroupBeans);
        });
    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("群");

        RecyclerView recyclerView = findViewById(R.id.rv_content);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mBuddyAdapter);
        recyclerView.addItemDecoration(new SuspendDecoration(this) {
            @Override
            public boolean isSameGroup(int priorGroupId, int nowGroupId) throws Exception {
                return Objects.equals(mBuddyAdapter.getDataList().get(priorGroupId).getSortableString(), mBuddyAdapter.getDataList().get(nowGroupId).getSortableString());
            }

            @Override
            public String showTitle(int position) throws Exception {
                return mBuddyAdapter.getDataList().get(position).getInitial();
            }
        });
        WaveSideBar waveSideBar = findViewById(R.id.side_bar);
        waveSideBar.setOnSelectIndexItemListener(index -> {
            for (int i = 0; i < mBuddyAdapter.getDataList().size(); i++) {
                if (mBuddyAdapter.getDataList().get(i).getSortableString().equals(index)) {
                    layoutManager.scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
        });
        mBuddyAdapter.setOnItemClickListener((view1, position, userMsgBean) -> {
            String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("target_name", userMsgBean.getGroup_name());
            intent.putExtra("sender_name", MyApplication.getInstance().getUserMsgBean().getUser_name());
            intent.putExtra("userId", userMsgBean.getUser_id());
            intent.putExtra("senderId", user_id);
            intent.putExtra("targetId", userMsgBean.getGroup_id());
            intent.putExtra("isGroup", true);
            intent.putExtra("isGroupAdmin", userMsgBean.getUser_type().equals("1"));
            startActivity(intent);
        });

    }

    @Override
    public void initEvent() {
        super.initEvent();
        setRightTitleString("创建群", v -> {
            String userId = getIntent().getStringExtra("userId");
            HttpManager.groupRegister(userId, this, o -> {
                ToastUtil.showToast("创建成功!");
                initData();
            });
        });
        Disposable subscribe = RxTextView.textChanges(findViewById(R.id.et_search))
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        mBuddyAdapter.setDataList(mChatGroupBeans);
                    } else {
                        mBuddyAdapter.setDataList(search(charSequence.toString(), mChatGroupBeans));

                    }
                }, Throwable::printStackTrace);
        addDisposable(subscribe);
    }
    private List<ChatGroupBean> search(String key, List<ChatGroupBean> chatGroupBeans) {
        List<ChatGroupBean> showList = new ArrayList<>();
        for (ChatGroupBean chatGroupBean : chatGroupBeans) {
            //名称
            if (chatGroupBean.getGroup_name().contains(key)) {
                showList.add(chatGroupBean);
                continue;
            }
        }
        return showList;
    }
}
