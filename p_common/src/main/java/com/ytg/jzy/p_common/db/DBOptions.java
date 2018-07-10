package com.ytg.jzy.p_common.db;

import android.content.Context;

import java.util.ArrayList;

/**
 *不跟任何网络框架有关，但又适用于所有的网络框架，负责传递网络请求的一些配置信息，如需别的方法自行添加即可
 */
public class DBOptions<T> {
    public String id;
    public String name;
    public ArrayList<T> mlist = new ArrayList<>();
    public DBBack back;
    public DBOptions setMlist(ArrayList<T> mlist) {
        this.mlist.addAll(mlist);return this;
    }

    public DBOptions(String id) {
        this.id = id;
    }

    public DBOptions setName(String name) {
        this.name = name;return this;
    }

    public DBOptions setId(String id) {
        this.id = id;
        return this;
    }

    public void setBack(DBBack back) {
        this.back = back;
    }
    public void handleData(Context context, DBBack back) {
        MDBManager.getInstance().loadOptions(context, this,back);
    }

}
