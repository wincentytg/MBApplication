package com.ytg.jzy.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ytg.jzy.R;
import com.ytg.jzy.p_common.activity.SuperPresenterActivity;
import com.ytg.jzy.p_common.utils.Event;
import com.ytg.jzy.presenter.LogInContract;

public class LoginActivity extends SuperPresenterActivity<LogInContract.IView, LogInContract.IModel, LogInContract.LogInPresenter> implements LogInContract.IView {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.request();
        initAllRequestPerssion(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initContent() {

    }

    @Override
    public void onEventMainThread(Event event) {
        super.onEventMainThread(event);
        switch (event.getmIntTag()) {
            case Event.NET_WORK_STATE_CONNNECTED:
//
                break;
            case Event.NET_WORK_STATE_DISCONNNECT:
//
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LogInContract.LogInPresenter getPresenter() {
        return new LogInContract.LogPresenter();
    }

    @Override
    public void logSuccess() {
        Log.i("URIMVP", "logSuccess");
    }

    @Override
    public void updateUi() {
        Log.i("URIMVP", "updateUi");
    }

    @Override
    public void loadStart() {
        Log.i("URIMVP", "loadStart");
    }

    @Override
    public void loadComplete(Object obj) {
        Log.i("URIMVP", "loadComplete");
    }

    @Override
    public void loadFailure(String message) {
        Log.i("URIMVP", "loadFailure");
    }


    @Override
    public void onClick(View v) {

    }
}
