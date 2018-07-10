package com.ytg.p_db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

public class BaseDBManager {

	protected Config mConfig;
	protected boolean mDebug = false;

	/** The Constant FIELD_DATABASE_NAME. */
	private static final String DB_NAME = "database.db";

	/** The Manager Maps */
	protected static volatile Map<String, DBManager> mManagers = new HashMap<String, DBManager>();

	/** The Table NAME List */
	protected LinkedList<String> mTableNames = new LinkedList<String>();

	public BaseDBManager(Config config) {
		if (config.getDbName() == null
				|| config.getDbName().trim().length() == 0) {
			config.setDbName(DB_NAME);
		}
		mConfig = config;
	}

	public Config getConfig() {
		return mConfig;
	}

	public void setDebug(boolean isDebug) {
		mDebug = isDebug;
	}

	/**
	 * 打开或创建数据库
	 * 
	 * @return
	 */
	protected SQLiteDatabase openOrCreateDatabase() {
		return mConfig.getContext().openOrCreateDatabase(mConfig.getDbName(),
				0, null);
	}
}
