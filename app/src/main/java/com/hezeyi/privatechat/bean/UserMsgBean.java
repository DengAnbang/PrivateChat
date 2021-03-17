package com.hezeyi.privatechat.bean;

import com.xhab.utils.bean.Sortable;

import androidx.annotation.NonNull;

/**
 * Created by dab on 2021/3/8 21:36
 */
public class UserMsgBean extends Sortable {

    /**
     * user_name :
     * user_id : 355028FB188CEAE2BD1D
     * account : test
     * head_portrait :
     * vip_time : 2021-03-08 10:51:58
     */

    private String user_name;
    private String user_id;
    private String account = "";
    private String head_portrait;
    private String vip_time;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getVip_time() {
        return vip_time;
    }

    public void setVip_time(String vip_time) {
        this.vip_time = vip_time;
    }

    @NonNull
    @Override
    public String getSortableString() {
        return user_name;
    }
}
