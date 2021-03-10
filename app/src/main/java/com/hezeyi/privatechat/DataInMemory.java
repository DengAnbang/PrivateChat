package com.hezeyi.privatechat;

import com.hezeyi.privatechat.bean.UserMsgBean;

/**
 * Created by dab on 2018/4/18 09:42
 */

public class DataInMemory {
    private static DataInMemory sInstance;

    public static DataInMemory getInstance() {
        if (sInstance == null) {
            synchronized (DataInMemory.class) {
                if (sInstance == null) {
                    sInstance = new DataInMemory();
                }
            }
        }
        return sInstance;
    }

    public void clear() {
        sInstance = new DataInMemory();
    }

    private DataInMemory() {
    }

    private UserMsgBean mUserMsgBean;

    public UserMsgBean getUserMsgBean() {
        return mUserMsgBean;
    }

    public void setUserMsgBean(UserMsgBean userMsgBean) {
        mUserMsgBean = userMsgBean;
    }
}
