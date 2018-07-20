package com.ytg.jzy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ytg.jzy.db.DBManager;
import com.ytg.jzy.db.Options;
import com.ytg.jzy.http.ok.MRXmanager;
import com.ytg.jzy.http.ok.RequestOK;
import com.ytg.jzy.http.ok.RequestOptionRF;
import com.ytg.jzy.p_common.activity.ExampleActivity;
import com.ytg.jzy.p_common.activity.PermissionActivity;
import com.ytg.jzy.p_common.activity.SettingTextSizeActivity;
import com.ytg.jzy.p_common.bar.StatusBarCompat;
import com.ytg.jzy.p_common.db.DBBack;
import com.ytg.jzy.p_common.db.MDBManager;
import com.ytg.jzy.p_common.request.MRequestManager;
import com.ytg.jzy.p_common.request.RequestBack;
import com.ytg.jzy.p_common.request.RequestOptions;
import com.ytg.jzy.p_common.utils.EasyPermissionsEx;
import com.ytg.jzy.p_common.utils.GlideHelper;
import com.ytg.jzy.p_common.utils.NotificationUtil;
import com.ytg.jzy.p_common.utils.ToastUtil;
import com.ytg.jzy.p_common.utils.Utils;
import com.ytg.jzy.p_common.view.ProgressBarDeterminate;
import com.ytg.jzy.p_common.view.SwitchButton;
import com.ytg.jzy.p_common.view.SystemBarTintManager;
import com.ytg.jzy.p_common.view.drawable.WaterMarkUtils;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.ytg.jzy.p_common.activity.PermissionActivity.PERMISSIONS_REQUEST_CAMERA_EXTERNAL;
import static com.ytg.jzy.p_common.activity.PermissionActivity.needPermissionsCameraExternal;
import static com.ytg.jzy.p_common.activity.PermissionActivity.rationaleCameraExternal;

public class MainActivity extends BaseActivity {
    //    ButtonFlat btn;
    SwitchButton mSwitchBtn;
    SystemBarTintManager mSystemBarTintManager;
    ImageView mIvHead;
    protected RecyclerView mRecyler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        btn = findViewById(R.id.dilaog_button2);
//        btn.setText("按钮");
        mIvHead = findViewById(R.id.mIvHead);
        mSwitchBtn = findViewById(R.id.mSwitchBtn);
        mSwitchBtn.setChecked(true);
        mSwitchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSwitchBtn.toggle();
            }
        });
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(com.ytg.jzy.p_common.R.color.action_bar_color));
        mSystemBarTintManager = new SystemBarTintManager(this);
        ((TextView) findViewById(R.id.mTvActionBar)).setText(
                "StatusBarHeight:" + mSystemBarTintManager.getConfig().getStatusBarHeight() +
                        "\ngetActionBarHeight:" + mSystemBarTintManager.getConfig().getActionBarHeight() +
                        "\ngetActionBarHeightV7:" + mSystemBarTintManager.getConfig().getActionBarHeightV7() +
                        "\ngetNavigationBarHeight:" + mSystemBarTintManager.getConfig().getNavigationBarHeight() +
                        "\ngetNavigationBarWidth:" + mSystemBarTintManager.getConfig().getNavigationBarWidth() +
                        "\ngetPixelInsetBottom:" + mSystemBarTintManager.getConfig().getPixelInsetBottom() +
                        "\ngetPixelInsetRight:" + mSystemBarTintManager.getConfig().getPixelInsetRight() +
                        "\ngetPixelInsetTop true:" + mSystemBarTintManager.getConfig().getPixelInsetTop(true) +
                        "\ngetPixelInsetTop false:" + mSystemBarTintManager.getConfig().getPixelInsetTop(false)
        );
        ((TextView) findViewById(R.id.mTvActionBar)).setBackground(WaterMarkUtils.getWaterMark("DEMO 水印"));
        ProgressBarDeterminate progressDeterminate = (ProgressBarDeterminate) findViewById(R.id.progressDeterminate);
        progressDeterminate.setMax(100);
        progressDeterminate.setProgress(50);
        findViewById(R.id.mTvsetsize).setOnClickListener(this);
        findViewById(R.id.mBtnArouter).setOnClickListener(this);
//加载动画
//        YTGDialogMgr.showProgress(this, "对话框",true, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//
//            }
//        });
        //加载动画 自定义样式
