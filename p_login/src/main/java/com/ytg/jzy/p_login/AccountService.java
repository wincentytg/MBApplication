package com.ytg.jzy.p_login;

import com.ytg.jzy.p_common.servicefac.iservice.IAccountService;

//组件之间通过 ServiceFactory进行数据交互共享
public class AccountService implements IAccountService {
    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public String getAccountId() {
        return "0123456";
    }
}

