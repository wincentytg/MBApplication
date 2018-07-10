package com.ytg.p_db;

public class BaseDBException extends Exception {
	private static final long serialVersionUID = 1L;

	public BaseDBException() {
	}

	public BaseDBException(String detailMessage) {
		super(detailMessage);
	}

	public BaseDBException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public BaseDBException(Throwable throwable) {
		super(throwable);
	}

}
