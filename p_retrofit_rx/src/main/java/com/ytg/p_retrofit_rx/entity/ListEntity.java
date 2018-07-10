package com.ytg.p_retrofit_rx.entity;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 *
 * @author 于堂刚
 */
public abstract class ListEntity<T> extends Entity implements IListBean {

    @Override
    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    @Override
    public abstract Observable<BaseHttpResult<List<T>>> getPage(int page);
}
