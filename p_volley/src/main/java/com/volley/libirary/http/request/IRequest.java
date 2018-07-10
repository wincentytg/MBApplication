package com.volley.libirary.http.request;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
/**
 *
 * @author 于堂刚
 */
public abstract class IRequest<T> extends Request<T> {

	protected RequestParam mParam;
	protected Listener<T> mResponseListener;

	public IRequest(int method, String url, ErrorListener listener) {
		super(method, url, listener);

	}

	protected RetryPolicy getRetryPolicy(RequestParam param) {
		int timeOut = param.getTimeout() <= 0 ? RequestParam.DEFAULT_TIMEOUT
				: param.getTimeout();
		
		return new DefaultRetryPolicy(timeOut,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
	}
	
}
