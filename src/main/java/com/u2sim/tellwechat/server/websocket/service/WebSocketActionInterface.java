package com.u2sim.tellwechat.server.websocket.service;

import com.u2sim.tellwechat.server.websocket.wsMsg.WsStatus;

/**
 * Created by hanguojing on 2017/11/30 14:54
 */

public interface WebSocketActionInterface {
    void init();
    void reconnect();
    void sendTextMessage(String message);
    void disconnect();
    void cancelReconnect();
    WsStatus getStatus();
}
