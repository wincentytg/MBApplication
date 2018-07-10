package com.ytg.p_retrofit_rx.utils;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.ytg.p_retrofit_rx.entity.BaseEntity;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.observers.DisposableObserver;

public abstract class BaseObserver<T> extends DisposableObserver<BaseEntity<T>> {

    private static final String TAG = BaseObserver.class.getSimpleName();
    private Context mContext;

    protected BaseObserver() {
    }

    protected BaseObserver(Context context) {
        this.mContext = context;
    }

    @Override
    public void onNext(BaseEntity<T> tBaseEntity) {
        if (tBaseEntity.getStatus()==1) {
            try {
                onSuccees(tBaseEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                onCodeError(tBaseEntity.getStatus());
                onError(new Throwable(tBaseEntity.getMsg()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onFailure(e, true);
            } else {
                onFailure(e, false);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
    }


    /**
     * 返回成功
     *
     * @param data
     * @throws Exception
     */
    protected abstract void onSuccees(BaseEntity<T> data) throws Exception;

    /**
     * 返回失败
     *
     * @param e
     * @param isNetWorkError 是否是网络错误
     * @throws Exception
     */
    protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;

    /**
     * 返回成功了,但是code错误
     *
     * @param error
     * @throws Exception
     */
    protected void onCodeError(int error) throws Exception {
    }
}