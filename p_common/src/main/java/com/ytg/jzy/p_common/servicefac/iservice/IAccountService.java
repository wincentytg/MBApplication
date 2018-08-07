package com.ytg.jzy.p_common.servicefac.iservice;

public interface  IAccountService {
    /**
     * 是否已经登录
     * @return
     */
    boolean isLogin();

    /**
     * 获取登录用户的 AccountId
     * @return
     */
    String getAccountId();
}
