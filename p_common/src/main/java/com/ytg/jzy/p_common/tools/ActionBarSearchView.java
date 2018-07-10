package com.ytg.jzy.p_common.tools;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ytg.jzy.p_common.R;
import com.ytg.jzy.p_common.utils.LogUtil;
import com.ytg.jzy.p_common.utils.TextUtil;
import com.ytg.jzy.p_common.view.AutoMatchKeywordEditText;


/**
 * @author YTG
 * @since 2017/4/15
 */
public class ActionBarSearchView extends LinearLayout implements ISearch {

    private static final String TAG = "YTG.ActionBarSearchView";

    private View mActionBackLayout;
    private ActionBarSearchView.ActionBarEditText mActionBarEditText;
    private ImageButton mClearView;
    private OnFocusChangeListener mOnFocusChangeListener;
    /**搜索状态栏回调接口*/
    private OnActionBarSearchListener mOnActionBarSearchListener;
    /**输入框控件返回回调*/
    private OnActionBarNavigationListener mNavigationListener;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            initDelView();
            if(mOnActionBarSearchListener != null) {
                mOnActionBarSearchListener.onTextChanged(s == null ? "" : s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 焦点变化回调接口
     */
    private OnFocusChangeListener mInnerOnFocusChangeListener
            = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            LogUtil.d(TAG ,  "on edittext focus changed, hasFocus " + hasFocus);
            if(mOnFocusChangeListener != null) {
                mOnFocusChangeListener.onFocusChange(v, hasFocus);
            }
        }
    };

