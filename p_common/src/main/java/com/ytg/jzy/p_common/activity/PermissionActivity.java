package com.ytg.jzy.p_common.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ytg.jzy.p_common.utils.EasyPermissionsEx;
import com.ytg.jzy.p_common.utils.LogUtil;
import com.ytg.jzy.p_common.utils.ToastUtil;

import java.util.ArrayList;


/**
 * 权限检测
 *
 * @author YTG
 */
public class PermissionActivity extends AppCompatActivity {

    public static final String rationaleCameraExternal = "需要访问存储设置、相机、麦克风的权限";
    public static final String rationale = "需要访问相关权限";
    public static final String rationaleLocation = "需要访问定位的权限";
    public static final String rationaleSDCard = "需要访问存储的权限";
    public static final String rationalePhoneState = "需要访问电话权限";
    public static final String rationaleVoiceExternal = "需要访问录音和存储的权限";
    public static final String rationaleVoice = "需要访问麦克风的权限";
    public static final String rationaleCamera = "需要访问摄像头权限";
    public static final String goSettingsRationaleSDCard = "需要访问存储设备的权限，但此权限已被禁止，你可以到设置中更改";
    public static final String goSettingsRationaleCamera = "需要访问存摄像头权限，但此权限已被禁止，你可以到设置中更改";
    public static final String goSettingsRationaleVoice = "需要访问麦克风录音，但此权限或被禁止，你可以到设置中更改";
    public static final String goSettingsRationaleCameraExternal = "需要访问存储设备、相机、麦克风的权限，但此权限已被禁止，你可以到设置中更改";
    public static final String goSettingsRationaleVoiceExternal = "需要访问录音和存储的权限，但两者权限或被禁止，你可以到设置中更改";
    private static final String goSettingsRationaleInit = "需要访问设备的某项权限已被禁止，你可以到设置中更改";
    private static final String goSettingsRationaleLocation = "需要定位的权限,但此权限已被禁止，你可以到设置中更改";
    private static final String goSettingsRationaleContacts = "需要读取通讯录的权限,但此权限已被禁止，你可以到设置中更改";
    public static final String goSettingsRationaleReadPhoneState = "需要读取通讯录的权限,但此权限已被禁止，你可以到设置中更改";
    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0x10;
    public static final int PERMISSIONS_REQUEST_CAMERA = 0x11;
    public static final int PERMISSIONS_REQUEST_VOICE = 0x12;
    public static final int PERMISSIONS_REQUEST_CAMERA_EXTERNAL = 0x13;
    public static final int PERMISSIONS_REQUEST_VOICE_EXTERNAL = 0x14;

    public static final int PERMISSIONS_REQUEST_LOCATION = 0x15;
    public static final int PERMISSIONS_REQUEST_INIT = 0x16;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 0x17;
    public static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 0x18;

    public int RC_SETTINGS_SCREEN = 0x1001;
    public static final String[] needPermissionsVoiceExternal = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String[] needPermissionsCameraExternal = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    public static final String needPermissionsReadContacts = Manifest.permission.READ_CONTACTS;

    public static final String needPermissionsCallPhone = Manifest.permission.CALL_PHONE;
    public static final String[] needPermissionsReadPhoneState = new String[]{Manifest.permission.READ_PHONE_STATE};

