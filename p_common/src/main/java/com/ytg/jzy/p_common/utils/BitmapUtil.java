/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.ytg.jzy.p_common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.ytg.jzy.p_common.YTGApplicationContext;

import junit.framework.Assert;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;


/**
 * <p>Title: BitmapUtil.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 * @author Jorstin Chan
 * @date 2014-7-16
 * @version 1.0
 */
public class BitmapUtil {
	
//	public static final String TAG = LogUtil.getLogUtilsTag(BitmapUtil.class);
	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
	public static boolean inNativeAllocAccessError = false;
	public static DisplayMetrics mDisplayMetrics;
	
	/**
	 * 
	 * @param path
	 * @param height
	 * @param width
	 * @param crop
	 * @return
	 */
	@TargetApi(VERSION_CODES.HONEYCOMB)
	public static Bitmap extractThumbNail(final String path, final int width, final int height,  final boolean crop) {
		Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

		Options options = new Options();

		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}

//			LogUtil.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
//			LogUtil.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}

			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}

			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}

			options.inJustDecodeBounds = false;
			if (Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
				options.inMutable = true;
			}
//			LogUtil.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			setInNativeAlloc(options);
			if (bm == null) {
//				Log.e(TAG, "bitmap decode failed");
				return null;
			}

//			LogUtil.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
			if (scale != null) {
				bm.recycle();
				bm = scale;
			}

			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}

				bm.recycle();
				bm = cropped;
