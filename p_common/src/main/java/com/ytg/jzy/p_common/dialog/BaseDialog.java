package com.ytg.jzy.p_common.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ytg.jzy.p_common.R;
import com.ytg.jzy.p_common.utils.DensityUtil;

/**
 *
 * @author 于堂刚
 */
@SuppressLint("ResourceAsColor") public class BaseDialog extends Dialog {

	private LinearLayout mLayoutContainer, mLayoutBottom;
	private TextView mTvTitle;
	// private boolean mAddBottomBtn;//是否添加底部按钮
	protected Button mBtnLeft, mBtnRight;
	private RelativeLayout mRlTitleRoot;

	public BaseDialog(Context context) {
		super(context, R.style.dialog_style);
		setContentView(R.layout.dialog_basedialog);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		mLayoutContainer = (LinearLayout) findViewById(R.id.dialog_layout_container);
		mTvTitle = (TextView) findViewById(R.id.dialog_tv_title);
		mLayoutBottom = (LinearLayout) findViewById(R.id.dialogBottomView);
		mBtnLeft = (Button) findViewById(R.id.customdialog_btncancel);
		mBtnRight = (Button) findViewById(R.id.customdialog_btnok);
		mRlTitleRoot = (RelativeLayout) findViewById(R.id.titleroot);
		DisplayMetrics metric = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		int mScreenWidth = metric.widthPixels;
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width =mScreenWidth - DensityUtil.dip2px(context, 40); //设置宽度
		getWindow().setAttributes(lp);
	}

	public void setTitleGone() {
		mRlTitleRoot.setVisibility(View.GONE);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTvTitle.setText(title);
	}

	public void setLeftBtn(int visible, String btnText,
			View.OnClickListener listener) {
		if(visible == View.GONE){
			findViewById(R.id.dialogTvBar).setVisibility(View.GONE);
		}
		mBtnLeft.setVisibility(visible);
		mBtnLeft.setText(btnText);
		mBtnLeft.setOnClickListener(listener);
	}

	public void setRightBtn(int visible, String btnText,
			View.OnClickListener listener) {
		if(visible == View.GONE){
			findViewById(R.id.dialogTvBar).setVisibility(View.GONE);
		}
		mBtnRight.setVisibility(visible);
		mBtnRight.setText(btnText);
		mBtnRight.setOnClickListener(listener);
	}

	/**
	 * 默认不显示底部按钮
	 * 
	 * @param v
	 */
	public void addView(View v) {
		if (mLayoutContainer.getChildCount() >= 2) {
			mLayoutContainer.removeViewAt(mLayoutContainer.getChildCount() - 1);
		}
		mLayoutContainer.addView(v, new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));

	}

	/**
	 * @param v
	 * @param mAddBottomBtn
	 *            是否显示底部操作按钮
	 */
	public void addView(View v, boolean mAddBottomBtn) {
		// this.mAddBottomBtn = mAddBottomBtn;
		addView(v);
		if (mAddBottomBtn) {
			mLayoutBottom.setVisibility(View.VISIBLE);
		} else {
			mLayoutBottom.setVisibility(View.GONE);
		}
	}

	@Override
	public View findViewById(int id) {
		if (super.findViewById(id) == null) {
			return mLayoutContainer.findViewById(id);
		}
		return super.findViewById(id);
	}

	public void setAlert(boolean isAlert) {
		if (isAlert) {
			findViewById(R.id.dialogTvBar).setVisibility(View.GONE);
			mBtnLeft.setVisibility(View.GONE);
		}
	}
}