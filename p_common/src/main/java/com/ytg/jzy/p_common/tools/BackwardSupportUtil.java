package com.ytg.jzy.p_common.tools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.MediaMetadataRetriever;
import android.os.SystemClock;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ytg.jzy.p_common.YTGApplicationContext;
import com.ytg.jzy.p_common.utils.LogUtil;
import com.ytg.jzy.p_common.utils.TextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YTG
 * @version 5.2.0
 * @since 2016-05-29
 */
public class BackwardSupportUtil {
    public static final String TAG = "YTG.BackwardSupportUtil";

    private static float density;
    public static final String PHONE_PREFIX = "+86";

    static {
        density = -1.0F;
    }

    /**
     * 当前手机分辨率-宽
     * @param context 上下文
     * @return 手机分辨率-宽
     */
    public static int getWidthPixels(Context context) {
        if(context == null) {
            LogUtil.e(TAG , "get widthPixels but context is null");
            return 0;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 当前手机分辨率-高
     * @param context 上下文
     * @return 手机分辨率-高
     */
    public static int getHeightPixels(Context context) {
        if(context == null) {
            LogUtil.e(TAG , "get heightPixels but context is null");
            return 0;
        }
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 判断当前是否一个手机号码
     * @param mobiles 手机号码
     * @return 是否手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 屏幕密度转换
     * @param ctx 上下文
     * @param dp dp
     * @return 转换结果
     */
    public static int fromDPToPix(Context ctx, int dp) {
        return Math.round(getDensity(ctx) * dp);
    }

    public static float getDensity(Context ctx) {
        Context context = ctx;
        if (context == null)
            context = YTGApplicationContext.getContext();
        if (density < 0.0F)
            density = context.getResources().getDisplayMetrics().density;
        return density;
    }


    /**
     * 像素转换成手机屏幕密度
     * @param pxValue 像素
     * @return 密度
     */
    public static int px2dip(Context ctx , float pxValue) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        return Math.round(displayMetrics.densityDpi * pxValue / 160.0F);
    }

    /**
     * 按照一定的格式截取字符串
     * @param srcText 源字符串
     * @param p 截取字符
     * @return 结果
     */
    public static String getLastWords(String srcText, String p) {
        try {
            String[] array = TextUtils.split(srcText, p);
            int index = (array.length - 1 < 0) ? 0 : array.length - 1;
            return array[index];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否为空
     * @param value 判断字符串
     * @return 是否空
     */
    public static boolean isNullOrNil(String value) {
        return !((value != null) && (value.length() > 0));
    }

    /**
     * 如果为空则返回控制付出
     * @param value 源字符串
     * @return 结果
     */
    public static String nullAsNil(String value) {
        return isNullOrNil(value) ? "" : value;

    }


    /**
     * 当前时间
     * @return 当前时间
     */
    public static long currentTicks() {
        return SystemClock.elapsedRealtime();
    }

    /**
     * 模糊处理图片
     * @param sentBitmap 原图
     * @param radius 半径
     * @return 处理之后图片
     */
    public static Bitmap fastBlurBitmap(Bitmap sentBitmap, int radius) {

        /*int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);*/

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        LogUtil.i(TAG, w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        LogUtil.i(TAG, w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }


    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        if (TextUtil.isEmpty(strName)) {
            return false;
        }
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    // 只能判断部分CJK字符（CJK统一汉字）
    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }

    /**
     * 是否全数字
     * @param number 是否数字
     * @return 验证结果
     */
    public static boolean number(String number) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(number);
        return m.matches();
    }


    /**
     * 将字符串转换成Long数据，如果为空则返回默认值
     * @param str
     * @param def
     * @return
     */
    public static long getLong(String str, long def) {
        try {
            if (str == null) {
                return def;
            }
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return def;
    }


    /**
     * 根据提供的遮罩图片资源文件生成一个遮罩图片
     * @param context 上下文
     * @param maskId 遮罩图片资源
     * @return 遮罩图片
     */
    public static Drawable getMaskDrawable(Context context, int maskId) {
        Drawable drawable = null;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(maskId);
        } else {
            drawable = context.getResources().getDrawable(maskId);
        }*/
        Bitmap mMaskBmp = BitmapFactory.decodeResource(context.getResources(), maskId);
        byte[] ninePatchChunk = mMaskBmp.getNinePatchChunk();
        if (ninePatchChunk != null && NinePatch.isNinePatchChunk(ninePatchChunk)) {
            drawable = new NinePatchDrawable(context.getResources(), mMaskBmp, ninePatchChunk, new Rect(), null);
        }

        if (drawable == null) {
            throw new IllegalArgumentException("maskId is invalid");
        }

        return drawable;
    }



    /**
     * 图片缩略图地址
     * @param videoPath 视频地址
     * @return 图片缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever media =new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        return media.getFrameAtTime();
    }

    /**
     * 将字符串转换成整型，如果为空则返回默认值
     * @param str 字符串
     * @param def 默认值
     * @return
     */
    public static int getInt(String str, int def) {
        try {
            if (str == null) {
                return def;
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
        }
        return def;
    }

    /**
     * 将集合转换成字符串，用特殊字符做分隔符
     * @param srcList 转换前集合
     * @param separator 分隔符
     * @return 字符串
     */
    public static String listToString(List<String> srcList, String separator) {
        if (srcList == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < srcList.size(); ++i)
            if (i == srcList.size() - 1) {
                sb.append(srcList.get(i).trim());
            } else {
                sb.append(srcList.get(i).trim() + separator);
            }
        return sb.toString();
    }


    /**
     * 字符串数组转换成字符串
     * @param srcList 数组
     * @param separator 分隔符
     * @return 字符串
     */
    public static String arrayToString(String[] srcList, String separator) {
        if (srcList == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < srcList.length; ++i)
            if (i == srcList.length - 1) {
                sb.append(srcList[i].trim());
            } else {
                sb.append(srcList[i].trim() + separator);
            }
        return sb.toString();
    }

    /**
     * 将集合转换成字符串，用特殊字符做分隔符
     * @param str 转换前集合
     * @param separator 分隔符
     * @return 字符串
     */
    public static List<String> stringtoList(String str , String separator) {
        if(isNullOrNil(str)) {
            return new ArrayList<>();
        }

        String[] split = str.split(separator);
        return Arrays.asList(split);
    }

    /**
     * 将字符串数组转换成字符串集合o
     * @param src
     * @return
     */
    public static List<String> stringsToList(String[] src) {
        if ((src == null) || (src.length == 0)) {
            return null;
        }
        ArrayList<String> dest = new ArrayList<String>();
        for (int i = 0; i < src.length; ++i) {
            dest.add(src[i]);
        }
        return dest;
    }


    public static String stripSeparators(String str) {
        return PhoneNumberUtils.stripSeparators(str);
    }

    /**
     * 获取字母字符串的第一个字符
     * @param str 传入的字符串
     * @return 字符串的首字母，不是字母的返回#
     */
    public static String getAlpha(String str) {
        if (str == null || str.trim().length() == 0) {
            return "#";
        }
        String c = str.trim().substring(0, 1);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c).matches()) {
            return c.toUpperCase();
        } else {
            return "#";
        }
    }

    /**
     * 去除+86
     * @param phoneNumber 电话号码
     * @return 格式结果
     */
    public static String formatPhone(String phoneNumber) {
        if (phoneNumber == null) {
            return "";
        }
        if (phoneNumber.startsWith(PHONE_PREFIX)) {
            return phoneNumber.substring(PHONE_PREFIX.length()).trim();
        }
        return phoneNumber.trim();
    }


    /**
     * 是否是群组
     * @param account 是否群组
     * @return 是否群组
     */
    public static boolean isPeerChat(String account) {
        return account != null && account.toLowerCase().startsWith("g");
    }

    /**
     * 设置字体大小
     * @param context 上下文
     * @param text 文本
     * @param size 字体大小
     * @return
     */
    public static SpannableString getText(Context context , CharSequence text , int size) {
        if(text == null || isNullOrNil(text.toString())) {
            return new SpannableString("");
        }
        SpannableString msp = new SpannableString(text);
        msp.setSpan(new AbsoluteSizeSpan(size), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    /**
     * 获取当前状态栏的高度
     *
     * @param context 上下文
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 25;

        try {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = res.getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            LogUtil.e(TAG + e.getMessage());
        }

        return statusBarHeight;
    }

    public static int count(byte[] b) {
        if(b == null || b.length == 0) {
            return 0;
        }
        int result = 0;
        for (byte aB : b) {
            result += aB;
        }
        return Math.abs(result);
    }
}
