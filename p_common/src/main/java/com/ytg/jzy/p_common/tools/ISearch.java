package com.ytg.jzy.p_common.tools;

import android.view.View;
import android.widget.TextView;

/**
 * @author YTG
 * @since 2017/4/15
 */
public interface ISearch {

    /**
     * 设置输入框文本
     * @param text 输入框文本
     */
    void setText(String text);

    /**
     * 设置搜索状态栏回调接口
     * @param l 回调接口
     */
    void setOnActionBarSearchListener(OnActionBarSearchListener l);

    /**
     * 设置搜索状态栏返回回调接口
     * @param l 回调接口
     */
    void setOnActionBarNavigationListener(OnActionBarNavigationListener l);

    /**
     * 清空输入框焦点
     */
    void clearInputFocus();

    /**
     * 输入框是否获取焦点
     * @return 是否获取焦点
     */
    boolean hasInputFocus();

    /**
     * 获取焦点
     * @return 是否成功
     */
    boolean requestInputFocus() ;

    /**
     * 返回输入框文本
     * @return 输入框文本
     */
    String getInputText();

    /**
     * 设置输入框是否可用
     * @param enabled 是否可用
     */
    void setInputEnabled(boolean enabled) ;

    /**
     * 设置清除按钮是否可用
     * @param enabled 是否可用
     */
    void setDelEnabled(boolean enabled) ;

    /**
     * 设置输入框文本内容为空
     * @param ignoreNotify 是否需要监听输入框变化回调
     */
    void setTextNil(boolean ignoreNotify);

    /**
     * 设置输入框默认提醒文字
     * @param text 文字
     */
    void setHint(CharSequence text);

    /**
     * 设置输入框输入按键事件监听
     * @param l 监听
     */
    void setOnEditorActionListener(TextView.OnEditorActionListener l) ;

    /**
     * 设置输入框焦点回调接口
     * @param l 回调接口
     */
    void setOnSearchFocusChangeListener(View.OnFocusChangeListener l);

    /**
     * 设置输入框左边图标
     * @param resId 图标资源
     */
    void setDrawableLeft(int resId);

    /**
     * 输入框事件回调接口
     */
    interface OnActionBarSearchListener {

        void onTextChanged(String keyword);

        /**
         * 输入框清空回调接口
         */
        void onSearchClear();
    }

    /**
     * 搜索控件返回上一级事件回调接口
     */
    interface OnActionBarNavigationListener {

        /**
         * 触发在返回上一级按钮点击之后
         */
        void onNavigation();
    }
}
