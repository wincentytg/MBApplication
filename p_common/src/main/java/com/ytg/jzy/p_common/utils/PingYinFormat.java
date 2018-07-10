package com.ytg.jzy.p_common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * @author 拼音相关操作
 * @version 5.2.0
 * @since 2016-07-07
 */
public class PingYinFormat {
    public static StringBuffer sb = new StringBuffer();

    /**
     * 拼音转换
     * @param dname 需要转换拼音的SimpleContact,必须设置了name
     * @return 转换结果
     */
    public static String pyFormat(String dname) {
        String qpNameStr = ""; // 完整的全拼Str 首字母大写
        if (dname == null || dname.trim().length() == 0) {
            return "";
        }
        dname = dname.trim();
        // 拼音转换设置
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();// 定义转换格式
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不要声调
        format.setVCharType(HanyuPinyinVCharType.WITH_V);// 设置 女 nv

        @SuppressWarnings("unused")
        String qpName = ""; // 用于分隔全拼数组 转化拼音" "分隔字符串
        StringBuilder qpBuilder = new StringBuilder();

        StringBuilder qpStrBuilder = new StringBuilder();
        // 处理英文名
        if (dname.getBytes().length == dname.length()) {
            qpName = dname;
            String[] splitName = dname.split(" ");
            for (String s : splitName) {

                qpStrBuilder.append(
                        s.subSequence(0, 1).toString().toUpperCase())
                        .append(s.subSequence(1, s.length()));
            }
            // jpName = jpNameBuilder.toString();
        } else { // 含有中文
            for (int i = 0; i < dname.length(); i++) {
                try {
                    String[] pyArray = PinyinHelper
                            .toHanyuPinyinStringArray(dname.charAt(i),
                                    format);
                    if (pyArray == null) {
                        char c = dname.charAt(i);
                        if (' ' == c) {
                            continue;
                        }
                        qpBuilder.append(c).append(" ");
                        qpStrBuilder.append(c);
                        continue;
                    } else {
                        String py = pyArray[0];
                        qpBuilder.append(py).append(" ");
                        qpStrBuilder.append(
                                py.subSequence(0, 1).toString()
                                        .toUpperCase()).append(
                                py.subSequence(1, py.length()));// 将拼音第一个字母转成大写后拼接在一起。
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            qpName = qpBuilder.toString();
        }
        qpNameStr = qpStrBuilder.toString();

        return qpNameStr;
    }
    /**
     * 获取汉字字符串的第一个大写字母
     */
    public static String getPinYinFirstLetter(String str) {
        sb.setLength(0);
        char c = str.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
        if (pinyinArray != null&& pinyinArray.length>0) {
            sb.append(pinyinArray[0].charAt(0));
        } else {
            sb.append(c);
        }
        return sb.toString().toUpperCase();
    }



}
