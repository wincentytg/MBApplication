package com.ytg.jzy.p_common.db;

import android.content.Context;

/**
 * 网络加载类
 * 策略或者静态代理模式，开发者只需要关心RequestManager + RequestOptions
 * * @author 于堂刚
 */

public class MDBManager {
	private static IDBStrategy sRequest;
	private static volatile MDBManager sInstance;

	private MDBManager() {
	}

	//单例模式
	public static MDBManager getInstance() {
		if (sInstance == null) {
			synchronized (MDBManager.class) {
				if (sInstance == null) {
					//若切换其它加载框架，可以实现一键替换
					sInstance = new MDBManager();
				}
			}
		}
		return sInstance;
	}

	//提供实时替换网络加载框架的接口
	public void setDBLoader(IDBStrategy loader) {
		if (loader != null) {
			sRequest = loader;
		}
	}

	public DBOptions id(String path) {
		return new DBOptions(path);
	}


	public void loadOptions(Context context,DBOptions options,DBBack back) {
		sRequest.handleData(context,options,back);
	}

}