    public static final String[] needPermissionsLocations = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    //
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static final String[] requestPermissions = {
            PERMISSION_RECORD_AUDIO,
            PERMISSION_GET_ACCOUNTS,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_CALL_PHONE,
            PERMISSION_CAMERA,
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.showMessage(grantResults.length + " permision.length");
                    LogUtil.d("onRequestPermissionsResult: Permission granted");
                } else {
                    LogUtil.d("onRequestPermissionsResult: Permission denied");
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, requestPermissions[8])) {
                        EasyPermissionsEx.goSettings2Permissions(this, goSettingsRationaleSDCard, "去设置", RC_SETTINGS_SCREEN);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_VOICE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("onRequestPermissionsResult: Permission granted");
                } else {
                    LogUtil.d("onRequestPermissionsResult: Permission denied");
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, requestPermissions[0])) {
                        EasyPermissionsEx.goSettings2Permissions(this, goSettingsRationaleVoice, "去设置", RC_SETTINGS_SCREEN);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_VOICE_EXTERNAL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("onRequestPermissionsResult: Permission granted");
                } else {
                    LogUtil.d("onRequestPermissionsResult: Permission denied");
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, needPermissionsVoiceExternal)) {
                        EasyPermissionsEx.goSettings2Permissions(this, goSettingsRationaleVoiceExternal, "去设置", RC_SETTINGS_SCREEN);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("onRequestPermissionsResult: Permission granted");
                } else {
                    LogUtil.d("onRequestPermissionsResult: Permission denied");
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, requestPermissions[4])) {
                        EasyPermissionsEx.goSettings2Permissions(this, goSettingsRationaleCamera, "去设置", RC_SETTINGS_SCREEN);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_CAMERA_EXTERNAL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("onRequestPermissionsResult: Permission granted");
                } else {
                    LogUtil.d("onRequestPermissionsResult: Permission denied");
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, needPermissionsCameraExternal)) {
                        EasyPermissionsEx.goSettings2Permissions(this, goSettingsRationaleCameraExternal, "去设置", RC_SETTINGS_SCREEN);
                    }
                }
                break;

            case PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("onRequestPermissionsResult: Permission granted");
                } else {
                    LogUtil.d("onRequestPermissionsResult: Permission denied");
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, needPermissionsLocations)) {
                        EasyPermissionsEx.goSettings2Permissions(this, goSettingsRationaleLocation, "去设置", RC_SETTINGS_SCREEN);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_INIT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("onRequestPermissionsResult: Permission granted");
                } else {
                    LogUtil.d("onRequestPermissionsResult: Permission denied");
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, getRequestPermisstion(this))) {
                        EasyPermissionsEx.goSettings2Permissions(this, goSettingsRationaleInit, "去设置", RC_SETTINGS_SCREEN);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("onRequestPermissionsResult: Permission granted");
                } else {
                    LogUtil.d("onRequestPermissionsResult: Permission denied");
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, needPermissionsReadContacts)) {
                        EasyPermissionsEx.goSettings2Permissions(this, goSettingsRationaleContacts, "去设置", RC_SETTINGS_SCREEN);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("onRequestPermissionsResult: Permission granted");
                } else {
                    LogUtil.d("onRequestPermissionsResult: Permission denied");
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, needPermissionsReadContacts)) {
                        EasyPermissionsEx.goSettings2Permissions(this, goSettingsRationaleReadPhoneState, "去设置", RC_SETTINGS_SCREEN);
                    }
                }
                break;
            default:
                LogUtil.d("onRequestPermissionsResult: default error...");
                break;
        }
    }

    /**
     * 初始化所有的权限请求
     * @param context
     */
    public static void  initAllRequestPerssion(Activity context){
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (EasyPermissionsEx.hasPermissions(context, getRequestPermisstion(context))) {
            } else {

                EasyPermissionsEx.requestPermissions(context, rationale, PERMISSIONS_REQUEST_INIT,getRequestPermisstion(context));
            }
        }
    }

    /**
     * 获取本应用权限列表
     * @param act
     * @return
     */
    public static String[]getAppPermission(Activity act){
        try {
            String[] permission = act.getPackageManager().getPackageInfo(act.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
            return permission;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("getAppPermission", "获取权限列表失败");
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取匹配后的 所需要申请的权限
     * @return
     */
    static String[] getRequestPermisstion(Activity act){
        ArrayList<String> list= new ArrayList<String>();
        String[] permission = getAppPermission(act);
        for (int i = 0; i < requestPermissions.length; i++) {

            for (int j = 0; j < permission.length; j++) {
                if (requestPermissions[i].equals(permission[j])) {
                    list.add(requestPermissions[i]);
                }else{
                    continue;
                }
            }
        }
        return (String[]) list.toArray(new String[0]);

    }
}
