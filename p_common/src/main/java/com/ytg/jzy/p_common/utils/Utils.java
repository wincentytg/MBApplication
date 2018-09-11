package com.ytg.jzy.p_common.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.ytg.jzy.p_common.YTGApplicationContext;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {
	/**获取百分数格式数据
	 * @param data
	 * @return
	 */
	public static String getPercentData(String data){
		if (Utils.isEmpty(data)) {
			return "NUN";
		}
		Double value = Double.parseDouble(data);
		DecimalFormat format= new DecimalFormat("##.##%");
		return format.format(value);
	}
	public static String getNowtime2() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return df.format(new Date(System.currentTimeMillis()));//
	}
	/**
	 * 获取存储路径
	 * 
	 * @param i
	 * @return
	 */
	public static String getfile_path(int i) {
		String path = Environment.getExternalStorageDirectory().toString()
				+ File.separator + "common" + File.separator;
		String file[] = new String[] { path + "img", path + "file",
				path + "head", path + "splash" };
		File picFile = new File(file[i]);
		if (!picFile.exists()) {
			picFile.mkdirs();
		}
		return file[i] + File.separator;
	}

	public String getIMEI(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		return manager.getDeviceId();
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static boolean isDolwanServiceWorked() {
		ActivityManager myManager = (ActivityManager) YTGApplicationContext
				.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(50);
		String packagename =  YTGApplicationContext
				.getContext().getPackageName();
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals(packagename+".service.UpdateService")) {
				return true;
			}
		}
		return false;
	}

	// 保存图片（头像）
	public static void savePhotoToSDCard(Bitmap photoBitmap, String path,
			String photoName) {
		if (isAvailable()) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File photoFile = new File(path, photoName);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
						// fileOutputStream.close();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					if (null != fileOutputStream) {
						fileOutputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 判断SD卡是否可用
	public static boolean isAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());

	}

	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str)) {
			return true;
		}
		if (str.trim().equals("") || str.trim().equals("null")) {
			return true;
		}
		return false;
	}
	public static String formatFloat(double value) {

		DecimalFormat df = new DecimalFormat("0.00");
		df.setRoundingMode(RoundingMode.HALF_UP);
		return df.format(value);
	}
	public static String getNowtime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return df.format(new Date());//
	}
	public static int getBarHeight(Context context){
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 38;//默认为38，貌似大部分是这样的

		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(x);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return sbar;
	}
	/**
	 * 日期格式为 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getNowtimetoo() {
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		return df2.format(new Date(System.currentTimeMillis()));//
	}

	/**
	 * 根据属性名获取属性值
	 * 
	 */
	public static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取故障单编号 flag 厂内外的标志
	 * 
	 * @return
	 */
	public static String getCrashCode(String flag) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSSS");// 设置日期格式
		return flag + "MF" + df.format(new Date());
	}

	public static void saveFile(String toSaveString, String filePath) {
		try {
			File saveFile = new File(filePath);
			if (!saveFile.exists()) {
				File dir = new File(saveFile.getParent());
				dir.mkdirs();
				saveFile.createNewFile();
			}

			FileOutputStream outStream = new FileOutputStream(saveFile);
			outStream.write(toSaveString.getBytes());
			outStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static Object jsonToObj(Object object, JSONObject js) {
		try {
			Field[] fields = object.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getType() == String.class) {
					fields[i].set(object, js.optString(fields[i].getName()));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return object;
	}

	
	/**
	 * 是否是平板
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

		public static long getTimeBetween(String time1,String time2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(time1);
			Date d2 = df.parse(time2);
			long diff = d1.getTime() - d2.getTime();//new Date().getTime();// 这样得到的差值是微秒级别
			if (diff < 0) {
				return -1;
			}
			Log.i("http",(diff/1000)+"");
			return diff;
//			long days = diff / (1000 * 60 * 60 * 24);
//			long hours = (diff - days * (1000 * 60 * 60 * 24))
//					/ (1000 * 60 * 60);
//			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
//					* (1000 * 60 * 60))
//					/ (1000 * 60);
//			if (from==0) {//全日房 计算天数
//				return (int) days;
//			}else{//钟点房计算小时数
//				return (int)((days*24)+hours);
//			}
		} catch (Exception e) {
		}
		return -1;
	}
	/**
	 * 根据总条数 获取总页数
	 *
	 * @param count
	 * @return
	 */
	public static int getPageCount(int count,int pageSize) {
		int p = count % pageSize;
		if (p == 0) {
			return count / pageSize;
		} else {
			return count / pageSize + 1;
		}
	}
}
