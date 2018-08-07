package com.ytg.jzy.p_common.servicefac.iserviceimpl;

import com.ytg.jzy.p_common.servicefac.iservice.IAccountService;

public class EmptyAccountService implements IAccountService {
    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String getAccountId() {
        return null;
    }
}