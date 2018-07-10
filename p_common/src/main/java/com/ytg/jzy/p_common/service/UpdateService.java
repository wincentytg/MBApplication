package com.ytg.jzy.p_common.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.RemoteViews;

import com.ytg.jzy.p_common.R;
import com.ytg.jzy.p_common.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author 于堂刚
 */
public class UpdateService extends Service {

	private Context context;

	private String sdPath;

	private File updateFile;

	private NotificationManager updateNotificationMgr = null;
	private Notification updateNotification = null;
	private Intent updateIntent = null;
	private PendingIntent updatePendingIntent = null;
	private final int DOWNLOAD_COMPLETE = 1;
	private final int DOWNLOAD_FAIL = 2;
	private final int DOWNLOAD_SUCCESS = 3;

	private Intent serviceIntent;
	String apkName="";
	String downloadutl;
	 Notification.Builder builder;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		apkName=intent.getStringExtra("apk_name");
		downloadutl = intent.getStringExtra("utl");
		return super.onStartCommand(intent, flags, startId);
	}

	@SuppressLint("NewApi") @Override
	public void onStart(Intent intent, int startId) {
		serviceIntent = intent;
		sdPath = new Utils().getfile_path(1);
		
		updateFile = new File(sdPath);
		if (!updateFile.exists()) {
			updateFile.mkdirs();
		}
		if (sdPath != null) {
			if (!apkName.contains(".apk")) {
				apkName = apkName+".apk";
			}
			updateFile = new File(sdPath + "/" + apkName);
			updateNotificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//initNotification();
						
//			updateNotification.contentIntent = updatePendingIntent;
//			updateNotificationMgr.notify(0, updateNotification);
		}
		new updateThread().start();
		super.onStart(intent, startId);
	}

	private class updateThread extends Thread {
		Message msg = handler.obtainMessage();

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				if (!updateFile.exists()) {
					updateFile.createNewFile();
				}
				long downSize = downloadFile(downloadutl, updateFile);
					if (downSize>0) {					
					msg.what = DOWNLOAD_SUCCESS;
					handler.sendMessage(msg);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				msg.what = DOWNLOAD_FAIL;
				handler.sendMessage(msg);
			}
		}
	}

	private long downloadFile(String downloadUrl, File saveFile)
			throws Exception {
		int downloadCount = 0;
		int currentSize = 0;
		long totalSize = 0;
		int updateTotalSize = 12*1024*1024;
		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection
					.setRequestProperty("User-Agent", "PacificHttpClient");
			if (currentSize > 0) {
				httpConnection.setRequestProperty("RANGE", "bytes="
						+ currentSize + "-");
			}
			httpConnection.setRequestProperty("Accept-Encoding", "identity");
			httpConnection.setConnectTimeout(2*60*1000);
			httpConnection.setReadTimeout(2*60*1000);
			if (httpConnection.getResponseCode() == 404) {
				throw new Exception("conection net 404！");
			}
//			updateTotalSize = httpConnection.getContentLength();// 总大小
			is = httpConnection.getInputStream();
			fos = new FileOutputStream(saveFile);
			byte[] buf = new byte[8192];
			int readSize = -1;

			while ((readSize = is.read(buf)) != -1) {
				fos.write(buf, 0, readSize);
				// 通知更新进度
				totalSize += readSize;
				int tmp = (int) (totalSize * 100 / updateTotalSize);
				Log.i("URI", readSize+"--->"+updateTotalSize+"--->"+tmp);
				if (downloadCount == 0 || tmp > downloadCount) {
					downloadCount = tmp;
					Message msg = handler.obtainMessage();
					msg.what = DOWNLOAD_COMPLETE;
					msg.arg1 = downloadCount;
					handler.sendMessage(msg);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			totalSize=0;
			Message msg = handler.obtainMessage();
			msg.what = DOWNLOAD_FAIL;
			handler.sendMessage(msg);
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return totalSize;
	}

	private Handler handler = new Handler() {

		@SuppressLint("NewApi") @Override
		public void handleMessage(Message msg) {
			if(null == updateNotification){
//				updateNotification = builder.build();
				initNoti();
			}
			switch (msg.what) {
			case DOWNLOAD_COMPLETE:
				
			        
				updateNotification.flags = Notification.FLAG_NO_CLEAR;
				updateNotification.contentView.setProgressBar(
						R.id.content_view_progress, 100, msg.arg1, false);
				if (msg.arg1>=100) {
					msg.arg1 =100;
				}
				updateNotification.contentView.setTextViewText(
						R.id.content_view_text1, apkName+"已下载" + msg.arg1 + "%");
				updateNotificationMgr.notify(0, updateNotification);
				
			
				
				break;
			case DOWNLOAD_FAIL:
				updateNotification.contentView.setTextViewText(
						R.id.content_view_text1, "下载失败");
				updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
				updateNotificationMgr.notify(0, updateNotification);
				stopService(serviceIntent);
				break;
			case DOWNLOAD_SUCCESS:
				
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				Uri uri=null;
				 if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
			            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
					 uri = FileProvider.getUriForFile(UpdateService.this, getPackageName() + ".fileprovider", updateFile);
					//添加这一句表示对目标应用临时授权该Uri所代表的文件
					 installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				 }else{
					 uri = Uri.fromFile(updateFile);
				 }
				
				installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				installIntent.setDataAndType(uri,"application/vnd.android.package-archive");
				
				updatePendingIntent = PendingIntent.getActivity(context, 0,
						installIntent, 0);
				updateNotification.contentView.setTextViewText(
						R.id.content_view_text1, apkName+"下载完成  点击可安装");
				updateNotification.contentView.setProgressBar(
						R.id.content_view_progress, 100, 100, false);
			     updateNotification.defaults = 4;//设置铃声    Notification.DEFAULT_SOUND
			     updateNotification.vibrate = new long[] { 80L, 80L };
			     updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
			     updateNotification.iconLevel = -1;
				
				updateNotification.contentIntent = updatePendingIntent;
				updateNotificationMgr.notify(0, updateNotification);
				stopService(serviceIntent) ;
				startActivity(installIntent);	
				break;
			}
		}

	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//stopService(serviceIntent) ;
		super.onDestroy();
	}
	@SuppressLint("NewApi") void initNotification(){
		 builder = new Notification.Builder(this);
	        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
	        builder.setContentIntent(pendingIntent);
	        builder.setSmallIcon(R.mipmap.ic_launcher);
	        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
	        builder.setAutoCancel(false);
	        builder.setContentTitle("正在下载新版本...");
	        builder.setContent(new RemoteViews(getPackageName(),
					R.layout.notification_view));
//	        updateNotification = builder.build();
//	        updateNotificationMgr.notify(0, updateNotification);
	}
	private void initNoti() {
		updateNotification = new Notification();
		updateNotification.icon = R.mipmap.ic_launcher;
//		updateNotification.category = Notification.CATEGORY_SERVICE;
		
		updateNotification.defaults = 4;
		updateNotification.tickerText = "正在下载新版本...";
		 updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
		updateIntent = new Intent();
		updatePendingIntent = PendingIntent.getActivity(context, 0,
				updateIntent, 0);
		updateNotification.contentView = new RemoteViews(getPackageName(),
				R.layout.notification_view);
	}
}
