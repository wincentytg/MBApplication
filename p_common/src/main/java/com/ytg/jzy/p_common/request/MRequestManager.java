package com.ytg.jzy.p_common.request;

import android.content.Context;

/**
 * 网络加载类
 * 策略或者静态代理模式，开发者只需要关心RequestManager + RequestOptions
 * @author 于堂刚
 */

public class MRequestManager<T> {
    private static IRequestStrategy sRequest;
    private static IRequestCancleStrategy sRequestCancle;
    private static volatile MRequestManager sInstance;

    private MRequestManager() {
    }

    //单例模式
    public static MRequestManager getInstance() {
        if (sInstance == null) {
            synchronized (MRequestManager.class) {
                if (sInstance == null) {
                    //若切换其它加载框架，可以实现一键替换
                    sInstance = new MRequestManager();
                }
            }
        }
        return sInstance;
    }

    //提供实时替换网络加载框架的接口
    public void setRequestLoader(IRequestStrategy loader) {
        if (loader != null) {
            sRequest = loader;
        }
    }

    public static void setsRequestCancle(IRequestCancleStrategy sRequestCancle) {
        MRequestManager.sRequestCancle = sRequestCancle;
    }

    public RequestOptions url(String path) {
        return new RequestOptions(path);
    }


    public void loadOptions(Context context, RequestOptions options, RequestBack back) {
        sRequest.loadData(context, options, back);
    }

    /**
     * 取消单个请求
     * @param tag
     */
    public void cancle(String tag) {
        sRequestCancle.clear(tag);
    }

    /**
     * 取消全部请求
     */
    public void clear() {
        sRequestCancle.clear();
    }

    /**
     * 添加请求 用于取消
     * @param tag
     * @param disposable
     */
    public void add(String tag, T disposable){
        sRequestCancle.add(tag,disposable);
    }

}
