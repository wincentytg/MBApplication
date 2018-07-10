package com.volley.libirary.http.request;

public interface RequestCallBack<T> {

	public abstract void onResult(T response);

	public abstract void onError();
	
}
