package com.ytg.p_db;

public interface OnDbUpgradeListener {
	public abstract void onUpgrade(DBManager manager) throws DBException;
}
