package com.ytg.jzy.p_common.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ytg.jzy.p_common.R;
import com.ytg.jzy.p_common.YTGApplicationContext;
import com.ytg.jzy.p_common.menu.ActionMenuItem;
import com.ytg.jzy.p_common.utils.LogUtil;
import com.ytg.jzy.p_common.utils.TextUtil;
import com.ytg.jzy.p_common.view.SystemBarTintManager;

import java.util.LinkedList;

/**
 * @author YTG
 * @since 2017/3/8
 */
public abstract class ActivityBase {

    private static final String TAG = "YTG.RXActivityController";

    /**
     * 应用状态栏
     */
    private ActionBar mActionBar;
    /**
     * 新的标题栏
     */
    private Toolbar mToolBar;
    /**
     * 当前页面承载Activity
     */
    protected AppCompatActivity mActionBarActivity;
    /**
     * 当前页面跟布局
     */
    protected View mContentView;
    /**
     * 页面布局加载器
     */
    private LayoutInflater mLayoutInflater;
    /**
     * 设置到Activity布局
     */
    protected View mBaseLayoutView;
    /**
     * 背景
     */
    private View mTransLayerView;
    private FrameLayout mContentFrameLayout;
    /**
     * 搜索是否可用
     */
    private boolean mSearchEnabled;
    /**
     * 搜索帮助接口
     */
    private SearchViewHelper mSearchViewHelper;
    private int mSmallPadding;
    public View mActionBarLayout;
    SystemBarTintManager mSystemBarTintManager;
    /**
     * 菜单按钮缓存
     */
    private LinkedList<ActionMenuItem> mActionMenuItems = new LinkedList<ActionMenuItem>();
    /**
     * 返回菜单
     */
    private ActionMenuItem mDisplayHomeMenu = new ActionMenuItem();

    /**
     * 左侧第二按钮
     */
    private TextView mSecondLeftBtnTv;

    /**
     * 左侧第一按钮
     */
    private ImageView mFirstLeftBtnIv;

    /**
     * 溢出菜单
     */
    private ActionMenuItem mOverFlowAction;

    /**
     * 溢出菜单
     */
    private MenuItem mOverFlowMenuItem;
    /**
     * 标题
     */
    private CharSequence mTitleText;

    /**
     * 一级标题
     */
    private TextView mTitleView;
    /**
     * 网络链接断开时候展示
     */
    private TextView mTvNetworkInfo;
    /**
     * 二级标题
     */
    private TextView mSubTitleView;
    /**
     * 标题右边显示数字控件
     */
    private TextView mTitleSummaryView;
    /**
     * 状态栏阴影
     */
    private View mActionBarShadow;

    public void onStart() {

    }


    /**
     * 按钮单击事件处理
     */
    final private class ActionMenuOnClickListener implements View.OnClickListener {

        final private MenuItem mMenuItem;
        final private ActionMenuItem mActionMenuItem;

        /**
         * 按钮事件构造方法
         *
         * @param menuItem       按钮
         * @param actionMenuItem 按钮时间类型
         */
        ActionMenuOnClickListener(MenuItem menuItem, ActionMenuItem actionMenuItem) {
            mMenuItem = menuItem;
            mActionMenuItem = actionMenuItem;
        }

        @Override
        public void onClick(View v) {
            if (mActionMenuItem != null && mActionMenuItem.getItemClickListener() != null) {
                mActionMenuItem.getItemClickListener().onMenuItemClick(mMenuItem);
            }
        }

    }

    /**
     * 按钮长按事件处理
     */
    final private class ActionMenuOnLongClickListener implements View.OnLongClickListener {

        final private ActionMenuItem mActionMenuItem;

        ActionMenuOnLongClickListener(ActionMenuItem actionMenuItem) {
            mActionMenuItem = actionMenuItem;
        }

        @Override
        public boolean onLongClick(View v) {
            if (mActionMenuItem != null && mActionMenuItem.getLongClickListener() != null) {
                mActionMenuItem.getLongClickListener().onLongClick(v);
            }
            return false;
        }


    }

