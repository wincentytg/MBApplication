package com.ytg.jzy.p_common.db;
/**
 *
 * @author 于堂刚
 */
public interface DBBack<T> {
    void onFail(Object obj);
    void onSuccess(T obj);
}
