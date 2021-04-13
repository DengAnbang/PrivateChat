package com.hezeyi.privatechat.activity.account;

import android.content.Intent;
import android.widget.EditText;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.adapter.AccountListAdapter;
import com.hezeyi.privatechat.net.HttpManager;
import com.xhab.utils.base.BaseUtilActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/4/13 09:56
 */
public class AccountListActivity extends BaseUtilActivity {
    @Override
    public int getContentViewRes() {
        return R.layout.activity_account_list;
    }

    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private AccountListAdapter mAccountListAdapter = new AccountListAdapter();

    @Override
    public void initView() {
        super.initView();
        setTitleString("账号");
        mEditText = findViewById(R.id.et_search);
        mRecyclerView = findViewById(R.id.rv_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAccountListAdapter);
        setRightTitleString("搜索", v -> {
            String s = mEditText.getText().toString();
            HttpManager.userSelectByFuzzySearchAll(s, this, userMsgBeans -> {
                mAccountListAdapter.setUserMsgBeans(userMsgBeans);
            });
        });
    }

    @Override
    public void initEvent() {
        super.initEvent();
        mAccountListAdapter.setOnItemClickListener((view, position, userMsgBean) -> {
            Intent intent = new Intent(this, UserDetailsActivity.class);
            intent.putExtra("user_id", userMsgBean.getUser_id());
            startActivity(intent);
        });
    }
}
