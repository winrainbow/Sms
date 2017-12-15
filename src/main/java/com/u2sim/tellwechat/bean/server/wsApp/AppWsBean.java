package com.u2sim.tellwechat.bean.server.wsApp;

import com.u2sim.tellwechat.util.EnumUtil;

/**
 * Created by hanguojing on 2017/11/27 13:20
 * <p>
 * 用于生成 请求 服务器时需要的json数据
 */

public class AppWsBean<T> {

    private int result;
    private String id; // 消息唯一标识
    private @EnumUtil.MsgType
    int type;  // 0:
    private @EnumUtil.ActionType
    String action;
    private T data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getType() {
        return type;
    }

    public void setType(@EnumUtil.MsgType int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(@EnumUtil.ActionType String action) {
        this.action = action;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AppWsBean{" +
                "result=" + result +
                ", id='" + id + '\'' +
                ", type=" + type +
                ", action='" + action + '\'' +
                ", data=" + (data != null ? data.toString() : "") +
                '}';
    }
}
