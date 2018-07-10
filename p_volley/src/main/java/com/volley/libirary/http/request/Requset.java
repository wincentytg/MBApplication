package com.volley.libirary.http.request;

import android.os.Handler;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;
/**
 *
 * @author 于堂刚
 */
public class Requset {
	public static int timeout = 120000;
	public static int retry_Count = 2;
	public static void getStringResponse(RequestQueue requestQueue,
			RequestParam param, final RequestCallBack<String> callBack) {
		StringRequset request = new StringRequset(param,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (callBack == null) {
							return;
						}
						callBack.onResult(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (callBack == null) {
							return;
						}
						callBack.onError();
					}
				});
		request.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES+retry_Count, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag(param.getUrl());
		requestQueue.add(request);
	}
	public static void getStringResponse(RequestQueue requestQueue,
			RequestParam param, final RequestCallBack2<String> callBack) {
		StringRequset request = new StringRequset(param,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (callBack == null) {
							return;
						}
						callBack.onResult(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (callBack == null) {
							return;
						}
						callBack.onError(error);
					}
				});
		request.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES+retry_Count, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag(param.getUrl());
		requestQueue.add(request);
	}
	public static void getJsonObjectBodyResponse(RequestQueue requestQueue,
			RequestParam param, final RequestCallBack2<JSONObject> callBack) {
		JsonObjectBodyRequest request = new JsonObjectBodyRequest(param,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						if (callBack == null) {
							return;
						}
						callBack.onResult(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (callBack == null) {
							return;
						}
						callBack.onError(error);
					}
				});
		request.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES+retry_Count, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag(param.getUrl());
		requestQueue.add(request);
	}
	public static void getJsonObjectResponse(RequestQueue requestQueue,
			RequestParam param, final RequestCallBack<JSONObject> callBack) {
		JsonObjectRequest request = new JsonObjectRequest(param,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						if (callBack == null) {
							return;
						}
						callBack.onResult(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (callBack == null) {
							return;
						}
						callBack.onError();
					}
				});
		request.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES+retry_Count, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag(param.getUrl());
		requestQueue.add(request);
	}
	public static void getJsonObjectResponse2(RequestQueue requestQueue,
			RequestParam param, final RequestCallBack2<JSONObject> callBack) {
		JsonObjectRequest request = new JsonObjectRequest(param,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						if (callBack == null) {
							return;
						}
						callBack.onResult(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (callBack == null) {
							return;
						}
						callBack.onError(error);
					}
				});
		request.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES+retry_Count, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag(param.getUrl());
		requestQueue.add(request);
	}public static void getJsonObjectResponse3(RequestQueue requestQueue,
			String url,JSONObject jsonRequest, final RequestCallBack2<JSONObject> callBack) {
		com.android.volley.toolbox.JsonObjectRequest request = new com.android.volley.toolbox.JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				// TODO Auto-generated method stub
				callBack.onResult(arg0);
			}
		}, new com.android.volley.Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				callBack.onError(arg0);
			}
			
		});
		request.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES+retry_Count, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag(url);
		requestQueue.add(request);
	}
	public static void getJsonObjectResponse3(String cookie,RequestQueue requestQueue,
			String url,JSONObject jsonRequest, final RequestCallBack2<JSONObject> callBack) {
		JsonObjectBodySessionRequest request = new JsonObjectBodySessionRequest(cookie,Request.Method.POST, url, jsonRequest, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				// TODO Auto-generated method stub
				callBack.onResult(arg0);
			}
		}, new com.android.volley.Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				callBack.onError(arg0);
			}
			
		});
		
		request.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES+retry_Count, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag(url);
		requestQueue.add(request);
	}
	public static void getJsonArrayResponse(RequestQueue requestQueue,
			RequestParam param, final RequestCallBack<JSONArray> callBack) {
		JsonArrayRequest request = new JsonArrayRequest(param,
				new Response.Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						if (callBack == null) {
							return;
						}
						callBack.onResult(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (callBack == null) {
							return;
						}
						callBack.onError();
					}
				});
		request.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES+retry_Count, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag(param.getUrl());
		requestQueue.add(request);
	}

	public static void getMultipartResponse(RequestQueue requestQueue,
			RequestParam param, final RequestCallBack<String> callBack,
			Handler handler) {
		MultipartRequest request = new MultipartRequest(param,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (callBack == null) {
							return;
						}
						callBack.onResult(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (callBack == null) {
							return;
						}
						callBack.onError();
					}
				}, handler);
		request.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES+retry_Count, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag(param.getUrl());
		requestQueue.add(request);
	}
	public static void upload(RequestQueue requestQueue,String url,final Map<String, File> files,
			final Map<String, String> params, Listener<String> listener,ErrorListener errorListener){
		 MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
		            Request.Method.PUT, url, listener, errorListener) {

		        @Override
		        public Map<String, File> getFileUploads() {
		            return files;
		        }

		        @Override
		        public Map<String, String> getStringUploads() {
		            return params;
		        }

		    };

		    requestQueue.add(multiPartRequest);
	}
}
