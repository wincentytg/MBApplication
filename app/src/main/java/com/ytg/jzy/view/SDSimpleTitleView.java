package com.ytg.jzy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ytg.jzy.R;
import com.ytg.jzy.p_common.utils.Utils;


public class SDSimpleTitleView extends FrameLayout implements
		View.OnClickListener {

	public TextView mTxtLeft;
	public TextView mTxtRight;
	public TextView mTxtTitle;
	private TextView mTxtTitBot;

	private LinearLayout mLinLeft = null;
	private LinearLayout mLinRight = null;

	public ImageView mImgLeft = null;
	public ImageView mImgRight = null;

	private OnLeftButtonClickListener mOnLeftButtonClickListener;
	private OnRightButtonClickListener mOnRightButtonClickListener;

	public void setLeftButton(String text, int imageId,
			OnLeftButtonClickListener listener, Integer backgroundImgId) {
		mLinLeft.setVisibility(View.VISIBLE);
		mTxtLeft.setVisibility(View.VISIBLE);
		mImgLeft.setVisibility(View.VISIBLE);
		mTxtLeft.setText(text);
		mImgLeft.setImageResource(imageId);
		mOnLeftButtonClickListener = listener;
		if (backgroundImgId != null) {
			mLinLeft.setBackgroundResource(backgroundImgId);
		}
	}

	public void setLeftButtonText(String text,
			OnLeftButtonClickListener listener, Integer textBackgroundImgId,
			Integer backgroundImgId) {
		mLinLeft.setVisibility(View.VISIBLE);
		mTxtLeft.setVisibility(View.VISIBLE);
		mImgLeft.setVisibility(View.GONE);
		mTxtLeft.setText(text);
		mOnLeftButtonClickListener = listener;
		if (textBackgroundImgId != null) {
			mTxtLeft.setBackgroundResource(textBackgroundImgId);
		}
		if (backgroundImgId != null) {
			mLinLeft.setBackgroundResource(backgroundImgId);
		}
	}

	/**
	 * 
	 * @param imageId
	 *        
	 * @param listener
	 * @param backgroundImgId
	 */
	public void setLeftButtonImage(int imageId,
			OnLeftButtonClickListener listener, Integer backgroundImgId) {
		mLinLeft.setVisibility(View.VISIBLE);
		mTxtLeft.setVisibility(View.GONE);
		mImgLeft.setVisibility(View.VISIBLE);
		if (imageId == -1) {
			mImgLeft.setImageResource(R.mipmap.ic_back);
		} else {
			mImgLeft.setImageResource(imageId);
		}
		mOnLeftButtonClickListener = listener;
		if (backgroundImgId != null) {
			mLinLeft.setBackgroundResource(backgroundImgId);
		}
	}

	public void removeLeftButton() {
		mLinLeft.setVisibility(View.GONE);
		mOnLeftButtonClickListener = null;
	}

	public void setRightButton(String text, int imageId,
			OnRightButtonClickListener listener, Integer backgroundImgId) {
		mLinRight.setVisibility(View.VISIBLE);
		mTxtRight.setVisibility(View.VISIBLE);
		mImgRight.setVisibility(View.VISIBLE);
		mTxtRight.setText(text);
		mImgRight.setImageResource(imageId);
		mOnRightButtonClickListener = listener;
		if (backgroundImgId != null) {
			mLinRight.setBackgroundResource(backgroundImgId);
		}
	}

	public void setRightButtonText(String text,
			OnRightButtonClickListener listener, Integer textBackgroundImgId,
			Integer backgroundImgId) {
		mLinRight.setVisibility(View.VISIBLE);
		mTxtRight.setVisibility(View.VISIBLE);
		mImgRight.setVisibility(View.GONE);
		mTxtRight.setText(text);
		mOnRightButtonClickListener = listener;
		if (textBackgroundImgId != null) {
			mTxtRight.setBackgroundResource(textBackgroundImgId);
		}
		if (backgroundImgId != null) {
			mLinRight.setBackgroundResource(backgroundImgId);
		}
	}

	public void setRightButtonImage(int imageId,
			OnRightButtonClickListener listener, Integer backgroundImgId) {
		mLinRight.setVisibility(View.VISIBLE);
		mTxtRight.setVisibility(View.GONE);
		mImgRight.setVisibility(View.VISIBLE);
		mImgRight.setImageResource(imageId);
		mOnRightButtonClickListener = listener;
		if (backgroundImgId != null) {
			mLinRight.setBackgroundResource(backgroundImgId);
		}
	}
	public void setRightLayout(View v,OnClickListener onClickListener){
		mLinRight.setVisibility(View.VISIBLE);
		mLinRight.removeAllViews();
		mLinRight.addView(v);
		mLinRight.setOnClickListener(onClickListener);
	}
	public void removeRightButton() {
		mLinRight.setVisibility(View.GONE);
		mOnRightButtonClickListener = null;
	}

	public SDSimpleTitleView(Context context) {
		this(context, null);
	}

	public SDSimpleTitleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SDSimpleTitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_simple_title, this, true);

		mLinLeft = (LinearLayout) findViewById(R.id.view_simple_title_lin_left);
		mLinRight = (LinearLayout) findViewById(R.id.view_simple_title_lin_right);

		mImgLeft = (ImageView) findViewById(R.id.view_simple_title_img_left);
		mImgRight = (ImageView) findViewById(R.id.view_simple_title_img_right);

		mTxtLeft = (TextView) findViewById(R.id.view_simple_title_txt_left);
		mTxtRight = (TextView) findViewById(R.id.view_simple_title_txt_right);

		mTxtTitle = (TextView) findViewById(R.id.view_simple_title_txt_title);

		mTxtTitBot = (TextView) findViewById(R.id.view_simple_title_txt_title_bottom);

		mLinLeft.setOnClickListener(this);
		mLinRight.setOnClickListener(this);

		mLinLeft.setVisibility(View.GONE);
		mLinRight.setVisibility(View.GONE);
		mTxtTitle.setVisibility(View.GONE);
		mTxtTitle.setMaxWidth(Utils.dip2px(context, 200));
		mTxtTitle.setSingleLine();
	}

	public void setTitle(String text) {
		mTxtTitle.setVisibility(View.VISIBLE);
		mTxtTitle.setText(text);
	}

	public void setTitle(int stringID) {
		mTxtTitle.setVisibility(View.VISIBLE);
		mTxtTitle.setText(stringID);
	}

	public void setTitleBottom(String text) {
		mTxtTitBot.setVisibility(View.VISIBLE);
		mTxtTitBot.setText(text);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_simple_title_lin_left:
			if (mOnLeftButtonClickListener != null)
				mOnLeftButtonClickListener.onLeftBtnClick(v);
			break;
		case R.id.view_simple_title_lin_right:
			if (mOnRightButtonClickListener != null)
				mOnRightButtonClickListener.onRightBtnClick(v);
			break;
		}
	}

	public interface OnLeftButtonClickListener {
		public void onLeftBtnClick(View button);
	}

	public interface OnRightButtonClickListener {
		public void onRightBtnClick(View button);
	}

}
