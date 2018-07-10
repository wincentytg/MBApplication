package com.ytg.jzy.p_common.dialog;

import android.app.ProgressDialog;
import android.content.Context;

import com.ytg.jzy.p_common.utils.LogUtil;


/**
 * @author YTG
 */
public class YTGSafeProgressDialog extends ProgressDialog {

    private static final String TAG = "YTG.YTGSafeProgressDialog";

    /**
     * 构造方法
     * @param context 上下文
     */
    public YTGSafeProgressDialog(Context context) {
        super(context);
    }

    /**
     * 构造方法
     * @param context 上下文
     * @param theme 主题
     */
    public YTGSafeProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            LogUtil.e(TAG , "dismiss exception, e = " + e.getMessage());
        }
    }
}
