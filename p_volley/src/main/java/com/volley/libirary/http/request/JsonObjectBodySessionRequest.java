package com.volley.libirary.http.request;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JsonObjectBodySessionRequest extends com.android.volley.toolbox.JsonObjectRequest {

	String cookie;

	public JsonObjectBodySessionRequest(String cookie,int method, String url,
			JSONObject jsonRequest, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
		// TODO Auto-generated constructor stub
		this.cookie = cookie;
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
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Accept","application/json");
		headers.put("Content-Type","application/json; charset=UTF-8");
		headers.put("X-AUSERNAME","zxjfc");
		
		if(!TextUtils.isEmpty(cookie)){
			headers.put("cookie", cookie);
		}
		return headers;
	}
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
