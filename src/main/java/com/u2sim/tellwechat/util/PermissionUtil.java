package com.u2sim.tellwechat.util;

import android.content.ComponentName;
import android.provider.Settings;
import android.text.TextUtils;

import com.u2sim.tellwechat.app.InitApp;

/**
 * Created by hanguojing on 2017/12/10 14:05
 */

public class PermissionUtil {
    private static volatile PermissionUtil instance = null;

    public static PermissionUtil getInstance() {
        if (instance == null) {
            synchronized (PermissionUtil.class) {
                if (instance == null) {
                    instance = new PermissionUtil();
                }
            }
        }
        return instance;
    }

    public boolean notificationPermissionEnable() {
        String pkgName = InitApp.AppContext.getPackageName();
        String enabled_notification_listeners = Settings.Secure.getString(InitApp.AppContext.getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(enabled_notification_listeners)) {
            String[] split = enabled_notification_listeners.split(":");
            for (int i = 0; i < split.length; i++) {
                ComponentName componentName = ComponentName.unflattenFromString(split[i]);
                if (componentName != null) {
                    if(TextUtils.equals(pkgName,componentName.getPackageName())){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private PermissionUtil() {
    }
}
