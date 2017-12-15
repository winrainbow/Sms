package com.u2sim.tellwechat.util;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by hanguojing on 2017/12/6 14:35
 *
 */

public class ToastUtil {
    private static Toast sToast = null;
    private static final String TAG = "ToastUtil";

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param content
     */
    public static void shortToast(Context context, String content) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(content);
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param content
     */
    public static void longToast(Context context, String content) {
        LogUtil.d(TAG, "longToast start");
        if (sToast == null) {
            sToast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        } else {
            sToast.setText(content);
            sToast.setDuration(Toast.LENGTH_LONG);
        }
        LogUtil.d(TAG, "longToast will show ");
        sToast.show();
    }


}

