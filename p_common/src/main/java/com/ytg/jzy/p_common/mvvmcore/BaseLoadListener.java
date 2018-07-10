package com.ytg.jzy.p_common.mvvmcore;

import java.util.List;

/**
 *
 * @author 于堂刚
 */

public interface BaseLoadListener<T> {
    /**
     * 加载数据成功
     *
     * @param list
     */
    void loadSuccess(List<T> list);

    /**
     * 加载失败
     *
     * @param message
     */
    void loadFailure(String message);

    /**
     * 开始加载
     */
    void loadStart();

    /**
     * 加载结束
     */
    void loadComplete();
}
