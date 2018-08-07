package com.ytg.jzy.p_common.mvvmcore;

/**
 *
 * @author 于堂刚
 */
public interface IModel<T> {
    /**
     * 获取新闻数据
     *
     * @param page 页数
     * @param size 每页数据的条数
     * @param loadListener
     */
    void loadData(int page, int size, BaseLoadListener<T> loadListener);
}
