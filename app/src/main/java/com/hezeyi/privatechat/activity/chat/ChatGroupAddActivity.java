package com.hezeyi.privatechat.activity.chat;

import android.content.Intent;
import android.text.TextUtils;

import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.SelectFriendsAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
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
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2021/3/25 23:05
 * group_id 如果不填,就是先创建,再添加,如果填了,就是直接添加
 */
public class ChatGroupAddActivity extends BaseActivity {

    private String mGroup_id;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_chat_group_add;
    }


    private List<UserMsgBean> mUserMsgBeans = new ArrayList<>();
    private String mIds;
    private SelectFriendsAdapter mSelectFriendsAdapter = new SelectFriendsAdapter();


    @Override
    public void initView() {
        super.initView();
        setTitleString("发起群聊");
        mGroup_id = getIntent().getStringExtra("group_id");
        setRightTitleString("确定", v -> {
            submit(mGroup_id);
        });
        RecyclerView recyclerView = findViewById(R.id.rv_content);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mSelectFriendsAdapter);
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
                    layoutManager.scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
        });


    }

    private int i = 0;
    private String group_name = "";


    private void submit(String group_id) {
        Disposable subscribe = Observable.fromIterable(mSelectFriendsAdapter.getDataList()).filter(UserMsgBean::isChoose)
                .map(userMsgBean -> {
                    if (i <= 1) {
                        i = i + 1;
                        group_name = group_name + "、" + userMsgBean.getUser_name();
                    }
                    return userMsgBean.getUser_id() + "#";
                }).toList().subscribe((strings, throwable) -> {
                    if (TextUtils.isEmpty(group_id)) {
                        UserMsgBean userMsgBean = MyApplication.getInstance().getUserMsgBean();
                        HttpManager.groupRegister(userMsgBean.getUser_name() + group_name + "的群聊", userMsgBean.getUser_id(), this, chatGroupBean -> {
                            MyApplication.getInstance().setChatGroupBean(chatGroupBean);
                            String user_ids = strings.toString().replace("[", "").replace("]", "").replace(",", "").replace(" ", "");
                            HttpManager.groupAddUser(user_ids, chatGroupBean.getGroup_id(), this, o -> {
                                Intent intent = new Intent(this, ChatActivity.class);
                                ToastUtil.showToast("创建成功!");
                                intent.putExtra("targetId", chatGroupBean.getGroup_id());
                                intent.putExtra("isGroup", true);
                                startActivity(intent);
                                setResult(RESULT_OK);
                                finish();
                            });

                        });
                    } else {
                        String user_ids = strings.toString().replace("[", "").replace("]", "").replace(",", "");
                        HttpManager.groupAddUser(user_ids, group_id, this, o -> {
                            ToastUtil.showToast("添加成功!");
                            setResult(RESULT_OK);
                            finish();
                        });
                    }
                });
        addDisposable(subscribe);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        mSelectFriendsAdapter.setDataList(MyApplication.getInstance().getUserMsgBeans());
        mUserMsgBeans = new ArrayList<>(MyApplication.getInstance().getUserMsgBeans());
        Disposable subscribe = RxTextView.textChanges(findViewById(R.id.et_search))
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        mSelectFriendsAdapter.setDataList(mUserMsgBeans);
                    } else {
                        mSelectFriendsAdapter.setDataList(search(charSequence.toString(), mUserMsgBeans));

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

    @Override
    public void initData() {
        super.initData();
        mIds = getIntent().getStringExtra("ids");
        if (mIds == null) {
            mIds = "";
        }
        String user_id = MyApplication.getInstance().getUserMsgBean().getUser_id();
        HttpManager.userSelectFriend(user_id, this, userMsgBeans -> {

            Disposable subscribe = Observable.fromIterable(userMsgBeans).filter(userMsgBean -> !mIds.contains(userMsgBean.getUser_id()))
                    .toList().subscribe((userMsgBeans1, throwable) -> {
                        mUserMsgBeans = new ArrayList<>(userMsgBeans1);
//                        mSelectFriendsAdapter.setDataList(userMsgBeans1);
                    });
            addDisposable(subscribe);
        });
    }

}
