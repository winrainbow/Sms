package com.u2sim.tellwechat.bean;

/**
 * Created by hanguojing on 2017/12/6 17:15
 */

public class UserInfo {
    private String deviceName;
    private boolean isBind;
    private String weChatID;
    private String nickName;
    private String photoUrl;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    public String getWeChatID() {
        return weChatID;
    }

    public void setWeChatID(String weChatID) {
        this.weChatID = weChatID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "deviceName='" + deviceName + '\'' +
                ", isBind=" + isBind +
                ", weChatID='" + weChatID + '\'' +
                ", nickName='" + nickName + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
