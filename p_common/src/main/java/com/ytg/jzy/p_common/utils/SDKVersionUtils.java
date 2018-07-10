package com.ytg.jzy.p_common.utils;

import android.os.Build;

/**

 */
public class SDKVersionUtils {

    public static boolean isSmallerVersion(int version) {
        return (Build.VERSION.SDK_INT < version);
    }

    public static boolean isGreatThanOrEqualTo(int version) {
        return (Build.VERSION.SDK_INT >= version);
    }

    public static boolean isSmallerorEqual(int version) {
        return (Build.VERSION.SDK_INT <= version);
    }
}
