package com.ytg.jzy.p_common.tools;

import android.content.Context;
import android.content.DialogInterface;

import com.ytg.jzy.p_common.dialog.LoadingDialog;
import com.ytg.jzy.p_common.dialog.RXProgressDialog;


/**
 * 显示对话框工具类
 * @author YTG
 */
public class YTGDialogMgr {

    private static final String TAG = "YTG.YTGDialogMgr";
    public static void showDialog(Context context){
        LoadingDialog dialog = new LoadingDialog(context);
        dialog.show();
    }
    /**
     * 显示一个进度对话框
     * @param context 上下文
     * @param message 对话框显示文本
     * @param l 对话框取消回调接口
     * @return RXProgressDialog
     */
    public static RXProgressDialog showProgress(Context context , String message , DialogInterface.OnCancelListener l) {
        return showProgress(context, RXProgressDialog.STYLE_DIM_AMOUNT, message, true, l);
    }

    /**
     * 显示一个进度对话框
     * @param context 上下文
     * @param message 对话框显示文本
     * @param cancelable 对话框是否可以取消
     * @param l 对话框取消回调接口
     * @return RXProgressDialog
     */
    public static RXProgressDialog showProgress(Context context , String message , boolean cancelable , DialogInterface.OnCancelListener l) {
        return showProgress(context, RXProgressDialog.STYLE_HORIZONTAL, message, cancelable, l);
    }

    /**
     * 显示一个进度对话框
     * @param context 上下文
     * @param style 对话框模式
     * @param message 对话框显示文本
     * @param cancelable 对话框是否可以取消
     * @param l 对话框取消回调接口
     * @return RXProgressDialog
     */
    public static RXProgressDialog showProgress(Context context ,int style, String message , boolean cancelable , final DialogInterface.OnCancelListener l) {
        RXProgressDialog dialog = RXProgressDialog.show(context, message, cancelable, style, new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if(l != null) {
                    l.onCancel(dialog);
                }
            }
        });
        return dialog;
    }



    public interface OnEditDialogClickListener{
        boolean onEditClick(CharSequence text);
    }


    public interface OnSingleClickListener {

        void onClick(DialogInterface dialog, int which);
    }
}
