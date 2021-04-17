package com.hezeyi.privatechat.bean;

import android.text.TextUtils;

import com.hezeyi.privatechat.R;
import com.xhab.utils.inteface.ChooseAble;

import java.util.Objects;

import androidx.annotation.NonNull;

/**
 * Created by dab on 2021/3/8 21:36
 */
public class UserMsgBean extends SortableAndBuddyShowAble implements ChooseAble {

    /**
     * user_name :
     * user_id : 355028FB188CEAE2BD1D
     * account : test
     * head_portrait :
     * vip_time : 2021-03-08 10:51:58
     */

    private String user_name;
    private String nickname;
    private String user_id;
    private String account = "";
    private String head_portrait;
    private String vip_time;
    private String permissions;

    /**
     * 是否是管理员
     *
     * @return
     */
    public boolean isAdmin() {
        return Objects.equals(getPermissions(), "1");
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNickname() {
        if (!TextUtils.isEmpty(nickname)){
            return nickname;
        }
        return user_name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
        return getNickname();
    }

    @Override
    public String getShowName() {
        return getNickname() == null ? "" : getNickname();
    }

    @Override
    public String getId() {
        return user_id;
    }

    @Override
    public String getShowPortrait() {
        return head_portrait == null ? "" : head_portrait;
    }

    @Override
    public int getPlaceholder() {
        return R.mipmap.logo;
    }


    private boolean isChoose = false;

    @Override
    public boolean isChoose() {
        return isChoose;
    }

    @Override
    public void choose() {
        isChoose = !isChoose;
    }

    @Override
    public String getShowString() {
        return user_id;
    }
}