    /**
     * 当前Activity初始化入口
     *
     * @param ctx      上下文
     * @param activity 承载Activity
     */
    public void init(Context ctx, AppCompatActivity activity) {
        mActionBarActivity = activity;
        mSystemBarTintManager = new SystemBarTintManager(mActionBarActivity);
        onInit();
        int layoutId = getLayoutId();
        mLayoutInflater = LayoutInflater.from(mActionBarActivity);
        mBaseLayoutView = mLayoutInflater.inflate(R.layout.ytx_activity, null);
        mTransLayerView = mBaseLayoutView.findViewById(R.id.ytx_trans_layer);
        mContentFrameLayout = (FrameLayout) mBaseLayoutView.findViewById(R.id.ytx_content_fl);
        ViewGroup mRootView = (ViewGroup) mBaseLayoutView.findViewById(R.id.ytx_root_view);
        if (hasActionBar()) {
            // 当前是否需要状态栏
            mActionBarLayout = mLayoutInflater.inflate(R.layout.ytx_recycler_view_toolbar, null);
            Toolbar toolbar = (Toolbar) mActionBarLayout.findViewById(R.id.toolbar);
            mTitleView = (TextView) mActionBarLayout.findViewById(R.id.ytx_action_title);
            mTvNetworkInfo = (TextView) mActionBarLayout.findViewById(R.id.mTvNetworkInfo);
            mSubTitleView = (TextView) mActionBarLayout.findViewById(R.id.ytx_action_sub_title);
            mTitleSummaryView = (TextView) mActionBarLayout.findViewById(R.id.ytx_action_title_count);
            mActionBarShadow = mActionBarLayout.findViewById(R.id.toolbar_shadow);
            if (mActionBarActivity != null) {
                mRootView.addView(mActionBarLayout,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
            }
            if (toolbar != null) {
                activity.setSupportActionBar(toolbar);
                mToolBar = toolbar;
                if (buildActionBarPadding()) {
                    // 设置状态栏和顶部有一个padding间距
                    mRootView.setPadding(mRootView.getPaddingLeft(), mSystemBarTintManager.getConfig().getStatusBarHeight(), mRootView.getPaddingRight(), mRootView.getPaddingBottom());
                }
                mActionBar = activity.getSupportActionBar();
               /*if (mActionBar != null) {
                    mActionBar.setHomeAsUpIndicator(mActionBarActivity.getResources().getDrawable(R.drawable.ic_arrow_back_24));
                }*/
            }
            mFirstLeftBtnIv = (ImageView) mActionBarLayout.findViewById(R.id.ytx_action_first);
            mSecondLeftBtnTv = (TextView) mActionBarLayout.findViewById(R.id.ytx_action_second);
        }

        if (layoutId != -1) {
            mContentView = getContentLayoutView();
            if (mContentView == null) {
                mContentView = mLayoutInflater.inflate(layoutId, null);
            }

            mRootView.addView(mContentView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            mSmallPadding = mActionBarActivity.getResources().getDimensionPixelSize(R.dimen.SmallPadding);
        }
        onPreDealWithContentViewAttach(mBaseLayoutView);
    }

    public void setNetworkInfoStatus(boolean isConnected) {
//        if (isConnected) {
//            mTvNetworkInfo.setVisibility(View.GONE);
//        } else {
//            mTvNetworkInfo.setVisibility(View.VISIBLE);
//        }
        if(mActionMenuItems.size()>0){
            ActionMenuItem item = mActionMenuItems.get(mActionMenuItems.size()-1);
            if(item.getTitle().toString().contains("在线")||item.getTitle().toString().contains("离线")){
                if(isConnected){
                    item.setResId(R.mipmap.ic_online); item.setTitle("在线");
                }else{
                    item.setResId(R.mipmap.ic_offline); item.setTitle("离线");
                }
            }
            invalidateActionMenu();
        }


    }


    /**
     * 返回状态栏高度
     *
     * @return 状态栏高度
     */
    public int getStatusBarHeight() {
        return mSystemBarTintManager.getConfig().getStatusBarHeight();
    }

    /**
     * 设置状态栏右边显示按钮
     *
     * @param menuId            按钮菜单ID
     * @param title             按钮显示内容
     * @param resId             按钮背景
     * @param itemClickListener 按钮事件回调监听
     */
    public final void setActionMenuItem(int menuId, int title, int resId, MenuItem.OnMenuItemClickListener itemClickListener) {
        this.setActionMenuItem(menuId, resId, mActionBarActivity.getString(title), itemClickListener, null, ActionMenuItem.ActionType.TEXT);
    }


    /**
     * 根据提供的图片添加一个按钮菜单
     *
     * @param menuId            按钮菜单ID
     * @param resId             按钮资源图片
     * @param itemClickListener 按钮事件回调监听
     */
    public final void setActionMenuItem(int menuId, int resId, MenuItem.OnMenuItemClickListener itemClickListener) {
        this.setActionMenuItem(menuId, resId, "", itemClickListener, null, ActionMenuItem.ActionType.TEXT);
    }

    /**
     * 根据提供的图片添加一个按钮菜单
     *
     * @param menuId            按钮菜单ID
     * @param resId             按钮资源图片
     * @param itemClickListener 按钮事件回调监听
     */
    public final void setActionMenuItem(int menuId, int resId, String title, MenuItem.OnMenuItemClickListener itemClickListener) {
        this.setActionMenuItem(menuId, resId, title, itemClickListener, null, ActionMenuItem.ActionType.TEXT);
    }

    /**
     * 设置状态栏右边显示按钮
     *
     * @param title             标题文本
     * @param resId             资源图片
     * @param itemClickListener 按钮事件回调监听
     * @param longClickListener 按钮长按事件回调
     */
    public final void setActionMenuItem(int title, int resId, MenuItem.OnMenuItemClickListener itemClickListener, View.OnLongClickListener longClickListener) {
        this.setActionMenuItem(0, resId, mActionBarActivity.getString(title), itemClickListener, longClickListener, ActionMenuItem.ActionType.TEXT);
    }


    /**
     * 设置文字显示actionBar
     *
     * @param menuId            按钮菜单ID
     * @param title             按钮显示文本内容
     * @param itemClickListener 按钮事件回调监听
     */
    public final void setActionMenuItem(int menuId, String title, MenuItem.OnMenuItemClickListener itemClickListener) {
        this.setActionMenuItem(menuId, 0, title, itemClickListener, null, ActionMenuItem.ActionType.TEXT);
    }

    /**
     * 设置状态栏右边显示按钮
     *
     * @param menuId            按钮菜单ID
     * @param title             按钮显示文本内容
     * @param itemClickListener 按钮事件回调监听
     * @param actionType        当前按钮显示风格
     */
    public final void setActionMenuItem(int menuId, String title, MenuItem.OnMenuItemClickListener itemClickListener, ActionMenuItem.ActionType actionType) {
        this.setActionMenuItem(menuId, 0, title, itemClickListener, null, actionType);
    }


    /**
     * 根据提供的资源文件构建一个菜单按钮
     *
     * @param menuId            按钮编号
     * @param resId             按钮资源图片（可以试按钮图片、或者是按钮背景）
     * @param title             按钮显示文本
     * @param itemClickListener 按钮点击事件通知
     * @param longClickListener 按钮长按事件通知
     */
    private void setActionMenuItem(int menuId, int resId, String title, MenuItem.OnMenuItemClickListener itemClickListener, View.OnLongClickListener longClickListener, ActionMenuItem.ActionType actionType) {
        ActionMenuItem actionMenuItem = new ActionMenuItem();
        actionMenuItem.setMenuId(menuId);
        actionMenuItem.setResId(resId);
        actionMenuItem.setTitle(title);
        actionMenuItem.setItemClickListener(itemClickListener);
        actionMenuItem.setLongClickListener(longClickListener);
        actionMenuItem.setActionType(actionType);
        if (actionMenuItem.getResId() == R.drawable.ytx_title_btn_menu) {
            actionMenuItem.setTitle(mActionBarActivity.getString(R.string.app_more));
        }

        for (int i = 0; i < mActionMenuItems.size(); i++) {
            ActionMenuItem _tempItem = mActionMenuItems.get(i);
            if (_tempItem.getMenuId() != menuId) {
                continue;
            }
            LogUtil.e(TAG, "match menu, id " + menuId + ", remove it");
            mActionMenuItems.remove(i);
        }
        mActionMenuItems.add(actionMenuItem);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                invalidateActionMenu();
            }
        }, 200L);
    }

    /**
     * 创建菜单
     *
     * @param menu 菜单
     * @return 结果
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (mActionBar == null || mActionMenuItems.size() == 0) {

            return false;
        }

        mOverFlowAction = null;
        mOverFlowMenuItem = null;

        int height = mActionBar.getHeight();
        int minimumHeight = height;
        if (height == 0) {
            DisplayMetrics displayMetrics = mActionBarActivity.getResources().getDisplayMetrics();
            if (displayMetrics.widthPixels <= displayMetrics.heightPixels) {
                minimumHeight = mActionBarActivity.getResources().getDimensionPixelSize(R.dimen.DefaultActionbarHeightPort);
            } else {
                minimumHeight = mActionBarActivity.getResources().getDimensionPixelSize(R.dimen.DefaultActionbarHeightLand);
            }
        }

        for (final ActionMenuItem actionMenuItem : mActionMenuItems) {
            if (actionMenuItem.getMenuId() == android.R.id.home) {
                LogUtil.v(TAG, "match back option menu, continue ");
                continue;
            } else if (actionMenuItem.getMenuId() == R.id.ytx_action_bar_search) {
                boolean enabled = this.mSearchEnabled;
                boolean isNil = mSearchViewHelper == null;
                LogUtil.v(TAG, "match search menu, enable search view[" + enabled + "], search view helper is null[ " + isNil + " ]");
                if (enabled && !isNil) {
                    mSearchViewHelper.onCreateOptionsMenu(mActionBarActivity, menu);
                }
            } else {
                final MenuItem menuItem = menu.add(0, actionMenuItem.getMenuId(), 0, actionMenuItem.getTitle());
                String className = this.getClass().getName();
                if (menuItem == null) {
                    LogUtil.w("YTG.MenuItemUtil", "fixTitleCondensed fail, item is null");
                } else if (menuItem.getTitleCondensed() == null) {
                    LogUtil.w("YTG.MenuItemUtil", className + " title condensed is null, fix it");
                    menuItem.setTitleCondensed("");
                } else if (!(menuItem.getTitleCondensed() instanceof String)) {
                    LogUtil.w("YTG.MenuItemUtil", className + " title condensed is not String type, cur type[ "
                            + menuItem.getTitleCondensed().getClass().getName() + "], cur value[" + menuItem.getTitleCondensed() + "], fix it");
                    menuItem.setTitleCondensed(menuItem.getTitleCondensed().toString());
                }

                ActionMenuOnClickListener mClickListener = new ActionMenuOnClickListener(menuItem, actionMenuItem);
                ActionMenuOnLongClickListener mLongClickListener = new ActionMenuOnLongClickListener(actionMenuItem);

                if (actionMenuItem.getResId() != 0 && actionMenuItem.getTitle().equals("")) {
                    ImageButton button;
                    if (actionMenuItem.getLongClickListener() != null) {
                        int minWidth = BackwardSupportUtil.fromDPToPix(mActionBarActivity, 56);
                        if (actionMenuItem.getLongClickCustomView() == null) {
                            button = new ImageButton(mActionBarActivity);
                            button.setLayoutParams(new ViewGroup.MarginLayoutParams(minWidth, minimumHeight));
                            button.setBackgroundResource(R.drawable.ytx_actionbar_menu_selector);
                            button.setMinimumHeight(minimumHeight);
                            button.setMinimumWidth(minWidth);
                            button.setImageResource(actionMenuItem.getResId());

                            MenuItemCompat.setActionView(menuItem, button);
                            button.getLayoutParams().width = minWidth;
                            button.getLayoutParams().height = minimumHeight;
                            button.setOnClickListener(mClickListener);
                            button.setOnLongClickListener(mLongClickListener);
                            button.setEnabled(actionMenuItem.isEnabled());
                            button.setContentDescription(actionMenuItem.getTitle());

                            actionMenuItem.setLongClickCustomView(button);
                        }
                        menuItem.setEnabled(actionMenuItem.isEnabled());
                        menuItem.setVisible(actionMenuItem.isVisible());
                    } else {
                        LogUtil.d(TAG, "set "
                                + actionMenuItem.getMenuId() + " " + actionMenuItem.getTitle()
                                + " option menu enable " + actionMenuItem.isEnabled()
                                + ", visible " + actionMenuItem.isVisible());
                        menuItem.setIcon(actionMenuItem.getResId());
                        menuItem.setEnabled(actionMenuItem.isEnabled());
                        menuItem.setVisible(actionMenuItem.isVisible());
                        menuItem.setTitle(menuItem.getTitle());
                        menuItem.setOnMenuItemClickListener(actionMenuItem.getItemClickListener());
                        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);
                        if (actionMenuItem.getResId() == R.drawable.ytx_title_btn_menu) {
                            LogUtil.d(TAG, "match action overflow .");
                            mOverFlowAction = actionMenuItem;
                            mOverFlowMenuItem = menuItem;
                        }
                    }
                } else {
                    if (actionMenuItem.getCustomView() == null) {
                        actionMenuItem.setCustomView(View.inflate(mActionBarActivity, R.layout.ytx_action_option_view, null));
                    }
                    LinearLayout rootll = actionMenuItem.getCustomView().findViewById(R.id.rootll);
                    TextView mNormalAction;
                    mNormalAction = (TextView) actionMenuItem.getCustomView().findViewById(R.id.action_option_button);
                    if (actionMenuItem.getActionType() == ActionMenuItem.ActionType.BUTTON) {
//                        mNormalAction = (TextView) actionMenuItem.getCustomView().findViewById(R.id.action_option_button);
////                        actionMenuItem.getCustomView().findViewById(R.id.divider).setVisibility(View.GONE);
////                        actionMenuItem.getCustomView().findViewById(R.id.action_option_button).setVisibility(View.GONE);
////                        mNormalAction.setPadding(mSmallPadding, 0, mSmallPadding, 0);
//
                    } else {
////                        actionMenuItem.getCustomView().findViewById(R.id.divider).setVisibility(View.GONE);
////                        actionMenuItem.getCustomView().findViewById(R.id.action_option_button).setVisibility(View.GONE);
////                        mNormalAction.setBackgroundResource(R.drawable.ytx_actionbar_menu_selector);
                    }
                    if (actionMenuItem.getResId() != 0) {
                        ((ImageView) actionMenuItem.getCustomView().findViewById(R.id.divider)).setImageResource(actionMenuItem.getResId());
                    }
                    if (actionMenuItem.getBackgroundres() != 0) {
                        rootll.setBackground(YTGApplicationContext.getContext().getResources().getDrawable(actionMenuItem.getBackgroundres()));
                    }else {
                        mNormalAction.setBackgroundResource(R.drawable.ytx_actionbar_menu_selector);
                    }
                    mNormalAction.setVisibility(View.VISIBLE);
                    mNormalAction.setText(actionMenuItem.getTitle());
                    mNormalAction.setOnClickListener(mClickListener);
                    mNormalAction.setOnLongClickListener(mLongClickListener);
                    mNormalAction.setEnabled(actionMenuItem.isEnabled());
                    mNormalAction.setVisibility(actionMenuItem.isVisible() ? View.VISIBLE : View.GONE);
                    MenuItemCompat.setActionView(menuItem, actionMenuItem.getCustomView());
                    MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);
                }

                LogUtil.v(TAG, "set " + actionMenuItem.getMenuId() + " " + actionMenuItem.getTitle()
                        + " option menu enable " + actionMenuItem.isEnabled() + ", visible " + actionMenuItem.isVisible());

            }

        }


        return true;
    }

    /**
     * 菜单创建准备工作
     *
     * @param menu 菜单
     * @return 结果
     */
    public boolean onPrepareOptionsMenu(Menu menu) {
        LogUtil.d(TAG, "on prepare option menu");
        if (mSearchEnabled && mSearchViewHelper != null) {
            mSearchViewHelper.onPrepareOptionsMenu(mActionBarActivity, menu);
        }
        return true;
    }

    /**
     * 菜单选择事件
     *
     * @param item 菜单
     * @return 是否处理
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        LogUtil.d(TAG, "on options item selected");
        if (item.getItemId() == mDisplayHomeMenu.getMenuId() && mDisplayHomeMenu.isEnabled()) {
            callMenuCallback(item, mDisplayHomeMenu);
            return true;
        }

        for (ActionMenuItem actionMenuItem : mActionMenuItems) {
            if (item.getItemId() != actionMenuItem.getMenuId()) {
                continue;
            }

            LogUtil.d(TAG, "on option menu " + item.getItemId() + " click");
            callMenuCallback(item, actionMenuItem);
            return true;
        }

        return false;
    }

    /**
     * 设置状态栏
     *
     * @param toolbar 状态栏
     */
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        mToolBar = toolbar;
        if (mActionBarActivity != null) {
            mActionBar = mActionBarActivity.getSupportActionBar();
        }
    }

    /**
     * 设置标题栏返回菜单事件监听
     *
     * @param listener 事件监听
     */
    public void setOnBackMenuItemClickListener(MenuItem.OnMenuItemClickListener listener) {
        if (mActionBar == null) {
            return;
        }

        if (listener == null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
            // mActionBar.setLogo(R.drawable.actionbar_icon_layer);
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            // mActionBar.setLogo(R.drawable.actionbar_icon_layer_big_padding);
        }

        mDisplayHomeMenu.setMenuId(android.R.id.home);
        mDisplayHomeMenu.setItemClickListener(listener);
    }

    /**
     * 根据菜单派发菜单事件回调
     *
     * @param menuItem       菜单
     * @param actionMenuItem 菜单动作
     */
    private void callMenuCallback(MenuItem menuItem, ActionMenuItem actionMenuItem) {
        LogUtil.d(TAG, "item id " + menuItem.getItemId());

        if (actionMenuItem.getItemClickListener() == null) {
            return;
        }
        actionMenuItem.getItemClickListener().onMenuItemClick(menuItem);
    }

    /**
     * 设置单个按钮是否可用状态
     *
     * @param menuId  按钮菜单ID
     * @param enabled 按钮是否可用
     */
    public void setSingleActionMenuItemEnabled(int menuId, boolean enabled) {
        setActionMenuItemEnabled(false, menuId, enabled);
    }

    /**
     * 设置按钮是否可用状态
     *
     * @param enabled 是否可用
     */
    public void setAllActionBarMenuItemEnabled(boolean enabled) {
        setActionMenuItemEnabled(true, -1, enabled);
    }

    /**
     * 设置菜单是否可用
     *
     * @param all     是否所有的菜单
     * @param menuId  菜单编号
     * @param enabled 菜单是否可用
     */
    private void setActionMenuItemEnabled(boolean all, int menuId, boolean enabled) {
        boolean changed = false;
        for (ActionMenuItem menuItem : mActionMenuItems) {
            if ((menuId == menuItem.getMenuId() && (menuItem.isEnabled() != enabled))) {
                menuItem.setEnabled(enabled);
                changed = true;
                if (!all) {
                    break;
                }
            } else if (all && menuItem.isEnabled() != enabled) {
                menuItem.setEnabled(enabled);
                changed = true;
            }
        }

        boolean searching = (mSearchViewHelper != null) && mSearchViewHelper.searchViewExpand;
        LogUtil.v(TAG, "enable option menu, target id " + menuId + ", changed " + changed + ", searching " + searching);

        if (changed) invalidateActionMenu();
    }


    /**
     * 添加一个搜索按钮到状态栏
     *
     * @param enabled 是否可用
     * @param helper  搜索接口
     */
    public void addSearchMenu(boolean enabled, SearchViewHelper helper) {
        LogUtil.d(TAG, "add search menu");
        ActionMenuItem search = new ActionMenuItem();
        search.setMenuId(R.id.ytx_action_bar_search);
        search.setResId(R.drawable.ic_search_24);
        search.setTitle(mActionBarActivity.getString(R.string.app_search));
        search.setItemClickListener(null);
        search.setLongClickListener(null);
        removeActionMenu(R.id.ytx_action_bar_search);
        mActionMenuItems.add(0, search);
        mSearchEnabled = enabled;
        mSearchViewHelper = helper;
        invalidateActionMenu();
    }

    /**
     * 设置单个按钮是否可显示
     *
     * @param menuId  按钮菜单ID
     * @param enabled 是否可显示
     */
    public void setSingleActionMenuItemVisibility(int menuId, boolean enabled) {
        setActionMenuItemVisibility(false, menuId, enabled);
    }

    /**
     * 设置所有按钮是否可显示
     *
     * @param enabled 是否可显示
     */
    public void setAllActionBarMenuItemVisibility(boolean enabled) {
        setActionMenuItemVisibility(true, -1, enabled);
    }

    /**
     * 设置菜单是否可以显示
     *
     * @param all        是否全部的菜单
     * @param menuId     菜单编号
     * @param visibility 菜单是否显示
     */
    private void setActionMenuItemVisibility(boolean all, int menuId, boolean visibility) {
        boolean changed = false;
        for (ActionMenuItem menuItem : mActionMenuItems) {

            if ((menuId == menuItem.getMenuId() && (menuItem.isVisible() != visibility))) {
                menuItem.setVisible(visibility);
                if (!all) {
                    changed = true;
                    LogUtil.d(TAG, "show option menu, target id " + menuId + ", changed " + visibility);
                    break;
                }
            } else if (all && menuItem.isVisible() != visibility) {
                menuItem.setVisible(visibility);
                changed = true;
                LogUtil.d(TAG, "show option menu, target id " + menuId + ", changed " + visibility);
            }
        }
        if (changed) invalidateActionMenu();
    }

    /**
     * 根据菜单编号判断是否已经添加到ToolBar中
     *
     * @param menuId 菜单
     * @return
     */
    public final boolean hasActionMenu(int menuId) {
        return findActionMenuItemById(menuId) != null;
    }

    /**
     * 根据菜单编号查找菜单
     *
     * @param menuId 菜单
     * @return 匹配的菜单
     */
    public ActionMenuItem findActionMenuItemById(int menuId) {
        for (ActionMenuItem item : mActionMenuItems) {
            if (item.getMenuId() == menuId) {
                return item;
            }
        }

        return null;
    }

    /**
     * 根据菜单编号设置菜单显示内容
     *
     * @param menuId 菜单编号
     * @param text   显示内容
     */
    public void setActionMenuText(int menuId, CharSequence text) {
        ActionMenuItem actionMenuItem = findActionMenuItemById(menuId);

        if (actionMenuItem == null || text == null || actionMenuItem.getTitle().equals(text.toString())) {
            return;
        }

        actionMenuItem.setTitle(text);
        invalidateActionMenu();
    }
    public void setActionMenuText(int menuId, CharSequence text,int resid) {
        ActionMenuItem actionMenuItem = findActionMenuItemById(menuId);

        if (actionMenuItem == null || text == null || actionMenuItem.getTitle().equals(text.toString())) {
            return;
        }

        actionMenuItem.setTitle(text);
        actionMenuItem.setResId(resid);
        invalidateActionMenu();
    }
    /**
     * 删除全部的菜单
     */
    public void removeAllActionMenuItem() {
        if (mActionMenuItems.isEmpty()) {
            return;
        }
        mActionMenuItems.clear();
        invalidateActionMenu();
    }
