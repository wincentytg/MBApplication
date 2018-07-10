package com.volley.libirary.http.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;
/**
 *
 * @author 于堂刚
 */
public class StringRequset extends IRequest<String> {

	public StringRequset(RequestParam param, Listener<String> responseListener,
			ErrorListener listener) {
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
	protected void deliverResponse(String response) {
		if (mResponseListener == null) {
			return;
		}
		mResponseListener.onResponse(response);
	}
//	@Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//      HashMap<String, String> headers = new HashMap<String, String>();
//      headers.put("Cookie", mParam.getParams().get("Cookie"));
//      return headers;
//    }
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String result = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(result,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new VolleyError(e));
		}
		
//		try {
//            String jsonString = new String(response.data, "UTF-8");
//            return Response.success(jsonString,
//                    HttpHeaderParser.parseCacheHeaders(response));
//        } catch (UnsupportedEncodingException e) {
//            return Response.error(new ParseError(e));
//        } catch (Exception je) {
//            return Response.error(new ParseError(je));
//        }
	}

}
