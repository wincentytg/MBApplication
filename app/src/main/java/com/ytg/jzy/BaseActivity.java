package com.ytg.jzy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ytg.jzy.db.DBManager;
import com.ytg.jzy.p_common.db.DBBack;
import com.ytg.jzy.p_common.db.DBOptions;
import com.ytg.jzy.p_common.db.MDBManager;
import com.ytg.jzy.p_common.dialog.LoadingDialog;
import com.ytg.jzy.p_common.request.MRequestManager;
import com.ytg.jzy.p_common.request.RequestBack;
import com.ytg.jzy.p_common.tools.SharedPreferencesHelper;
import com.ytg.jzy.p_common.utils.Event;
import com.ytg.jzy.p_common.utils.Utils;
import com.ytg.jzy.tools.PermissionUtils;
import com.ytg.jzy.view.SDSimpleTitleView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import de.greenrobot.event.EventBus;


public abstract class BaseActivity extends FragmentActivity implements
        View.OnClickListener {

    public static final String TAG = BaseActivity.class.getSimpleName();
    /**
     * 标题栏 START
     */
    protected SDSimpleTitleView mTitle;
    public LinearLayout mllRoot;
    /**
     * 屏幕宽度
     */
    public int mScreenWidth;

    /**
     * 屏幕高度
     */
    protected int mScreenHeight;

    /**
     * 屏幕密度
     */
    protected float mScreenDensity;

    /**
     * 字体缩放比例
     */
    protected float mScaledDensity;

    /**
     * 全局LayoutInflater对象
     */
    protected LayoutInflater mInflater;

    /**
     * 窗口管理�?
     */
    protected WindowManager mWindowManager;

    /**
     * 是否打印Log日志
     */
    protected boolean mDebug = false;

    protected BaseApplication mApplication;

        protected DBManager mDbManager;
    //
    //
    public LoadingDialog mLoadingDialog;

    protected Context context;

    protected SharedPreferencesHelper sp;
    public String userName = "";// xml格式里的 传登录返回的mroCode 自己服务器用的传登录返回的userName

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
//        setOrientation();
        mInflater = LayoutInflater.from(this);
        mWindowManager = getWindowManager();
        DisplayMetrics metric = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mScreenDensity = metric.density;
        mScaledDensity = metric.scaledDensity;

        mApplication = BaseApplication.getInstance();
        mDbManager = DBManager.getInstance();
//        mBodyRequestManager = RequestManagerForRequestBoday.getInstance(this);
        sp = SharedPreferencesHelper.getInstance(getApplicationContext());
        mLoadingDialog = new LoadingDialog(this);
        EventBus.getDefault().register(this);
        userName = sp.getString("mroCode");
        requestAllPermission();
    }

    //	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (null != this.getCurrentFocus()) {
//			/**
//			 * 点击空白位置 隐藏软键盘
//			 */
//			InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//			return mInputMethodManager.hideSoftInputFromWindow(this
//					.getCurrentFocus().getWindowToken(), 0);
//		}
//		return super.onTouchEvent(event);
//	}
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                hidenSoft(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void requestData(String url, LinkedHashMap<String, String> map,RequestBack back) {
        MRequestManager.getInstance().url("").setParamsMap(map).request(context, back);
    }
    public void handleDbData(DBOptions options, DBBack back){
        MDBManager.getInstance().loadOptions(this,options,back);
    }


    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
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

    public void hidenSoft(IBinder token) {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(token, 0);
    }

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

            default:
                break;
        }
    }

    public void setTitleGone() {
        mTitle.setVisibility(View.GONE);
    }

    public void setTitleInvisible() {
        mTitle.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setContentView(int layoutResID) {
        // super.setContentView(layoutResID);
//        StatusBarUtils.setWindowStatusBarColor(this, R.color.red);
//        StatusBarUtils.StatusBarLightMode(this);
        initRootViewById(layoutResID);
        initViews();
        setListener();
        initContent();
    }

    // @Override
    // public void setContentView(View view, LayoutParams params) {
    // super.setContentView(view, params);
    // initViews();
    // setListener();
    // initContent();
    // }
    private void initRootViewById(int layoutResID) {
        super.setContentView(R.layout.activity_base_view);
        mllRoot = (LinearLayout) findViewById(R.id.base_activity_main);
        mTitle = (SDSimpleTitleView) findViewById(R.id.base_title);
        View view = LayoutInflater.from(BaseActivity.this).inflate(layoutResID,
                null);
        addSonView(view);
    }

    private void addSonView(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
        view.setLayoutParams(params);
        mllRoot.setFitsSystemWindows(true);
        mllRoot.addView(view);
    }

    @Override
    public void setContentView(View view) {
//        StatusBarUtils.setWindowStatusBarColor(this, R.color.red);
//        StatusBarUtils.StatusBarLightMode(this);
        super.setContentView(view);
        initViews();
        setListener();
        initContent();
    }

    protected abstract void initViews();

    protected abstract void setListener();

    protected abstract void initContent();

    /**
     * 显示LoadingDialog
     */
    protected void showLoadingDialog() {
        showLoadingDialog(null);
    }

    /**
     * 显示LoadingDialog
     *
     * @param listener 隐藏事件
     */
    protected void showLoadingDialog(OnDismissListener listener) {
        if (listener != null) {
            mLoadingDialog.setOnDismissListener(listener);
        } else {
            mLoadingDialog.setOnDismissListener(null);
        }
        if (mLoadingDialog.isShowing()) {
            return;
        }
        mLoadingDialog.show();
    }

    /**
     * 隐藏LoadingDialog
     */
    protected void dismissLoadingDialog() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * Debug输出Log日志
     *
     * @param msg
     */
    protected void showLogDebug(String msg) {
        if (mDebug) {
            Log.d(TAG, msg);
        }
    }

    /**
     * Error输出Log日志
     *
     * @param msg
     */
    protected void showLogError(String msg) {
        if (mDebug) {
            Log.e(TAG, msg);
        }
    }

    /**
     * 显示Toast提示
     *
     * @param text
     */
    protected void showToast(String text) {
        Toast.makeText(BaseActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public void setRootBgnull() {
//	mllRoot.setBackgroundDrawable(null);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTitle.getLayoutParams();
        layoutParams.leftMargin = Utils.dip2px(context, 100);
        mTitle.setLayoutParams(layoutParams);
    }

    public void requestAllPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            PermissionUtils.requestMultiPermissions(this, new PermissionUtils.PermissionGrant() {

                @Override
                public void onPermissionGranted(int requestCode) {
                    // TODO Auto-generated method stub

                }
            });
        }
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    int mIntPermissionIndex;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 0x13:
//                if(grantResults.length > 0
//                        && grantResults[mIntPermissionIndex] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(context, "获取权限成功", 0).show();
//                }else{
//                    showDialogTipUserGoToAppSettting();
//                }
//                break;
//
//            default:
//                break;
//        }

        //TODO
//        Map<String, Integer> perms = new HashMap<>();
        ArrayList<String> notGranted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
//            perms.put(permissions[i], grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permissions[i]);
            }
        }

        if (notGranted.size() > 0) {
            showDialogTipUserGoToAppSettting();
        } else {
//            Toast.makeText(context, "all permission success", Toast.LENGTH_SHORT)
//                    .show();
        }
    }

    // 提示用户去应用设置界面手动开启权限
    public void showDialogTipUserGoToAppSettting() {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("拍照权限不可用")
                .setMessage("请在-应用设置-权限管理，允许使用相关权限")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }
}