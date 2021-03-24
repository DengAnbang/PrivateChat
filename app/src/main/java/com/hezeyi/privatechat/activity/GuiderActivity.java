package com.hezeyi.privatechat.activity;

import android.content.Intent;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.account.LoginActivity;
import com.xhab.utils.base.BaseGuiderUtilActivity;
import com.xhab.utils.utils.SPUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dab on 2021/3/8 15:39
 */
public class GuiderActivity extends BaseGuiderUtilActivity {
    @Override
    public List<Integer> setImages() {
        return Arrays.asList(R.mipmap.guider_1, R.mipmap.guider_2, R.mipmap.guider_3, R.mipmap.guider_4);
    }

    @Override
    public void next() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void isFirst() {
        boolean notFirst = SPUtils.getBoolean("notFirst");
        if (!notFirst) {
            SPUtils.save("notFirst", true);
        } else {
            next();

        }
    }
}
