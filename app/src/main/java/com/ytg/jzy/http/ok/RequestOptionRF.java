package com.ytg.jzy.http.ok;

import com.ytg.jzy.p_common.request.RequestOptions;

public class RequestOptionRF extends RequestOptions {
    public RequestOptionRF(String url) {
        super(url);
    }

    public RequestOptionRF(String url, Method methodName) {
        super(url);
        this.methodName = methodName;
    }

    public Method methodName;

    public enum Method {
        insertData,
        getWeather("insertData");
        String value;

        Method() {
        }

        Method(String value) {
            this.value = value;
        }
    }
}
