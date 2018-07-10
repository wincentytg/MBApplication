package com.volley.libirary.http.request;
/**
 *
 * @author 于堂刚
 */
public interface RequestCallBack<T> {

	public abstract void onResult(T response);

	public abstract void onError();
	
}
