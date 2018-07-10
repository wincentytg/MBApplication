package com.ytg.p_db;

import android.content.Context;

public class Config {
	private Context context;
	private String dbName;
	private int version;
	private Class<?>[] tables;
	private OnDbUpgradeListener listener;

	public Config(Context context) {
		super();
		this.context = context;
	}

	public Config(Context context, String dbName, int version,
			Class<?>[] tables, OnDbUpgradeListener listener) {
		super();
		this.context = context;
		this.dbName = dbName;
		this.version = version;
		this.tables = tables;
		this.listener = listener;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Class<?>[] getTables() {
		return tables;
	}

	public void setTables(Class<?>[] tables) {
		this.tables = tables;
	}

	public OnDbUpgradeListener getDbUpgradeListener() {
		return listener;
	}

	public void setDbUpgradeListener(OnDbUpgradeListener listener) {
		this.listener = listener;
	}

}
