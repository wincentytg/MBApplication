package com.ytg.jzy.p_common.request;

public interface RequestBack<T> {
    void onFail(Object obj);
    void onSuccess(T obj);
}
