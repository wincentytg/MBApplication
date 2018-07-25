package com.ytg.jzy.activity;

import android.databinding.Observable;
import android.os.Bundle;

import com.ytg.jzy.R;
import com.ytg.jzy.databinding.ActivityLoginBinding;
import com.ytg.jzy.p_common.BR;
import com.ytg.jzy.p_common.activity.SuperMvvmActivity;
import com.ytg.jzy.p_common.utils.ToastUtil;
import com.ytg.jzy.vm.LoginViewModel;

public class LogInMVVMActivity extends SuperMvvmActivity<ActivityLoginBinding,LoginViewModel> {

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

    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void initContent() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }
}
