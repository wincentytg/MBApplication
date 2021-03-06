package com.ytg.jzy;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ytg.jzy.p_common.YTGApplication;
import com.ytg.jzy.p_common.tools.AppConfig;


/**
 * @author 于堂刚 2015年10月17日
 * @Description:TODO
 */
public class BaseApplication extends YTGApplication {
    private static volatile BaseApplication mApplication;

    public static synchronized BaseApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initArouter();
    }

    public void initModuleService() {
        for (String moduleApp : AppConfig.moduleApps) {
            try {
                Class clazz = Class.forName(moduleApp);
                YTGApplication baseApp = (YTGApplication) clazz.newInstance();
                baseApp.initModuleService();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    void initArouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun(就是AndroidStudio2.0以后新增的一个可以减少很多编译时间的运行机制)模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        // 初始化尽可能早，推荐在Application中初始化
        ARouter.init(this);
    }

}
