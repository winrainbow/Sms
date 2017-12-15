package com.u2sim.tellwechat.greenDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.u2sim.tellwechat.greenDao.bean.DaoMaster;
import com.u2sim.tellwechat.util.LogUtil;

import org.greenrobot.greendao.database.Database;

/**
 * Created by hanguojing on 2017/12/1 16:19
 */

public class SmsSQLiteOpenHelper extends DaoMaster.DevOpenHelper {
    private static final String TAG = "SmsSQLiteOpenHelper";
    public SmsSQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    public SmsSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        LogUtil.d(TAG,"[oldVersion:%d,newVersion:%d]",oldVersion,newVersion);
        switch (oldVersion){
            case 1:
                alterSmsTableBeanReceive(db);
                break;
        }

        // TODO: 2017/12/1 处理数据库的更新
    }

    private void alterSmsTableBeanReceive(Database db){
        String sql = "alter table SMS_TABLE_BEAN add COLUMN IS_RECEIVE_SMS INTEGER NULL";
        db.execSQL(sql);
    }
}
