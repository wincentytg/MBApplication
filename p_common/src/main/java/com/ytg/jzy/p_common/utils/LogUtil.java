/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package com.ytg.jzy.p_common.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.ytg.jzy.p_common.BuildConfig;
import com.ytg.jzy.p_common.YTGApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 日志打印工具（使用mars xlog日志打印框架）
 * @author YTG
 * @since 2017/8/8
 */
public class LogUtil {

    public static final String TAG ="YTG";
    static  boolean debug = BuildConfig.DEBUG;
    private static Map<String, String> info = new HashMap<String, String>();

    public static void setDebugMode(boolean debugMode) {
        debug = debugMode;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @return 进程名
     */
    public static String getProcessName() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + android.os.Process.myPid() + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            String[] split = processName.split(":");
            if(split.length == 2) {
                return "[" + split[1] +"]";
            }
            return "";
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return "";
    }


    /**
     * 使用默认的TAG 打印日志
     * @param msg 日志内容
     */
    public static void v(String msg) {
        v(TAG, msg);
    }

    /**
     * 根据提供的日志TAG进行日志格式化输出
     * @param tag 日志tag
     * @param format 格式化占位符
     */
    public static void v(String tag, final String format) {
        if(debug){
            Log.v(tag, format);
        }
    }

    /**
     * 使用默认的TAG 打印日志
     * @param msg 日志内容
     */
    public static void d(String msg) {
        d(TAG, msg);
    }


    /**
     * 根据提供的日志TAG进行日志格式化输出
     * @param tag 日志tag
     * @param format 格式化占位符
     */
    public static void d(String tag, final String format) {
        if(debug){
            Log.d(tag, format);
        }
    }

    /**
     * 使用默认的TAG 打印日志
     * @param msg 日志内容
     */
    public static void i(String msg) {
        i(TAG, msg);
    }


    /**
     * 根据提供的日志TAG进行日志格式化输出
     * @param tag 日志tag
     * @param format 格式化占位符
     */
    public static void i(String tag, final String format) {
        if(debug) {
            Log.i(tag, format);
        }
    }

    /**
     * 使用默认的TAG 打印日志
     * @param msg 日志内容
     */
    public static void w(String msg) {
        w(TAG, msg);
    }


    /**
     * 根据提供的日志TAG进行日志格式化输出
     * @param tag 日志tag
     * @param format 格式化占位符
     */
    public static void w(String tag, final String format) {
        if(debug) {
            Log.w(tag, format);
        }
    }

    /**
     * 使用默认的TAG 打印日志
     * @param msg 日志内容
     */
    public static void e(String msg) {
        e(TAG, msg);
    }

    /**
     * 根据提供的日志TAG进行日志格式化输出
     * @param tag 日志tag
     * @param format 格式化占位符
     */
    public static void e(String tag, final String format) {
        if(debug) {
            Log.e(tag, format);
        }
    }

    /**
     * 保存日志到本地目录下
     * @param str
     */
    public static void saveLogToLocalFile(String str){
        collectDeviceInfo(YTGApplicationContext.getContext());
        saveCrashInfo2File(str);
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    public static void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();// 获得包管理器
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();// 反射机制
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get("").toString());
                Log.d(TAG, field.getName() + ":" + field.get(""));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static String saveCrashInfo2File(String str) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");
        }
        sb.append(str);
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss");
        //保存文件
        String time = format.format(new Date());
        String fileName = "log-" + time + "-" + ".txt";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(FileAccessor.getfile_path(4));
                FileOutputStream fos = new FileOutputStream(dir +File.separator+ fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
                return fileName;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getLogUtilsTag(Class<? extends Object> clazz) {
        return LogUtil.TAG + "." + clazz.getSimpleName();
    }
}
