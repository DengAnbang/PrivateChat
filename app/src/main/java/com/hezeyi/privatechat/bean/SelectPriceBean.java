package com.hezeyi.privatechat.bean;

import android.text.TextUtils;

import com.xhab.utils.inteface.ChooseAble;

/**
 * Created by dab on 2021/4/13 17:24
 */
public class SelectPriceBean implements ChooseAble, Comparable<SelectPriceBean> {
    private int money;
    private String id;
    private String day;
    private String giving_day;
    private String pay_image;
    private boolean choose;
    private int position;

    public String getPay_image() {
        return pay_image;
    }

    public void setPay_image(String pay_image) {
        this.pay_image = pay_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGiving_day() {
        if (TextUtils.isEmpty(giving_day)) return "";
        return giving_day;
    }

    public void setGiving_day(String giving_day) {
        this.giving_day = giving_day;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public boolean isChoose() {
        return choose;
    }

    @Override
    public void choose() {
        choose = !choose;
    }

    @Override
    public String getShowString() {
        return money + "";
    }

    public int getTotalDay() {
        int iday = 0;
        try {
            iday = Integer.parseInt(getDay());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int giving_day = 0;
        try {
            giving_day = Integer.parseInt(getGiving_day());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iday + giving_day;
    }

    @Override
    public int compareTo(SelectPriceBean o) {
        return getMoney() - (o.getMoney());
    }
}
