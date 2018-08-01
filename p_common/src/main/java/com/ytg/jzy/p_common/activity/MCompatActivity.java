package com.ytg.jzy.p_common.activity;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.ytg.jzy.p_common.R;
import com.ytg.jzy.p_common.YTGApplicationContext;
import com.ytg.jzy.p_common.bar.StatusBarCompat;
import com.ytg.jzy.p_common.db.DBBack;
import com.ytg.jzy.p_common.db.DBOptions;
import com.ytg.jzy.p_common.db.MDBManager;
import com.ytg.jzy.p_common.dialog.YTGProgressDialog;
import com.ytg.jzy.p_common.menu.ActionMenuItem;
import com.ytg.jzy.p_common.request.MRequestManager;
import com.ytg.jzy.p_common.request.RequestBack;
import com.ytg.jzy.p_common.tools.ActivityBase;
import com.ytg.jzy.p_common.tools.SearchViewHelper;
import com.ytg.jzy.p_common.tools.SharedPreferencesHelper;
import com.ytg.jzy.p_common.tools.YTGDialogMgr;
import com.ytg.jzy.p_common.utils.Event;
import com.ytg.jzy.p_common.utils.LogUtil;
import com.ytg.jzy.p_common.utils.NetWorkUtils;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

import de.greenrobot.event.EventBus;

/**
 * 当前Activity为所有的Activity父类，主要处理子界面布局的初始化工作
 * 1、提供了对系统ActionBar的基本设置API
 * 2、提供了对ui界面的基本处理
 *
 * @author 于堂刚
 */
//@ActivityTransition(0)
public abstract class MCompatActivity extends PermissionActivity {

