package com.ytg.jzy.p_common.request;

/**
 */

public interface IRequestCancleStrategy<T> {

    void add(String tag, T disposable);

    void add(T disposable);

    void remove(String tag);

    /**
     * 取消请求
     *
     * @param tag
     */
    void clear(String tag);

    void clear();
}
