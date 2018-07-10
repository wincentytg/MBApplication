package com.ytg.p_retrofit_rx.entity;

import java.util.Map;

import io.reactivex.Observable;

/**
 *
 * @author 于堂刚
 */
interface IListBean {
    //获取第几页
    Observable getPage(int page);

    //设置网络请求参数
    void setParam(Map<String, String> param);
}
