package com.hezeyi.privatechat.bean;

/**
 * Created by dab on 2021/4/13 22:10
 */
public class RechargeRecordBean {
    private String user_id;
    private String user_name;
    private String user_account;
    private String execution_user_id;
    private String execution_user_name;
    private String execution_user_account;
    private String money;
    private String day;
    private String recharge_type;
    private String created;

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getExecution_user_account() {
        return execution_user_account;
    }

    public void setExecution_user_account(String execution_user_account) {
        this.execution_user_account = execution_user_account;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getExecution_user_id() {
        return execution_user_id;
    }

    public void setExecution_user_id(String execution_user_id) {
        this.execution_user_id = execution_user_id;
    }

    public String getExecution_user_name() {
        return execution_user_name;
    }

    public void setExecution_user_name(String execution_user_name) {
        this.execution_user_name = execution_user_name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getRecharge_type() {
        return recharge_type;
    }

    public String getRechargeTypeShowString() {
        switch (recharge_type) {
            case "zfb":
                return "支付宝";
            case "wx":
                return "微信";
            case "gly":
                return "管理员";
        }
        return "管理员";
    }

    public void setRecharge_type(String recharge_type) {
        this.recharge_type = recharge_type;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
