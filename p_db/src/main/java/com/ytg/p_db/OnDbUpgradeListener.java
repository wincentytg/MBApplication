package com.ytg.p_db;
/**
 *
 * @author 于堂刚
 */
public interface OnDbUpgradeListener {
	public abstract void onUpgrade(DBManager manager) throws DBException;
}
