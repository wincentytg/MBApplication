package com.ytg.p_retrofit_rx.entity;

public class BaseEntity<T> {
    private String msg;//请求成功或失败描述
    private int status;//错误码
    private T data;//数据集

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}