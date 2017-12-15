package com.u2sim.tellwechat.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.elvishew.xlog.printer.file.FilePrinter;
import com.u2sim.tellwechat.bean.UserInfo;
import com.u2sim.tellwechat.greenDao.bean.DaoMaster;
import com.u2sim.tellwechat.greenDao.bean.DaoSession;
import com.u2sim.tellwechat.util.EnumUtil;
import com.u2sim.tellwechat.util.GsonFormatUtil;
import com.u2sim.tellwechat.util.LogUtil;
import com.u2sim.tellwechat.util.SPUtils;

/**
 * Created by hanguojing on 2017/11/27 14:27
 */

public class InitApp extends Application {
    private static final String TAG = "InitApp";
    public static Context AppContext;
    public static SQLiteDatabase Db;
    public static DaoSession DaoSession;
    private static boolean isLogin = false;
    public static FilePrinter FILE_PRINTER;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        FILE_PRINTER = InitAppUtil.initXlog(AppContext);
        String userInfoStr = (String) SPUtils.get(EnumUtil.SP_KEY_USER_INFO, "");
        LogUtil.d(TAG,userInfoStr==null ? "userInfo == null" : userInfoStr);
        if(!TextUtils.isEmpty(userInfoStr)){
            UserInfo userInfo = GsonFormatUtil.getInstance().fromJson(userInfoStr, UserInfo.class);
            if(userInfo != null && userInfo.isBind()){
                isLogin = true;
            }
        }
        // 使用subscribe index 增加EventBus 执行效率
        InitAppUtil.initEventBus();

        // 初始化数据库信息
        initDataBase();

        // 注册网络状态监听
        InitAppUtil.registerNetReceiver(AppContext);

        // 启动wsService
        InitAppUtil.startWbService(AppContext);

        InitAppUtil.startJbService(AppContext);




    }

    /**
     * 保存用户登录状态
     * @param loginState
     */
    public static void setLoginState(boolean loginState){
        isLogin = loginState;
    }

    /**
     * 获取用户登录状态
     * @return
     */
    public static boolean getLoginState(){
        return isLogin;
    }


    /**
     * 初始化 database,daoSession
     */
    private void initDataBase() {
        Db = InitAppUtil.initDatabase(AppContext);
        DaoSession = new DaoMaster(Db).newSession();
    }

}
