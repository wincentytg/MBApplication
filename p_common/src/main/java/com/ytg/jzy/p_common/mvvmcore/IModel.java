package com.ytg.jzy.p_common.mvvmcore;

/**
 * 作者： 周旭 on 2017年10月18日 0018.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public interface IModel<T> {
    /**
     * 获取新闻数据
     *
     * @param page 页数
     * @param size 每页数据的条数
     * @param loadListener
     */
    void loadData(int page,int size, BaseLoadListener<T> loadListener);
}
