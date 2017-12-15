package com.u2sim.tellwechat.alive;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.app.InitAppUtil;

/**
 * Created by hanguojing on 2017/12/10 11:31
 */

public class NotificationMonitor extends NotificationListenerService{
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        InitAppUtil.startWbService(InitApp.AppContext);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        InitAppUtil.startWbService(InitApp.AppContext);
    }
}
