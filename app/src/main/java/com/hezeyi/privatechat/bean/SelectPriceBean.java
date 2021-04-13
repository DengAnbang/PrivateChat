package com.hezeyi.privatechat.bean;

import com.xhab.utils.inteface.ChooseAble;

/**
 * Created by dab on 2021/4/13 17:24
 */
public class SelectPriceBean implements ChooseAble {
    private String money;
    private String day;
    private boolean choose;
    private int position;

    public SelectPriceBean(String day, String money) {
        this.money = money;
        this.day = day;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
        return money;
    }
}
