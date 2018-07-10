package com.ytg.p_db;

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
