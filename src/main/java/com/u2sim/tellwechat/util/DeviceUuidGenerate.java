package com.u2sim.tellwechat.util;

/**
 * Created by hanguojing on 2017/11/28 10:39
 */

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.server.EncryptUtil;

import java.util.UUID;

public class DeviceUuidGenerate {
    private static final String PREFS_DEVICE_ID = "device_id";
    private static final String PREFS_FILE = "device_id.xml";
    private static final String TAG = "DeviceUuidGenerate";
    private String uuid;

    private static DeviceUuidGenerate instance;

    private DeviceUuidGenerate() {
    }


    public static DeviceUuidGenerate getInstance() {

        if (instance == null) {
            synchronized (DeviceUuidGenerate.class) {
                if (instance == null) {
                    instance = new DeviceUuidGenerate();
                }
            }
        }
        return instance;
    }

    public String getDeviceUuid() {
        if (TextUtils.isEmpty(uuid)) {
            synchronized (DeviceUuidGenerate.class) {
                SharedPreferences prefs = InitApp.AppContext.getSharedPreferences(PREFS_FILE, 0);
                String id = prefs.getString(PREFS_DEVICE_ID, null);
                if (id != null) {
                    uuid = id;
                } else {
                    StringBuffer temp = new StringBuffer();
                    String androidId = Settings.Secure.getString(InitApp.AppContext.getContentResolver(), "android_id");
                    temp.append(androidId);

                    UUID nameUUIDFromBytes;
                    String deviceId;
                    if (InitApp.AppContext.getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE, InitApp.AppContext.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                        deviceId = ((TelephonyManager) InitApp.AppContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    } else {
                        LogUtil.d(TAG,"未获取到读取电话信息权限");
                        deviceId = null;
                    }
                    if (deviceId != null) {
                       temp.append(deviceId);
                    } else {
                        nameUUIDFromBytes = UUID.randomUUID();
                        temp.append(nameUUIDFromBytes.toString());
                    }
                    uuid = EncryptUtil.Md5(temp.toString());
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();
                }

            }
        }

        return uuid;
    }


}
