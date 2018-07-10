package com.ytg.jzy.p_common.mvpcore.model;

import com.ytg.jzy.p_common.mvvmcore.BaseLoadListener;

/**
 * Created by Tervor on 2016/7/21 0021.
 */
public interface BaseModel<T> {
    /**
     *获取列表数据
     * @param page 页数
     * @param size 每页数据的条数
     * @param loadListener
     */
    void loadData(int page,int size, BaseLoadListener<T> loadListener);

}
