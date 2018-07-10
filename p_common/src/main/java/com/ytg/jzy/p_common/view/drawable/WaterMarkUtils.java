package com.ytg.jzy.p_common.view.drawable;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;


/**
 * Created by Gethin on 2017/3/7 0007.
 */

public class WaterMarkUtils {


    public static Drawable getWaterMark(String strMarker) {
        WaterMarkDrawable.WaterMarkBuilder drawable = new WaterMarkDrawable.WaterMarkBuilder(strMarker);
        return drawable.build();
    }
    public static void setWaterMarkToIV(ImageView miv,String marker){

        miv.setImageDrawable(getWaterMark(marker));
    }

}
