package com.ytg.jzy.p_common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;

import com.ytg.jzy.p_common.R;

public class SwitchButton extends RelativeLayout {
    CheckedTextView mCheckedTextView;

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCheckedTextView = new CheckedTextView(context);
        addView(mCheckedTextView);
        initView();
    }

    void initView() {
        mCheckedTextView.setCheckMarkDrawable(getContext().getResources().getDrawable(R.drawable.switch_style));
        mCheckedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    public void toggle() {
        mCheckedTextView.toggle();
    }

    /**
     * 是否处于选中状态
     *
     * @return
     */
    public boolean isChecked() {
        return mCheckedTextView.isChecked();
    }

    /**
     * 设置状态
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        mCheckedTextView.setChecked(checked);
    }
}
