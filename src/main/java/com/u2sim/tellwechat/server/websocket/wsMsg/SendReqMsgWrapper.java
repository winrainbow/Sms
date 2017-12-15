package com.u2sim.tellwechat.server.websocket.wsMsg;

import com.google.gson.reflect.TypeToken;
import com.u2sim.tellwechat.bean.server.wsApp.AppWsBean;
import com.u2sim.tellwechat.util.EnumUtil;
import com.u2sim.tellwechat.util.GsonFormatUtil;

/**
 * Created by hanguojing on 2017/11/30 15:20
 */

public class SendReqMsgWrapper {


    private String sendMsg; // json格式
    private String id;      // 唯一标识

    @EnumUtil.ActionType
    private String actionType;    //

    private long timeout = 10000; // 默认超时时间是 10 秒；

    public <T> SendReqMsgWrapper(AppWsBean<T> appWsBean) {

        this.actionType = appWsBean.getAction();
        this.id = appWsBean.getId();
        this.sendMsg = GsonFormatUtil.getInstance().toJson(appWsBean, new TypeToken<AppWsBean<T>>() {
        }.getType());
    }


    /**
     * 重发时，需要用到
     *
     * @param id
     * @param msgType
     * @param sendMsg
     */
    public SendReqMsgWrapper(String id, @EnumUtil.ActionType String actionType, String sendMsg) {

        this.id = id;
        this.actionType = actionType;
        this.sendMsg = sendMsg;

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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
