package com.ytg.jzy.p_common.mvpcore;


import com.ytg.jzy.p_common.mvpcore.model.BaseModel;
import com.ytg.jzy.p_common.mvpcore.presenter.BasePresenter;
import com.ytg.jzy.p_common.mvpcore.view.BaseView;

/**
 * Created by Tervor on 2016/7/21 0021.
 */
public interface IBase<V extends BaseView,M extends BaseModel,T extends BasePresenter<V,M>> {
    T getPresenter();
    void showPostingDialog();
    void dismissDialog();


}
