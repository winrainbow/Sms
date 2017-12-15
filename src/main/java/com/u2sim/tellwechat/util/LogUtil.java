package com.u2sim.tellwechat.util;

import android.util.Log;

import com.elvishew.xlog.LogLevel;
import com.u2sim.tellwechat.BuildConfig;
import com.u2sim.tellwechat.app.InitApp;

import java.io.File;

/**
 * Created by hanguojing on 2017/11/28 11:01
 */
public class LogUtil {
    private static final String TAG = "LogUtil";

    public static void i(String tag, String message) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i(tag, rebuildMsg(stackTraceElement, message));
        }
        InitApp.FILE_PRINTER.println(LogLevel.INFO,tag,message);
    }

    public static void w(String tag, String message) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.w(tag, rebuildMsg(stackTraceElement, message));
        }
    }

    public static void v(String tag, String message) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.v(tag, rebuildMsg(stackTraceElement, message));
        }
    }

    public static void d(String tag, String message) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.d(tag, rebuildMsg(stackTraceElement, message));
        }
        InitApp.FILE_PRINTER.println(LogLevel.DEBUG,tag,message);
    }
    public static void d(String tag, String msgFormat,Object... params) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String format = String.format(msgFormat, params);
            Log.d(tag, rebuildMsg(stackTraceElement, format));
        }
        InitApp.FILE_PRINTER.println(LogLevel.DEBUG,tag,String.format(msgFormat, params));
    }

    public static void e(String tag, String message) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.e(tag, rebuildMsg(stackTraceElement, message));
        }
        InitApp.FILE_PRINTER.println(LogLevel.ERROR,tag,message);
    }
    public static void e(String tag, String msgFormat,Object... params) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String format = String.format(msgFormat, params);
            Log.e(tag, rebuildMsg(stackTraceElement, format));
        }
        InitApp.FILE_PRINTER.println(LogLevel.ERROR,tag,String.format(msgFormat, params));
    }

    public static void i(String message) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i(TAG, rebuildMsg(stackTraceElement, message));
        }
    }

    public static void w(String message) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.w(TAG, rebuildMsg(stackTraceElement, message));
        }
    }

    public static void v(String message) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.v(TAG, rebuildMsg(stackTraceElement, message));
        }
    }

    public static void d(String message) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.d(TAG, rebuildMsg(stackTraceElement, message));
        }
    }

    public static void e(String message) {
        if (BuildConfig.LOG_DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.e(TAG, rebuildMsg(stackTraceElement, message));
        }
    }

    /**
     * Rebuild Log Msg
     *
     * @param msg
     * @return
     */
    private static String rebuildMsg(StackTraceElement stackTraceElement, String msg) {
        StringBuffer sb = new StringBuffer();
        sb.append(stackTraceElement.getFileName());
        sb.append(" (");
        sb.append(stackTraceElement.getLineNumber());
        sb.append(") ");
        sb.append(stackTraceElement.getMethodName());
        sb.append(": ");
        sb.append(msg);
        return sb.toString();
    }



    private static final void write(String TAG,String msg,String level){
        String logPath = InitApp.AppContext.getFilesDir().getAbsolutePath() + File.separator + "log";
        File file = new File(logPath);
        if(!file.exists()){
            file.mkdirs();
        }


    }



}
