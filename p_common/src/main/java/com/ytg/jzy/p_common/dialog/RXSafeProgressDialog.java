package com.ytg.jzy.p_common.dialog;

import android.app.ProgressDialog;
import android.content.Context;

import com.ytg.jzy.p_common.utils.LogUtil;


/**
 * @author YTG
 * @since 2017/4/9
 */
public class RXSafeProgressDialog extends ProgressDialog {

    private static final String TAG = "YTG.RXSafeProgressDialog";

    /**
     * 构造方法
     * @param context 上下文
     */
    public RXSafeProgressDialog(Context context) {
        super(context);
    }

    /**
     * 构造方法
     * @param context 上下文
     * @param theme 主题
     */
    public RXSafeProgressDialog(Context context, int theme) {
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
