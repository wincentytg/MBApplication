package com.ytg.jzy.p_common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;

import com.ytg.jzy.p_common.tools.SharedPreferencesHelper;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author 需要在 主项目的application里调用
 */
public class YTGApplicationContext {

    public static final String TAG = "YTGApplicationContext";

    private static Context mContext;
    private static String mPackageName;
    private static Resources mResources;
    public static SharedPreferencesHelper sp;

    static {
        mPackageName = "com.ytg.jzy";
    }

    public static void setContext(Context context) {
        mContext = context;
        if (mContext == null) {
            return;
        }
        mPackageName = context.getPackageName();
        sp = SharedPreferencesHelper.getInstance(mContext);
    }


    /**
     * 程序上下文对象
     *
     * @return 上下文
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 应用程序包名
     *
     * @return 应用程序包名
     */
    public static String getPackageName() {
        return mPackageName;
    }

    public static Resources getResources() {
        return mResources;
    }

    public static void setResource(Resources resource) {
        mResources = resource;
    }

    /**
     * 获取应用程序版本名称
     *
     * @return
     */
    public static String getVersion() {
        String version = "0.0.0";
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 获取应用版本号
     *
     * @return 版本号
     */
    public static int getVersionCode() {
        int code = 1;
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return code;
    }

    /**
     * 获取应用的SHA1
     *
     * @return
     */
    public static String getSHA1() {
        String apkPath = getContext().getPackageCodePath();
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = new byte[1024];
            int byteCount;
            FileInputStream fis = new FileInputStream(new File(apkPath));
            while ((byteCount = fis.read(bytes)) > 0) {
                msgDigest.update(bytes, 0, byteCount);
            }
            fis.close();
            BigInteger bi = new BigInteger(1, msgDigest.digest());
            String sha = bi.toString(16);
            return sha;
            // 这里添加从服务器中获取哈希值然后进行对比校验
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void sendBroadcast(String action) {
        Intent i = new Intent(action);
        getContext().sendBroadcast(i);
    }

    public static void sendBroadcast(Intent i) {
        getContext().sendBroadcast(i);
    }

    public static void sendLocalBroadcast(String action) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        manager.sendBroadcast(new Intent(action));
    }

    public static void sendLocalBroadcast(Intent i) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        manager.sendBroadcast(i);
    }


}
