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
@Entity(indexes = {@Index(value = "callId DESC",unique = true)})
public class CallTableBean {
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Unique
    private String callId;             // 短信的唯一标识

    private String sysId;             // 对应系统中的短信id;

    @NotNull
    private String senderPhone;       // 短信发送人号码

    private String senderName;        // 短信发送人名称，如果通讯录中存在则显示，否则为 senderPhone
    @NotNull
    private String receiveTime;       // 接收短信的时间

    @NotNull
    private int sendWsState;            // 短信是否发送成功  0 ：未发送，1：已发送


    @Generated(hash = 464076632)
    public CallTableBean(Long id, @NotNull String callId, String sysId,
            @NotNull String senderPhone, String senderName,
            @NotNull String receiveTime, int sendWsState) {
        this.id = id;
        this.callId = callId;
        this.sysId = sysId;
        this.senderPhone = senderPhone;
        this.senderName = senderName;
        this.receiveTime = receiveTime;
        this.sendWsState = sendWsState;
    }


    @Generated(hash = 2133475567)
    public CallTableBean() {
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getCallId() {
        return this.callId;
    }


    public void setCallId(String callId) {
        this.callId = callId;
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


}
