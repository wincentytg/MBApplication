package com.ytg.jzy.p_common.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.ytg.jzy.p_common.R;
import com.ytg.jzy.p_common.utils.LogUtil;


/**
 * @author YTG
 * @since 2017/4/15
 */
public class SearchViewHelper {

    private final String TAG;
    /**
     * 搜索菜单按钮
     */
    MenuItem mMenuItem;
    /**
     * 动态控制输入框显示与隐藏
     */
    boolean dynamicExpand;
    /**
     * 搜索菜单是否展开
     */
    public boolean searchViewExpand;
    /**
     * 搜索菜单是否正在展开
     */
    private boolean triggerExpand;
    /**
     * 搜索菜单是否可以展开
     */
    private boolean canExpand;
    /**
     * 是否展开搜索菜单并且打开输入法
     */
    boolean showSoftInput;
    /**
     * 搜索状态栏事件回调接口
     */
    public OnSearchViewListener mOnSearchViewListener;
    /**
     * 搜索功能实现接口
     */
    public ISearch mSearchImpl;
    /**
     * 搜索框左边图标
     */
    private int inputDrawableLeft;
    private Handler mHandler;
    /**
     * 搜索菜单关闭回调接口
     */
    OnActionCollapseListener mOnActionCollapseListener;

    /**
     * 默认显示未展开模式
     */
    public SearchViewHelper() {
        this(true);
    }

    /**
     * 根据提供的显示模式初始化搜索栏
     *
     * @param normal 是否显示未展开模式
     */
    public SearchViewHelper(boolean normal) {
        dynamicExpand = false;
        searchViewExpand = false;
        triggerExpand = false;
        canExpand = true;
        showSoftInput = true;
        mMenuItem = null;
        mSearchImpl = null;
        inputDrawableLeft = 0;
        dynamicExpand = normal;
        mHandler = new Handler();
        TAG = "YTG.SearchViewHelper" + String.valueOf(System.currentTimeMillis());
    }

    /**
     * 设置搜索栏是否展开模式
     *
     * @param expand 是否展开
     */
    public void setSearchExpand(boolean expand) {
        this.dynamicExpand = expand;
    }

