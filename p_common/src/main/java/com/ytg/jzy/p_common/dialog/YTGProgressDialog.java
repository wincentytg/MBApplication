package com.ytg.jzy.p_common.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ytg.jzy.p_common.R;
import com.ytg.jzy.p_common.utils.LogUtil;


/**
 * @author YTG
 */
public class YTGProgressDialog extends YTGSafeProgressDialog {

    private static final String TAG = "YTG.YTGProgressDialog";

    public static final int STYLE_HORIZONTAL = 0;
    public static final int STYLE_VERTICAL = 1;
    public static final int STYLE_DIM_AMOUNT = 2;

    /**上下文*/
    private Context mContext;
    /**对话框模式*/
    private int style;
    /**对话框布局文件*/
    private View mDialogView;
    /**对话框进度条*/
    private ProgressBar mProgressBar;
    /**对话框显示内容*/
    private TextView mProgressMessager;

    /**
     * 对话框构造方法
     * @param context 上下文
     * @param theme 主题
     * @param style 对话框模式
     */
    private YTGProgressDialog(Context context , int theme , int style) {
        super(context, theme);
        int resource ;
        this.mContext = context;
        this.style = style;
        switch(this.style) {
            case STYLE_HORIZONTAL:
            case STYLE_DIM_AMOUNT:
                resource = R.layout.ytx_progress_dialog;
                break;
            case STYLE_VERTICAL:
                resource = R.layout.ytx_progress_dialog_with_bg;
                break;
            default:
                resource = R.layout.ytx_progress_dialog;
        }
        mDialogView = View.inflate(context, resource, null);
        this.mProgressMessager = (TextView) mDialogView.findViewById(R.id.ytx_progress_dialog_msg);
        this.mProgressBar = (ProgressBar) mDialogView.findViewById(R.id.ytx_progress_dialog_icon);
        this.setCanceledOnTouchOutside(true);
    }

    /**
     * 创建一个对话框
     * @param context 上下文
     * @param message 对话框文本
     * @param cancelable 对话框是否可用取消
     * @param style 对话框模式
     * @param listener 对话框取消回调接口
     * @return YTGProgressDialog
     */
    public static YTGProgressDialog create(Context context , CharSequence message , boolean cancelable , int style , DialogInterface.OnCancelListener listener){
        int theme ;
        switch (style) {
            case STYLE_HORIZONTAL:
            case STYLE_VERTICAL:
            case STYLE_DIM_AMOUNT:
                theme = R.style.YuntxAlertDialog;
                break;
            default:
                theme = R.style.YuntxTipsDialog;
                break;
        }

        YTGProgressDialog dialog = new YTGProgressDialog(context, theme, style);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(listener);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 创建并显示对话框
     * @param context 上下文
     * @param message 对话框显示文本
     * @param cancelable 对话框是否可以取消
     * @param style 对话框模式
     * @param listener 对话框取消回调接口
     * @return YTGProgressDialog
     */
    public static YTGProgressDialog show(Context context , CharSequence message , boolean cancelable , int style , DialogInterface.OnCancelListener listener){
        YTGProgressDialog dialog = create(context, message, cancelable, style, listener);
        dialog.show();
        return dialog;
    }


    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mDialogView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        WindowManager.LayoutParams mLayoutParams = getWindow().getAttributes();
        mLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        // everything behind this window will be dimmed.
        // Use dimAmount to control the amount of dim.
        if(style == STYLE_DIM_AMOUNT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mLayoutParams.dimAmount = 0.65F;
        }
        onWindowAttributesChanged(mLayoutParams);
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            LogUtil.e(TAG , "dismiss exception, e = " + e.getMessage());
        }
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        this.setCanceledOnTouchOutside(flag);
    }

    /**
     * 设置对话框显示文字信息
     * @param text 显示文字信息
     */
    public void setMessage(CharSequence text) {
        this.mProgressMessager.setText(text);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            LogUtil.e(TAG , "show exception, e = " + e.getMessage());
        }
    }
}
