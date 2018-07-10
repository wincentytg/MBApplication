/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.ytg.jzy.p_common.utils;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Build.VERSION_CODES;

import com.ytg.jzy.p_common.MainActivity;
import com.ytg.jzy.p_common.R;


/**
 * @date 2015-1-4
 * @version 4.0
 */
public class NotificationUtil extends ContextWrapper {
	
	public static final String TAG = LogUtil.getLogUtilsTag(Notification.class);
	public NotificationUtil(Context base) {
		super(base);
	}

	/**
	 *
	 * @param context
	 * @param Vibrate 有无震动
	 */
	public static void senNotify(Context context,boolean Vibrate){

		Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification n= NotificationUtil.buildNotification(context,R.mipmap.ic_launcher,Vibrate?Notification.DEFAULT_ALL:Notification.DEFAULT_LIGHTS,true,"","标题","contentText",null,contentIntent);
		n.flags = (Notification.FLAG_AUTO_CANCEL | n.flags);
		((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE)).notify(0, n);
	}


	@TargetApi(VERSION_CODES.HONEYCOMB)
	public static Notification buildNotification(Context context, int icon,
												 int defaults, boolean onlyVibrate, String tickerText,
												 String contentTitle, String contentText, Bitmap largeIcon,
												 PendingIntent intent) {
		
		if(Build.VERSION.SDK_INT > VERSION_CODES.HONEYCOMB) {
			Notification.Builder builder = new Notification.Builder(context);
			builder.setLights(-16711936, 300, 1000)
			.setSmallIcon(icon)
			.setTicker(tickerText)
			.setContentTitle(contentTitle)
			.setContentText(contentText)
			.setContentIntent(intent);
			
			if(onlyVibrate) {
				defaults &= Notification.DEFAULT_VIBRATE;
			} 
			
			LogUtil.d(TAG, "defaults flag " + defaults);
			builder.setDefaults(defaults);
			if(largeIcon != null) {
				builder.setLargeIcon(largeIcon);
			}
			return builder.getNotification();
		}
		
		Notification notification = new Notification();
		notification.ledARGB = -16711936;
		notification.ledOnMS = 300;
		notification.ledOffMS = 1000;
		notification.flags = (Notification.FLAG_SHOW_LIGHTS | notification.flags);
		notification.icon = icon;
		notification.tickerText = tickerText;
		LogUtil.d(TAG, "defaults flag " + defaults);
		if(onlyVibrate) {
			defaults &= Notification.DEFAULT_VIBRATE;
		} 
		notification.defaults = defaults;
		//notification.setLatestEventInfo(context, contentTitle, contentText, intent);
	    return notification;
	}
	@TargetApi(VERSION_CODES.HONEYCOMB)
	public static Notification showAppUpdater(Context context,String contentText, PendingIntent intent) {
		
		if(Build.VERSION.SDK_INT > VERSION_CODES.HONEYCOMB) {
			Notification.Builder builder = new Notification.Builder(context);
			builder.setSmallIcon(android.R.drawable.stat_sys_download)
			.setContentTitle(context.getString(R.string.app_download_update_package , "某APP"))
			.setContentText(contentText)
			.setContentIntent(intent);
			builder.setDefaults(Notification.DEFAULT_LIGHTS);
			return builder.getNotification();
		}
		
		Notification notification = new Notification();
		notification.defaults = Notification.DEFAULT_LIGHTS;
		notification.icon = android.R.drawable.stat_sys_download;
		//notification.setLatestEventInfo(context, context.getString(ResourceHelper.getInstance().getStringId("app_download_update_package")), contentText, intent);
	    return notification;
	}
}
