package com.volley.libirary.http.request;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;

public class RequestParam {

	private String mUrl;
	private String mCharSet = HTTP.UTF_8;
	private int mTimeout = DEFAULT_TIMEOUT;
	private RequestMode mRequestMode = RequestMode.POST;
	private LinkedHashMap<String, String> mParams = new LinkedHashMap<String, String>();
	private ArrayList<File> mFiles = new ArrayList<File>();
	private ArrayList<String> mFileNames = new ArrayList<String>();

	private Object mTag;

	public static final int DEFAULT_TIMEOUT = 15000;
	private String mDatabyte = "";

	public void setmDatabyte(String mDatabyte) {
		this.mDatabyte = mDatabyte;
	}
public String getmDatabyte() {
	return mDatabyte;
}
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return mDatabyte.getBytes();
	}

	public RequestParam addFile(File file, String fileName) {
		if (file == null || fileName == null) {
			return this;
		}
		mFiles.add(file);
		mFileNames.add(fileName);
		return this;
	}

	public RequestParam addParam(String key, Object value) {
		if (key == null || value == null) {
			return this;
		}
		mParams.put(key, value + "");
		return this;
	}

	public RequestParam addMapParams(Map<String, String> params) {
		if (params == null) {
			return this;
		}
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (key == null || value == null) {
				continue;
			}
			mParams.put(key, value);
		}
		return this;
	}

	public RequestParam removeParams(String key) {
		if (key == null) {
			return this;
		}
		if (mParams.containsKey(key)) {
			mParams.remove(key);
		}
		return this;
	}

	public RequestParam removeMapParams(Map<String, ? extends Object> params) {
		if (params == null) {
			return this;
		}
		for (Map.Entry<String, ? extends Object> entry : params.entrySet()) {
			String key = entry.getKey();
			if (key == null) {
				continue;
			}
			if (mParams.containsKey(key)) {
				mParams.remove(key);
			}
		}
		return this;
	}

	public String getUrl() {
		return mUrl;
	}

	public RequestParam setUrl(String url) {
		this.mUrl = url;
		return this;
	}

	public int getTimeout() {
		return mTimeout;
	}

	public RequestParam setTimeout(int timeout) {
		this.mTimeout = timeout;
		return this;
	}

	public String getCharSet() {
		return mCharSet;
	}

	public RequestParam setCharSet(String charset) {
		this.mCharSet = charset;
		return this;
	}

	public RequestMode getRequestMode() {
		return mRequestMode;
	}

	public RequestParam setRequestMode(RequestMode requestMode) {
		this.mRequestMode = requestMode;
		return this;
	}

	public LinkedHashMap<String, String> getParams() {
		return mParams;
	}

	public ArrayList<File> getFiles() {
		return mFiles;
	}

	public void setFiles(ArrayList<File> files) {
		this.mFiles = files;
	}

	public ArrayList<String> getFileNames() {
		return mFileNames;
	}

	public void setFileNames(ArrayList<String> fileNames) {
		this.mFileNames = fileNames;
	}

	public Object getTag() {
		return mTag;
	}

	public void setTag(Object tag) {
		this.mTag = tag;
	}

	public String spliceGetRequestUrl() {
		if (mUrl == null) {
			return null;
		}
		if (mParams == null || mParams.size() == 0) {
			return mUrl;
		}
		StringBuffer buffer = new StringBuffer(mUrl);
		if (mUrl.endsWith("?")) {
		} else {
			buffer.append("?");
		}
		for (Map.Entry<String, String> entry : mParams.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (key == null || value == null) {
				continue;
			}
			buffer.append(key).append("=").append(value).append("&");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}
}
