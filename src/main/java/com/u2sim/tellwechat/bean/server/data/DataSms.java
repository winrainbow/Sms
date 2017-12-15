package com.u2sim.tellwechat.bean.server.data;

/**
 * Created by hanguojing on 2017/11/27 13:30
 *
 * 用于 短信提醒时，data部分的 json转换
 */

public class DataSms {
    private String smsId;             // 短信的唯一标识
    private String senderPhone;       // 短信发送人号码
    private String senderName;        // 短信发送人名称，如果通讯录中存在则显示，否则为 senderPhone
    private String Content;           // 短信内容
    private String receiveTime;       // 接收短信的时间

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
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

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }
}
