package com.u2sim.tellwechat.server.websocket.wsMsg;

/**
 * Created by hanguojing on 2017/11/29 14:47
 * webSocket 的连接状态
 */

public enum WsStatus {
    CONNECT_SUCCESS, // 连接成功
    CONNECT_FAILURE, // 连接失败
    CONNECT_ING  // 正在连接ing
}
