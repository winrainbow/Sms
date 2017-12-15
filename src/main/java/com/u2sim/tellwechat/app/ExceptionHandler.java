package com.u2sim.tellwechat.app;

import android.content.Context;

/**
 * Created by hanguojing on 2017/12/10 13:30
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context context;
    public boolean openUpload = true;
    private static final String LOG_FILE_DIR = "log";
    private static final String FILE_NAME = ".log";
    private static volatile  ExceptionHandler instance = null;
    private Thread.UncaughtExceptionHandler defaultCrashHandler;

    private ExceptionHandler(Context context){
        this.context = context.getApplicationContext();
        defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(defaultCrashHandler);

    }
    public static ExceptionHandler create(Context context){
        if(instance == null){
            synchronized (ExceptionHandler.class){
                if(instance == null){
                    instance = new ExceptionHandler(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

    }
    private void saveToFile(Throwable ex){

    }
}
