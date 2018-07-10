package com.ytg.jzy.p_common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private static MessageDigest md = null;

	public static String md5(final String c) {
		if (md == null) {
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		if (md != null) {
			md.update(c.getBytes());
			return byte2hex(md.digest());
		}
		return "";
	}
	
	public static String byte2hex(byte b[]) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = (new StringBuffer(String.valueOf(hs))).toString();
		}

		return hs.toUpperCase();
	}
}
