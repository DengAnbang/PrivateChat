package com.hezeyi.privatechat.activity.account;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;

import com.hezeyi.privatechat.Const;
import com.hezeyi.privatechat.MyApplication;
import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.activity.chat.ChatActivity;
import com.hezeyi.privatechat.activity.recharge.RechargeActivity;
import com.hezeyi.privatechat.base.BaseActivity;
import com.hezeyi.privatechat.bean.UserMsgBean;
import com.hezeyi.privatechat.net.HttpManager;
import com.hezeyi.privatechat.popupWindow.ModifyNameWindow;
import com.hezeyi.privatechat.popupWindow.ModifyVipWindow;
import com.xhab.chatui.utils.GlideUtils;
import com.xhab.utils.activity.SelectPhotoDialog;
import com.xhab.utils.utils.FunUtils;
import com.xhab.utils.utils.LogUtils;
import com.xhab.utils.utils.TimeUtils;
import com.xhab.utils.utils.ToastUtil;

import java.util.Objects;

/**
 * Created by dab on 2021/3/9 20:33
 * user_id
 */
public class UserDetailsActivity extends BaseActivity {

    private String mUserId;
    private String myUserId;
    private boolean isMe;
    private boolean isAdmin;
    private boolean isFriend;
    private UserMsgBean mUserMsgBean;

    @Override
    public int getContentViewRes() {
        return R.layout.activity_user_details;
    }


    @Override
    public void initData() {
        super.initData();
        mUserId = getIntent().getStringExtra("user_id");
        myUserId = MyApplication.getInstance().getUserMsgBean().getUser_id();
        isMe = Objects.equals(mUserId, myUserId);
        isAdmin = MyApplication.getInstance().getUserMsgBean().isAdmin();
        mUserMsgBean = MyApplication.getInstance().getFriendUserMsgBeanById(mUserId);
        isFriend = (mUserMsgBean != null) && (mUserMsgBean != MyApplication.getInstance().getUserMsgBean());
    }

    @Override
    public void initEvent() {
        super.initEvent();
        if (isFriend) {
            visibility(R.id.tv_friend_delete, true);
            click(R.id.tv_friend_delete, view -> {
                FunUtils.affirm(this, "确认删除好友?", "删除", aBoolean -> {
                    if (aBoolean) {
                        HttpManager.friendDelete(mUserId, myUserId, this, o -> {
                            MyApplication.getInstance().removeFriendUserMsgBeanById(mUserId);
                            ToastUtil.showToast("删除成功!");
                            finish();
                        });
                    }
                });
            });
        }

        click(R.id.tv_submit, view -> {
            HttpManager.friendAdd(myUserId, mUserId, "2", this, o -> {
                ToastUtil.showToast("提交成功,等待对方通过!");
                finish();
            });
        });
        if (isMe || isAdmin) {
            click(R.id.ttv_name, view -> {
                ModifyNameWindow modifyNameWindow = new ModifyNameWindow(this, mUserMsgBean.getUser_name());
                modifyNameWindow.setOnDataCallBack(s -> {
                    if (TextUtils.isEmpty(s)) {
                        showSnackBar("名字不能为空");
                        return;
                    }
                    HttpManager.userUpdate(mUserMsgBean.getAccount(), "", s, "", "", this, userMsgBean1 -> {
                        mUserMsgBean.setUser_name(s);
                        showSnackBar("修改完成");
                        initView();
                        modifyNameWindow.dismiss();
                    });
                });
                modifyNameWindow.showAsDropDown(findViewById(R.id.ttv_account), 0, 0, Gravity.END);
            });
            click(R.id.ttv_qr, view -> {
                Intent intent = new Intent(this, MeQrCodeActivity.class);
                startActivity(intent);
            });
            click(R.id.iv_head_portrait, view -> {
                startActivityForResult(new Intent(this, SelectPhotoDialog.class), 55);
            });

        }
        visibility(R.id.ttv_nickname, isFriend);
        click(R.id.ttv_nickname, view -> {
            ModifyNameWindow modifyNameWindow = new ModifyNameWindow(this, mUserMsgBean.getNickname());
            modifyNameWindow.setOnDataCallBack(s -> {
                if (TextUtils.isEmpty(s)) {
                    showSnackBar("名字不能为空");
                    return;
                }
                HttpManager.friendCommentSet(myUserId, mUserMsgBean.getUser_id(), s, this, userMsgBean1 -> {
                    showSnackBar("修改完成");
                    initView();
                    modifyNameWindow.dismiss();
                });
            });
            modifyNameWindow.showAsDropDown(findViewById(R.id.ttv_account), 0, 0, Gravity.END);
        });
        click(R.id.tv_send, view -> {
            ChatActivity.startChatActivity(this, mUserId, false);
        });
        click(R.id.tv_recharge, view -> {
            Intent intent = new Intent(this, RechargeActivity.class);
            intent.putExtra("user_id", mUserId);
            startActivity(intent);
        });
        click(R.id.tv_recharge_admin, view -> {
            ModifyVipWindow modifyVipWindow = new ModifyVipWindow(this);
            modifyVipWindow.setOnDataCallBack(vipTime -> {
                if (TextUtils.isEmpty(vipTime) || vipTime.equals("-")) {
                    return;
                }
                HttpManager.userUpdate(mUserMsgBean.getAccount(), "", "", vipTime, "", this, userMsgBean1 -> {
                    HttpManager.rechargeAdd(mUserMsgBean.getUser_id(), myUserId, "0", vipTime, "0", this, o -> {
                        showSnackBar("修改完成");
                        initView();
                        modifyVipWindow.dismiss();
                    });
                });

            });
            modifyVipWindow.showAsDropDown(findViewById(R.id.ttv_account), 0, 0, Gravity.END);
        });

    }

