package com.ytg.jzy.p_common.db;

public interface DBBack<T> {
    void onFail(Object obj);
    void onSuccess(T obj);
}
