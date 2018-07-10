package com.ytg.jzy.p_common.request;

import android.content.Context;

import java.util.LinkedHashMap;

/**
 *不跟任何网络框架有关，但又适用于所有的网络框架，负责传递网络请求的一些配置信息，如需别的方法自行添加即可
 */
public class RequestOptions {
    public String url;
    public RequestBack back;
    public LinkedHashMap<String, String> paramsMap;

    public RequestOptions setParamsMap(LinkedHashMap<String, String> paramsMap) {
        this.paramsMap = paramsMap;return this;
    }
    public RequestOptions(String url) {
        this.url = url;
    }

    public RequestOptions setUrl(String url) {
        this.url = url;
        return this;
    }

    public RequestOptions setBack(RequestBack back) {
        this.back = back;return this;
    }

    public void request(Context context,RequestBack back) {
        MRequestManager.getInstance().loadOptions(context, this,back);
    }

}
