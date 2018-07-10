package com.ytg.jzy.p_common.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {

	public static SharedPreferencesHelper spHelper;

	public static SharedPreferences preferences;

	public static Editor editor;

	private SharedPreferencesHelper() {

	}

	public static SharedPreferencesHelper getInstance(Context mContext) {
		if (spHelper == null) {
			spHelper = new SharedPreferencesHelper();
			preferences = mContext.getSharedPreferences(mContext.getPackageName().replace(".","_")+"_Preferences",
					Activity.MODE_PRIVATE);
			editor = preferences.edit();
		}
		return spHelper;
	}

	public void putString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key) {
		return preferences.getString(key, "");
	}

	public void putInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public int getInt(String key) {
		return preferences.getInt(key, 0);
	}
	public int getInt(String key,int defaultv) {
		return preferences.getInt(key, defaultv);
	}
	public float getFloat(String key) {
		return preferences.getFloat(key, 0);
	}
	public float getFloat(String key,float defaultv) {
		return preferences.getFloat(key, defaultv);
	}
	public void putFloat(String key,float defaultv) {
		 editor.putFloat(key, defaultv);editor.commit();
	}
	public void putBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key) {
		return preferences.getBoolean(key, false);
	}
	public boolean getBoolean(String key,Boolean defValue) {
		return preferences.getBoolean(key, defValue);
	}
	public void putLong(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public long getLong(String key) {
		return preferences.getLong(key, 0L); 
	}
	
	
}
