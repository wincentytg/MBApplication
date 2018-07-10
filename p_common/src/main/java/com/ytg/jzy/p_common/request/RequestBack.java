package com.ytg.jzy.p_common.request;
/**
 *
 * @author 于堂刚
 */
public interface RequestBack<T> {
    void onFail(Object obj);
    void onSuccess(T obj);
}
