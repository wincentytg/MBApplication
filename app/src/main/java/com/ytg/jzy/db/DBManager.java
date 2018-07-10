package com.ytg.jzy.db;


import android.content.Context;

import com.ytg.jzy.BaseApplication;
import com.ytg.jzy.p_common.db.DBBack;
import com.ytg.jzy.p_common.db.DBOptions;
import com.ytg.jzy.p_common.db.IDBStrategy;

/**
 * 数据库的操作策略类
 */
public class DBManager implements IDBStrategy {
    private static volatile DBManager mManager;
    private BaseApplication mApplication;
    private com.ytg.p_db.DBManager mDbManager;

    private DBManager() {
        mApplication = BaseApplication.getInstance();
        mDbManager = mApplication.mDbManager;
    }

    public synchronized static DBManager getInstance() {
        if (mManager == null) {
            mManager = new DBManager();
        }
        return mManager;
    }

    @Override
    public void handleData(Context context, DBOptions opt, DBBack back) {
        if (opt instanceof Options) {
            if (((Options) opt).methodName==Options.Method.insertData) {
                insertData(back);
            }
        }
    }

    private void insertData(DBBack back) {
        back.onSuccess(null);
    }
//	/**
//	 * 以TICKET_NUMBER为单位存储故障单数据
//	 *
//	 * @param info
//	 * @param TICKET_NUMBER
//	 */
//	public void insert_X_FT_TICKETS_TBL_ITEM(X_FT_TICKETS_TBL_ITEM info,
//			String TICKET_NUMBER) {
//		if (null != info) {
//			info.setMroCode(sp.getString("mroCode"));
//			try {
//				mDbManager.insertOrUpdate(Utils.ObjToAesData(info), " TICKET_NUMBER = ? ",
//						new String[] { AES.encode(TICKET_NUMBER)});
//			} catch (DBException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//
}
