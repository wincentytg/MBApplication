package com.ytg.jzy.p_common.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ytg.jzy.p_common.R;


/**
 * 单条的dialog
 * @author yl
 *
 */
public class DialogOnTextView extends BaseDialog {
	private TextView mTvcon;
	private View rootView;

	public DialogOnTextView(Context context) {
		super(context);
		rootView = LayoutInflater.from(context).inflate(
				R.layout.dialog_textview, null);
		mTvcon = (TextView) rootView.findViewById(R.id.alert_content);
		addView(rootView);
	}
	/**
	 * @param mAddBottomBtn 是否显示底部操作按钮
	 * @param context
	 */
	public DialogOnTextView(Context context, boolean mAddBottomBtn) {
		super(context);
		rootView = LayoutInflater.from(context).inflate(
				R.layout.dialog_textview, null);
		mTvcon = (TextView) rootView.findViewById(R.id.alert_content);
		addView(rootView,mAddBottomBtn);
	}
	public void setDialogMsg(CharSequence message){
		mTvcon.setText(message);
	}
	
}
