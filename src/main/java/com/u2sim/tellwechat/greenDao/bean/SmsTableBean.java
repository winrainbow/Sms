package com.u2sim.tellwechat.greenDao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by hanguojing on 2017/12/1 15:44
 */
@Entity(indexes = {@Index(value = "smsId DESC",unique = true)})
public class SmsTableBean{
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Unique
    private String smsId;             // 短信的唯一标识


    private String sysId;             // 对应系统中的短信id;

    @NotNull
    private String senderPhone;       // 短信发送人号码

    private String senderName;        // 短信发送人名称，如果通讯录中存在则显示，否则为 senderPhone
    private String content;           // 短信内容
    @NotNull
    private String receiveTime;       // 接收短信的时间

    @NotNull
    private int sendWsState;            // 短信是否发送成功  0 ：未发送，1：发送成功 2：已发送


    private boolean isReceiveSms;       // 是接收到的短信还是发送的短信    true：是收到的短信 false:是发送的短信



    @Generated(hash = 682431319)
    public SmsTableBean(Long id, @NotNull String smsId, String sysId,
            @NotNull String senderPhone, String senderName, String content,
            @NotNull String receiveTime, int sendWsState, boolean isReceiveSms) {
        this.id = id;
        this.smsId = smsId;
        this.sysId = sysId;
        this.senderPhone = senderPhone;
        this.senderName = senderName;
        this.content = content;
        this.receiveTime = receiveTime;
        this.sendWsState = sendWsState;
        this.isReceiveSms = isReceiveSms;
    }



    @Generated(hash = 2082817692)
    public SmsTableBean() {
    }



    public Long getId() {
        return this.id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getSmsId() {
        return this.smsId;
    }



    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }



    public String getSysId() {
        return this.sysId;
    }



    public void setSysId(String sysId) {
        this.sysId = sysId;
    }



    public String getSenderPhone() {
        return this.senderPhone;
    }



    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }



    public String getSenderName() {
        return this.senderName;
    }



    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }



    public String getContent() {
        return this.content;
    }



    public void setContent(String content) {
        this.content = content;
    }



    public String getReceiveTime() {
        return this.receiveTime;
    }



    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }



    public int getSendWsState() {
        return this.sendWsState;
    }



    public void setSendWsState(int sendWsState) {
        this.sendWsState = sendWsState;
    }



    public boolean getIsReceiveSms() {
        return this.isReceiveSms;
    }



    public void setIsReceiveSms(boolean isReceiveSms) {
        this.isReceiveSms = isReceiveSms;
    }






}
