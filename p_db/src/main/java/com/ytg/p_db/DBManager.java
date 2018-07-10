package com.ytg.p_db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ytg.p_db.annotation.Column;
import com.ytg.p_db.annotation.Id;
import com.ytg.p_db.annotation.Ignore;
import com.ytg.p_db.annotation.Table;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class DBManager extends BaseDBManager implements IDBManager {

	private static final String TAG = "DBManager";

	/** The Constant METHOD_INSERT. */
	public static final int METHOD_INSERT = 0;

	/** The Constant METHOD_UPDATE. */
	public static final int METHOD_UPDATE = 1;

	/** The Constant TYPE_NOT_INCREMENT. */
	public static final int TYPE_NOT_INCREMENT = 0;

	/** The Constant TYPE_INCREMENT. */
	public static final int TYPE_INCREMENT = 1;

	private static volatile DBManager mManager;

	private static SQLiteDatabase mDataBase;

	private Lock mLock = new ReentrantLock();

	private DBManager(Config config) {
		super(config);
		mDataBase = openOrCreateDatabase();
		if (config.getTables() != null) {
			try {
				for (int i = 0; i < config.getTables().length; i++) {
					Class<?> cls = config.getTables()[i];
					if (cls.isAnnotationPresent(Table.class)) {
						Table table = cls.getAnnotation(Table.class);
						createTableIfNotExist(cls, table.name());
					}
				}
			} catch (DBException e) {

			}
		}
	}

	public static synchronized DBManager getInstance(Config config) {
		if (config == null) {
			throw new NullPointerException("config is null");
		}
		if (mManagers.containsKey(config.getDbName())) {
			return mManagers.get(config.getDbName());
		}
		mManager = new DBManager(config);
		mManagers.put(config.getDbName(), mManager);
		checkUpgrade();
		return mManager;
	}

	/**
	 * 检查数据库版本更新
	 */
	private static void checkUpgrade() {
		if (mManager.getConfig().getDbUpgradeListener() != null) {
			mManager.lock();
			mManager.openDB();
			int oldVersion = mManager.getDataBase().getVersion();
			int newVersion = mManager.getConfig().getVersion();
			if (newVersion > oldVersion) {
				try {
					mManager.getConfig().getDbUpgradeListener()
							.onUpgrade(mManager);
					if(null!=mManager.getDataBase()){
						mManager.getDataBase().setVersion(newVersion);
					}
				} catch (DBException e) {
					e.printStackTrace();
				}
			}
			mManager.closeDB(null);
			mManager.unLock();
		}
	}

	public void setVersion(int newVersion){
	mManager.getDataBase().setVersion(newVersion);
}


	@Override
	public SQLiteDatabase getDataBase() {
		return mDataBase;
	}

	@Override
	public long insert(Object entity) throws DBException {
		return insert(entity, true);
	}

	@Override
	public long insert(Object entity, boolean flag) throws DBException {
		String sql = "";
		String tableName = null;
		long row = 0L;

		if (entity == null) {
			throw new NullPointerException();
		}

		Class<?> cls = entity.getClass();
		if (cls.isAnnotationPresent(Table.class)) {
			Table table = cls.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = cls.getSimpleName();
		}
		createTableIfNotExist(cls, tableName);
		try {
			mLock.lock();
			openDB();
			ContentValues cv = new ContentValues();
			if (flag) {
				sql = setContentValue(entity, cv, TYPE_INCREMENT, METHOD_INSERT);
			} else {
				sql = setContentValue(entity, cv, TYPE_NOT_INCREMENT,
						METHOD_INSERT);
			}
			if (mDebug) {
				Log.d(TAG, "[insert]: " + "INSERT INTO " + tableName + " "
						+ sql);
			}

			row = mDataBase.insert(tableName, null, cv);
			return row;
		} finally {
			closeDB(null);
			mLock.unlock();
		}

	}

	@Override
	public long insertList(List<?> entityList) throws DBException {
		return insertList(entityList, true);
	}

	@Override
	public long insertList(List<?> entityList, boolean flag) throws DBException {

		String sql = "";
		String tableName = null;
		Class<?> cls = null;
		long row = 0L;

		if (entityList == null) {
			throw new NullPointerException();
		}

		if (entityList.size() > 0) {
			cls = entityList.get(0).getClass();
			if (cls.isAnnotationPresent(Table.class)) {
				Table table = cls.getAnnotation(Table.class);
				tableName = table.name();
			} else {
				tableName = cls.getSimpleName();
			}
			createTableIfNotExist(cls, tableName);
			try {
				mLock.lock();
				openDB();
				mDataBase.beginTransaction();
				for (Object entity : entityList) {
					cls = entity.getClass();
					if (cls.isAnnotationPresent(Table.class)) {
						Table table = cls.getAnnotation(Table.class);
						tableName = table.name();
					} else {
						tableName = cls.getSimpleName();
					}

					ContentValues cv = new ContentValues();
					if (flag) {
						sql = setContentValue(entity, cv, TYPE_INCREMENT,
								METHOD_INSERT);
					} else {
						sql = setContentValue(entity, cv, TYPE_NOT_INCREMENT,
								METHOD_INSERT);
					}
					if (mDebug) {
						Log.d(TAG, "[insertList]: " + "INSERT INTO "
								+ tableName + " " + sql);
					}
					row += mDataBase.insert(tableName, null, cv);

				}
				mDataBase.setTransactionSuccessful();
			} finally {
				mDataBase.endTransaction();
				closeDB(null);
				mLock.unlock();
			}

		}
		return row;
	}

	@Override
	public long insertOrUpdateById(Object entity) throws DBException {
		return insertOrUpdateById(entity, true);
	}

	@Override
	public long insertOrUpdateById(Object entity, boolean flag)
			throws DBException {
		if (entity == null) {
			throw new NullPointerException();
		}
		Class<?> cls = entity.getClass();
		String primaryKey = getPrimaryKey(cls);
		Object primaryKeyValue = getEntityFieldValue(entity, primaryKey);
		if (primaryKeyValue == null) {
			primaryKeyValue = 0;
		}
		String whereClause = primaryKey + " = ?";
		String[] whereArgs = new String[] { primaryKeyValue + "" };
		return insertOrUpdate(entity, flag, whereClause, whereArgs);
	}

	@Override
	public long insertOrUpdate(Object entity, String whereClause,
			String[] whereArgs) throws DBException {
		return insertOrUpdate(entity, true, whereClause, whereArgs);
	}

	@Override
	public long insertOrUpdate(Object entity, boolean flag, String whereClause,
			String[] whereArgs) throws DBException {
		String sql = "";
		String tableName = null;
		Cursor cursor = null;
		long row = 0L;

		if (entity == null) {
			throw new NullPointerException();
		}

		Class<?> cls = entity.getClass();
		if (cls.isAnnotationPresent(Table.class)) {
			Table table = cls.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = cls.getSimpleName();
		}
		createTableIfNotExist(cls, tableName);
		try {
			mLock.lock();
			openDB();
			if (mDebug) {
				if (whereClause == null && whereArgs == null) {
					Log.d(TAG, "[insertOrUpdate]: SELECT * FROM " + tableName);
				} else {
					Log.d(TAG, "[insertOrUpdate]: SELECT * FROM " + tableName
							+ " WHERE " + getLogSql(whereClause, whereArgs));
				}

			}
			cursor = mDataBase.query(tableName, null, whereClause, whereArgs,
					null, null, null);

			if (cursor.getCount() > 0) {
				ContentValues cv = new ContentValues();
				sql = setContentValue(entity, cv, TYPE_INCREMENT, METHOD_UPDATE);
				if (mDebug) {
					if (whereClause == null && whereArgs == null) {
						Log.d(TAG, "[insertOrUpdate]: UPDATE " + tableName
								+ " SET " + sql);
					} else {
						Log.d(TAG,
								"[insertOrUpdate]: UPDATE " + tableName
										+ " SET " + sql + " WHERE "
										+ getLogSql(whereClause, whereArgs));
					}

				}
				row = mDataBase.update(tableName, cv, whereClause, whereArgs);
				return row;
			} else {
				ContentValues cv = new ContentValues();
				if (flag) {
					sql = setContentValue(entity, cv, TYPE_INCREMENT,
							METHOD_INSERT);
				} else {
					sql = setContentValue(entity, cv, TYPE_NOT_INCREMENT,
							METHOD_INSERT);
				}
				if (mDebug) {
					Log.d(TAG, "[insertOrUpdate]: " + "INSERT INTO "
							+ tableName + " " + sql);
				}

				row = mDataBase.insert(tableName, null, cv);
				return row;
			}
		} finally {
			closeDB(cursor);
			mLock.unlock();
		}
	}

	@Override
	public long insertOrUpdateListById(List<?> entityList) throws DBException {
		return insertOrUpdateListById(entityList, true);
	}

	@Override
	public long insertOrUpdateListById(List<?> entityList, boolean flag)
			throws DBException {
		if (entityList == null) {
			throw new NullPointerException();
		}
		if (entityList.size() == 0) {
			return 0L;
		}
		Class<?> cls = entityList.get(0).getClass();
		String primaryKey = getPrimaryKey(cls);
		String whereClause = primaryKey + " = ?";
		List<String[]> whereArgs = new ArrayList<String[]>();
		for (int i = 0; i < entityList.size(); i++) {
			Object primaryKeyValue = getEntityFieldValue(entityList.get(i),
					primaryKey);
			if (primaryKeyValue == null) {
				primaryKeyValue = 0;
			}
			String[] whereArg = new String[] { primaryKeyValue + "" };
			whereArgs.add(whereArg);
		}
		return insertOrUpdateList(entityList, flag, whereClause, whereArgs);
	}

	@Override
	public long insertOrUpdateList(List<?> entityList, String whereClause,
			List<String[]> whereArgs) throws DBException {
		return insertOrUpdateList(entityList, true, whereClause, whereArgs);
	}

	@Override
	public long insertOrUpdateList(List<?> entityList, boolean flag,
			String whereClause, List<String[]> whereArgs) throws DBException {
		String sql = "";
		String tableName = null;
		long row = 0L;
		Cursor cursor = null;
		if (entityList == null || whereArgs == null) {
			throw new NullPointerException();
		}
		if (entityList.size() != whereArgs.size()) {
			throw new DBException(
					"Length of the size of the collection is inconsistent");
		}

		Class<?> cls = entityList.get(0).getClass();
		if (cls.isAnnotationPresent(Table.class)) {
			Table table = cls.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = cls.getSimpleName();
		}
		createTableIfNotExist(cls, tableName);
		try {
			mLock.lock();
			openDB();
			mDataBase.beginTransaction();
			for (int i = 0; i < entityList.size(); i++) {
				if (mDebug) {
					if (whereClause == null && whereArgs.get(i) == null) {
						Log.d(TAG, "[insertOrUpdate]: SELECT * FROM "
								+ tableName);
					} else {
						Log.d(TAG,
								"[insertOrUpdate]: SELECT * FROM "
										+ tableName
										+ " WHERE "
										+ getLogSql(whereClause,
												whereArgs.get(i)));
					}
				}
				cursor = mDataBase.query(tableName, null, whereClause,
						whereArgs.get(i), null, null, null);
				int count = cursor.getCount();
				if (cursor != null) {
					cursor.close();
					cursor = null;
				}
				if (count > 0) {
					ContentValues cv = new ContentValues();
					sql = setContentValue(entityList.get(i), cv,
							TYPE_INCREMENT, METHOD_UPDATE);
					if (mDebug) {
						if (whereClause == null && whereArgs.get(i) == null) {
							Log.d(TAG, "[insertOrUpdate]: UPDATE " + tableName);
						} else {
							Log.d(TAG,
									"[insertOrUpdate]: UPDATE "
											+ tableName
											+ " SET "
											+ sql
											+ " WHERE "
											+ getLogSql(whereClause,
													whereArgs.get(i)));
						}
					}
					row += mDataBase.update(tableName, cv, whereClause,
							whereArgs.get(i));
				} else {
					ContentValues cv = new ContentValues();
					if (flag) {
						sql = setContentValue(entityList.get(i), cv,
								TYPE_INCREMENT, METHOD_INSERT);
					} else {
						sql = setContentValue(entityList.get(i), cv,
								TYPE_NOT_INCREMENT, METHOD_INSERT);
					}
					if (mDebug) {
						Log.d(TAG, "[insertOrUpdate]: " + "INSERT INTO "
								+ tableName + " " + sql);
					}

					row += mDataBase.insert(tableName, null, cv);
				}
			}
			mDataBase.setTransactionSuccessful();
		} finally {
			mDataBase.endTransaction();
			closeDB(cursor);
			mLock.unlock();
		}

		return row;
	}

	@Override
	public void deleteById(Object entity) throws DBException {
		String tableName = null;
		String primaryKey = null;
		Object primaryKeyValue = null;

		if (entity == null) {
			throw new NullPointerException();
		}

		Class<?> cls = entity.getClass();
		if (cls.isAnnotationPresent(Table.class)) {
			Table table = cls.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = cls.getSimpleName();
		}

		if (!isTableExist(tableName)) {
			return;
		}
		primaryKey = getPrimaryKey(cls);
		primaryKeyValue = getEntityFieldValue(entity, primaryKey);
		if (primaryKeyValue == null) {
			primaryKeyValue = 0;
		}
		try {
			mLock.lock();
			openDB();
			String whereClause = primaryKey + " = ?";
			String[] whereArgs = new String[] { primaryKeyValue + "" };
			if (mDebug) {
				Log.d(TAG, "[delete]: DELETE * FROM " + tableName + " WHERE "
						+ getLogSql(whereClause, whereArgs));
			}
			mDataBase.delete(tableName, whereClause, whereArgs);
		} finally {
			closeDB(null);
			mLock.unlock();
		}

	}

	@Override
	public void deleteListById(List<?> entityList) throws DBException {
		String tableName = null;
		String primaryKey = null;
		Object primaryKeyValue = null;
		if (entityList == null || entityList.size() == 0) {
			return;
		}
		Object entity = entityList.get(0);
		if (entity == null) {
			throw new NullPointerException();
		}
		Class<?> cls = entity.getClass();
		if (cls.isAnnotationPresent(Table.class)) {
			Table table = cls.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = cls.getSimpleName();
		}

		if (!isTableExist(tableName)) {
			return;
		}
		primaryKey = getPrimaryKey(cls);

		try {
			mLock.lock();
			openDB();
			for (int i = 0; i < entityList.size(); i++) {
				primaryKeyValue = getEntityFieldValue(entityList.get(i),
						primaryKey);
				if (primaryKeyValue == null) {
					primaryKeyValue = 0;
				}

				String whereClause = primaryKey + " = ?";
				String[] whereArgs = new String[] { primaryKeyValue + "" };
				if (mDebug) {
					Log.d(TAG, "[delete]: DELETE * FROM " + tableName
							+ " WHERE " + getLogSql(whereClause, whereArgs));
				}
				mDataBase.delete(tableName, whereClause, whereArgs);

			}
		} finally {
			closeDB(null);
			mLock.unlock();
		}

	}

	@Override
	public void delete(Class<?> clsType, String whereClause, String[] whereArgs)
			throws DBException {
		String tableName = null;
		if (clsType == null) {
			throw new NullPointerException();
		}
		if (clsType.isAnnotationPresent(Table.class)) {
			Table table = clsType.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = clsType.getSimpleName();
		}

		if (!isTableExist(tableName)) {
			return;
		}
		try {
			mLock.lock();
			openDB();
			if (mDebug) {
				if (whereClause == null && whereArgs == null) {
					Log.d(TAG, "[delete]: DELETE * FROM " + tableName);
				} else {
					Log.d(TAG, "[delete]: DELETE * FROM " + tableName
							+ " WHERE " + getLogSql(whereClause, whereArgs));
				}
			}
			mDataBase.delete(tableName, whereClause, whereArgs);
		} finally {
			closeDB(null);
			mLock.unlock();
		}

	}

	@Override
	public void deleteAll(Class<?> clsType) throws DBException {
		String tableName = null;
		if (clsType == null) {
			throw new NullPointerException();
		}
		if (clsType.isAnnotationPresent(Table.class)) {
			Table table = clsType.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = clsType.getSimpleName();
		}

		if (!isTableExist(tableName)) {
			return;
		}

		try {
			mLock.lock();
			openDB();
			if (mDebug) {
				Log.d(TAG, "[delete]: DELETE * FROM " + tableName);
			}
			mDataBase.delete(tableName, null, null);
		} finally {
			closeDB(null);
			mLock.unlock();
		}

	}

	@Override
	public void deleteAllList(List<Class<?>> clsTypes) throws DBException {
		String tableName = null;
		if (clsTypes == null || clsTypes.size() == 0) {
			return;
		}
		try {
			mLock.lock();
			openDB();
			for (int i = 0; i < clsTypes.size(); i++) {
				Class<?> clsType = clsTypes.get(i);
				if (clsType == null) {
					throw new NullPointerException();
				}
				if (clsType.isAnnotationPresent(Table.class)) {
					Table table = clsType.getAnnotation(Table.class);
					tableName = table.name();
				} else {
					tableName = clsType.getSimpleName();
				}

				if (!isTableExist(tableName)) {
					return;
				}

				if (mDebug) {
					Log.d(TAG, "[delete]: DELETE * FROM " + tableName);
				}
				mDataBase.delete(tableName, null, null);

			}
		} finally {
			closeDB(null);
			mLock.unlock();
		}
	}

	@Override
	public long updateById(Object entity) throws DBException {
		return updateById(entity, "");
	}

	@Override
	public long updateById(Object entity, String... columns) throws DBException {
		return update(entity, null, null, columns);
	}

	@Override
	public long update(Object entity, String whereClause, String[] whereArgs)
			throws DBException {
		return update(entity, whereClause, whereArgs, "");
	}

	@Override
	public long update(Object entity, String whereClause, String[] whereArgs,
			String... columns) throws DBException {
		String tableName = null;
		String primaryKey = null;
		Object primaryKeyValue = null;
		String sql = "";
		long row = 0L;

		if (entity == null) {
			throw new NullPointerException();
		}

		Class<?> cls = entity.getClass();
		if (cls.isAnnotationPresent(Table.class)) {
			Table table = cls.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = cls.getSimpleName();
		}

		if (!isTableExist(tableName)) {
			return row;
		}
		primaryKey = getPrimaryKey(cls);
		primaryKeyValue = getEntityFieldValue(entity, primaryKey);
		if (primaryKeyValue == null) {
			primaryKeyValue = 0;
		}
		if (whereClause == null && whereArgs == null) {
			whereClause = primaryKey + " = ?";
			whereArgs = new String[] { primaryKeyValue + "" };
		}
		try {
			mLock.lock();
			openDB();
			ContentValues cv = new ContentValues();
			sql = setContentValue(entity, cv, TYPE_INCREMENT, METHOD_UPDATE,
					columns);
			if (mDebug) {
				if (whereClause == null && whereArgs == null) {
					Log.d(TAG, "[update]: update " + tableName);
				} else {
					Log.d(TAG, "[update]: update " + tableName + " set " + sql
							+ " where " + getLogSql(whereClause, whereArgs));
				}
			}
			row = mDataBase.update(tableName, cv, whereClause, whereArgs);
			return row;
		} finally {
			closeDB(null);
			mLock.unlock();
		}

	}

	@Override
	public long updateListById(List<?> entityList) throws DBException {
		return updateListById(entityList, null);
	}

	@Override
	public long updateListById(List<?> entityList, List<String[]> columns)
			throws DBException {
		return updateList(entityList, null, null, columns);
	}

	@Override
	public long updateList(List<?> entityList, List<String> whereClauses,
			List<String[]> whereArgs) throws DBException {
		return updateList(entityList, whereClauses, whereArgs, null);
	}

	@Override
	public long updateList(List<?> entityList, List<String> whereClauses,
			List<String[]> whereArgs, List<String[]> columns)
			throws DBException {
		String tableName = null;
		String primaryKey = null;
		Object primaryKeyValue = null;
		String sql = "";
		long row = 0L;

		if (entityList == null || entityList.size() == 0) {
			return 0L;
		}
		if (!(entityList.size() == whereClauses.size()
				&& entityList.size() == whereArgs.size() && entityList.size() == columns
				.size())) {
			throw new DBException("Parameter length inconsistent");
		}
		Object entity = entityList.get(0);
		if (entity == null) {
			throw new NullPointerException();
		}
		Class<?> cls = entity.getClass();
		if (cls.isAnnotationPresent(Table.class)) {
			Table table = cls.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = cls.getSimpleName();
		}

		if (!isTableExist(tableName)) {
			return row;
		}
		primaryKey = getPrimaryKey(cls);
		try {
			mLock.lock();
			openDB();
			for (int i = 0; i < entityList.size(); i++) {
				String whereClause = whereClauses.get(i);
				String[] whereArg = whereArgs.get(i);
				if (whereClause == null && whereArg == null) {
					primaryKeyValue = getEntityFieldValue(entityList.get(i),
							primaryKey);
					if (primaryKeyValue == null) {
						primaryKeyValue = 0;
					}
					whereClause = primaryKey + " = ?";
					whereArg = new String[] { primaryKeyValue + "" };
				}

				ContentValues cv = new ContentValues();
				sql = setContentValue(entityList.get(i), cv, TYPE_INCREMENT,
						METHOD_UPDATE, columns.get(i));
				if (mDebug) {
					if (whereClause == null && whereArg == null) {
						Log.d(TAG, "[update]: update " + tableName);
					} else {
						Log.d(TAG,
								"[update]: update " + tableName + " set " + sql
										+ " where "
										+ getLogSql(whereClause, whereArg));
					}
				}
				row += mDataBase.update(tableName, cv, whereClause, whereArg);
			}
		} finally {
			closeDB(null);
			mLock.unlock();
		}

		return row;
	}

	@Override
	public <T> List<T> rawQuery(Class<T> clsType, String sql,
			String[] selectionArgs) throws DBException {
		String tableName = null;
		Cursor cursor = null;
		if (clsType == null) {
			throw new NullPointerException();
		}
		if (clsType.isAnnotationPresent(Table.class)) {
			Table table = clsType.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = clsType.getSimpleName();
		}

		if (!isTableExist(tableName)) {
			return null;
		}
		if (mDebug) {
			if (sql == null && selectionArgs == null) {
				Log.d(TAG, "[rawQuery]:");
			} else {
				Log.d(TAG, "[rawQuery]:" + getLogSql(sql, selectionArgs));
			}
		}

		try {
			mLock.lock();
			openDB();
			cursor = mDataBase.rawQuery(sql, selectionArgs);
			List<T> list = new ArrayList<T>();
			list = getListFromCursor(clsType, cursor);
			return list;
		} finally {
			closeDB(cursor);
			mLock.unlock();
		}

	}

	@Override
	public <T> T queryOneById(Class<T> clsType, int id) throws DBException {
		String tableName = null;
		String primaryKey = null;
		if (clsType == null) {
			throw new NullPointerException();
		}
		if (clsType.isAnnotationPresent(Table.class)) {
			Table table = clsType.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = clsType.getSimpleName();
		}
		if (!isTableExist(tableName)) {
			return null;
		}
		primaryKey = getPrimaryKey(clsType);
		String selection = primaryKey + " = ?";
		String[] selectionArgs = new String[] { Integer.toString(id) };
		return queryOne(clsType, selection, selectionArgs);

	}

	@Override
	public <T> T queryOne(Class<T> clsType, String selection,
			String[] selectionArgs) throws DBException {
		String tableName = null;
		Cursor cursor = null;
		if (clsType == null) {
			throw new NullPointerException();
		}
		if (clsType.isAnnotationPresent(Table.class)) {
			Table table = clsType.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = clsType.getSimpleName();
		}

		if (!isTableExist(tableName)) {
			return null;
		}
		try {
			mLock.lock();
			openDB();
			if (mDebug) {
				if (selection == null && selectionArgs == null) {
					Log.d(TAG, "[queryOne]:" + "SELECT * FROM " + tableName);
				} else {
					Log.d(TAG, "[queryOne]:" + "SELECT * FROM " + tableName
							+ " WHERE " + getLogSql(selection, selectionArgs));
				}
			}
			cursor = mDataBase.query(tableName, null, selection, selectionArgs,
					null, null, null, "1");
			List<T> list = new ArrayList<T>();
			list = getListFromCursor(clsType, cursor);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
			return null;
		} finally {
			closeDB(cursor);
			mLock.unlock();
		}

	}

	@Override
	public <T> List<T> queryList(Class<T> clsType) throws DBException {
		return queryList(clsType, null, null, null, null, null, null, null);
	}

	@Override
	public <T> List<T> queryList(Class<T> clsType, String selection,
			String[] selectionArgs) throws DBException {
		return queryList(clsType, null, selection, selectionArgs, null, null,
				null, null);
	}

	@Override
	public <T> List<T> queryList(Class<T> clsType, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) throws DBException {
		String tableName = null;
		Cursor cursor = null;
		if (clsType == null) {
			throw new NullPointerException();
		}
		if (clsType.isAnnotationPresent(Table.class)) {
			Table table = clsType.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = clsType.getSimpleName();
		}
		if (!isTableExist(tableName)) {
			return null;
		}
		try {
			mLock.lock();
			openDB();
			if (mDebug) {
				if (selection == null && selectionArgs == null) {
					Log.d(TAG, "[queryList]:" + "SELECT "
							+ getLogColumn(columns) + " FROM " + tableName);
				} else {
					Log.d(TAG, "[queryList]:" + "SELECT "
							+ getLogColumn(columns) + " FROM " + tableName
							+ " WHERE " + getLogSql(selection, selectionArgs));
				}
			}
			cursor = mDataBase.query(tableName, columns, selection,
					selectionArgs, groupBy, having, orderBy, limit);
			List<T> list = new ArrayList<T>();
			list = getListFromCursor(clsType, cursor);
			return list;
		} finally {
			closeDB(cursor);
			mLock.unlock();
		}

	}

	@Override
	public int queryCount(Class<?> clsType) throws DBException {
		String tableName = null;
		if (clsType == null) {
			throw new NullPointerException();
		}
		if (clsType.isAnnotationPresent(Table.class)) {
			Table table = clsType.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = clsType.getSimpleName();
		}

		if (!isTableExist(tableName)) {
			return 0;
		}

		String sql = "SELECT * FROM " + tableName;
		return queryCount(sql, null);

	}

	@Override
	public int queryCount(String sql, String[] selectionArgs)
			throws DBException {
		Cursor cursor = null;
		try {
			mLock.lock();
			openDB();
			if (mDebug) {
				if (sql == null && selectionArgs == null) {
					Log.d(TAG, "[queryCount]:");
				} else {
					Log.d(TAG, "[queryCount]:" + getLogSql(sql, selectionArgs));
				}

			}
			cursor = mDataBase.rawQuery(sql, selectionArgs);
			int count = cursor.getCount();
			return count;
		} finally {
			closeDB(cursor);
			mLock.unlock();
		}
	}

	@Override
	public void execSql(String sql, Object[] selectionArgs) throws DBException {
		try {
			mLock.lock();
			openDB();
			if (mDebug) {
				if (sql == null && selectionArgs == null) {
					Log.d(TAG, "[execSql]:");
				} else {
					Log.d(TAG, "[execSql]:" + getLogSql(sql, selectionArgs));
				}
			}
			if (selectionArgs == null) {
				mDataBase.execSQL(sql);
			} else {
				mDataBase.execSQL(sql, selectionArgs);
			}
		} finally {
			closeDB(null);
			mLock.unlock();
		}
	}

	@Override
	public boolean isExist(Class<?> clsType) throws DBException {
		String tableName = null;
		if (clsType == null) {
			throw new NullPointerException();
		}
		if (clsType.isAnnotationPresent(Table.class)) {
			Table table = clsType.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = clsType.getSimpleName();
		}

		if (!isTableExist(tableName)) {
			return false;
		}
		String sql = "SELECT * FROM " + tableName;
		return isExist(sql, null);

	}

	@Override
	public boolean isExist(String sql, String[] selectionArgs)
			throws DBException {
		Cursor cursor = null;
		try {
			mLock.lock();
			openDB();
			if (mDebug) {
				if (sql == null && selectionArgs == null) {
					Log.d(TAG, "[isExist]:");
				} else {
					Log.d(TAG, "[isExist]:" + getLogSql(sql, selectionArgs));
				}
			}
			cursor = mDataBase.rawQuery(sql, selectionArgs);
			if (cursor.getCount() > 0) {
				return true;
			}
		} finally {
			closeDB(cursor);
			mLock.unlock();
		}
		return false;
	}

	@Override
	public boolean isTableExist(String tableName) throws DBException {
		if (mTableNames.contains(tableName)) {
			return true;
		}
		String sql = "SELECT count(*) as count FROM sqlite_master WHERE type='table' AND name='"
				+ tableName + "'";
		if (mDebug) {
			Log.d(TAG, "[isTableExist]: " + sql);
		}
		mLock.lock();
		openDB();
		Cursor cursor = mDataBase.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			int count = cursor.getInt(cursor.getColumnIndex("count"));
			if (count > 0) {
				mTableNames.add(tableName);
				closeDB(cursor);
				mLock.unlock();
				return true;
			}
		}
		closeDB(cursor);
		mLock.unlock();
		return false;
	}

	/**
	 * 如果表明不存在时创建表
	 * 
	 * @param cls
	 *            映射类类型
	 * @param tableName
	 *            表名
	 * @throws DBException
	 */
	public <T> void createTableIfNotExist(Class<T> cls, String tableName)
			throws DBException {
		if (isTableExist(tableName)) {
			return;
		}
		createTable(cls);
	}

	@Override
	public <T> void createTable(Class<T> cls) throws DBException {
		String tableName = null;
		int primaryKeyCount = 0;
		if (cls == null) {
			throw new NullPointerException();
		}

		if (cls.isAnnotationPresent(Table.class)) {
			Table table = cls.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = cls.getSimpleName();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ").append(tableName).append(" (");

		List<Field> allFields = getFields(cls.getDeclaredFields(), cls
				.getSuperclass().getDeclaredFields());

		for (Field field : allFields) {
			if (field.isAnnotationPresent(Ignore.class)) {
				continue;
			}
			String columnName = "";
			String columnType = "";
			int columnLength = 0;

			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);

				if ("".equals(column.name())) {
					columnName = field.getName();
				} else {
					columnName = column.name();
				}

				if ("".equals(column.type())) {
					columnType = getColumnType(field.getType());
				} else {
					columnType = column.type();
				}

				columnLength = column.length();

			} else {
				columnLength = 0;
				columnName = field.getName();
				columnType = getColumnType(field.getType());
			}

			sb.append(columnName + " " + columnType);

			if (columnLength != 0) {
				sb.append("(" + columnLength + ")");
			}

			if (field.isAnnotationPresent(Id.class)) {
				primaryKeyCount++;
				if (((field.getType() == Integer.TYPE)
						|| (field.getType() == Integer.class) || (field
							.getType() == int.class))) {
					sb.append(" primary key autoincrement");
				} else {
					sb.append(" primary key");
				}
			}
			sb.append(", ");
		}
		if (primaryKeyCount > 1) {
			throw new DBException("Primary Key Not Unique");
		}
		if (primaryKeyCount == 0) {
			sb.append("_id" + " " + "INTEGER");
			sb.append(" primary key autoincrement");
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length() - 1);
		sb.append(")");

		String sql = sb.toString();

		Log.d(TAG, "[createTable]: " + sql);
		mLock.lock();
		openDB();
		mDataBase.execSQL(sql);
		mTableNames.add(tableName);
		closeDB(null);
		mLock.unlock();

	}

	@Override
	public <T> void createTables(Class<T>[] classes) throws DBException {
		for (Class<T> cls : classes) {
			createTable(cls);
		}
	}

	@Override
	public <T> void dropTable(Class<T> cls) throws DBException {
		String tableName = null;
		if (cls == null) {
			throw new NullPointerException();
		}
		if (cls.isAnnotationPresent(Table.class)) {
			Table table = cls.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = cls.getSimpleName();
		}
		mLock.lock();
		openDB();
		String sql = "DROP TABLE IF EXISTS " + tableName;
		Log.d(TAG, "[dropTable]: " + sql);
		mDataBase.execSQL(sql);
		mTableNames.remove(tableName);
		closeDB(null);
		mLock.unlock();
	}

	@Override
	public <T> void dropTables(Class<T>[] classes) throws DBException {
		for (Class<T> cls : classes) {
			dropTable(cls);
		}
	}

	/**
	 * 通过属性的类创建类型
	 * 
	 * @param type
	 * @return 属性类型
	 */
	private <T> String getColumnType(Class<T> type) {
		if (String.class == type) {
			return "TEXT";
		}
		if ((Integer.TYPE == type) || (Integer.class == type)) {
			return "INTEGER";
		}
		if (int.class == type) {
			return "INTEGER";
		}
		if ((Long.TYPE == type) || (Long.class == type)) {
			return "BIGINT";
		}
		if ((Float.TYPE == type) || (Float.class == type)) {
			return "FLOAT";
		}
		if ((Short.TYPE == type) || (Short.class == type)) {
			return "INT";
		}
		if ((Double.TYPE == type) || (Double.class == type)) {
			return "DOUBLE";
		}
		if (Blob.class == type) {
			return "BLOB";
		}
		return "TEXT";
	}

	/**
	 * 获取所有非Ignore注解的属性(合并属性数组去重,去掉Ignore属性,并将ID放在首字段)
	 * 
	 * @param childFields
	 *            子类的属性
	 * @param parentFields
	 *            父类的属性
	 * @return 添加Column注解的属性集合
	 */
	private List<Field> getFields(Field[] childFields, Field[] parentFields) {
		Map<String, Field> map = new LinkedHashMap<String, Field>();

		for (Field field : childFields) {
			if (field.isAnnotationPresent(Ignore.class)) {
				continue;
			}
			String columnName = "";

			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);

				if ("".equals(column.name())) {
					columnName = field.getName();
				} else {
					columnName = column.name();
				}

			} else {
				columnName = field.getName();
			}
			map.put(columnName, field);
		}

		for (Field field : parentFields) {
			if (field.isAnnotationPresent(Ignore.class)) {
				continue;
			}
			String columnName = "";

			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);

				if ("".equals(column.name())) {
					columnName = field.getName();
				} else {
					columnName = column.name();
				}

			} else {
				columnName = field.getName();
			}
			map.put(columnName, field);
		}
		List<Field> list = new ArrayList<Field>();

		for (String key : map.keySet()) {
			Field field = map.get(key);
			if (field.isAnnotationPresent(Id.class)) {
				list.add(0, field);
			} else {
				list.add(field);
			}
		}
		return list;
	}

	/**
	 * 通过映射类获取数据存入ContentValues
	 * 
	 * @param entity
	 *            映射类类型
	 * @param cv
	 *            ContentValues对象
	 * @param type
	 *            是否主键自增
	 * @param method
	 *            插入方法还是更新方法
	 * @param columns
	 *            需要修改的字段名称集合
	 * @return sql语句
	 */
	protected String setContentValue(Object entity, ContentValues cv, int type,
			int method, String... columns) {
		Class<?> cls = entity.getClass();
		StringBuffer strField = new StringBuffer("(");
		StringBuffer strValue = new StringBuffer(" values(");
		StringBuffer strUpdate = new StringBuffer(" ");
		List<Field> allFields = getFields(cls.getDeclaredFields(), cls
				.getSuperclass().getDeclaredFields());
		for (Field field : allFields) {
			if (field.isAnnotationPresent(Ignore.class)) {
				continue;
			}
			String columnName = "";
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				if ("".equals(column.name())) {
					columnName = field.getName();
				} else {
					columnName = column.name();
				}
			} else {
				columnName = field.getName();
			}
			field.setAccessible(true);
			Object fieldValue = null;
			try {
				fieldValue = field.get(entity);
			} catch (Exception e) {

			}
			if (fieldValue == null) {
				fieldValue = "";
			}
			if ((type == TYPE_INCREMENT)
					&& (field.isAnnotationPresent(Id.class))) {
				continue;
			}
			if (Date.class == field.getType()) {
				cv.put(columnName, ((Date) fieldValue).getTime());
				continue;
			}
			String value = String.valueOf(fieldValue);
			cv.put(columnName, value);
			if (method == METHOD_INSERT) {
				strField.append(columnName).append(",");
				strValue.append("'").append(value).append("',");
			} else {
				if (columns != null && columns.length > 0) {
					boolean isExist = false;
					if ((columns.length == 1 && "".equals(columns[0]))) {
						isExist = true;
					} else {
						for (int i = 0; i < columns.length; i++) {
							String c = columns[i];
							if (columnName.equals(c)) {
								isExist = true;
								break;
							}
						}
					}

					if (isExist) {
						strUpdate.append(columnName).append("=").append("'")
								.append(value).append("',");
					} else {
						cv.remove(columnName);
					}
				} else {
					strUpdate.append(columnName).append("=").append("'")
							.append(value).append("',");
				}

			}
		}
		if (method == METHOD_INSERT) {
			strField.deleteCharAt(strField.length() - 1).append(")");
			strValue.deleteCharAt(strValue.length() - 1).append(")");
			return strField.toString() + strValue.toString();
		} else {
			return strUpdate.deleteCharAt(strUpdate.length() - 1).append(" ")
					.toString();
		}
	}

	/**
	 * 通过Cursor获取映射类对象集合
	 * 
	 * @param clsType
	 *            映射类
	 * @param cursor
	 *            Cursor对象
	 * @return 映射类对象集合
	 */
	protected <T> List<T> getListFromCursor(Class<T> clsType, Cursor cursor) {
		List<T> list = new ArrayList<T>();
		List<Field> allFields = getFields(clsType.getDeclaredFields(), clsType
				.getSuperclass().getDeclaredFields());
		while (cursor.moveToNext()) {
			try {
				T entity = clsType.newInstance();
				for (Field field : allFields) {
					if (field.isAnnotationPresent(Ignore.class)) {
						continue;
					}
					String columnName = "";

					if (field.isAnnotationPresent(Column.class)) {
						Column column = field.getAnnotation(Column.class);
						if ("".equals(column.name())) {
							columnName = field.getName();
						} else {
							columnName = column.name();
						}
					} else {
						columnName = field.getName();
					}

					field.setAccessible(true);
					Class<?> fieldType = field.getType();

					int c = cursor.getColumnIndex(columnName);
					if (c < 0) {
						continue;
					} else if ((Integer.TYPE == fieldType)
							|| (Integer.class == fieldType)) {
						field.set(entity, cursor.getInt(c));
					} else if (String.class == fieldType) {
						field.set(entity, cursor.getString(c));
					} else if ((Long.TYPE == fieldType)
							|| (Long.class == fieldType)) {
						field.set(entity, Long.valueOf(cursor.getLong(c)));
					} else if ((Float.TYPE == fieldType)
							|| (Float.class == fieldType)) {
						field.set(entity, Float.valueOf(cursor.getFloat(c)));
					} else if ((Short.TYPE == fieldType)
							|| (Short.class == fieldType)) {
						field.set(entity, Short.valueOf(cursor.getShort(c)));
					} else if ((Double.TYPE == fieldType)
							|| (Double.class == fieldType)) {
						field.set(entity, Double.valueOf(cursor.getDouble(c)));
					} else if (Date.class == fieldType) {
						Date date = new Date(cursor.getLong(c));
						field.set(entity, date);
					} else if (Blob.class == fieldType) {
						field.set(entity, cursor.getBlob(c));
					} else if (Character.TYPE == fieldType) {
						String fieldValue = cursor.getString(c);
						if ((fieldValue != null) && (fieldValue.length() > 0)) {
							field.set(entity,
									Character.valueOf(fieldValue.charAt(0)));
						}
					}
				}

				list.add((T) entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 通过映射类获取主键名称
	 * 
	 * @param cls
	 *            映射类
	 * @return 主键名称
	 */
	private String getPrimaryKey(Class<?> cls) {
		String primaryKey = "_id";
		if (cls == null) {
			return primaryKey;
		}

		List<Field> allFields = getFields(cls.getDeclaredFields(), cls
				.getSuperclass().getDeclaredFields());

		for (Field field : allFields) {
			if (field.isAnnotationPresent(Id.class)) {
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					if ("".equals(column.name())) {
						primaryKey = field.getName();
					} else {
						primaryKey = column.name();
					}
				} else {
					primaryKey = field.getName();
				}
			}
		}
		return primaryKey;
	}

	/**
	 * 通过字段名称获取映射类中属性的值
	 * 
	 * @param entity
	 *            映射类对象
	 * @param name
	 *            字段名称
	 * @return 属性值
	 */
	private Object getEntityFieldValue(Object entity, String name) {
		if (entity == null || name == null) {
			return null;
		}
		Class<?> cls = entity.getClass();

		List<Field> allFields = getFields(cls.getDeclaredFields(), cls
				.getSuperclass().getDeclaredFields());
		for (Field field : allFields) {
			String columnName = "";
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				if ("".equals(column.name())) {
					columnName = field.getName();
				} else {
					columnName = column.name();
				}
			} else {
				columnName = field.getName();
			}
			if (columnName.equals(name)) {
				field.setAccessible(true);
				Object fieldValue = null;
				try {
					fieldValue = field.get(entity);
				} catch (Exception e) {

				}
				return fieldValue;
			}
		}

		return null;
	}

	/**
	 * 将columns字段数据拼接成SQL
	 * 
	 * @param columns
	 *            字段集合
	 * @return SQL
	 */
	private String getLogColumn(String... columns) {
		if (columns != null) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < columns.length; i++) {
				buffer.append(",").append(columns[i]);
			}
			if (buffer.length() > 0) {
				buffer.deleteCharAt(0);
			}
			return buffer.toString();
		}
		return "*";
	}

	/**
	 * 将sql里的?替换成对应的字段
	 * 
	 * @param sql
	 *            带?的sql语句
	 * @param args
	 *            绑定的字段
	 * @return
	 */
	private String getLogSql(String sql, Object[] args) {
		if (sql == null) {
			return null;
		}
		if (args == null || args.length == 0) {
			return sql;
		}
		for (int i = 0; i < args.length; i++) {
			sql = sql.replaceFirst("\\?", "'" + String.valueOf(args[i]) + "'");
		}
		return sql;
	}

	/**
	 * 锁数据库
	 */
	private void lock() {
		mLock.lock();
	}

	/**
	 * 解锁数据库
	 */
	private void unLock() {
		mLock.unlock();
	}

	/**
	 * 打开数据库
	 */
	private void openDB() {
		if (mDataBase == null || !mDataBase.isOpen()) {
			mDataBase = openOrCreateDatabase();
		}
	}

	/**
	 * 关闭数据库
	 * 
	 * @param cursor
	 *            Cursor对象
	 */
	private void closeDB(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}

		if (mDataBase != null) {
			mDataBase.close();
			mDataBase = null;
		}
	}

	@Override
	public <T> void AddColumn(Class<T> cls, String columnName)
			throws DBException {
		String tableName = null;
		if (cls == null) {
			throw new NullPointerException();
		}
		if (cls.isAnnotationPresent(Table.class)) {
			Table table = cls.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = cls.getSimpleName();
		}
		if (isTableExist(tableName)) {//&&!isExistColumn(cls, columnName)
			mLock.lock();
			openDB();
			String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName;
			Log.d(TAG, "[ALTER TABLE]: " + sql);
			mDataBase.execSQL(sql);
			closeDB(null);
			mLock.unlock();
		}else{
			createTable(cls);
		}
	}
	@Override
	public <T> boolean isExistColumn(Class<T> cls, String columnName) {
		String tableName = null;
		if (cls == null) {
			throw new NullPointerException();
		}
		if (cls.isAnnotationPresent(Table.class)) {
			Table table = cls.getAnnotation(Table.class);
			tableName = table.name();
		} else {
			tableName = cls.getSimpleName();
		}
		try {
			if (isTableExist(tableName)) {
				StringBuilder builder = new StringBuilder();
				builder.append("name = '").append(tableName)
						.append("' AND sql LIKE '%").append(columnName)
						.append("%'");
				mLock.lock();
				openDB();
				Cursor cursor = null;
				if (mDebug) {
					Log.d(TAG, "[isExistColumn]: "
							+ "SELECT * FROM sqlite_master  where name = '"
							+ tableName + "' and sql like '%" + columnName
							+ "%' ");
				}
				cursor = mDataBase.query("sqlite_master", null,
						builder.toString(), null, null, null, null);
				boolean isExist = cursor.getCount() > 0;
				closeDB(cursor);
				mLock.unlock();
				return isExist;
			}
		} catch (DBException e) {
			e.printStackTrace();
		}
		return false;
	}

}
