package com.u2sim.tellwechat.bean.server.data;

/**
 * Created by hanguojing on 2017/11/27 13:34
 */

public class DataCallTip {

    private String callID;               // 通话记录唯一ID
    private String senderPhone;
    private String senderName;
    private String receiveTime;


    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }
}
