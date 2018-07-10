package com.ytg.jzy.p_common.tools;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.ytg.jzy.p_common.utils.ToastUtil;
import com.ytg.jzy.p_common.utils.Utils;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class UploadRequestAsyncTask extends AsyncTask<Void, Void, String>
		implements OnDismissListener {
	private HashMap<String, Object> mapIMs;
	private HashMap<String, File> files;
	private String actionUrl;
	private String filename;
	private Context context;
	Dialog dialog;
	private String path;
	private String servceType;
	private UpdateSucBack onback;
	Handler handlerUp;

	public interface UpdateSucBack {
		void onback(Object info);
	}

	public UploadRequestAsyncTask(Context context, String actionUrl,
			HashMap<String, Object> mapIMs, HashMap<String, File> files,
			String filename, Dialog dialog, String path, String servceType,
			UpdateSucBack onback) {
		this.mapIMs = mapIMs;
		this.files = files;
		this.actionUrl = actionUrl;
		this.filename = filename;
		this.context = context;
		this.dialog = dialog;
		this.path = path;
		this.servceType = servceType;
		this.onback = onback;
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			if (null!=dialog) {
				dialog.show();
			}
			return new Upload().postFiles(actionUrl, mapIMs, files, filename,
					servceType);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object) GONGYOUNAME
	 * 上传工友头像 MENDIANNAME 上传基本信息头像 FragmentCertificate 上传资质认证图片（其余的上传）
	 */
	@Override
	protected void onPostExecute(String result) {
		if (null != dialog) {
			dialog.dismiss();
		}
		if (Utils.isEmpty(result)) {
			Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			JSONObject info = new JSONObject(result);
			if (info.optBoolean("state")) {
				Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
				Object infoTemp = new Object();
				onback.onback(infoTemp);
			} else {
				ToastUtil.showMessage(info.optString("msg"));
			}
		} catch (Exception e) {
//			Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		if (null != dialog) {
			dialog.show();
		}
		super.onPreExecute();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}

}
