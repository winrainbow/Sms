package com.u2sim.tellwechat.event;

import com.google.gson.JsonObject;
import com.u2sim.tellwechat.util.EnumUtil;

/**
 * Created by hanguojing on 2017/11/29 19:20
 */

public class EventBusBase {
    /**
     * app->ws 发送消息超时
     */
    public static class SendTimeout {
        private String id;   // 用于查找数据库时的 条件
        @EnumUtil.ActionType
        private String actionType;  // 用于标识查看哪个数据表

        public SendTimeout(String id, @EnumUtil.ActionType String actionType) {
            this.id = id;
            this.actionType = actionType;
        }

        public String getId() {
            return id;
        }

        public String getActionType() {
            return actionType;
        }
    }

    /**
     * ws->app 接收到消息 String 类型 ,加密或者未加密
     */
    public static class ReceiveWsMsg {
        private String wsAppMsg;

        public ReceiveWsMsg(String wsAppMsg) {
            this.wsAppMsg = wsAppMsg;
        }

        public String getWsAppMsg() {
            return wsAppMsg;
        }
    }

    /**
     * 获取到qrCode
     */
    public static class GetQrCodeSuccess {
        private String qrCodeUrl;

        public GetQrCodeSuccess(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }
    }


    /**
     * 微信的绑定结果
     */
    public static class WeChatBindResult {
        private String resultJsonStr;

        public WeChatBindResult(String resultJsonStr) {
            this.resultJsonStr = resultJsonStr;
        }

        public String getResultJsonStr() {
            return resultJsonStr;
        }
    }

    /**
     * 获取用户信息，返回的结果
     */
    public static class GetUserInfo {
        private JsonObject data;

        public GetUserInfo(JsonObject data) {
            this.data = data;
        }

        public JsonObject getData() {
            return data;
        }
    }


    public static class Sys2AppComplete {
        private @EnumUtil.Sys2AppType
        int type;

        public Sys2AppComplete(@EnumUtil.Sys2AppType int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }


    public static class App2WsComplete {
        private @EnumUtil.App2WsRspType int type;

        public App2WsComplete(@EnumUtil.App2WsRspType int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