public ActionMenuItem getActionMenuItem(int menuId){
    for (int i = 0; i < mActionMenuItems.size(); i++) {
        if (mActionMenuItems.get(i).getMenuId() == menuId) {
            return mActionMenuItems.get(i);
        }
    }
    return null;
}
    /**
     * 根据菜单的编号删除菜单
     *
     * @param menuId 菜单编号
     * @return 是否删除成功
     */
    public boolean removeActionMenu(int menuId) {
        for (int i = 0; i < mActionMenuItems.size(); i++) {
            if (mActionMenuItems.get(i).getMenuId() != menuId) {
                continue;
            }
            LogUtil.d(TAG, "match menu, id " + menuId + ", remove it");
            mActionMenuItems.remove(i);
            invalidateActionMenu();
            return true;
        }
        return false;
    }

    /**
     * 隐藏 状态栏
     */
    public final void hideActionBar() {
        LogUtil.d(TAG, "hideActionBar hasTitle :" + (mActionBar != null));
        if (mActionBar != null) {
            mActionBar.hide();
        }
        if (mActionBarShadow != null) mActionBarShadow.setVisibility(View.GONE);
    }

    /**
     * 显示状态栏
     */
    public final void showActionBar() {
        LogUtil.d(TAG, "showActionBar hasTitle :" + (mActionBar != null));
        if (mActionBar != null) {
            mActionBar.show();
        }
        if (mActionBarShadow != null) mActionBarShadow.setVisibility(View.VISIBLE);
    }


    /**
     * 状态栏是否现实状态
     *
     * @return
     */
    public final boolean isActionBarShowing() {
        LogUtil.d(TAG, "isTitleShowing hasTitle :" + (mActionBar != null));
        return mActionBar != null && mActionBar.isShowing();

    }

    /**
     * 返回状态栏的高度
     *
     * @return 状态栏高度
     */
    public final int getActionBarHeight() {
        if (mActionBar == null) {
            return 0;
        }

        return mActionBar.getHeight();
    }

    /**
     * 返回当前界面加载的跟布局
     *
     * @return 当前界面加载的跟布局
     * @see #getLayoutId()
     */
    public View getActivityLayoutView() {
        return mContentView;
    }

    /**
     * 返回当前设置到窗口的根布局
     *
     * @return 设置到窗口的根布局
     */
    public View getContentView() {
        return mBaseLayoutView;
    }

    /**
     * 设置状态栏的显示状态
     *
     * @param visiable 状态栏显示状态
     */
    public void setActionBarVisiable(int visiable) {
        if (mActionBar == null) {
            return;
        }
        if (visiable == View.VISIBLE) {
            mActionBar.show();
            return;
        }
        mActionBar.hide();
    }

    /**
     * 返回当前窗口承载的Activity
     *
     * @return 当前窗口承载的Activity
     */
    public AppCompatActivity getActionBarActivity() {
        return mActionBarActivity;
    }

    /**
     * 设置返回菜单是否可用
     *
     * @param showHomeAsUp 是否可用
     */
    public final void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        if (mActionBar == null) {
            return;
        }

        mActionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
    }

    /**
     * 设置是否显示返回按钮
     *
     * @param enabled 是否可用
     */
    public void setDisplayHomeActionMenuEnabled(boolean enabled) {
        if (mDisplayHomeMenu == null || mDisplayHomeMenu.isEnabled() == enabled) {
            return;
        }

        mDisplayHomeMenu.setEnabled(enabled);
        invalidateActionMenu();
    }

    /**
     * 回掉当前的按钮事件
     *
     * @return 是否执行
     */
    public boolean setDisplayHomeMenuCallback() {
        if (mDisplayHomeMenu != null && mDisplayHomeMenu.isEnabled()) {
            callMenuCallback(null, mDisplayHomeMenu);
            return true;
        }
        return false;
    }

    /**
     * 设置当前界面的描述
     *
     * @param contentDescription 当前界面的描述
     */
    public void setContentViewDescription(CharSequence contentDescription) {
        if (TextUtils.isEmpty(contentDescription)) {
            return;
        }
        String description = mActionBarActivity.getString(R.string.common_enter_activity) + contentDescription;
        mActionBarActivity.getWindow().getDecorView().setContentDescription(description);
    }


    /**
     * 显示键盘
     */
    public void toggleSoftInput() {
        final AppCompatActivity activity = mActionBarActivity;
        // Display the soft keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = activity.getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    // 判定是否需要隐藏
    public boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {

            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (ev.getRawX() > left && ev.getRawX() < right && ev.getRawY() > top
                    && ev.getRawY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏键盘
     */
    public void hideSoftKeyboard(View view) {
        if (view == null) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) mActionBarActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            IBinder binder = view.getWindowToken();
            if (binder != null)
                inputMethodManager.hideSoftInputFromWindow(binder, 0);
        }
    }

    /**
     * 隐藏键盘
     */
    public boolean hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mActionBarActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = mActionBarActivity.getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                boolean result = false;
                try {
                    result = inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
                } catch (Exception e) {
                    LogUtil.e(TAG, "hide VKB exception " + e.getLocalizedMessage());
                }
                LogUtil.v(TAG, "hide VKB result " + result);
                return result;
            }
        }

        return false;
    }


    /**
     * 设置状态栏标题
     *
     * @param title 标题
     */
    public final void setActionBarTitle(CharSequence title) {
        if (mActionBar == null) {
            return;
        }

        mTitleText = title;
        onBuildActionBarTitle(title, mActionBar);
        setContentViewDescription(title);
    }

    /**
     * 设置状态栏子标题
     *
     * @param title 标题内容
     */
    public final void setActionBarSubTitle(CharSequence title) {
        if (isActionBarTitleMiddle()) {
            mSubTitleView.setText(title);
            mSubTitleView.setVisibility(View.VISIBLE);
            if (mActionBar != null) mActionBar.setDisplayShowTitleEnabled(false);
            return;
        }

        if (mActionBar == null) {
            return;
        }

        mActionBar.setSubtitle(title);
        mActionBar.setDisplayShowTitleEnabled(true);
    }

    /**
     * 设置显示标题文字后面追加显示，
     *
     * @param summary 补充文字
     */
    public final void setActionBarSupplementTitle(CharSequence summary) {
        if (isActionBarTitleMiddle()) {
            if (summary == null || summary.length() == 0) {
                mTitleSummaryView.setVisibility(View.GONE);
                return;
            }
            TextUtil.setChangeTextViewSize(mTitleSummaryView, 19);
            mTitleSummaryView.setText(summary);
            mTitleSummaryView.setVisibility(View.VISIBLE);
            if (mActionBar != null) mActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * 设置左侧第一个按钮
     *
     * @param
     * @param listener
     */
    public final void setFirstButtonLeftInfo(@DrawableRes int resId, View.OnClickListener listener) {
        if (mActionBar == null || mFirstLeftBtnIv == null) {
            return;
        }
        mFirstLeftBtnIv.setVisibility(View.VISIBLE);
        mFirstLeftBtnIv.setImageResource(resId);
        mFirstLeftBtnIv.setOnClickListener(listener);
    }

    /**
     * 设置左侧第二个按钮
     *
     * @param name
     * @param listener
     */
    public final void setSecondButtonLeftInfo(String name, View.OnClickListener listener) {
        if (mActionBar == null || mSecondLeftBtnTv == null) {
            return;
        }
        mSecondLeftBtnTv.setVisibility(View.VISIBLE);
        mSecondLeftBtnTv.setText(name);
        mSecondLeftBtnTv.setOnClickListener(listener);
    }

    /**
     * 设置状态栏下部阴影是否显示
     *
     * @param show 是否显示
     */
    public final void setActionBarShadowVisibility(boolean show) {
        mActionBarShadow.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * 当前标题栏的文本内容
     *
     * @return 标题栏的文本内容
     */
    public final CharSequence getActionBarTitle() {
        return mTitleText;
    }

    /**
     * 返回状态栏
     *
     * @return 状态栏
     */
    public final Toolbar getToolBar() {
        return mToolBar;
    }

    /**
     * 返回旧版本的ActionBar
     *
     * @return 旧版本的ActionBar
     */
    public final ActionBar getActionBar() {
        return mActionBar;
    }

    /**
     * 派发当前状态栏的设置文本事件
     *
     * @param title 标题栏
     * @param bar   状态栏
     */
    private void onBuildActionBarTitle(CharSequence title, ActionBar bar) {

        if (isActionBarTitleMiddle()) {
            TextUtil.setChangeTextViewSize(mTitleView, 19);
            mTitleView.setText(title);
            mTitleView.setVisibility(View.VISIBLE);
            if (mActionBar != null) mActionBar.setDisplayShowTitleEnabled(false);
            return;
        }
        if (mActionBar != null) {
            mActionBar.setTitle(title);
            mActionBar.setDisplayShowTitleEnabled(true);
        }

    }

    /**
     * 处理按键按下事件
     *
     * @param keyCode 按键编号
     * @param event   案件类型
     * @return 是否处理
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mSearchEnabled && mSearchViewHelper != null && mSearchViewHelper.onKeyDown(keyCode, event)) {
            LogUtil.d(TAG, "match search view on key down");
            return true;
        }
        return false;
    }

    /**
     * 处理按键抬起事件
     *
     * @param keyCode 按键编号
     * @param event   按键类型
     * @return 是否处理
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) {
            if (mOverFlowAction != null && mOverFlowAction.isEnabled()) {
                callMenuCallback(mOverFlowMenuItem, mOverFlowAction);
                return true;
            }
        }
        return false;
    }


    public void onResume() {

    }

    public void onPause() {

    }


    /**
     * Activity初始化
     */
    public abstract void onInit();

    /**
     * 当前Activity设置的布局界面
     *
     * @return 布局界面
     * @see Activity#setContentView(int)
     */
    public abstract int getLayoutId();

    /**
     * 根据{getLayoutId}加载生成的布局界面
     *
     * @return 布局View
     * @see #getLayoutId()
     * @see Activity#setContentView(int)
     */
    public abstract View getContentLayoutView();

    /**
     * 当前Activity名字
     *
     * @return Activity名字
     */
    public abstract String getClassName();

    /**
     * 调用setContentView设置的布局View
     *
     * @param contentView 布局View
     * @see #getLayoutId()
     * @see Activity#setContentView(int)
     */
    public abstract boolean onPreDealWithContentViewAttach(View contentView);

    /**
     * 当前是否需要初始化状态栏
     *
     * @return 是否需要初始化状态栏
     */
    public abstract boolean hasActionBar();

    /**
     * 状态栏标题是否剧中
     *
     * @return 是否剧中
     */
    public abstract boolean isActionBarTitleMiddle();

    /**
     * 当前的状态栏和顶部是否需要设置padding值
     *
     * @return 是否需要设置padding值
     */
    public abstract boolean buildActionBarPadding();

    /**
     * 重新绘制状态栏
     */
    private final void invalidateActionMenu() {
        mActionBarActivity.supportInvalidateOptionsMenu();
    }
}
