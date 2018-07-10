package com.ytg.jzy.db;

import com.ytg.jzy.p_common.db.DBOptions;

public class Options extends DBOptions {
    public Options(String id) {
        super(id);
    }

    public Options(String id, Method methodName) {
        super(id);
        this.methodName = methodName;
    }

    public Method methodName;

    public enum Method {
        insertData,
        handleData("insertData");
        String value;

        Method() {
        }

        Method(String value) {
            this.value = value;
        }
    }
}