//				LogUtil.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
			}
			return bm;

		} catch (final OutOfMemoryError e) {
//			LogUtil.e(TAG, "decode bitmap failed: " + e.getMessage());
			options = null;
		}

		return null;
	}
	
	/**
	 * rotate bitmap
	 * @param srcPath
	 * @param degrees
	 * @param format
	 * @param root
	 * @param fileName
	 * @return
	 */
	public static boolean rotateCreateBitmap(String srcPath , int degrees , CompressFormat format , String root , String fileName) {
		Bitmap decodeFile = BitmapFactory.decodeFile(srcPath);
		if(decodeFile == null) {
//			LogUtil.e(TAG, "rotate: create bitmap fialed");
			return false;
		}
		int width = decodeFile.getWidth();
		int height = decodeFile.getHeight();
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees, width / 2.0F, height / 2.0F);
		Bitmap createBitmap = Bitmap.createBitmap(decodeFile, 0, 0, width, height, matrix, true);
		decodeFile.recycle();
		try {
			saveImageFile(createBitmap, 60, format, root, fileName);
			return true;
		} catch (Exception e) {
//			LogUtil.e(TAG,  "create thumbnail from orig failed: " + fileName);
		}
		return false;
	}
	
	public static int getBitmapDegrees(String filePath) {
		if(TextUtils.isEmpty(filePath)) {
//			LogUtil.d(TAG, "filePath is null or nil");
			return 0;
		}
		if(!new File(filePath).exists()) {
//			LogUtil.d(TAG, "file not exist:" + filePath);
			return 0;
		}
		ExifInterface exifInterface = null;
		try {
			
			if(Integer.valueOf(Build.VERSION.SDK).intValue() >= 5) {
				exifInterface = new ExifInterface(filePath);
				int attributeInt = - 1;
				if(exifInterface != null) {
					attributeInt = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
				}
				
				if(attributeInt != -1) {
					switch (attributeInt) {
					case ExifInterface.ORIENTATION_FLIP_VERTICAL:
					case ExifInterface.ORIENTATION_TRANSPOSE:
					case ExifInterface.ORIENTATION_TRANSVERSE:
						return 0;
					case ExifInterface.ORIENTATION_ROTATE_180:
						return 180;
					case ExifInterface.ORIENTATION_ROTATE_90:
						return 90;
					case ExifInterface.ORIENTATION_ROTATE_270:
						return 270;
					default:
						break;
					}
				}
			}
		} catch (IOException e) {
//			LogUtil.e(TAG,  "cannot read exif :" + e.getMessage());
		} finally {
			exifInterface = null;
		}
		return 0;
	}
	
	/**
	 * 压缩发送到服务器的图片
	 * @param origPath 原始图片路径
	 * @param widthLimit 图片宽度限制
	 * @param heightLimit 图片高度限制
	 * @param format 图片格式
	 * @param quality 图片压缩率
	 * @param authorityDir 图片目录
	 * @param outPath 图片详细目录
	 * @return
	 */
	public static boolean createThumbnailFromOrig(String origPath,
			int widthLimit, int heightLimit, CompressFormat format,
			int quality, String authorityDir, String outPath) {
		Bitmap bitmapThumbNail = extractThumbNail(origPath, widthLimit, heightLimit, false);
		if(bitmapThumbNail == null) {
			return false;
		}
		
		try {
			saveImageFile(bitmapThumbNail, quality, format, authorityDir, outPath);
			return true;
		} catch (IOException e) {
//			LogUtil.e(TAG,  "create thumbnail from orig failed: " + outPath);
		}
		return false;
	}
	
	/**
	 * 
	 * @param bitmap
	 * @param quality
	 * @param format
	 * @param authorityDir
	 * @param outPath
	 * @throws IOException 
	 */
	public static void saveImageFile(Bitmap bitmap , int quality ,CompressFormat format , String authorityDir, String outPath) throws IOException {
		if(!TextUtils.isEmpty(authorityDir) && !TextUtils.isEmpty(outPath)) {
//			LogUtil.d(TAG, "saving to " + authorityDir + outPath);
			File file = new File(authorityDir);
			if(!file.exists()) {
				file.mkdirs();
			}
			File outfile = new File(file, outPath);
			outfile.createNewFile();
			
			try {
				FileOutputStream outputStream = new FileOutputStream(outfile);
				bitmap.compress(format, quality, outputStream);
				outputStream.flush();
			} catch (Exception e) {
//				LogUtil.e(TAG, "saveImageFile fil=" + e.getMessage());
			}
		}
	}

	
	/**
	 * 
	 * @param width
	 * @param height
	 * @param config
	 * @return
	 */
	public static Bitmap createBitmap4ReturnSuitable(int width , int height , Config config , boolean dontSuitableforFail) {
		Bitmap bitmap = null;
		try {
			Bitmap _bitmap = Bitmap.createBitmap(width, height, config);
			bitmap = _bitmap;
			return bitmap;
		} catch (IncompatibleClassChangeError e) {
			e.printStackTrace();
			throw ((IncompatibleClassChangeError)new IncompatibleClassChangeError("May cause dvmFindCatchBlock crash!").initCause(e));
		} catch (Throwable e) {
			if(dontSuitableforFail) {
				return null;
			}
			try {
				
				if(mDisplayMetrics == null) {
					mDisplayMetrics = YTGApplicationContext.getContext().getResources().getDisplayMetrics();
				}
				DisplayMetrics displayMetrics = mDisplayMetrics;
				if ((width > displayMetrics.widthPixels) && (height > displayMetrics.heightPixels)){
					width = displayMetrics.widthPixels;
					height = displayMetrics.heightPixels;
				}
				Bitmap retryBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
				return retryBitmap;
			} catch (IncompatibleClassChangeError e2) {
				e.printStackTrace();
				throw ((IncompatibleClassChangeError)new IncompatibleClassChangeError("May cause dvmFindCatchBlock crash!").initCause(e));
			}  catch (Throwable e2) {
				
			}
		}
		
		return null;
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, boolean recycle ,float paramFloat ) {
		return getRoundedCornerBitmap(bitmap, recycle, paramFloat, false);
	}
	
	/**
	 * 
	 * @param bitmap
	 * @param recycle
	 * @param paramFloat
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, boolean recycle ,float paramFloat , boolean dontSuitableforFail) {
		if(bitmap == null || bitmap.isRecycled()) {
//			LogUtil.e(TAG,  "getRoundedCornerBitmap in bitmap is null");
			return null;
		}
		Bitmap tempBitmap = createBitmap4ReturnSuitable(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888, dontSuitableforFail);
		if(tempBitmap == null) {
			return null;
		}
		
		Canvas canvas = new Canvas(tempBitmap);
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setFilterBitmap(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(-4144960);
		canvas.drawRoundRect(rectF, paramFloat, paramFloat, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		if(recycle) {
			bitmap.recycle();
		}
		
		return tempBitmap;
	}
	
	/**
	 * 
	 * @param bitmap
	 * @param paramFloat
	 * @return
	 */
	public static Bitmap processBitmap(Bitmap bitmap, float paramFloat) {
		Assert.assertNotNull(bitmap);
		Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(resultBitmap);
		Paint paint = new Paint();
		Rect localRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(localRect);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setFilterBitmap(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(-4144960);
		canvas.drawRoundRect(rectF, paramFloat, paramFloat, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, localRect, localRect, paint);
		bitmap.recycle();
		return resultBitmap;
	}
	
//	/**
//	 * @param context
//	 * @param intent
//	 * @param appPath
//	 * @return
//	 */
//	@TargetApi(19)
//	public static String resolvePhotoFromIntent(Context context , Intent intent , String appPath) {
//		if(context == null || intent == null || appPath == null) {
//			LogUtil.e(LogUtil.getLogUtilsTag(BitmapUtil.class), "resolvePhotoFromIntent fail, invalid argument");
//			return null;
//		}
//		Uri uri = Uri.parse(intent.toURI());
//		 Cursor cursor = null;
//		  if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) && DocumentsContract.isDocumentUri(context, uri)){
//		      String wholeID = DocumentsContract.getDocumentId(uri);
//		      String id = wholeID.split(":")[1];
//		      String sel = MediaStore.Images.Media._ID + "=?";
//		      cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, sel, new String[] { id }, null);
//		  } else {
//		   cursor = context.getContentResolver().query(uri, null, null, null, null);
//		  }
//		try {
//			
//			String pathFromUri = null;
//			if(cursor != null && cursor.getCount() > 0) {
//				/*cursor.moveToFirst();
//				int columnIndex = cursor.getColumnIndex(MediaColumns.DATA);*/
//				int columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
//				cursor.moveToFirst();
//				// if it is a picasa image on newer devices with OS 3.0 and up
//				if(uri.toString().startsWith("content://com.google.android.gallery3d")) {
//					// Do this in a background thread, since we are fetching a large image from the web
//					pathFromUri =  saveBitmapToLocal(appPath, createChattingImageByUri(intent.getData()));
//				} else {
//					// it is a regular local image file
//					pathFromUri =  cursor.getString(columnIndex);
//				}
//				cursor.close();
////				LogUtil.d(TAG, "photo from resolver, path: " + pathFromUri);
//				return pathFromUri;
//			} else {
//				
//				if(intent.getData() != null) {
//					pathFromUri = intent.getData().getPath();
//					if(new File(pathFromUri).exists()) {
////						LogUtil.d(TAG, "photo from resolver, path: " + pathFromUri);
//						return pathFromUri;
//					}
//				}
//				
//				// some devices (OS versions return an URI of com.android instead of com.google.android
//				if((intent.getAction() != null) && (!(intent.getAction().equals("inline-data")))){
//					// use the com.google provider, not the com.android provider.
//					// Uri.parse(intent.getData().toString().replace("com.android.gallery3d","com.google.android.gallery3d"));
//					pathFromUri =  saveBitmapToLocal(appPath, (Bitmap)intent.getExtras().get("data"));
////					LogUtil.d(TAG, "photo from resolver, path: " + pathFromUri);
//					return pathFromUri;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if(cursor!=null)
//			cursor.close();
//		}
//		
////		LogUtil.e(TAG, "resolve photo from intent failed ");
//		return null;
//	}
	
	/**
	 * save image from uri
	 * @param outPath
	 * @param bitmap
	 * @return
	 */
//	public static String saveBitmapToLocal(String outPath , Bitmap bitmap) {
//		try {
//			String imagePath = outPath + FileAccessor.getFileNameMD5(DateFormat.format("yyyy-MM-dd-HH-mm-ss", System.currentTimeMillis()).toString().getBytes()) + ".jpg";
//			File file = new File(imagePath);
//			if(!file.exists()) {
//				file.createNewFile();
//			}
//			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
//			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
//			bufferedOutputStream.close();
////			LogUtil.d(TAG, "photo image from data, path:" + imagePath);
//			return imagePath;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/**
	 * save image from uri
	 * @param bitmap
	 * @return
	 */
	public static String saveBitmapToLocal(File absoluteFile , Bitmap bitmap) {
		try {
			if(!absoluteFile.exists()) {
				absoluteFile.createNewFile();
			}
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(absoluteFile));
			bitmap.compress(CompressFormat.PNG, 100, bufferedOutputStream);
			bufferedOutputStream.close();
//			LogUtil.d(TAG, "photo image from data, path:" + absoluteFile.getAbsolutePath());
			return absoluteFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param src
	 * @param degree
	 * @return
	 */
	public static Bitmap degreeBitmap(Bitmap src , float degree) {
		if(degree == 0.0F) {
			return src;
		}
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.setRotate(degree, src.getWidth() / 2, src.getHeight() / 2);
		Bitmap resultBitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
		boolean filter = true;
		if(resultBitmap == null) {
//			LogUtil.e(TAG, "resultBmp is null: ");
			filter = true;
		} else {
			filter = false;
		}
		
		if(resultBitmap != src) {
			src.recycle();
		}
//		LogUtil.d(TAG, "filter: " + filter + "  degree:" + degree);
		return resultBitmap;
	}
	
	/**
	 * 
	 * @param imagePath
	 * @return
	 */
	public static Options getImageOptions(String imagePath) {
		if(TextUtils.isEmpty(imagePath)) {
//			LogUtil.e(TAG, "getImageOptions invalid path");
			return null;
		}
		Options options = new Options();
		options.inJustDecodeBounds = true;
		setInNativeAlloc(options);
		
		try {
			int attempts = 0;
			Bitmap bitmap = null;
			do {
				bitmap = BitmapFactory.decodeFile(imagePath);
			} while (bitmap == null && attempts <=5);
			
			if(bitmap != null) {
				bitmap.recycle();
			}
			
		} catch (OutOfMemoryError e) {
//			LogUtil.e(TAG, "decode bitmap failed: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * @param options
	 * @param b
	 * @param path
	 * @param uri
	 * @param resource
	 * @return
//	 */
//	public static Bitmap decodeMuilt(BitmapFactory.Options options , byte[] data , String path , Uri uri , int resource) {
//		try {
//			
//			if(!checkByteArray(data) && TextUtils.isEmpty(path) && uri == null && resource <= 0) {
//				return null;
//			}
//			
//			if(checkByteArray(data)) {
//				return BitmapFactory.decodeByteArray(data, 0, data.length, options);
//			}
//			
//			if (uri != null){
//				InputStream inputStream = CCPAppManager.getContext().getContentResolver().openInputStream(uri);
//				Bitmap localBitmap = BitmapFactory.decodeStream(inputStream, null, options);
//				inputStream.close();
//				return localBitmap;
//			}
//			
//			if(resource > 0) {
//				return BitmapFactory.decodeResource(CCPAppManager.getContext().getResources(), resource, options);
//			}
//			return BitmapFactory.decodeFile(path, options);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/**
	 * 
	 * @param srcBip
	 * @param quality
	 * @param format
	 * @param outPath
	 * @param recycle
	 * @throws IOException 
	 */
	public static void saveBitmapToImage(Bitmap srcBip , int quality ,  CompressFormat format
			, String outPath , boolean recycle) throws IOException {
		if(TextUtils.isEmpty(outPath)) {
			throw new IOException("saveBitmapToImage pathName null or nil");
		}
		
		File file = new File(outPath);
		file.createNewFile();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			srcBip.compress(format, quality, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			if(recycle) {
				srcBip.recycle();
			}
		} catch (FileNotFoundException e) {
//			LogUtil.e(TAG, "FileNotFoundException " + e.toString());
		} catch (IOException e) {
//			LogUtil.e(TAG, "IOException = " + e.toString());
		} catch (Exception e) {
//			LogUtil.e(TAG, "Exception " + e.toString());
		}
	}
	
	/**
	 * 
	 * @param stream
	 * @return
	 */
	public static Bitmap decodeStream(InputStream stream) {
		return decodeStream(stream, 0.0F);
	}
	
	/**
	 * 
	 * @param stream
	 * @param dip
	 * @return
	 */
	public static Bitmap decodeStream(InputStream stream , float dip) {
		Options options = new Options();
		if(dip != 0.0F) {
			options.inDensity = (int)(160.0F * dip);
		}
		options.inPreferredConfig = Config.ARGB_8888;
		setInNativeAlloc(options);
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
			return bitmap;
		} catch (OutOfMemoryError e) {
			options.inPreferredConfig = Config.RGB_565;
			setInNativeAlloc(options);
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
				return bitmap;
			} catch (OutOfMemoryError e2) {
			}
		}
		return null;
	}
	
	public static void setInNativeAlloc(Options options) {
		if (Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH && !inNativeAllocAccessError) {
			try {
				Options.class.getField("inNativeAlloc").setBoolean(options, true);
				return ;
			} catch (Exception e) {
				inNativeAllocAccessError = true;
			}
		}
	}
	
	public int computeSampleSize(Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	} 
	
	private  int computeInitialSampleSize(Options options,int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;              
		double h = options.outHeight;              
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));              
		int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));              
		if (upperBound < lowerBound) {                   
			// return the larger one when there is no overlapping zone.                   
			return lowerBound;               
		}              
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {                 
			return 1;               
		} else if (minSideLength == -1) {       
			return lowerBound;              
		} else {             
			return upperBound;   
		}          
	}
	 
    /**
     * get the orientation of the bitmap {@link ExifInterface}
     * @param path
     * @return
     */
    public final static int getDegress(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
     
    /**
     * rotate the bitmap 
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress); 
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }
    
    private static int mScreenWidth ;
//    public static int getImageMinWidth(Context context) {
//		if(mScreenWidth <= 0){
//			mScreenWidth = DensityUtil.getImageWeidth(context, 1.0F)- DensityUtil.getDisplayMetrics(context, 40F);
//			mScreenWidth = mScreenWidth / 4; 
//		}
//		return mScreenWidth;
//	}
     
    /**
     * caculate the bitmap sampleSize
     * @return
     */
    public final static int caculateInSampleSize(Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) return 1;
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height/ (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
     
    /**
     * 压缩指定路径的图片，并得到图片对象
     * @param path bitmap source path
     * @return Bitmap {@link Bitmap}
     */
    public final static Bitmap compressBitmap(String path, int rqsW, int rqsH) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
     
    /**
     * 压缩指定路径图片，并将其保存在缓存目录中，通过isDelSrc判定是否删除源文件，并获取到缓存后的图片路径
     * @param context
     * @param srcPath
     * @param rqsW
     * @param rqsH
     * @param isDelSrc
     * @return
     */
    public final static String compressBitmap(Context context, String srcPath, int rqsW, int rqsH, boolean isDelSrc) {
        Bitmap bitmap = compressBitmap(srcPath, rqsW, rqsH);
        File srcFile = new File(srcPath);
        String desPath = getImageCacheDir(context) + srcFile.getName();
        int degree = getDegress(srcPath);
        try {
            if (degree != 0) bitmap = rotateBitmap(bitmap, degree);
            File file = new File(desPath);
            FileOutputStream  fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 70, fos);
            fos.close();
            if (isDelSrc) srcFile.deleteOnExit();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage()+"");
        }
        return desPath;
    }
     
    /**
     * 此方法过期，该方法可能造成OutOfMemoryError，使用不含isAdjust参数的方法
     * @param is
     * @param reqsW
     * @param reqsH
     * @param isAdjust
     * @return
     */
    @Deprecated
    public final static Bitmap compressBitmap(InputStream is, int reqsW, int reqsH, boolean isAdjust) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return compressBitmap(bitmap, reqsW, reqsH, isAdjust);
    }
     
    /**
     * 压缩某个输入流中的图片，可以解决网络输入流压缩问题，并得到图片对象
     * @return Bitmap {@link Bitmap}
     */
    public final static Bitmap compressBitmap(InputStream is, int reqsW, int reqsH) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ReadableByteChannel channel = Channels.newChannel(is);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (channel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) baos.write(buffer.get());
                buffer.clear();
            }
            byte[] bts = baos.toByteArray();
            Bitmap bitmap = compressBitmap(bts, reqsW, reqsH);
            is.close();
            channel.close();
            baos.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
     
    /**
     * 压缩指定byte[]图片，并得到压缩后的图像
     * @param bts
     * @param reqsW
     * @param reqsH
     * @return
     */
    public final static Bitmap compressBitmap(byte[] bts, int reqsW, int reqsH) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
        options.inSampleSize = caculateInSampleSize(options, reqsW, reqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
    }
     
    /**
     * 此方法已过期，该方法可能造成OutOfMemoryError，使用不含isAdjust参数的方法
     * @param bitmap
     * @param reqsW
     * @param reqsH
     * @return
     */
    @Deprecated
    public final static Bitmap compressBitmap(Bitmap bitmap, int reqsW, int reqsH, boolean isAdjust) {
        if (bitmap == null || reqsW == 0 || reqsH == 0) return bitmap;
        if (bitmap.getWidth() > reqsW || bitmap.getHeight() > reqsH) {
            float scaleX = new BigDecimal(reqsW).divide(new BigDecimal(bitmap.getWidth()), 4, RoundingMode.DOWN).floatValue();
            float scaleY = new BigDecimal(reqsH).divide(new BigDecimal(bitmap.getHeight()), 4, RoundingMode.DOWN).floatValue();
            if (isAdjust) {
                scaleX = scaleX < scaleY ? scaleX : scaleY;
                scaleY = scaleX;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(scaleX, scaleY);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }
     
    /**
     * 压缩已存在的图片对象，并返回压缩后的图片
     * @param bitmap
     * @param reqsW
     * @param reqsH
     * @return
     */
    public final static Bitmap compressBitmap(Bitmap bitmap, int reqsW, int reqsH) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, baos);
            byte[] bts = baos.toByteArray();
            Bitmap res = compressBitmap(bts, reqsW, reqsH);
            baos.close();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        }
    }
     
    /**
     * 此方法过期，该方法可能造成OutOfMemoryError，使用不含isAdjust参数的方法
     * get bitmap form resource dictory, and then compress bitmap according to reqsW and reqsH
     * @param res {@link Resources}
     * @param resID
     * @param reqsW
     * @param reqsH
     * @return
     */
    @Deprecated
    public final static Bitmap compressBitmap(Resources res, int resID, int reqsW, int reqsH, boolean isAdjust) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, resID);
        return compressBitmap(bitmap, reqsW, reqsH, isAdjust);
    }
     
    /**
     * 压缩资源图片，并返回图片对象
     * @param res {@link Resources}
     * @param resID
     * @param reqsW
     * @param reqsH
     * @return
     */
    public final static Bitmap compressBitmap(Resources res, int resID, int reqsW, int reqsH) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resID, options);
        options.inSampleSize = caculateInSampleSize(options, reqsW, reqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resID, options);
    }

    /**
     * 基于质量的压缩算法， 此方法未 解决压缩后图像失真问题
     * <br> 可先调用比例压缩适当压缩图片后，再调用此方法可解决上述问题
     * @param maxBytes 压缩后的图像最大大小 单位为byte
     * @return
     */
    public final static Bitmap compressBitmap(Bitmap bitmap, long maxBytes) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, baos);
            int options = 90;
            while (baos.toByteArray().length > maxBytes) {
                baos.reset();
                bitmap.compress(CompressFormat.PNG, options, baos);
                options -= 10;
            }
            byte[] bts = baos.toByteArray();
            Bitmap bmp = BitmapFactory.decodeByteArray(bts, 0, bts.length);
            baos.close();
            return bmp;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
     
    /**
     * 得到指定路径图片的options
     * @param srcPath
     * @return Options {@link Options}
     */
    public final static Options getBitmapOptions(String srcPath) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        return options;
    }
     
    /**
     * 获取图片缓存路径
     * @param context
     * @return
     */
    private static String getImageCacheDir(Context context) {
        String dir = /*FileHelper.getCacheDir(context) + "Image" + File.separator*/"";
        File file = new File(dir);
        if (!file.exists()) file.mkdirs();
        return dir;
    }
	


    public interface ResizeImageResultCallback {
        void onResizeResult(byte[] part);
    }
    
    public static boolean checkByteArray(byte[] b) {
    	return b != null && b.length > 0;
    }
    
	public  static Bitmap getMyThumbNail(String origPath, int widthLimit,
			int heightLimit) {
		Options options = getBitmapOptions(origPath);

		final double beY = options.outHeight * 1.0 / heightLimit;
		final double beX = options.outWidth * 1.0 / widthLimit;
		int newHeight = heightLimit;
		int newWidth = widthLimit;
		if (beY < beX) {
			newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
		} else {
			newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
		}

		Bitmap bitmapThumbNail;
		Bitmap bitmap = BitmapFactory.decodeFile(origPath);
		bitmapThumbNail = ThumbnailUtils.extractThumbnail(bitmap, newWidth,
				newHeight);
		return bitmapThumbNail;
	}
	
	/**
     * 把图片变成圆角
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels){
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
    	
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
        } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
        }
       
        Bitmap output = Bitmap.createBitmap(width,
                        height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
       
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);
       
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        bitmap = output;
        return output;
}
    

	private static final String TAG = "ECSDK.Demo.BitmapUtil";
	public static Bitmap getCombineBitmaps(List<InnerBitmapEntity> mEntityList,
			Bitmap... bitmaps) {
		LogUtil.d(TAG , "count=" + mEntityList.size());
		Bitmap newBitmap = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
		LogUtil.d(TAG , "newBitmap=" + newBitmap.getWidth() + ","
				+ newBitmap.getHeight());
		for (int i = 0; i < mEntityList.size(); i++) {
			newBitmap = mixtureBitmap(newBitmap, bitmaps[i], new PointF(
					mEntityList.get(i).x, mEntityList.get(i).y));
		}
		return newBitmap;
	}
	
	public static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
			PointF fromPoint) {
		if (first == null || second == null || fromPoint == null) {
			return null;
		}
		Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(),
				first.getHeight(), Config.ARGB_8888);
		Canvas cv = new Canvas(newBitmap);
		cv.drawBitmap(first, 0, 0, null);
		cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		return newBitmap;
	}
	
	public static String saveBitmapToLocal(String imagePath ,String outPath , Bitmap bitmap) {
		try {
			File file = new File(imagePath);
			if(!file.exists()) {
				file.createNewFile();
			}
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(CompressFormat.PNG, 100, bufferedOutputStream);
			bufferedOutputStream.close();
			LogUtil.d(TAG, "photo image from data, path:" + imagePath);
			return imagePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static class InnerBitmapEntity {
		public float x;
		public float y;
		public float width;
		public float height;
		public static int devide = 1;
		public int index = -1;

		@Override
		public String toString() {
			return "InnerBitmapEntity [x=" + x + ", y=" + y + ", width=" + width
					+ ", height=" + height + ", devide=" + devide + ", index="
					+ index + "]";
		}
	}
	


	
	public static void setImageBitmapWithRoundCorner(Bitmap photo,ImageView imageView){
		if(photo == null){
			return;
		}
		Bitmap roundCorner = toRoundBitmap(photo);
//		Bitmap roundCorner = toRoundCorner(photo, photo.getWidth() / 2);
		if(roundCorner!=null)
			imageView.setImageBitmap(roundCorner);
	}

	public static String saveBitmapToLocalSDCard(Bitmap bitmap,String fileName) {
		try {
			File file = new File(fileName);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(CompressFormat.PNG, 100, bufferedOutputStream);
			bufferedOutputStream.close();
			LogUtil.d(TAG, "photo image from data, path:" + fileName);
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getBitmap(String url) {
		Bitmap bm = null;
		try {
			URL iconUrl = new URL(url);
			URLConnection conn = iconUrl.openConnection();
			HttpURLConnection http = (HttpURLConnection) conn;

			int length = http.getContentLength();

			conn.connect();
			// 获得图像的字符流
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, length);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();// 关闭流
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}


}
