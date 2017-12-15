package com.u2sim.tellwechat.bean.server.data;

/**
 * Created by hanguojing on 2017/11/28 15:17
 */

public class DataSmsRsp {
    private String msgID;
    private String sendTime;

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "DataSmsRsp{" +
                "msgID='" + msgID + '\'' +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }
}
