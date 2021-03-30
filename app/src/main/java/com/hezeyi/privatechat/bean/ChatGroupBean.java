package com.hezeyi.privatechat.bean;

import com.hezeyi.privatechat.inteface.BuddyShowAble;
import com.xhab.utils.bean.Sortable;

import androidx.annotation.NonNull;

/**
 * Created by dab on 2021/3/26 09:35
 */
public class ChatGroupBean extends Sortable implements BuddyShowAble {
    private String user_id;
    private String user_type;
    private String chat_pwd;
    private String group_name;
    private String group_id;
    private String group_portrait = "";

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getChat_pwd() {
        return chat_pwd;
    }

    public void setChat_pwd(String chat_pwd) {
        this.chat_pwd = chat_pwd;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_portrait() {
        return group_portrait;
    }

    public void setGroup_portrait(String group_portrait) {
        this.group_portrait = group_portrait;
    }

    @Override
    public String getShowName() {
        return group_name == null ? "" : group_name;
    }

    @Override
    public String getId() {
        return group_id;
    }

    @Override
    public String getShowPortrait() {
        return group_portrait == null ? "" : group_portrait;
    }

    @NonNull
    @Override
    public String getSortableString() {
        return group_name;
    }

}
