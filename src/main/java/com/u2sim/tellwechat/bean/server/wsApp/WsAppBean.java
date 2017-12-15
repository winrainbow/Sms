package com.u2sim.tellwechat.bean.server.wsApp;

import com.google.gson.JsonObject;
import com.u2sim.tellwechat.util.EnumUtil;

/**
 * Created by hanguojing on 2017/11/27 13:24
 *
 * 用于接收 服务器返回的 json数据
 *
 */

public class WsAppBean {
    private String id;
    private @EnumUtil.ActionType String action;
    private int result;
    private String msg;
    private @EnumUtil.MsgType int type;      // 0 代表请求消息，1代表响应消息
    private JsonObject data;


    /*
    {
	"action": "smsNew",
	"data": {
		"msgID": "smsId_00001",
		"sendTime": "2017-12-06 10:05:21"
	},
	"id": "msg_1001",
	"msg": "成功",
	"result": 1,
	"type": 1
}
    * */




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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(@EnumUtil.MsgType int type) {
        this.type = type;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }





    @Override
    public String toString() {
        return "WsAppBean{" +
                "id='" + id + '\'' +
                ", action='" + action + '\'' +
                ", result=" + result +
                ", msg='" + msg + '\'' +
                ", data=" + (data==null?"null":data.toString()) +
                '}';
    }
}
