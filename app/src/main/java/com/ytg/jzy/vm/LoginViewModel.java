package com.ytg.jzy.vm;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.ytg.jzy.p_common.mvvmcore.BaseViewModel;
import com.ytg.jzy.p_common.mvvmcore.binding.command.BindingAction;
import com.ytg.jzy.p_common.mvvmcore.binding.command.BindingCommand;
import com.ytg.jzy.p_common.mvvmcore.binding.command.BindingConsumer;
import com.ytg.jzy.p_common.utils.ToastUtil;


/**
 * Created by goldze on 2017/7/17.
 */

public class LoginViewModel extends BaseViewModel {
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("123");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {

        //密码开关观察者
        public ObservableBoolean pSwitchObservable = new ObservableBoolean(false);
    }

    public LoginViewModel(Context context) {
        super(context);
    }
    //密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,在View层的监听则会被调用
            uc.pSwitchObservable.set(!uc.pSwitchObservable.get());
        }
    });
    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            if (hasFocus) {
            } else {
            }
        }
    });
    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            login();
        }
    });

    /**
     * 网络模拟一个登陆操作
     **/
    private void login() {
        ToastUtil.showMessage("model");
        userName.set("1232");
//        if (TextUtils.isEmpty(userName.get())) {
//            ToastUtils.showShort("请输入账号！");
//            return;
//        }
//        if (TextUtils.isEmpty(password.get())) {
//            ToastUtils.showShort("请输入密码！");
//            return;
//        }
//        showDialog();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dismissDialog();
//                //进入DemoActivity页面
//                startActivity(DemoActivity.class);
//                //关闭页面
//                ((Activity) context).finish();
//            }
//        }, 3 * 1000);
    }
}
