package com.hezeyi.privatechat.activity.account;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.BuddyAdapter;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by dab on 2021/4/6 21:30
 */
public class SelectUserActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private EditText mEditText;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_select_user;
    }

    private BuddyAdapter<UserMsgBean> mBuddyAdapter = new BuddyAdapter<>();

    @Override
    public void initView() {
        super.initView();
        mRecyclerView = findViewById(R.id.rv_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mBuddyAdapter);
        mEditText = findViewById(R.id.et_search);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        setTitleString("搜索用户");
        Disposable subscribe = RxTextView.textChanges(mEditText)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        visibility(R.id.ll_content, false);
                    } else {
                        visibility(R.id.ll_content, true);
                        setTextViewString(R.id.tv_name, "搜索:" + charSequence);
                    }
                }, Throwable::printStackTrace);
        addDisposable(subscribe);
        click(R.id.ll_content, v -> {
            String s = mEditText.getText().toString();
            HttpManager.userSelectByFuzzySearch(s, this, userMsgBeans -> {
                mBuddyAdapter.setDataList(userMsgBeans);
            });
        });
        mBuddyAdapter.setOnItemClickListener((view, position, userMsgBean) -> {
            String user_id = userMsgBean.getUser_id();
            Intent intent = new Intent(this, UserDetailsActivity.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        });
    }

    @Override
    public void initData() {
        super.initData();

//        HttpManager.userSelectById();
    }
}
