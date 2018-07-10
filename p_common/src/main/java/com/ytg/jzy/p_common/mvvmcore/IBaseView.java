package com.ytg.jzy.p_common.mvvmcore;
/**
 *
 * @author 于堂刚
 */
public interface IBaseView<T> {
    /**
     * 开始加载
     *
     * @param loadType 加载的类型 0：第一次记载 1：下拉刷新 2：上拉加载更多
     */
    void loadStart(int loadType);

    /**
     * 加载完成
     */
    void loadComplete(T obj);

    /**
     * 加载失败
     *
     * @param message
     */
    void loadFailure(String message);
}
