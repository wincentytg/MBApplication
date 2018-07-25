package com.ytg.jzy.p_common.mvvmcore;

/**
 * Created by goldze on 2017/6/15.
 */

public interface IBaseViewModel {
//    void initData();

    /**
     * View的界面创建时回调
     */
    void onCreate();

    /**
     * View的界面销毁时回调
     */
    void onDestroy();

}
