package com.ytg.jzy.p_common.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ytg.jzy.p_common.R;
/**
 *
 * @author 于堂刚
 */

public class LoadingDialog extends AlertDialog {
private TextView mTvLoad;
private Context context;
private View view = null;
	public LoadingDialog(Context context) {
		super(context, R.style.loadingdialog_style);
		this.context = context;
		view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
	}
	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(view);
		setCanceledOnTouchOutside(false);
		mTvLoad  =(TextView) view.findViewById(R.id.dialog_loadtext);
//		mTvLoad.setVisibility(View.INVISIBLE);
	}
	
	public void setTextVisible(int flag){
		view.findViewById(R.id.dialog_loadtext).setVisibility(flag);
	}
}
