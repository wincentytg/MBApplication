package com.ytg.p_db;
/**
 *
 * @author 于堂刚
 */
public class DBException extends BaseDBException {
	private static final long serialVersionUID = 1L;

	public DBException() {
	}

	public DBException(String detailMessage) {
		super(detailMessage);
	}

	public DBException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DBException(Throwable throwable) {
		super(throwable);
	}
}
