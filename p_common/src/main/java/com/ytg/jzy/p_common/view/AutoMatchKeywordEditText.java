package com.ytg.jzy.p_common.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.widget.EditText;

import com.ytg.jzy.p_common.utils.LogUtil;


/**
 * @author YTG
 * @since 2017/4/15
 */
@SuppressLint("AppCompatCustomView")
public class AutoMatchKeywordEditText extends EditText {

    private static final String TAG = "YTG.AutoMatchKeywordEditText";

    /**绘制回调监听*/
    public OnEditTextDrawListener mOnDrawListener;
    private int selectionStart;
    private int selectionEnd;

    public AutoMatchKeywordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoMatchKeywordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        selectionStart = this.getSelectionStart();
        selectionEnd = this.getSelectionEnd();
    }

    @Override
    public void extendSelection(int index) {
        LogUtil.d(TAG, "extendSelection");
        super.extendSelection(index);
        this.selectionStart = this.getSelectionStart();
        this.selectionEnd = this.getSelectionEnd();
    }

    @Override
    public boolean moveCursorToVisibleOffset() {
        LogUtil.d(TAG,  "moveCursorToVisibleOffset");
        return super.moveCursorToVisibleOffset();
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        LogUtil.d(TAG,  "onDragEvent");
        return super.onDragEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        if(this.selectionStart != selectionStart || this.selectionEnd != selectionEnd) {
            this.selectionStart = selectionStart;
            this.selectionEnd = selectionEnd;
            if(mOnDrawListener != null) {
                mOnDrawListener.onDraw(this, getSelectionStart(), getSelectionEnd());
            }
        }
    }

    @Override
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        LogUtil.d(TAG , "performAccessibilityAction");
        return super.performAccessibilityAction(action, arguments);
    }

    @Override
    public void setSelection(int index) {
        LogUtil.d(TAG, "setSelection");
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        super.setSelection(selectionStart);
        this.selectionStart = getSelectionStart();
        this.selectionEnd = getSelectionEnd();
        if((selectionStart != getSelectionStart() || selectionEnd != getSelectionEnd()) && mOnDrawListener != null) {
            mOnDrawListener.onDraw(this, getSelectionStart(), getSelectionEnd());
        }
    }

    @Override
    public void setSelection(int start, int stop) {
        LogUtil.d(TAG, "setSelection.");
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        super.setSelection(start, stop);
        this.selectionStart = getSelectionStart();
        this.selectionEnd = getSelectionEnd();
        if((selectionStart != getSelectionStart() || selectionEnd != getSelectionEnd()) && mOnDrawListener != null) {
            mOnDrawListener.onDraw(this, getSelectionStart(), getSelectionEnd());
        }

    }

    /**
     * 绘制回调监听
     */
    public interface OnEditTextDrawListener {
        void onDraw(EditText editText, int start, int stop);
    }
}
