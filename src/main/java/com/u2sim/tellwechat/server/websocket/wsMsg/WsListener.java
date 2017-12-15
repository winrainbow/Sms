package com.u2sim.tellwechat.server.websocket.wsMsg;

import android.os.SystemClock;
import android.text.TextUtils;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.u2sim.tellwechat.event.EventBusBase;
import com.u2sim.tellwechat.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

/**
 * Created by hanguojing on 2017/11/29 14:49
 * <p>
 * 鉴听 webSocket 的状态
 */

public class WsListener extends WebSocketAdapter {
    private static final String TAG = "WsListener";
    private long timeStamp = -1;

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        super.onTextMessage(websocket, text);
        LogUtil.d(TAG, "onTextMessage ThreadID:%s", Thread.currentThread().getName());
        LogUtil.d(TAG, "onTextMessage:" + text);
        if (!TextUtils.isEmpty(text)) {
            EventBus.getDefault().post(new EventBusBase.ReceiveWsMsg(text));
        }
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);
        LogUtil.d(TAG, "onConnected ThreadID:%s", Thread.currentThread().getName());
        WebSocketManager.getInstance().setStatus(WsStatus.CONNECT_SUCCESS);
        LogUtil.d(TAG, "onConnected:webSocket连接成功");
        WebSocketManager.getInstance().cancelReconnect();
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        super.onConnectError(websocket, exception);
        WebSocketManager.getInstance().setStatus(WsStatus.CONNECT_FAILURE);
        LogUtil.d(TAG, "onConnectError[webSocketException:%s]", exception.getMessage());
        WebSocketManager.getInstance().reconnect();
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
        WebSocketManager.getInstance().setStatus(WsStatus.CONNECT_FAILURE);
        LogUtil.d(TAG,WebSocketManager.getInstance().getStatus().toString());
        WebSocketManager.getInstance().reconnect();
        LogUtil.d(TAG, "onDisconnected:webSocket断开连接");

    }

    @Override
    public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        super.onPingFrame(websocket, frame);
        LogUtil.d(TAG, "ping" + frame.getPayloadText());
    }

    @Override
    public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        super.onPongFrame(websocket, frame);
        LogUtil.d(TAG, "pong:" + frame.getPayloadText());
        long temp = SystemClock.currentThreadTimeMillis();
        if (temp - timeStamp > 70 * 1000L) {
            // TODO: 2017/12/8 重新连接ws
            timeStamp = -1;
        } else {
            timeStamp = SystemClock.currentThreadTimeMillis();
        }


    }
}
