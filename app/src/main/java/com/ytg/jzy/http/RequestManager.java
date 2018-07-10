package com.ytg.jzy.http;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.volley.libirary.http.request.RequestCallBack;
import com.volley.libirary.http.request.RequestCallBack2;
import com.volley.libirary.http.request.RequestMode;
import com.volley.libirary.http.request.RequestParam;
import com.volleyl.libirary.http.HttpManager;
import com.ytg.jzy.activity.LoginActivity;
import com.ytg.jzy.p_common.request.IRequestStrategy;
import com.ytg.jzy.p_common.request.MRequestManager;
import com.ytg.jzy.p_common.request.RequestBack;
import com.ytg.jzy.p_common.request.RequestOptions;
import com.ytg.jzy.p_common.tools.SharedPreferencesHelper;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;


public class RequestManager implements IRequestStrategy {
    private Context mContext;
    private static volatile RequestManager mManager;
    private HttpManager mHttpManager;
    // protected UserInfo mInfo;
    SharedPreferencesHelper sp;
    @Override
    public void loadData(Context context,RequestOptions options,final RequestBack back) {
        RequestParam param = getParam(options.url);
        param.addMapParams(options.paramsMap);
        param.setRequestMode(RequestMode.GET);
        mHttpManager.setTimeOut(30000);
        mHttpManager.getJsonObjectResponse2(param, requestCallBack2(new RequestCallBack2<JSONObject>() {
            @Override
            public void onResult(JSONObject jsonObject) {
                back.onSuccess(jsonObject);
            }

            @Override
            public void onError(VolleyError volleyError) {
                back.onFail(volleyError.getCause());
            }
        }));
        MRequestManager.getInstance().add(options.url,mHttpManager);
    }

    private RequestManager(Context context) {
        mContext = context;
        TelephonyManager manager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        mHttpManager = HttpManager.getInstance(context);
        mHttpManager.setDebug(true);
        sp = SharedPreferencesHelper.getInstance(context);
        // mInfo = new UserInfo("serviceUserName", "token", "userName");
    }

    public static synchronized RequestManager getInstance(Context context) {
        if (mManager == null) {
            mManager = new RequestManager(context);
        }
        return mManager;
    }

    public RequestParam getParam(String method) {
        return new RequestParam().setUrl(QInterface.BASE_URl + method);
    }

//	public RequestParam getLogInParam() {
////		return new RequestParam().setUrl(QInterface.LOGIN);
//	}

    String getDataJsons(LinkedHashMap<String, String> map) {
        if (map == null) {
            return null;
        }

        StringBuffer mDatas = new StringBuffer();
        mDatas.append("{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key == null || value == null) {
                continue;
            }
            mDatas.append("'" + key + "':").append("'" + value + "',");
        }
        return mDatas.substring(0, mDatas.lastIndexOf(",")) + "}";

    }

    String getDataJsonsArray(LinkedHashMap<String, String> map) {
        if (map == null) {
            return null;
        }

        StringBuffer mDatas = new StringBuffer();
        mDatas.append("[{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key == null || value == null) {
                continue;
            }
            mDatas.append("'" + key + "':").append("'" + value + "',");
        }
        return mDatas.substring(0, mDatas.lastIndexOf(",")) + "}]";

    }

    String getDatas(LinkedHashMap<String, String> map) {
        if (map == null) {
            return null;
        }

        StringBuffer mDatas = new StringBuffer();
        mDatas.append("<root>");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key == null || value == null) {
                continue;
            }
            mDatas.append("<" + key + ">").append(value).append("</")
                    .append(key).append(">");
        }

        mDatas.append("</root>");
        return mDatas.toString();
    }

    /**
     * 101 登录成功! 102 当前用户不存在！ 103 用户密码错误，请重新输入! 104 请先登录！ 105 您的账号在另一地点登录，您被迫下线！
     * 106 更新用户信息失败，请联系管理员！ 107 累计3次写错密码，冻结账号，如果需要请联系管理员！ 108 账号或密码有误，请重新输入! 109
     * 不存在当前用户类型，请联系管理员！ 110 传来的username为空！ 111 当前用户的账户类型不是接口类型！ 权限错误状态 201
     * 权限不够，无法操作!
     *
     * @param response
     * @param callBack
     */
    private void checkLogState(JSONObject response,
                               RequestCallBack<JSONObject> callBack) {
        // if (((BaseActivity)mContext).mLoadingDialog.isShowing()) {
        // ((BaseActivity)mContext).mLoadingDialog.dismiss();
        // }
        switch (response.optInt("msgCode")) {
            case 1:// 除登录外接口调用成功
                callBack.onResult(response);
                break;
            case 101:// 登陆成功
                callBack.onResult(response);
                break;
            case 104:
            case 105:
//			mContext.startActivity(new Intent(mContext, LoginActivity.class)
//					.putExtra("logdes", response.optString("description")));

                break;
            default:
                callBack.onError();
                Toast.makeText(mContext, response.optString("description"), Toast.LENGTH_LONG)
                        .show();
                break;
        }
    }

    private void checkLogState2(JSONObject response,
                                RequestCallBack2<JSONObject> callBack) {
        if (response.optInt("error")==0) {
            callBack.onResult(response);
        } else {
            if (response.optString("msg").contains("token失效")) {
                mContext.startActivity(new Intent(mContext, LoginActivity.class).putExtra("error",response.optString("msg")));
            } else {
                callBack.onError(null);
                Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT)
                        .show();
            }


        }
    }

    RequestCallBack2<JSONObject> requestCallBack2(
            final RequestCallBack2<JSONObject> callBack) {
        return new RequestCallBack2<JSONObject>() {

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(mContext, "网络故障，刷新重试" + error.getMessage(), Toast.LENGTH_SHORT).show();
                if (callBack != null) {
                    callBack.onError(error);
                }
            }

            @Override
            public void onResult(JSONObject response) {
                // TODO Auto-generated method stub
                Log.i("URI", response.toString());
                if (callBack != null) {
                    checkLogState2(response, callBack);
                }
            }

        };
    }


    private void base(RequestParam param, LinkedHashMap<String, String> datas,
                      final RequestCallBack2<JSONObject> callBack) {
        param.addParam("data", getDatas(datas))
                .setRequestMode(RequestMode.POST);
        param.addParam("serviceUserName", sp.getString("mroCode"));
        param.addParam("token", sp.getString("token"));
        param.addParam("userName", sp.getString("userName"));
        mHttpManager.getJsonObjectResponse2(param, requestCallBack2(callBack));
    }


//    public void customerLoginIn(String userName, String psd, RequestCallBack2<JSONObject> callBack) {
//        RequestParam param = getParam(QInterface.customerLoginIn);
//        param.addParam("password", psd)
//                .setRequestMode(RequestMode.POST)
//                .addParam("userName", userName);
//        mHttpManager.getJsonObjectResponse2(param, requestCallBack2(callBack));
//    }


}
