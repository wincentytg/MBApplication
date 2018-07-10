package com.ytg.p_retrofit_rx.entity;

/**
 *
 * @author 于堂刚
 */
public abstract class BaseHttpResult<T> extends Entity {

    public abstract boolean isSuccess();

    public abstract boolean isTokenInvalid();

    public abstract boolean isShowToast();

    public abstract String getCode();

    public abstract String getMsg();

    public abstract T getData();
}
