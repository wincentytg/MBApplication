package com.ytg.jzy.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.ytg.jzy.R;


/**
 * 创建快捷方式
 */
public class CheckCreateShortCut {
	private Context context;
	private String className;
	public CheckCreateShortCut(Activity splashActivity, String name) {
		this.context = splashActivity;
		this.className = name;
		checkShortCut();
	}
	
	private void checkShortCut() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		// 是否在桌面上添加了快捷方�?
		boolean never_check_shortCut = sp.getBoolean("never_check_shortCut",
				false);
		// 存在快捷方式或�?�不允许添加，return
		if (never_check_shortCut) {
			return;
		} else {

			addShortCut();

			// 保存已经添加了快捷方式的信息，以便程序下次启动的不再提示
			Editor editor = sp.edit();
			editor.putBoolean("never_check_shortCut", true);
			editor.commit();
		}
	}
	private void addShortCut() {
		// 添加快捷方式
		// 指定快捷方式的Action
		Intent installShortCut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 添加快捷方式的名�?
		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		installShortCut.putExtra("duplicate", false);
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.setClassName(context, className);
		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		// 指定图标
		ShortcutIconResource iconRes = ShortcutIconResource.fromContext(
				context, R.mipmap.ic_launcher);
		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		// 发�?�广�?
		context.sendBroadcast(installShortCut);
	}
}
