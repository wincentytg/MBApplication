package com.ytg.jzy.p_common.mvpcore.presenter;

import com.ytg.jzy.p_common.mvpcore.model.BaseModel;
import com.ytg.jzy.p_common.mvpcore.view.BaseView;

import java.lang.ref.WeakReference;

/**
 *
 * @author 于堂刚
 */
public abstract class BasePresenter<V extends BaseView, M extends BaseModel> {


    protected V mView;
    protected M mModel;
    public WeakReference<V> reference;

    public BasePresenter() {
        mModel = createModel();
    }

    public void attach(V mView) {
        reference = new WeakReference<V>(mView);
        this.mView = reference.get();
    }

    public void detachView() {
        if (reference != null) {
            reference.clear();
            reference = null;
        }
        if (mView != null) {
            mView = null;
        }
        mModel =null;

    }

    public boolean isViewAttached() {
        return reference != null && reference.get() != null;
    }

    protected abstract M createModel();
}
