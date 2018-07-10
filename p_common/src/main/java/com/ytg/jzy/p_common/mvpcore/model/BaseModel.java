package com.ytg.jzy.p_common.mvpcore.model;

import com.ytg.jzy.p_common.mvvmcore.BaseLoadListener;

/**
 *
 * @author 于堂刚
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
