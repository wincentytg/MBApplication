package com.ytg.jzy;

import com.ytg.jzy.p_common.YTGApplication;


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
    }

}
