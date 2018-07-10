package com.ytg.jzy.http;

import com.volleyl.libirary.http.HttpManager;
import com.ytg.jzy.p_common.request.IRequestCancleStrategy;

import java.util.ArrayList;

public class MVolleymanager implements IRequestCancleStrategy<HttpManager> {
    static MVolleymanager mManager;
    ArrayList<String> map = new ArrayList<>();
    HttpManager manager;

    public synchronized static MVolleymanager getInstance() {
        if (mManager == null) {
            mManager = new MVolleymanager();
        }
        return mManager;
    }

    @Override
    public void add(String tag, HttpManager disposable) {
        if (null == manager) {
            manager = disposable;
        }
        map.add(tag);
    }

    @Override
    public void add(HttpManager disposable) {

    }

    @Override
    public void remove(String tag) {
        map.remove(tag);
        manager.cancelAll(tag);
    }

    @Override
    public void clear(String tag) {
        map.remove(tag);
        manager.cancelAll(tag);
    }

    @Override
    public void clear() {
        map.clear();
        for (String i : map
                ) {
            manager.cancelAll(i);
        }
    }
}
