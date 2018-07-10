package com.ytg.jzy.http.ok;

import com.ytg.jzy.p_common.request.IRequestCancleStrategy;
import com.ytg.p_retrofit_rx.utils.RxManage;

import io.reactivex.disposables.Disposable;

public class MRXmanager extends RxManage implements IRequestCancleStrategy<Disposable> {
    static MRXmanager mManager;

    public synchronized static MRXmanager getInstance() {
        if (mManager == null) {
            mManager = new MRXmanager();
        }
        return mManager;
    }

    @Override
    public void add(Disposable disposable) {

    }

}
