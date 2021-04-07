package com.hezeyi.privatechat.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.account.FriendReplyActivity;
import com.hezeyi.privatechat.activity.chat.ChatActivity;
import com.hezeyi.privatechat.activity.chat.ChatGroupActivity;
import com.hezeyi.privatechat.adapter.BuddyAdapter;
import com.hezeyi.privatechat.base.BaseFragment;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.jakewharton.rxbinding2.widget.RxTextView;
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
 * Created by dab on 2021/3/9 09:54
 * 通讯录
 */
public class BuddyFragment extends BaseFragment {

    private List<UserMsgBean> mUserMsgBeans = new ArrayList<>();


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
        getUserList();
    }

    @Override
    public void onInvisibleToUser() {
        super.onInvisibleToUser();

    }

    @Override
    public void onFirstVisibleToUser(View view) {
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
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("targetId", userMsgBean.getUser_id());
            startActivity(intent);
        });
        click(R.id.tv_group, v -> {
            String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
            Intent intent = new Intent(getActivity(), ChatGroupActivity.class);
            intent.putExtra("userId", user_id);
            startActivity(intent);
        });
        click(R.id.ll_request_friend, v -> {
            Intent intent = new Intent(getActivity(), FriendReplyActivity.class);
            startActivity(intent);
        });
        mBuddyAdapter.setDataList(MyApplication.getInstance().getUserMsgBeans());
        mUserMsgBeans = new ArrayList<>(MyApplication.getInstance().getUserMsgBeans());
        Disposable subscribe = RxTextView.textChanges(view.findViewById(R.id.et_search))
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        mBuddyAdapter.setDataList(mUserMsgBeans);
                    } else {
                        mBuddyAdapter.setDataList(search(charSequence.toString(), mUserMsgBeans));

                    }
                }, Throwable::printStackTrace);
        addDisposable(subscribe);
    }

    private List<UserMsgBean> search(String key, List<UserMsgBean> projectInfos) {
        List<UserMsgBean> showList = new ArrayList<>();
        for (UserMsgBean userMsgBean : projectInfos) {
            //名称
            if (userMsgBean.getUser_name().contains(key)) {
                showList.add(userMsgBean);
                continue;
            }
        }
        return showList;
    }

    private void getUserList() {
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, "1", false, this, userMsgBeans -> {
            MyApplication.getInstance().setUserMsgBeans(userMsgBeans);
            mUserMsgBeans = new ArrayList<>(userMsgBeans);
            mBuddyAdapter.setDataList(userMsgBeans);
        });
    }
}