    /**
     * 内部执行搜索框展开操作
     *
     * @param activity 当前的Activity
     * @param menu     搜索框菜单
     */
    private void tryExpandActionView(final Activity activity, Menu menu) {
        if (canExpand && (searchViewExpand || triggerExpand)) {
            triggerExpand = false;
            if (menu != null) {
                for (int i = 0; i < menu.size(); ++i) {
                    MenuItem menuItem = menu.getItem(i);
                    if (menuItem.getItemId() != R.id.ytx_action_bar_search) {
                        menuItem.setVisible(false);
                    }
                }
            }

            mHandler.postDelayed(new Runnable() {
                public final void run() {
                    if (mMenuItem == null) {
                        LogUtil.w(TAG, "on post expand search menu, but item is null");
                    } else {
                        LogUtil.i(TAG, "try to expand action view, searchViewExpand " + searchViewExpand);
                        if (dynamicExpand) {
                            if (!searchViewExpand) {
                                MenuItemCompat.expandActionView(mMenuItem);
                            }
                        } else if (mOnActionCollapseListener != null) {
                            mOnActionCollapseListener.expandActionView();
                        }

                        final View actionView = MenuItemCompat.getActionView(mMenuItem);
                        if (actionView != null && searchViewExpand) {
                            actionView.findViewById(R.id.ytx_search_view).requestFocus();
                            if (showSoftInput) {
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        InputMethodManager service = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        if (service != null) {
                                            service.showSoftInput(actionView.findViewById(R.id.ytx_search_view), 0);
                                        }
                                    }
                                }, 128L);
                            }
                        }

                    }

                }
            }, 128L);
        }
    }

    /**
     * 菜单创建准备工作
     *
     * @param menu 菜单
     */
    public void onPrepareOptionsMenu(Activity activity, Menu menu) {
        LogUtil.v(TAG, "on prepare options menu, searchViewExpand " + searchViewExpand + ", triggerExpand " + triggerExpand + ", canExpand " + canExpand);
        if (activity == null) {
            LogUtil.w(TAG, "on handle status fail, activity is null");
        } else {
            mMenuItem = menu.findItem(R.id.ytx_action_bar_search);
            if (mMenuItem == null) {
                LogUtil.w(TAG, "can not find search menu, error");
            } else {
                mMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
                tryExpandActionView(activity, menu);
            }
        }
    }

    /**
     * 初始化搜索菜单
     *
     * @param activity 承载Activity
     * @param menu     菜单
     */
    public void onCreateOptionsMenu(final FragmentActivity activity, Menu menu) {
        LogUtil.v(TAG, "on create options menu");
        if (activity == null) {
            LogUtil.w(TAG, "on add search menu, activity is null");
        } else {
            if (mSearchImpl == null) {
                mSearchImpl = new ActionBarSearchView(activity);
            }

            mSearchImpl.setOnActionBarSearchListener(new ISearch.OnActionBarSearchListener() {
                @Override
                public void onTextChanged(String keyword) {
                    if (!searchViewExpand) {
                        LogUtil.v(TAG, "onSearchTextChange " + keyword + ", but not in searching");
                    } else {
                        if (mOnSearchViewListener != null) {
                            mOnSearchViewListener.onSearchTextChange(keyword);
                        }

                    }
                }

                @Override
                public void onSearchClear() {
                    if (mOnSearchViewListener != null) {
                        mOnSearchViewListener.onSearchClear();
                    }
                }
            });
            mSearchImpl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    return EditorInfo.IME_ACTION_SEARCH == actionId
                            && mOnSearchViewListener != null
                            && mOnSearchViewListener.startSearch(getInputText());
                }
            });
            if (inputDrawableLeft != 0) {
                mSearchImpl.setDrawableLeft(inputDrawableLeft);
            }
            mMenuItem = menu.add(0, R.id.ytx_action_bar_search, 0, "搜索");
            mMenuItem.setEnabled(canExpand);
            mMenuItem.setIcon(R.drawable.ic_search_24);

            MenuItemCompat.setActionView(mMenuItem, (View) mSearchImpl);
            if (dynamicExpand) {
                MenuItemCompat.setShowAsAction(mMenuItem, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            } else {
                MenuItemCompat.setShowAsAction(mMenuItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
            }
            if (dynamicExpand) {
                MenuItemCompat.setOnActionExpandListener(mMenuItem, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        doNewExpand(activity, false);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        doNewCollapse(activity, false);
                        return true;
                    }
                });
            } else {
                mOnActionCollapseListener = new OnActionCollapseListener() {
                    public final void expandActionView() {
                        doNewExpand(activity, true);
                    }

                    public final void collapseActionView() {
                        doNewCollapse(activity, true);
                    }
                };
            }

            mSearchImpl.setOnActionBarNavigationListener(new ISearch.OnActionBarNavigationListener() {
                @Override
                public void onNavigation() {
                    if (dynamicExpand) {
                        if (mMenuItem != null) {
                            MenuItemCompat.collapseActionView(mMenuItem);
                        }
                    } else if (mOnActionCollapseListener != null) {
                        mOnActionCollapseListener.collapseActionView();
                    }
                }
            });
        }
    }

    /**
     * 关闭搜索框打开模式
     *
     * @param activity   当前的Activity
     * @param invalidate 是否刷新状态
     */
    public void doNewCollapse(final FragmentActivity activity, final boolean invalidate) {
        LogUtil.d(TAG, "doNewCollapse, searchViewExpand " + searchViewExpand);
        if (searchViewExpand) {
            searchViewExpand = false;
            if (mSearchImpl != null) {
                mSearchImpl.setTextNil(false);
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (activity != null && !activity.isFinishing()) {
                        if (invalidate) {
                            activity.invalidateOptionsMenu();
                        }
                    } else {
                        LogUtil.w(TAG, "want to collapse search view, but activity status error");
                    }
                }
            });
            if (mOnSearchViewListener != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mOnSearchViewListener.startCollapseView();
                    }
                });
            }
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mMenuItem == null) {
                    LogUtil.w(TAG, "want to collapse search view, but search menu item is null");
                } else {
                    if (activity != null && !activity.isFinishing()) {
                        InputMethodManager service = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (service != null) {
                            View currentFocus = activity.getCurrentFocus();
                            if (currentFocus != null) {
                                IBinder binder = currentFocus.getWindowToken();
                                if (binder != null) {
                                    service.hideSoftInputFromWindow(binder, 0);
                                }
                            }
                        }
                    }

                    View actionView = MenuItemCompat.getActionView(mMenuItem);
                    if (actionView != null) {
                        actionView.findViewById(R.id.ytx_search_view).clearFocus();
                    }

                }
            }
        });
    }

    /**
     * 展开搜索框
     *
     * @param activity   当前的Activity
     * @param invalidate 是否刷新
     */
    public void doNewExpand(final FragmentActivity activity, final boolean invalidate) {
        LogUtil.d(TAG, "doNewExpand, searchViewExpand " + searchViewExpand);
        if (!searchViewExpand) {
            searchViewExpand = true;
            tryExpandActionView(activity, null);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (activity != null && !activity.isFinishing()) {
                        if (invalidate) {
                            activity.invalidateOptionsMenu();
                        }
                    } else {
                        LogUtil.w(TAG, "want to expand search view, but activity status error");
                    }
                }
            });
            if (mOnSearchViewListener != null) {
                mOnSearchViewListener.startExpandSearchView();
            }
        }
    }

    /**
     * 输入框是否获取焦点
     *
     * @return 是否获取焦点
     */
    public boolean hasFocus() {
        return (mSearchImpl != null) && mSearchImpl.hasInputFocus();
    }

    /**
     * 获取焦点
     *
     * @return 是否成功
     */
    public final boolean requestInputFocus() {
        return (mSearchImpl != null) && mSearchImpl.requestInputFocus();
    }

    /**
     * 关闭搜索框为默认状态
     */
    public void doCollapseActionView() {
        LogUtil.d(TAG, "do collapse");
        if (searchViewExpand && mMenuItem != null) {
            if (dynamicExpand) {
                MenuItemCompat.collapseActionView(mMenuItem);
                return;
            }

            if (mOnActionCollapseListener != null) {
                mOnActionCollapseListener.collapseActionView();
            }
        }

    }

    /**
     * 清除搜索框焦点
     */
    public final void clearFocus() {
        if (mSearchImpl != null) {
            mSearchImpl.clearInputFocus();
        }
    }

    /**
     * 开始展开搜索框
     *
     * @param canShowSoftInput 是否先是输入法
     */
    public void doExpandActionView(boolean canShowSoftInput) {
        LogUtil.d(TAG, "do expand, expanded[" + searchViewExpand + "], search menu item null[" + (mMenuItem == null) + "]");
        if (searchViewExpand) {
            return;
        }

        if (!canExpand) {
            LogUtil.w(TAG, "can not expand now");
        } else {
            showSoftInput = canShowSoftInput;
            if (mMenuItem != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mMenuItem == null) {
                            LogUtil.w(TAG, "post do expand search menu, but search menu item is null");
                        } else if (dynamicExpand) {
                            MenuItemCompat.expandActionView(mMenuItem);
                        } else {
                            if (mOnActionCollapseListener != null) {
                                mOnActionCollapseListener.expandActionView();
                            }

                        }
                    }
                });
            } else {
                triggerExpand = true;
            }
        }

    }

    /**
     * 获取搜索框输入文字
     *
     * @return 搜索框输入文字
     */
    public String getInputText() {
        return (mSearchImpl != null) ? mSearchImpl.getInputText() : "";
    }

    /**
     * 处理按键按下事件
     *
     * @param keyCode 按键编号
     * @param event   案件类型
     * @return 是否处理
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.v(TAG, "on key down, key code " + keyCode + ", expand " + searchViewExpand);
        if (KeyEvent.KEYCODE_BACK == keyCode && searchViewExpand) {
            doCollapseActionView();
            return true;
        }
        return false;
    }


    /**
     * 设置输入框默认提示文本
     *
     * @param text 框默认提示文本
     */
    public void setHint(CharSequence text) {
        if (mSearchImpl != null) {
            mSearchImpl.setHint(text);
        }

    }


    /**
     * 搜索状态栏回调接口
     */
    public interface OnActionCollapseListener {

        /**
         * 搜索状态栏展开
         */
        void expandActionView();

        /**
         * 搜索状态栏关闭
         */
        void collapseActionView();
    }

    /**
     * 搜索插件回调接口
     */
    public interface OnSearchViewListener {

        /**
         * 开始关闭搜索状态栏
         */
        void startCollapseView();

        /**
         * 开始做含开搜索状态栏
         */
        void startExpandSearchView();

        /**
         * 搜索状态栏输入文字变化
         *
         * @param keyword 变化后的文本
         */
        void onSearchTextChange(String keyword);

        /**
         * 搜索状态栏文本被清空
         */
        void onSearchClear();

        /**
         * 开始执行搜索操作（输入法搜索按钮）
         *
         * @param keyword 关键字
         * @return 是否处理
         */
        boolean startSearch(String keyword);
    }
}
