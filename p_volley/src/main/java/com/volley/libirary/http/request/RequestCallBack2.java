package com.volley.libirary.http.request;

import com.android.volley.VolleyError;

public interface RequestCallBack2<T> {

	public abstract void onResult(T response);

	public abstract void onError(VolleyError error);
	
}
