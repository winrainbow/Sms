package com.u2sim.tellwechat.util;

import android.app.Activity;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by hanguojing on 2017/12/5 15:31
 * 统一管理activity
 */

public class BaseActManager {
    private static final String TAG = "BaseActManager";
    private static volatile BaseActManager instance;

    private static List<Activity> actList;

    private BaseActManager() {
    }

    public static BaseActManager getInstance() {
        if (instance == null) {
            synchronized (BaseActManager.class) {
                if (instance == null) {
                    instance = new BaseActManager();
                    actList = new LinkedList<>();
                }
            }
        }
        return instance;
    }

    public int getActSize() {
        return actList.size();
    }

    public synchronized Activity getForwardActivity() {
        int size = getActSize();
        return size > 0 ? actList.get(size - 1) : null;
    }
    public synchronized void addActivity(Activity activity){
        actList.add(activity);
    }

    public synchronized void removeActivity(Activity activity){
        if(actList.contains(activity))
            actList.remove(activity);
    }
    public synchronized void clear(){
        for(int i = actList.size() -1 ; i>-1;i--){
            Activity activity = actList.get(i);
            removeActivity(activity);
            activity.finish();
            i = actList.size();
        }
    }


}
