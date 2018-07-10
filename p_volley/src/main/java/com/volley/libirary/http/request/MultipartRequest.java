package com.volley.libirary.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.volley.libirary.http.request.MyMultipartEntity.OnProgressListener;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends IRequest<String> {

	private HttpEntity mEntity;
	private Handler mHandler;
	private long mFileSize;

	public MultipartRequest(RequestParam param,
			Listener<String> responseListener, ErrorListener listener,
			Handler handler) {
		super(Method.POST, param.getUrl(), listener);
		mParam = param;
		mResponseListener = responseListener;
		mHandler = handler;
		setShouldCache(false);
		setRetryPolicy(getRetryPolicy(param));
		mFileSize = getFileSize();
		mEntity = getEntity();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			mEntity.writeTo(baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	@Override
	public String getBodyContentType() {
		return mEntity.getContentType().getValue();
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders();
		if (null == headers || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}
		return headers;
	}

	@Override
	protected void deliverResponse(String response) {
		if (mResponseListener == null) {
			return;
		}
		mResponseListener.onResponse(response);
	}

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
	}

	private long getFileSize() {
		long fileSize = 0;
		for (int i = 0; i < mParam.getFiles().size(); i++) {
			File file = mParam.getFiles().get(i);
			fileSize += getFileSize(file.getPath());
		}
		return fileSize;
	}
	/**
	 * 获取文件的大小
	 *
	 * @param path
	 * @return
	 */
	public static long getFileSize(String path) {
		if (TextUtils.isEmpty(path)) {
			return -1;
		}
		File file = new File(path);
		return (file.exists() && file.isFile() ? file.length() : -1);
	}
	private HttpEntity getEntity() {
		MyMultipartEntity entity = new MyMultipartEntity(
				new OnProgressListener() {

					@Override
					public void transferred(long size) {
						int progress = (int) Math.ceil(size * 100
								* mParam.getFiles().size() / mFileSize);
						if (mHandler != null) {
							Message msg = mHandler.obtainMessage(0, progress);
							mHandler.sendMessage(msg);
						}
					}
				});
		for (int i = 0; i < mParam.getFiles().size(); i++) {
			File file = mParam.getFiles().get(i);
			String fileName = mParam.getFileNames().get(i);
			ContentBody body = new FileBody(file);
			entity.addPart(fileName, body);
		}
		for (Map.Entry<String, String> entry : mParam.getParams().entrySet()) {
			try {
				ContentBody body = new StringBody(entry.getValue().toString(),
						Charset.forName(mParam.getCharSet()));
				entity.addPart(entry.getKey(), body);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return entity;
	}
}