//        YTGDialogMgr.showProgress(this, R.style.dialog_style,"对话框",true, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//
//            }
//        });
        mIvHead.setImageDrawable(GlideHelper.getDefaultDrawable("Y的名字", Utils.dip2px(this, 16), Utils.dip2px(this, 60), Utils.dip2px(this, 60)));
//        BackwardSupportUtil.fastBlurBitmap();//设置图片模糊
        NotificationUtil.senNotify(context, true);
//        LogUtil.i("URI","1231243452");
//        LogUtil.saveLogToLocalFile("日食数据");

        if (EasyPermissionsEx.hasPermissions(context, PermissionActivity.needPermissionsLocations)) {
            ToastUtil.showMessage("权限以获取");
        } else {
            EasyPermissionsEx.requestPermissions(context, PermissionActivity.rationalePhoneState,
                    PermissionActivity.PERMISSIONS_REQUEST_READ_PHONE_STATE, PermissionActivity.needPermissionsLocations);
        }
        if (EasyPermissionsEx.hasPermissions(context, needPermissionsCameraExternal)) {
        } else {
            EasyPermissionsEx.requestPermissions(context, rationaleCameraExternal, PERMISSIONS_REQUEST_CAMERA_EXTERNAL, needPermissionsCameraExternal);
        }
        findViewById(R.id.mTvactivity).setOnClickListener(this);

        MRequestManager.getInstance().setsRequestCancle(MRXmanager.getInstance());//初始化网络请求取消框架
        MRequestManager.getInstance().setRequestLoader(RequestOK.getInstance(context));//初始化网络请求加载策略
//        MRequestManager.getInstance().setRequestLoader(RequestManager.getInstance(context));
//        MRequestManager.getInstance().setsRequestCancle(MVolleymanager.getInstance());
        MDBManager.getInstance().setDBLoader(DBManager.getInstance());//初始化数据库操作的的策略

        findViewById(R.id.mBtnlog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingDialog.show();
                LinkedHashMap<String, String> map = new LinkedHashMap<>();

                map.put("api", "login");
                map.put("userMobile", "13356399629");
                map.put("userPassword", "123123");
                map.put("sign", initSign(map));

                MRequestManager.getInstance().loadOptions(context, new RequestOptionRF("loginin")
                                .setParamsMap(map)
                        , new RequestBack<JSONObject>() {
                            @Override
                            public void onFail(Object obj) {
                                mLoadingDialog.dismiss();
                                ToastUtil.showMessage(obj.toString());
                            }

                            @Override
                            public void onSuccess(JSONObject obj) {
                                mLoadingDialog.dismiss();
                                ToastUtil.showMessage(obj.toString());
                            }

                        });
                MRequestManager.getInstance().cancle("loginin");
            }
        });
        findViewById(R.id.mBtncancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MRequestManager.getInstance().clear();
            }
        });
    }

    void requestTest() {


        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //下边两种方法都可以执行加载
        //1
        requestData("", map, new RequestBack() {
            @Override
            public void onFail(Object obj) {

            }

            @Override
            public void onSuccess(Object obj) {

            }
        });//2
        MRequestManager.getInstance().loadOptions(context, new RequestOptions("http://")
                        .setParamsMap(map)
                , new RequestBack<JSONObject>() {
                    @Override
                    public void onFail(Object obj) {

                    }

                    @Override
                    public void onSuccess(JSONObject obj) {

                    }

                });

        handleDbData(new Options("id", Options.Method.insertData), new DBBack() {
            @Override
            public void onFail(Object obj) {

            }

            @Override
            public void onSuccess(Object obj) {

            }
        });
    }

    String salt = "jxgk753";

    String initSign(LinkedHashMap<String, String> mParams) {
        if (mParams != null && mParams.size() != 0) {
            StringBuffer buffer = new StringBuffer(salt);

            Iterator var3 = mParams.entrySet().iterator();

            while (var3.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) var3.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                if (key != null && value != null) {
                    buffer.append(key).append(value);
                }
            }
            buffer.append(salt);

            return md5Password(buffer.toString());
        }
        return "";
    }

    public static String md5Password(String password) {

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initContent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvsetsize:
                startActivity(new Intent(context, SettingTextSizeActivity.class));
                break;
            case R.id.mTvactivity:
                startActivity(new Intent(context, ExampleActivity.class));
                break;
            case R.id.mBtnArouter:

//Test test;跳转到model的activity
//        startActivity(new Intent(this,TestActivity.class));
                ARouter.getInstance().build("/com/test/TestActivity").navigation();
                break;
        }
    }
}
