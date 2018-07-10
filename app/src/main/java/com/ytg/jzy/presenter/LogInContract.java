package com.ytg.jzy.presenter;

import com.ytg.jzy.p_common.mvvmcore.BaseLoadListener;
import com.ytg.jzy.p_common.mvpcore.model.BaseModel;
import com.ytg.jzy.p_common.mvpcore.presenter.BasePresenter;
import com.ytg.jzy.p_common.mvpcore.view.BaseView;

import java.util.List;

/**
 * mvp最本质的思路 是p持有m和v，p去执行m里的方法，
 * 之后p会通过v的接口把m方法里的执行结果传递给v来体现在UI界面上，
 * p起到一个中间者的作用。
 */
public interface LogInContract {
    class LogModel implements LogInContract.IModel {

        @Override
        public void login(BaseLoadListener back) {
            //网络请求
            back.loadStart();
            back.loadSuccess(null);
        }

        @Override
        public void loadData(int page, int size, BaseLoadListener loadListener) {

        }
    }

    class LogPresenter extends LogInContract.LogInPresenter {

        @Override
        protected LogInContract.IModel createModel() {
            return new LogModel();
        }
    }

    interface IView extends BaseView {
        void logSuccess();
    }

    interface IModel extends BaseModel {
        void login(BaseLoadListener back);
    }

    abstract class LogInPresenter extends BasePresenter<IView, IModel> {
        public void request() {
            mModel.login(new BaseLoadListener() {
                @Override
                public void loadSuccess(List list) {
                    List<String> li = list;
                    mView.logSuccess();
                }

                @Override
                public void loadFailure(String message) {
                    mView.loadFailure(message);
                }

                @Override
                public void loadStart() {
                    mView.loadStart();
                }

                @Override
                public void loadComplete() {
//                   mView.loadComplete(IModel);
                }
            });

        }

    }
}
