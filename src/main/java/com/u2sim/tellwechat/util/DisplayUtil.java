package com.u2sim.tellwechat.util;

import com.u2sim.tellwechat.app.InitApp;

/**
 * Created by hanguojing on 2017/12/10 16:15
 */

public class DisplayUtil {

    public static int px2dp(int pxSize){

        float density = InitApp.AppContext.getResources().getDisplayMetrics().density;
        return (int) (pxSize/density+0.5f);
    }
    public static int dp2px(int dpSize){
        float density = InitApp.AppContext.getResources().getDisplayMetrics().density;
        return (int) (dpSize*density+0.5f);

    }

    private DisplayUtil(){}
}
