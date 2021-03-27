package com.hezeyi.privatechat.activity.chat;

import android.content.Intent;

import com.hezeyi.privatechat.DataInMemory;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.SelectFriendsAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.view.SuspendDecoration;
import com.xhab.utils.view.WaveSideBar;

import java.util.ArrayList;
import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2021/3/27 16:11
 */
public class SelectFriendsActivity extends BaseActivity {

    private String mIds;
    private SelectFriendsAdapter mSelectFriendsAdapter = new SelectFriendsAdapter();

    @Override
    public int getContentViewRes() {
        return R.layout.activity_select_friends;
    }

    @Override
    public void initView() {
        super.initView();
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(mSelectFriendsAdapter);
        setTitleString("选择好友");
        setRightTitleString("确定", v -> {
            Disposable subscribe = Observable.fromIterable(mSelectFriendsAdapter.getDataList()).filter(UserMsgBean::isChoose)
                    .map(UserMsgBean::getId).toList().subscribe((strings, throwable) -> {
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("select_ids", (ArrayList<String>) strings);
                        setResult(RESULT_OK, intent);
                        finish();
                    });
            addDisposable(subscribe);
        });
        recyclerView.addItemDecoration(new SuspendDecoration(this) {
            @Override
            public boolean isSameGroup(int priorGroupId, int nowGroupId) throws Exception {
                return Objects.equals(mSelectFriendsAdapter.getDataList().get(priorGroupId).getSortableString(), mSelectFriendsAdapter.getDataList().get(nowGroupId).getSortableString());
            }

            @Override
            public String showTitle(int position) throws Exception {
                return mSelectFriendsAdapter.getDataList().get(position).getInitial();
            }
        });
        WaveSideBar waveSideBar = findViewById(R.id.side_bar);
        waveSideBar.setOnSelectIndexItemListener(index -> {
            for (int i = 0; i < mSelectFriendsAdapter.getDataList().size(); i++) {
                if (mSelectFriendsAdapter.getDataList().get(i).getSortableString().equals(index)) {
                    layout.scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        mIds = getIntent().getStringExtra("ids");
        String user_id = DataInMemory.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, this, userMsgBeans -> {
            Disposable subscribe = Observable.fromIterable(userMsgBeans).filter(userMsgBean -> !mIds.contains(userMsgBean.getUser_id()))
                    .toList().subscribe((userMsgBeans1, throwable) -> {
                        mSelectFriendsAdapter.setDataList(userMsgBeans1);
                    });
            addDisposable(subscribe);
        });
    }
}
