package com.ytg.jzy.p_common.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ytg.jzy.p_common.R;
import com.ytg.jzy.p_common.YTGApplicationContext;
import com.ytg.jzy.p_common.tools.BackwardSupportUtil;
import com.ytg.jzy.p_common.view.TextDrawable;

import java.util.zip.CRC32;

public class GlideHelper {
    public static final boolean loadLocal=true;

    public static final int[] COLORS = {
            Color.parseColor("#f6b565"),
            Color.parseColor("#f07363"),
            Color.parseColor("#af8b7c"),
            Color.parseColor("#578bab"),
            Color.parseColor("#369bec"),
            Color.parseColor("#6072a5"),
            Color.parseColor("#28c196"),
            Color.parseColor("#56ccb5"),
            Color.parseColor("#ecba41"),
            Color.parseColor("#51bfe4"),
    };
    private static final int COLORS_NUMBER = COLORS.length;
    public static void showImage(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.mipmap.icon_fault_pic)
//                .error(R.mipmap.icon_fault_pic)
                .dontAnimate()
                .into(iv);
    }

    /**圆角
     * @param context
     * @param avatar
     * @param iv
     */
    public static void showAvatar(Context context, String avatar, ImageView iv) {
        Glide.with(context)
                .load(avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.zc_default_avatar)
                .error(R.drawable.zc_default_avatar)
                .transform(new GlideCircleTransform(context))
                .dontAnimate()
//                .crossFade()
                .into(iv);
    }
    /**
     * 返回一个默认的头像
     *
     * @param name 昵称
     * @return 默认的头像
     */
    public static Drawable getDefaultDrawable(String name, int fontRes) {
        Context context = YTGApplicationContext.getContext();
        if(loadLocal&&context!=null){
            return  context.getResources().getDrawable(R.drawable.zc_default_avatar);
        }
        return getDefaultDrawable(name, fontRes, DensityUtil.dip2px(60), DensityUtil.dip2px(60));
    }

    /**
     * 根据文字生成drawable
     * @param name
     * @param fontRes
     * @param width
     * @param height
     * @return
     */
    public static TextDrawable getDefaultDrawable(String name, int fontRes, int width, int height) {
        String username = name;
        if ((name != null && name.length() >= 2)) {
            username = name.substring(name.length() - 2, name.length());
        } else if (BackwardSupportUtil.isNullOrNil(name)) {
            username = "";
        }
        return TextDrawable.builder()
                .beginConfig()
                .fontSize(fontRes)
                .width(width)
                .height(height)
                .endConfig()
                .buildRound(username, getColorBySeed(BackwardSupportUtil.nullAsNil(username)));
    }

    private static int getColorBySeed(String seed) {
        if (TextUtils.isEmpty(seed)) {
            return COLORS[0];
        }
        CRC32 c = new CRC32();
        c.update(seed.getBytes());
        int index = ((Long) Math.abs(c.getValue() % COLORS_NUMBER)).intValue();
        return COLORS[index];
    }

}
