package com.volley.libirary.http.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;
/**
 *
 * @author 于堂刚
 */
public class JsonArrayRequest extends IRequest<JSONArray> {

	public JsonArrayRequest(RequestParam param,
			Listener<JSONArray> responseListener, ErrorListener listener) {
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
	protected void deliverResponse(JSONArray response) {
		if (mResponseListener == null) {
			return;
		}
		mResponseListener.onResponse(response);
	}

	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
		try {
			String result = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONArray(result),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new VolleyError(e));
		} catch (JSONException e) {
			return Response.error(new VolleyError(e));
		}
	}
}