    private static final String TAG = "MCompatActivity";
    /**
     * 正在进行中提示框
     */
    private YTGProgressDialog mPostingDialog;
    private boolean isNotCancleHandle;
    public Context context;
    //    WeakReference<MCompatActivity> context;
    SharedPreferencesHelper sp;
    public ActivityBase mBaseActivity = new ActivityBase() {
        @Override
        public void onInit() {
            MCompatActivity.this.onActivityInit();
        }

        @Override
        public int getLayoutId() {
            return MCompatActivity.this.getLayoutId();
        }

        @Override
        public View getContentLayoutView() {
            return MCompatActivity.this.getContentLayoutView();
        }

        @Override
        public String getClassName() {
            return MCompatActivity.this.getClass().getName();
        }

        @Override
        public boolean onPreDealWithContentViewAttach(View contentView) {
            return MCompatActivity.this.onPreDealWithContentViewAttach(contentView);
        }

        @Override
        public boolean hasActionBar() {
            return MCompatActivity.this.hasActionBar();
        }

        @Override
        public boolean isActionBarTitleMiddle() {
            return true;
        }

        @Override
        public boolean buildActionBarPadding() {
            return MCompatActivity.this.buildActionBarPadding();
        }
    };


    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!allowPlenaryContentView()) {
            // 不需要帮忙初始化标题栏
            LogUtil.e(TAG, "can not initBaseIp activity");
        } else {
            mBaseActivity.init(getBaseContext(), this);

            // 默认设置返回按键
            setNavigationOnClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onDisplayHomeAsUp();
                    return false;
                }
            });
        }

        context = new WeakReference<MCompatActivity>(this).get();
        sp = YTGApplicationContext.sp;

        EventBus.getDefault().register(this);
        initViews();
        setListener();
        initContent();
        initNetworkInfoLis();
    }

    /**
     * 初始化网络链接状态的监听 ，在没网络的时候提供更好的交互
     */
    void initNetworkInfoLis() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.requestNetwork(new NetworkRequest.Builder().
                    build(), new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
//                LogUtil.i("网络链接"+ NetWorkUtils.ping());
                    Event event = new Event();
                    if (NetWorkUtils.ping()) {
                        event.setmIntTag(Event.NET_WORK_STATE_CONNNECTED);
                    } else {
                        event.setmIntTag(Event.NET_WORK_STATE_DISCONNNECT);
                    }
                    postEvent(event);
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
//                LogUtil.i("网络断开"+NetWorkUtils.ping());
                    Event event = new Event();
                    event.setmIntTag(Event.NET_WORK_STATE_DISCONNNECT);
                    postEvent(event);
                }
            });
        }
    }

    protected abstract void initViews();

    protected abstract void setListener();

    protected abstract void initContent();

    /**
     * 发送event事件
     *
     * @param event
     */
    public void postEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 接event事件
     *
     * @param event
     */
    public void onEventMainThread(Event event) {
        switch (event.getmIntTag()) {
            case Event.LOG_OUT:
//			Utils.onDestroyEmmService(this);
                finish();
                break;
            case Event.NET_WORK_STATE_CONNNECTED:
                mBaseActivity.setNetworkInfoStatus(true);
                break;
            case Event.NET_WORK_STATE_DISCONNNECT:
                mBaseActivity.setNetworkInfoStatus(false);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mBaseActivity.onCreateOptionsMenu(menu)) {
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        LogUtil.d(TAG, "on prepare option menu");
        mBaseActivity.onPrepareOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mBaseActivity.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaseActivity.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBaseActivity.onResume();
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            EventBus.getDefault().unregister(this);
            if (mPostingDialog != null && mPostingDialog.isShowing()) {
                mPostingDialog.dismiss();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (mBaseActivity.isHideInput(view, ev)) {
                hideSoftKeyboard();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void requestData(String url, LinkedHashMap<String, String> map, RequestBack back) {
        MRequestManager.getInstance().url("").setParamsMap(map).request(context, back);
    }

    public void handleDbData(DBOptions options, DBBack back) {
        MDBManager.getInstance().loadOptions(this, options, back);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mBaseActivity.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mBaseActivity.onKeyUp(keyCode, event)) {
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    /**
     * 默认关闭当前页面
     */
    public void onDisplayHomeAsUp() {
        hideSoftKeyboard();
        finish();
    }

    /**
     * 显示键盘
     */
    public void showSoftKeyboard() {
        mBaseActivity.toggleSoftInput();
    }


    /**
     * 隐藏键盘
     */
    public void hideSoftKeyboard() {
        mBaseActivity.hideSoftKeyboard();
    }

    /**
     * 是否需要全权交由框架处理初始化当前UI主界面
     * 1、会增加初始化状态栏、标题栏
     * 2、会提供默认的设置状态栏的接口等
     *
     * @return 是否需要初始化
     */
    public boolean allowPlenaryContentView() {
        return true;
    }


    /**
     * 当前活动承载的父Activity
     *
     * @return 活动承载的父Activity
     */
    public Activity getParentActivity() {
        if (getParent() != null) {
            return getParent();
        }
        return null;
    }

    /**
     * 当前View的上下文
     *
     * @return 上下文
     */
    public Activity getActivity() {
        return mBaseActivity.getActionBarActivity();
    }

    /**
     * 返回状态栏
     *
     * @return 状态栏
     */
    public Toolbar getToolbar() {
        return mBaseActivity.getToolBar();
    }

    /**
     * 设置状态栏标题
     *
     * @param resId 标题
     */
    public void setActionBarTitle(int resId) {
        setActionBarTitle(getString(resId));
    }

    /**
     * 设置状态栏标题
     *
     * @param text 标题
     */
    public void setActionBarTitle(CharSequence text) {
        mBaseActivity.setActionBarTitle(text);
    }


    /**
     * 设置左侧第一个按钮点击信息
     */
    public void setFirstButtonLeftInfo(@DrawableRes int resId, View.OnClickListener listener) {
        mBaseActivity.setFirstButtonLeftInfo(resId, listener);
    }

    /**
     * 设置左侧第二个按钮点击信息
     */
    public void setSecondButtonLeftInfo(String name, View.OnClickListener listener) {
        mBaseActivity.setSecondButtonLeftInfo(name, listener);
    }

    /**
     * 设置状态栏子标题
     *
     * @param title 标题内容
     */
    public final void setActionBarSubTitle(int title) {
        setActionBarSubTitle(getString(title));
    }

    /**
     * 设置状态栏子标题
     *
     * @param title 标题内容
     */
    public final void setActionBarSubTitle(CharSequence title) {
        mBaseActivity.setActionBarSubTitle(title);
    }

    /**
     * 设置显示标题文字后面追加显示，
     *
     * @param summary 补充文字
     */
    public final void setActionBarSupplementTitle(CharSequence summary) {
        mBaseActivity.setActionBarSupplementTitle(summary);
    }

    /**
     * 设置状态栏下部阴影是否显示
     *
     * @param show 是否显示
     */
    final void setActionBarShadowVisibility(boolean show) {
        mBaseActivity.setActionBarShadowVisibility(show);
    }

    /**
     * 设置当前界面的描述
     *
     * @param contentDescription 当前界面的描述
     */
    public void setContentViewDescription(CharSequence contentDescription) {
        mBaseActivity.setContentViewDescription(contentDescription);
    }

    /**
     * 设置返回菜单是否可用
     *
     * @param showHomeAsUp 是否可用
     */
    public final void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        mBaseActivity.setDisplayHomeAsUpEnabled(showHomeAsUp);
    }

    /**
     * 返回状态栏
     *
     * @return 状态栏
     */
    public Toolbar getToolBar() {
        return mBaseActivity.getToolBar();
    }

    /**
     * 返回旧版本的ActionBar
     *
     * @return 旧版本的ActionBar
     */
    public ActionBar getActonBarV7() {
        return mBaseActivity.getActionBar();
    }

    /**
     * 当前标题栏的文本内容
     *
     * @return 标题栏的文本内容
     */
    public final CharSequence getActionBarTitle() {
        return mBaseActivity.getActionBarTitle();
    }

    /**
     * 设置标题栏返回菜单事件监听
     *
     * @param listener 事件监听
     */
    public void setNavigationOnClickListener(MenuItem.OnMenuItemClickListener listener) {
        mBaseActivity.setOnBackMenuItemClickListener(listener);
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        mBaseActivity.setSupportActionBar(toolbar);
    }

    /**
     * 添加一个搜索按钮到状态栏
     *
     * @param helper 搜索接口
     */
    public void addSearchMenu(SearchViewHelper helper) {
        mBaseActivity.addSearchMenu(true, helper);
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
        mBaseActivity.setActionMenuItem(menuId, title, resId, itemClickListener);
    }


    /**
     * 根据提供的图片添加一个按钮菜单
     *
     * @param menuId            按钮菜单ID
     * @param resId             按钮资源图片
     * @param itemClickListener 按钮事件回调监听
     */
    public final void setActionMenuItem(int menuId, int resId, MenuItem.OnMenuItemClickListener itemClickListener) {
        mBaseActivity.setActionMenuItem(menuId, resId, itemClickListener);
    }

    /**
     * 根据提供的图片添加一个按钮菜单
     *
     * @param menuId            按钮菜单ID
     * @param resId             按钮资源图片
     * @param resId             按钮资源图片
     * @param itemClickListener 按钮事件回调监听
     */
    public final void setActionMenuItem(int menuId, int resId, String title, MenuItem.OnMenuItemClickListener itemClickListener) {
        mBaseActivity.setActionMenuItem(menuId, resId, title, itemClickListener);
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
        mBaseActivity.setActionMenuItem(title, resId, itemClickListener, longClickListener);
    }


    /**
     * 设置文字显示actionBar
     *
     * @param menuId            按钮菜单ID
     * @param title             按钮显示文本内容
     * @param itemClickListener 按钮事件回调监听
     */
    public final void setActionMenuItem(int menuId, String title, MenuItem.OnMenuItemClickListener itemClickListener) {
        mBaseActivity.setActionMenuItem(menuId, title, itemClickListener);
    }

    /**
     * 根据菜单ID判断是否存在ActionBar中
     *
     * @param menuId 菜单
     * @return
     */
    public final boolean hasActionMenu(int menuId) {
        return mBaseActivity.hasActionMenu(menuId);
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
        mBaseActivity.setActionMenuItem(menuId, title, itemClickListener, actionType);
    }

    /**
     * 设置单个按钮是否可用状态
     *
     * @param menuId  按钮菜单ID
     * @param enabled 按钮是否可用
     */
    public void setSingleActionMenuItemEnabled(int menuId, boolean enabled) {
        mBaseActivity.setSingleActionMenuItemEnabled(menuId, enabled);
    }

    /**
     * 设置是否显示返回按钮
     *
     * @param enabled 是否可用
     */
    public void setDisplayHomeActionMenuEnabled(boolean enabled) {
        mBaseActivity.setDisplayHomeActionMenuEnabled(enabled);
    }

    /**
     * 设置按钮是否可用状态
     *
     * @param enabled 是否可用
     */
    public void setAllActionBarMenuItemEnabled(boolean enabled) {
        mBaseActivity.setAllActionBarMenuItemEnabled(enabled);
    }

    /**
     * 设置单个按钮是否可显示
     *
     * @param menuId  按钮菜单ID
     * @param enabled 是否可显示
     */
    public void setSingleActionMenuItemVisibility(int menuId, boolean enabled) {
        mBaseActivity.setSingleActionMenuItemVisibility(menuId, enabled);
    }

    /**
     * 设置所有按钮是否可显示
     *
     * @param enabled 是否可显示
     */
    public void setAllActionBarMenuItemVisibility(boolean enabled) {
        mBaseActivity.setAllActionBarMenuItemVisibility(enabled);
    }

    /**
     * 根据菜单编号设置菜单显示内容
     *
     * @param menuId 菜单编号
     * @param text   显示内容
     */
    public void setActionMenuText(int menuId, int text) {
        setActionMenuText(menuId, getString(text));
    }

    /**
     * 根据菜单编号设置菜单显示内容
     *
     * @param menuId 菜单编号
     * @param text   显示内容
     */
    public void setActionMenuText(int menuId, CharSequence text) {
        mBaseActivity.setActionMenuText(menuId, text);
    }

    /**
     * 设置默认第一顺位的按钮处理响应事件
     *
     * @param l 响应事件
     */
    public void setTopActionClickListener(MenuItem.OnMenuItemClickListener l) {
        ActionMenuItem actionMenu = mBaseActivity.findActionMenuItemById(1);
        if (actionMenu != null) {
            actionMenu.setItemClickListener(l);
        }
    }

    /**
     * 返回当前界面加载的跟布局
     *
     * @return 当前界面加载的跟布局
     * @see #getLayoutId()
     */
    public View getActivityLayoutView() {
        return mBaseActivity.getActivityLayoutView();
    }

    /**
     * 显示状态栏
     */
    public final void showActionBar() {
        mBaseActivity.showActionBar();
    }

    /**
     * 隐藏 状态栏
     */
    public final void hideActionBar() {
        mBaseActivity.hideActionBar();
    }


    /**
     * 返回当前UI 界面布局文件
     *
     * @return UI 布局文件
     */
    public abstract int getLayoutId();


    /**
     * 当前UI 界面开始初始化
     * 1、处理初始化之前的准备工作，比如隐藏系统标题栏
     */
    public void onActivityInit() {

    }

    /**
     * 根据{getLayoutId}加载生成的布局界面
     *
     * @return 布局View
     * @see #getLayoutId()
     * @see Activity#setContentView(int)
     */
    public View getContentLayoutView() {
        return null;
    }


    /**
     * 返回状态栏高度
     *
     * @return 状态栏高度
     */
    protected int getStatusBarHeight() {
        return mBaseActivity.getStatusBarHeight();
    }

    /**
     * 转发UI 界面初始化完成
     * 这里可以处理一些初始化操作，比如去除状态栏
     *
     * @param contentView 当前界面窗体
     */
    protected boolean onPreDealWithContentViewAttach(View contentView) {
        setContentView(contentView);
        //AndroidBug5497Workaround.assistActivity(this);
        if (contentView != null) {
            return onDealWithContentViewAttach(contentView.findViewById(R.id.ytx_content_fl));
        }
        return onDealWithContentViewAttach(null);
    }

    /**
     * 转发UI 界面初始化完成
     * 这里可以处理一些初始化操作，比如去除状态栏
     *
     * @param contentView 当前界面窗体
     */
    public boolean onDealWithContentViewAttach(View contentView) {
        setSystemStatusBar();
        return false;
    }

    public void setSystemStatusBar() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.action_bar_color));
    }


    /**
     * 当前是否需要初始化状态栏
     *
     * @return 是否需要初始化状态栏
     */
    public boolean hasActionBar() {
        return true;
    }


    /**
     * 当前的状态栏和顶部是否需要设置padding值
     *
     * @return 是否需要设置padding值
     */
    public boolean buildActionBarPadding() {
        return false;
    }

    /**
     * 显示提示框
     */
    public void showPostingDialog() {
        showPostingDialog(R.string.loading_press);
    }

    /**
     * 根据提示语弹出正在操作对话框
     *
     * @param tips 正在操作
     */
    public void showPostingDialog(int tips) {
        showPostingDialog(getString(tips));
    }

    /**
     * 根据提示语弹出正在操作对话框
     *
     * @param tips 正在操作
     */
    public void showPostingDialog(int tips, boolean isNotCancleHandle) {
        this.isNotCancleHandle = isNotCancleHandle;
        showPostingDialog(getString(tips));
    }

    /**
     * 根据提示语弹出正在操作对话框
     *
     * @param tips 正在操作
     */
    public void showPostingDialog(String tips) {
        if (this.isFinishing()) {
            return;
        }
        if (mPostingDialog == null) {
            initDialog(tips);
        }
        if (mPostingDialog != null && !mPostingDialog.isShowing()) {
            mPostingDialog.setMessage(tips);
            mPostingDialog.show();
            return;
        }

        if (mPostingDialog != null) {
            mPostingDialog.setMessage(tips);
        }
        mPostingDialog.setCancelable(!isNotCancleHandle);
    }

    /**
     * 提示正在提交提示框
     *
     * @param res 文本资源显示
     */
    public void initDialog(int res) {
        initDialog(getResources().getString(res));
    }

    /**
     * 提示正在提交提示框
     *
     * @param str 问题提示
     */
    public void initDialog(String str) {
        if (mPostingDialog == null) {
            mPostingDialog = YTGDialogMgr.showProgress(this, str, null);
            return;
        }
        mPostingDialog.setMessage(str);
    }


    /**
     * 关闭提示框
     */
    public void dismissDialog() {
        if (mPostingDialog != null && mPostingDialog.isShowing()) {
            mPostingDialog.dismiss();
            mPostingDialog = null;
        }
    }

}