    @Override
    public void initView() {
        super.initView();
        setTitleString("个人信息");
        initData();

        visibility(R.id.tv_send, isFriend && !isMe);
        visibility(R.id.tv_submit, (!isFriend) && !isMe);
        visibility(R.id.ttv_qr, isMe || isAdmin);
        visibility(R.id.ttv_vip_time, isMe || isAdmin);
        visibility(R.id.tv_recharge_admin, isAdmin);
        visibility(R.id.tv_recharge, isMe && !isAdmin);


        HttpManager.userSelectById(mUserId, MyApplication.getInstance().getUserMsgBean().getUser_id(), true, this, userMsgBean -> {
            if (userMsgBean != null) {
                mUserMsgBean = userMsgBean;
                setTwoTextLinearRightText(R.id.ttv_account, userMsgBean.getAccount()).getRightTextView().setGravity(Gravity.RIGHT);
                setTwoTextLinearRightText(R.id.ttv_name, userMsgBean.getUser_name()).getRightTextView().setGravity(Gravity.RIGHT);
                setTwoTextLinearRightText(R.id.ttv_nickname, userMsgBean.getNickname()).getRightTextView().setGravity(Gravity.RIGHT);
                setTwoTextLinearRightText(R.id.ttv_vip_time, TimeUtils.toTimeByString(userMsgBean.getVip_time())).getRightTextView().setGravity(Gravity.RIGHT);
                GlideUtils.loadHeadPortrait(userMsgBean.getHead_portrait(), findViewById(R.id.iv_head_portrait), userMsgBean.getPlaceholder());
            } else {
                ToastUtil.showToast("用户不存在!");
                finish();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 55 && data != null) {
            String stringExtra = data.getStringExtra(SelectPhotoDialog.DATA);

            HttpManager.fileUpload(Const.FilePath.userFileType, stringExtra, this, s -> {
                HttpManager.userUpdate(mUserMsgBean.getAccount(), "", "", "", s, this, userMsgBean1 -> {
                    mUserMsgBean.setHead_portrait(s);
                    initView();
                    showSnackBar("修改完成");
                });
            });
            LogUtils.e("onActivityResult*****: " + stringExtra);

        }
    }
}