    /**
     *
     */
    private OnClickListener mOnInnerClickListener =
            new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTextNil(true);
                    if(mOnActionBarSearchListener != null) {
                        mOnActionBarSearchListener.onSearchClear();
                    }
                }
            };


    /**
     * 搜索输入框状态栏返回
     */
    private OnClickListener mOnBackListener
            = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mNavigationListener != null) {
                mNavigationListener.onNavigation();
            }
        }
    };

    public ActionBarSearchView(Context context) {
        super(context);
        init();
    }

    public ActionBarSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化删除按钮
     */
    private void initDelView() {
        if(mActionBarEditText.getEditableText() != null && !BackwardSupportUtil.isNullOrNil(mActionBarEditText.getEditableText().toString())) {
            setDelImageResource(R.drawable.ic_clear_24, getResources().getDimensionPixelSize(R.dimen.LargestPadding));
        } else {
            setDelImageResource(0, 0);
        }

    }

    /**
     * 设置清空按钮资源
     * @param resId 资源
     * @param width 大小
     */
    private void setDelImageResource(int resId, int width) {
        mClearView.setImageResource(resId);
        mClearView.setBackgroundResource(0);
        mClearView.setContentDescription("了解更多");
        ViewGroup.LayoutParams params = mClearView.getLayoutParams();
        params.width = width;
        mClearView.setLayoutParams(params);
    }

    /**
     * 初始化
     */
    private void init() {
        ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(TextUtil.isLoadBigLayout()?R.layout.ytx_layout_actionbar_search_view_big:R.layout.ytx_layout_actionbar_search_view, this, true);
        mActionBackLayout = findViewById(R.id.ytx_actionbar_back_ll);
        mActionBackLayout.setOnClickListener(mOnBackListener);
        mActionBarEditText = (ActionBarSearchView.ActionBarEditText) findViewById(R.id.ytx_search_view);
        mActionBarEditText.mSearchView = this;
        mActionBarEditText.post(new Runnable() {
            @Override
            public void run() {
               ActionBarSearchView.this.mActionBarEditText.setText("");
            }
        });
        mClearView = (ImageButton) findViewById(R.id.ytx_del);
        mActionBarEditText.addTextChangedListener(mTextWatcher);
        mActionBarEditText.setOnFocusChangeListener(mInnerOnFocusChangeListener);
        mClearView.setOnClickListener(mOnInnerClickListener);
        initDelView();
    }

    /**
     * 设置输入框文本
     * @param text 输入框文本
     */
    @Override
    public void setText(String text) {
        text = BackwardSupportUtil.nullAsNil(text);
        mActionBarEditText.setText(text);
        mActionBarEditText.setSelection(text.length());
    }

    @Override
    public void setOnActionBarSearchListener(OnActionBarSearchListener l) {
        mOnActionBarSearchListener = l;
    }

    @Override
    public void setOnActionBarNavigationListener(OnActionBarNavigationListener l) {
        mNavigationListener = l;
    }

    /**
     * 清空输入框焦点
     */
    @Override
    public void clearInputFocus() {
        mActionBarEditText.clearFocus();
    }

    /**
     * 输入框是否获取焦点
     * @return 是否获取焦点
     */
    @Override
    public boolean hasInputFocus() {
        return (mActionBarEditText != null) && mActionBarEditText.hasFocus();

    }

    /**
     * 获取焦点
     * @return 是否成功
     */
    @Override
    public boolean requestInputFocus() {
        return (mActionBarEditText != null) && mActionBarEditText.requestFocus();
    }

    /**
     * 返回输入框文本
     * @return 输入框文本
     */
    @Override
    public String getInputText() {
        return (mActionBarEditText.getEditableText() != null) ? mActionBarEditText.getEditableText().toString() : "";
    }

    /**
     * 设置输入框是否可用
     * @param enabled 是否可用
     */
    @Override
    public void setInputEnabled(boolean enabled) {
        mActionBarEditText.setEnabled(enabled);
    }

    /**
     * 设置清除按钮是否可用
     * @param enabled 是否可用
     */
    @Override
    public void setDelEnabled(boolean enabled) {
        mClearView.setEnabled(enabled);
    }

    /**
     * 设置输入框文本内容为空
     * @param ignoreNotify 是否需要监听输入框变化回调
     */
    @Override
    public void setTextNil(boolean ignoreNotify) {
        if(!ignoreNotify) {
            mActionBarEditText.removeTextChangedListener(mTextWatcher);
            mActionBarEditText.setText("");
            mActionBarEditText.addTextChangedListener(mTextWatcher);
        } else {
            mActionBarEditText.setText("");
        }

    }

    /**
     * 设置输入框默认提醒文字
     * @param text 文字
     */
    @Override
    public  void setHint(CharSequence text) {
        mActionBarEditText.setHint(text);
    }

    /**
     * 设置输入框输入按键事件监听
     * @param l 监听
     */
    @Override
    public  void setOnEditorActionListener(TextView.OnEditorActionListener l) {
        mActionBarEditText.setOnEditorActionListener(l);
    }

    @Override
    public void setOnSearchFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }

    /**
     * 设置输入框左边图标
     * @param resId 图标资源
     */
    @Override
    public void setDrawableLeft(int resId) {
        if(mActionBarEditText != null) {
            mActionBarEditText.setCompoundDrawables(getResources().getDrawable(resId), null, null, null);
        }
    }

    /**
     * 状态栏输入框
     */
    public static class ActionBarEditText extends AutoMatchKeywordEditText {

        /**标题栏搜索框*/
        ActionBarSearchView mSearchView;

        public ActionBarEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ActionBarEditText(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public boolean onKeyPreIme(int keyCode, KeyEvent event) {
            LogUtil.d(TAG , "on onKeyPreIme");
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                KeyEvent.DispatcherState mDispatcherState;
                if(event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                    LogUtil.v(TAG, "on onKeyPreIme action down");
                    mDispatcherState = this.getKeyDispatcherState();
                    if(mDispatcherState != null) {
                        mDispatcherState.startTracking(event, this);
                    }
                    return true;
                }
                if(event.getAction() == KeyEvent.ACTION_UP) {
                    LogUtil.v(TAG, "on onKeyPreIme action up");
                    mDispatcherState = this.getKeyDispatcherState();
                    if(mDispatcherState != null) {
                        mDispatcherState.handleUpEvent(event);
                    }

                    if(event.isTracking() && !event.isCanceled()) {
                        LogUtil.v(TAG, "on onKeyPreIme action up is tracking");
                        mSearchView.clearFocus();
                        InputMethodManager inputMgr = (InputMethodManager)this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(inputMgr != null) {
                            inputMgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
                        }
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(keyCode, event);
        }
    }

}
