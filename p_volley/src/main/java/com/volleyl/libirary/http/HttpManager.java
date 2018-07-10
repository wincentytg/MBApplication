package com.volleyl.libirary.http;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.volley.libirary.http.request.ExtHttpClientStack;
import com.volley.libirary.http.request.RequestCallBack;
import com.volley.libirary.http.request.RequestCallBack2;
import com.volley.libirary.http.request.RequestParam;
import com.volley.libirary.http.request.Requset;
import com.volley.libirary.http.request.SslHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public class HttpManager implements IHttpManager {

	private static final String TAG = "HttpManager";

	private static volatile HttpManager mManager;

	private RequestQueue mRequestQueue;
	private boolean mDebug = false;

	private HttpManager(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}
	private HttpManager(Context context,InputStream keyStore) {
		mRequestQueue = Volley  
		        .newRequestQueue(context,  new ExtHttpClientStack(new SslHttpClient(keyStore,"smartmro", 8441)));
	}
	public static synchronized HttpManager getInstance(Context context) {
		if (mManager == null) {
			mManager = new HttpManager(context);
		}
		return mManager;
	}
	public static synchronized HttpManager getInstance(Context context,int RawResourceID) {
		if (mManager == null) {
			InputStream keyStore = context.getResources().openRawResource(RawResourceID);
			mManager = new HttpManager(context,keyStore);
		}
		return mManager;
	}
	public void setTimeOut(int timeout){
		Requset.timeout = timeout;
	}
	public void setRetryCount(int retry_Count){
		Requset.retry_Count = retry_Count;
	}
	@Override
	public void getStringResponse(RequestParam param,
			RequestCallBack<String> callBack) {
		if (mDebug) {
			Log.d(TAG, "[getStringResponse]-->" + param.spliceGetRequestUrl());
		}
		Requset.getStringResponse(mRequestQueue, param, callBack);
	}
	@Override
	public void getStringResponse2(RequestParam param,
			RequestCallBack2<String> callBack) {
		if (mDebug) {
			Log.d(TAG, "[getStringResponse]-->" + param.spliceGetRequestUrl());
		}
		Requset.getStringResponse(mRequestQueue, param, callBack);
	}
	@Override
	public void getJsonObjectResponseHttps(RequestParam param,
			RequestCallBack<JSONObject> callBack) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getJsonObjectResponseHttps(RequestParam param,
			RequestCallBack2<JSONObject> callBack) {
		if (mDebug) {
			Log.d(TAG,""+param.spliceGetRequestUrl());
		}
		Requset.getJsonObjectResponse2(mRequestQueue, param, callBack);
	}
	public void getJsonObjectBodyResponse(String url,JSONObject jsonRequest,
			RequestCallBack2<JSONObject> callBack) {
//		if (mDebug) {
//			Log.d(TAG,
//					"[getJsonObjectResponse]-->" + param.spliceGetRequestUrl());
//		}
		Requset.getJsonObjectResponse3(mRequestQueue, url, jsonRequest, callBack);
//		Requset.getJsonObjectBodyResponse(mRequestQueue, param, callBack);
	}
	public void getJsonObjectBodyResponse(String cookie,String url,JSONObject jsonRequest,
			RequestCallBack2<JSONObject> callBack) {
//		if (mDebug) {
//			Log.d(TAG,
//					"[getJsonObjectResponse]-->" + param.spliceGetRequestUrl());
//		}
		Requset.getJsonObjectResponse3(cookie,mRequestQueue, url, jsonRequest, callBack);
//		Requset.getJsonObjectBodyResponse(mRequestQueue, param, callBack);
	}
	@Override
	public void getJsonObjectResponse(RequestParam param,
			RequestCallBack<JSONObject> callBack) {
		if (mDebug) {
			Log.d(TAG,
					"[getJsonObjectResponse]-->" + param.spliceGetRequestUrl());
		}
		Requset.getJsonObjectResponse(mRequestQueue, param, callBack);
	}
	@Override
	public void getJsonObjectResponse2(RequestParam param,
			RequestCallBack2<JSONObject> callBack) {
		if (mDebug) {
			Log.d(TAG,
					"[getJsonObjectResponse]-->" + param.spliceGetRequestUrl());
		}
		Requset.getJsonObjectResponse2(mRequestQueue, param, callBack);
	}
	@Override
	public void getJsonArrayResponse(RequestParam param,
			RequestCallBack<JSONArray> callBack) {
		if (mDebug) {
			Log.d(TAG,
					"[getJsonArrayResponse]-->" + param.spliceGetRequestUrl());
		}
		Requset.getJsonArrayResponse(mRequestQueue, param, callBack);
	}

	@Override
	public void getMultipartResponse(RequestParam param,
			RequestCallBack<String> callBack) {
		if (mDebug) {
			Log.d(TAG,
					"[getMultipartResponse]-->" + param.spliceGetRequestUrl());
		}
		Requset.getMultipartResponse(mRequestQueue, param, callBack, null);
	}

	@Override
	public void getMultipartResponse(RequestParam param,
			RequestCallBack<String> callBack, Handler handler) {
		if (mDebug) {
			Log.d(TAG,
					"[getMultipartResponse]-->" + param.spliceGetRequestUrl());
		}
		Requset.getMultipartResponse(mRequestQueue, param, callBack, handler);
	}

	@Override
	public void cancelAll(Object tag) {
		if (mDebug) {
			Log.d(TAG, "[cancelAll]-->" + tag);
		}
		mRequestQueue.cancelAll(tag);
	}

	public void setDebug(boolean isDebug) {
		mDebug = isDebug;
	}
	@Override
	public void upload( String url,
			Map<String, File> files, Map<String, String> params,
			 Listener<String> listener,
			ErrorListener errorListener) {
		// TODO Auto-generated method stub
		Requset.upload(mRequestQueue, url, files, params, listener, errorListener);
	}
	
}
