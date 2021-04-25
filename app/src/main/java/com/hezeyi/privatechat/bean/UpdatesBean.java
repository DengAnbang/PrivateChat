package com.hezeyi.privatechat.bean;

/**
 * Created by dab on 2021/4/24 21:28
 */

public class UpdatesBean {
    private int version_code;
    private String version_name;
    private String version_msg;
    private String packages;

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getVersion_msg() {
        return version_msg;
    }

    public void setVersion_msg(String version_msg) {
        this.version_msg = version_msg;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }
}
