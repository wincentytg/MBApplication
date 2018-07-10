package com.volley.libirary.http.request;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

public class JsonObjectBodyRequest extends IRequest<JSONObject> {

	public JsonObjectBodyRequest(RequestParam param,
			Listener<JSONObject> responseListener, ErrorListener listener) {
		super(param.getRequestMode() == RequestMode.GET ? Method.GET
				: Method.POST,
				param.getRequestMode() == RequestMode.GET ? param
						.spliceGetRequestUrl() : param.getUrl(), listener);
		mParam = param;
		mResponseListener = responseListener;
		setShouldCache(true);
		setRetryPolicy(getRetryPolicy(param));
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mParam.getParams();
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		if (mResponseListener == null) {
			return;
		}
		mResponseListener.onResponse(response);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String result = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(result),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new VolleyError(e));
		} catch (JSONException e) {
			return Response.error(new VolleyError(e));
		}
	}
//	@Override
//	public Map<String, String> getHeaders() throws AuthFailureError {
//		HashMap<String, String> headers = new HashMap<String, String>();
//		headers.put("Accept","application/json");
//		headers.put("Content-Type","application/json; charset=UTF-8");
//		return headers;
//	}
//@Override
//public byte[] getBody() throws AuthFailureError {
//	byte[] sss = null;
//	try {
//		sss = mParam.getmDatabyte().getBytes("UTF-8");
//		
//		Log.i("URI",  mParam.getmDatabyte()+"-->"+sss+"-->"+new String(sss,"UTF-8"));
//	} catch (UnsupportedEncodingException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	return  sss; //return mParam == null ? super.getBody() : mParam.getBytes();  
//}
}
