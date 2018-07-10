package com.ytg.p_retrofit_rx.utils;

import java.util.HashMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 用于管理Rxjava相关代码的生命周期处理
 */
public class RxManage{
    public RxManage() {

    }

    HashMap<String, Disposable> map = new HashMap<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();// 管理订阅者者

    public void add(String tag, Disposable disposable) {
        map.put(tag, disposable);
        add(disposable);
    }

    private void add(Disposable disposable) {
        compositeDisposable.add(disposable);
    }
    public void remove(String tag) {
        compositeDisposable.remove(map.get(tag));// 取消订阅 断开网络请求
        map.remove(tag);
    }

    /**
     * 取消请求
     * @param tag
     */
    public void clear(String tag) {
        map.get(tag).dispose();
        remove(tag);
    }
    public void clear() {
        compositeDisposable.clear();// 取消全部的订阅
        map.clear();
    }
}
