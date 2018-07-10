package com.ytg.p_retrofit_rx.utils;

import android.accounts.NetworkErrorException;
import android.content.Context;

import org.json.JSONObject;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.observers.DisposableObserver;

public abstract class BaseObserverString extends DisposableObserver<String> {

    private static final String TAG = BaseObserverString.class.getSimpleName();
    private Context mContext;

    protected BaseObserverString() {
    }

    protected BaseObserverString(Context context) {
        this.mContext = context;
    }

    @Override
    public void onNext(String data) {
            try {
                JSONObject object =new JSONObject(data);
                if (object.optInt("status")==1) {
                    onSuccees(object);
                }else{
                    try {
                        onCodeError(object.optInt("status"),object.optString("msg"));
                        onError(new Throwable(object.optString("msg")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
    protected abstract void onSuccees(JSONObject data) throws Exception;

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
    protected void onCodeError(int error,String msg) throws Exception {
    }
}