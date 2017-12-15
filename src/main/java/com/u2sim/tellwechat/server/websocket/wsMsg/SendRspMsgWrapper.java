package com.u2sim.tellwechat.server.websocket.wsMsg;

import com.u2sim.tellwechat.bean.server.wsApp.AppWsBean;

/**
 * Created by hanguojing on 2017/11/30 15:20
 */

public class SendRspMsgWrapper {


    private String sendMsg; // json格式
    private String id;      // 唯一标识
    private int msgType;    //

    private long timeout = 5000; // 默认超时时间是 500 秒；

    public <T> SendRspMsgWrapper(AppWsBean<T> appWsBean) {

    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
