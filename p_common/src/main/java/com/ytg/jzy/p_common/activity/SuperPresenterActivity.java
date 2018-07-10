package com.ytg.jzy.p_common.activity;

import android.os.Bundle;
import android.view.Menu;

import com.ytg.jzy.p_common.mvpcore.IBase;
import com.ytg.jzy.p_common.mvpcore.model.BaseModel;
import com.ytg.jzy.p_common.mvpcore.presenter.BasePresenter;
import com.ytg.jzy.p_common.mvpcore.view.BaseView;

/**
 * MVP模式基础类
 *
 * @author
 * @since 2017/3/15
 */
public abstract class SuperPresenterActivity<V extends BaseView,M extends BaseModel, T extends BasePresenter<V ,M>> extends MCompatActivity implements IBase<V, M,T> {

    protected T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();

        if (mPresenter != null) {
            mPresenter.attach((V) this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }



    protected boolean onStartAction() {
        return true;
    }
}
