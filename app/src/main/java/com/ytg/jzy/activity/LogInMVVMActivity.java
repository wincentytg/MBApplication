package com.ytg.jzy.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.Observable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.IBinder;

import com.ytg.jzy.R;
import com.ytg.jzy.databinding.ActivityLoginBinding;
import com.ytg.jzy.p_common.BR;
import com.ytg.jzy.p_common.activity.SuperMvvmActivity;
import com.ytg.jzy.p_common.service.DownloadService;
import com.ytg.jzy.p_common.utils.LogUtil;
import com.ytg.jzy.p_common.utils.NetWorkUtils;
import com.ytg.jzy.p_common.utils.ToastUtil;
import com.ytg.jzy.vm.LoginViewModel;

public class LogInMVVMActivity extends SuperMvvmActivity<ActivityLoginBinding, LoginViewModel> {

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel initViewModel() {
        return new LoginViewModel(this);
    }

    @Override
    public void initViewObservable() {
//监听ViewModel中pSwitchObservable的变化, 当ViewModel中执行【uc.pSwitchObservable.set(!uc.pSwitchObservable.get());】时会回调该方法
        viewModel.uc.pSwitchObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //pSwitchObservable是boolean类型的观察者,所以可以直接使用它的值改变密码开关的图标
                if (viewModel.uc.pSwitchObservable.get()) {
                    //密码可见
                    //在xml中定义id后,使用binding可以直接拿到这个view的引用,不再需要findViewById去找控件了
//                    binding.ivSwichPasswrod.setImageResource(R.mipmap.show_psw_press);
//                    binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //密码不可见
//                    binding.ivSwichPasswrod.setImageResource(R.mipmap.show_psw);
//                    binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
//        viewModel.userName.set("123");
        viewModel.userName.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ToastUtil.showMessage(viewModel.userName.get());
            }
        });

    }

    @Override
    protected void initViews() {
//        DownloadUtils downloadUtils =   new DownloadUtils(this);
//        downloadUtils.downloadAPK("http://www.zhaoshangdai.com/file/android.apk", "aa.apk");

        bindService("http://10.73.50.91:8085/meeting/fileController/fileGet.act?file=file/20180730/f5d6845d8a5b47009965c8bb7dc9f76e.xls", "android.xls");
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
            DownloadService downloadService = binder.getService();

            //接口回调，下载进度
            downloadService.setOnProgressListener(new DownloadService.OnProgressListener() {
                @Override
                public void onProgress(float fraction) {
                    LogUtil.i("ttt", "下载进度：" + fraction);

                    //判断是否真的下载完成进行安装了，以及是否注册绑定过服务
                    if (fraction == DownloadService.UNBIND_SERVICE) {
                        unbindService(conn);
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void bindService(String apkUrl, String name) {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, apkUrl);
        intent.putExtra(DownloadService.BUNDLE_KEY_FILE_NAME, name);

        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initContent() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login_mvvm;
    }
}
