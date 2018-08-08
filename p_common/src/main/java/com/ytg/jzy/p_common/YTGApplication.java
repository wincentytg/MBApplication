package com.ytg.jzy.p_common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.ytg.jzy.p_common.utils.LogUtil;
import com.ytg.p_db.Config;
import com.ytg.p_db.DBManager;


/**
 * @author 于堂刚 2015年10月17日
 * @Description:TODO
 */
public class YTGApplication extends Application {

    public DBManager mDbManager;
//    public SharedPreferencesHelper sp;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        YTGApplicationContext.setContext(this);
        LogUtil.setDebugMode(true);
        MultiDex.install(this);
//        sp = SharedPreferencesHelper.getInstance(mApplication);
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		 crashHandler.init(this);

        // // 数据�?
        // mDbManager = DBManager.getInstance(new Config(
        // getApplicationContext(), null, 2, null,
        // new OnDbUpgradeListener() {
        //
        // @Override
        // public void onUpgrade(DBManager manager)
        // throws DBException {
        // if (manager.getDataBase().getVersion() == 1) {
        // manager.AddColumn(cls, columnName);
        // }
        // }
        // }));
        mDbManager = DBManager.getInstance(new Config(getApplicationContext(),
                null, 1, null, null));
        mDbManager.setDebug(true);

        initModuleService();

    }
public void initModuleService(){}

}
