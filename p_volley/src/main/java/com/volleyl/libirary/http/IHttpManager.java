package com.volleyl.libirary.http;

import java.io.File;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.volley.libirary.http.request.RequestCallBack2;
import com.volley.libirary.http.request.RequestCallBack;
import com.volley.libirary.http.request.RequestParam;

public interface IHttpManager {

	/**
	 * String请求
	 * 
	 * @param param
	 *            参数
	 * @param callBack
	 *            请求回调
	 */
	public abstract void getStringResponse(RequestParam param,
			RequestCallBack<String> callBack);
	public abstract void getStringResponse2(RequestParam param,
			RequestCallBack2<String> callBack);
	public abstract void getJsonObjectResponseHttps(RequestParam param,
			RequestCallBack<JSONObject> callBack);

	/**
	 * JsonObject请求
	 * 
	 * @param param
	 *            参数
	 * @param callBack
	 *            请求回调
	 */
	public abstract void getJsonObjectResponse(RequestParam param,
			RequestCallBack<JSONObject> callBack);

	/**
	 * 返回VolleyError的请求
	 * 
	 * @param param
	 * @param callBack
	 */
	public abstract void getJsonObjectResponse2(RequestParam param,
			RequestCallBack2<JSONObject> callBack);

	/**
	 * JsonArray请求
	 * 
	 * @param param
	 *            参数
	 * @param callBack
	 *            请求回调
	 */
	public abstract void getJsonArrayResponse(RequestParam param,
			RequestCallBack<JSONArray> callBack);

	/**
	 * 上传文件请求(可包含参数)
	 * 
	 * @param param
	 *            参数
	 * @param callBack
	 *            请求回调
	 */
	public abstract void getMultipartResponse(RequestParam param,
			RequestCallBack<String> callBack);

	/**
	 * 上传文件请求(可包含参数)
	 * 
	 * @param param
	 *            参数
	 * @param callBack
	 *            请求回调
	 * @param handler
	 *            进度handler
	 */
	public abstract void getMultipartResponse(RequestParam param,
			RequestCallBack<String> callBack, Handler handler);

	/**
	 * 取消请求
	 * 
	 * @param tag
	 *            标签
	 */
	public abstract void cancelAll(Object tag);

	public abstract void getJsonObjectResponseHttps(RequestParam param,
			RequestCallBack2<JSONObject> callBack);
	public abstract void upload(String url,final Map<String, File> files,
			final Map<String, String> params, Listener<String> listener,ErrorListener errorListener);
}
