package com.ytg.jzy.p_common.mvpcore.view;
/**
 *
 * @author 于堂刚
 */
public interface BaseView<T> {
    void updateUi();
    /**
     * 开始加载
     *
     *
     */
    void loadStart();

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
