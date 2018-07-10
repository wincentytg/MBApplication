package com.ytg.jzy.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.volley.libirary.http.request.RequestCallBack;
import com.volley.libirary.http.request.RequestMode;
import com.volley.libirary.http.request.RequestParam;
import com.volleyl.libirary.http.HttpManager;
import com.ytg.jzy.http.QInterface;
import com.ytg.jzy.p_common.dialog.DialogOnTextView;
import com.ytg.jzy.p_common.service.UpdateService;
import com.ytg.jzy.p_common.tools.SharedPreferencesHelper;
import com.ytg.jzy.p_common.utils.ToastUtil;
import com.ytg.jzy.p_common.utils.Utils;

import org.json.JSONObject;

public class CheckVersion {
	Double version = 1.0;
	public CheckVersion() {

	}

	public void check(HttpManager mHttpManager, final Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			 version = Double.parseDouble(packInfo.versionName);
			RequestParam params = new RequestParam();
			params.setUrl(QInterface.CHECK_VERSION)
					.setRequestMode(RequestMode.GET)
					.setTimeout(60000);// ��������ʱ��
			mHttpManager.getJsonObjectResponse(params, new RequestCallBack<JSONObject>() {
				
				@Override
				public void onResult(JSONObject jsonObject) {
					// TODO Auto-generated method stub
					Log.i("http",jsonObject.toString());
					try {
						if (!TextUtils.isEmpty(jsonObject.toString())) {
							SharedPreferencesHelper spHelper = SharedPreferencesHelper.getInstance(context);
							if (version < jsonObject.optDouble("version")) {
								spHelper.putBoolean("newVersion", true);
								versionDialog(jsonObject.optString("title"),jsonObject.optString("version"), jsonObject.optString("content"), jsonObject.optString("name"),jsonObject.optString("url"),context);
							}else{
								spHelper.putBoolean("newVersion", false);
							}
						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				@Override
				public void onError() {
					// TODO Auto-generated method stub
					
				}
			});
			
			
		} catch (Exception e) {
				e.printStackTrace();
		}

	}
	 String apkname;
	void versionDialog(String title,final String version, String contents,final String name,final String url,final Context context) {
//		if (url.contains(".")) {
//			apkname = url.substring(url.lastIndexOf("/")+1);
//		}else{
			apkname = name+".apk";
//		}
		final DialogOnTextView dialog = new DialogOnTextView(context, true);
		
		dialog.setTitle(TextUtils.isEmpty(title)?"新版本更新提示":title);
//		dialog.setVersionNum(version);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setDialogMsg(contents.replace("|", "\n"));
		dialog.setLeftBtn(View.VISIBLE, "稍后再说", new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.setRightBtn(View.VISIBLE, "立即更新", new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (Utils.isDolwanServiceWorked()) {
					ToastUtil.showMessage("正在下载中");
				} else {
					context.startService(new Intent(context.getApplicationContext(),
							UpdateService.class).putExtra("version_name",
							version).putExtra("apk_name",
									apkname)
									.putExtra("url", QInterface.BASE_URl+url));
				}

			}
		});
		dialog.show();
		
//		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//				if (keyCode == KeyEvent.ACTION_DOWN) {
//					return true;
//				}else {
//					return true;
//				}
//				
//			}
//		});
		 
	}
	
}
