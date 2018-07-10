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
 */
package com.ytg.jzy.p_common.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.ytg.jzy.p_common.YTGApplicationContext;
import com.ytg.jzy.p_common.tools.BackwardSupportUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;


/**
 * 文件操作工具类
 * Created by Jorstin on 2015/3/17.
 */
public class FileAccessor {


    public static final String TAG = FileAccessor.class.getName();
    public static String EXTERNAL_STOREPATH = getExternalStorePath();
    public static final String APPS_ROOT_DIR = EXTERNAL_STOREPATH + File.separator + "zhongche";

    /**
     * 初始化应用文件夹目录
     */
    public static void initFileAccess() {
        File rootDir = new File(APPS_ROOT_DIR);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }
    }
    /**
     * 获取存储路径
     *
     * @param i
     * @return
     */
    public static String getfile_path(int i) {
        String path = APPS_ROOT_DIR+ File.separator;
        String file[] = new String[] { path + "img", path + "file",
                path + "head", path + "splash", path + "log" };
        File picFile = new File(file[i]);
        if (!picFile.exists()) {
            picFile.mkdirs();
        }
        return file[i] + File.separator;
    }

    public static String readContentByFile(String path) {
        BufferedReader reader = null;
        String line = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine()) != null) {
                    sb.append(line.trim());
                }
                return sb.toString().trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * 获取文件名
     *
     * @param pathName
     * @return
     */
    public static String getFileName(String pathName) {

        int start = pathName.lastIndexOf("/");
        if (start != -1) {
            return pathName.substring(start + 1, pathName.length());
        }
        return pathName;

    }

    /**
     * 外置存储卡的路径
     *
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 是否有外存卡
     *
     * @return
     */
    public static boolean isExistExternalStore() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * /data/data/com.YTG.bluetooth/files
     * @return
     */
    public static String getAppContextPath() {
        return YTGApplicationContext.getContext().getFilesDir().getAbsolutePath();
    }

    /**
     * @param filePaths
     */
    public static void delFiles(List<String> filePaths) {
        if (filePaths == null || filePaths.size() == 0) {
            return;
        }
        for (String url : filePaths) {
            if (!TextUtils.isEmpty(url)) {
                LogUtil.d(TAG, "to be deleted file local is " + url);
                delFile(url);
            }
        }
    }

    public static void  delelteAPK(){
        File file=new File(FileAccessor.APPS_ROOT_DIR);
        File[] files= file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".apk")){
                    return true;
                }else{
                    return false;
                }
            }
        });
        if(files!=null&&files.length>0){
            for(File f:files){
                delFile(f.getPath());
            }
        }
    }


    public static boolean delFile(String filePath) {
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return false;
        }
        return file.delete();
    }

    /**
     * @param fileName
     * @return
     */
    public static String getSecondLevelDirectory(String fileName) {
        if (TextUtils.isEmpty(fileName) || fileName.length() < 4) {
            return null;
        }

        String sub1 = fileName.substring(0, 2);
        String sub2 = fileName.substring(2, 4);
        return sub1 + File.separator + sub2;
    }

    /**
     * @param root
     * @param srcName
     * @param destName
     */
    public static void renameTo(String root, String srcName, String destName) {
        if (TextUtils.isEmpty(root) || TextUtils.isEmpty(srcName) || TextUtils.isEmpty(destName)) {
            return;
        }

        File srcFile = new File(root + srcName);
        File newPath = new File(root + destName);

        if (srcFile.exists()) {
            srcFile.renameTo(newPath);
        }
    }

    public static File getTackPicFilePath() {
//        File localFile = new File(getExternalStorePath()+ "/YTG/.tempchat" , "temp.jpg");
//        if ((!localFile.getParentFile().exists())
//                && (!localFile.getParentFile().mkdirs())) {
//            LogUtil.e("hhe", "SD卡不存在");
//            localFile = null;
//        }
//        return localFile;
        return getTackPicFilePath("temp");
    }

    public static File getTackPicFilePath(String picName) {
        if (TextUtils.isEmpty(picName)) {
            return getTackPicFilePath();
        }
        File localFile = new File(getExternalStorePath() + "/YTG/.tempchat", picName + ".jpg");
        if ((!localFile.getParentFile().exists())
                && (!localFile.getParentFile().mkdirs())) {
            LogUtil.e("hhe", "SD卡不存在");
            localFile = null;
        }
        return localFile;
    }

    public static String getExternalStorageState() {

        return Environment.getExternalStorageState();
    }

    /**
     * 文件名称
     *
     * @param b
     * @return
     */
    public static String getFileNameMD5(byte[] b) {
        char[] src = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(b);
            byte[] digestByte = digest.digest();
            int length = digestByte.length;
            char[] result = new char[length * 2];
            int index = 0;
            for (int i = 0; i < digestByte.length; i++) {
                byte d = digestByte[i];
                result[index] = src[(0xF & d >>> 4)];
                index += 1;
                result[index] = src[d & 0xF];
                index += 1;
            }
            return new String(result);
        } catch (Exception e) {
        }
        return null;
    }

//    递归进行清楚文件
    public static void deleteFileLog() {
        deleteAllFilesOfDir(new File(FileAccessor.APPS_ROOT_DIR + "/log"));
    }

    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists()){
            return;
        }
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }
    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否成功
     */
    public static boolean deleteFile(String filePath) {
        if (TextUtil.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (file.exists())
            return file.delete();
        return true;
    }


    /**
     * 判断文件是否存在
     * @param fileUrl 文件路径
     * @return 文件是否存在
     */
    public static boolean exists(String fileUrl) {
        return !BackwardSupportUtil.isNullOrNil(fileUrl) && new File(fileUrl).exists();

    }
}
