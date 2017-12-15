package com.u2sim.tellwechat.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.u2sim.tellwechat.app.InitApp;

/**
 * Created by hanguojing on 2017/12/6 17:35
 */

public class AppVersionUtil {


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName() {
        try {
            PackageManager manager = InitApp.AppContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(InitApp.AppContext.getPackageName(), 0);
            String version = info.versionName;
            return "V" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionCode() {
        LogUtil.d("AppVersionUtil","getVersionCode");
        try {
            PackageManager manager = InitApp.AppContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(InitApp.AppContext.getPackageName(), 0);
            LogUtil.d("AppVersionUtil","versionCode = "+info.versionCode);
            return String.valueOf(info.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}
