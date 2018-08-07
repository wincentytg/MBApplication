package com.ytg.jzy.p_moduletest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ytg.jzy.p_common.servicefac.ServiceFactory;
import com.ytg.jzy.p_common.utils.ToastUtil;

@Route(path = "/TestActivity/module/")
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_main);
        Toast.makeText(this,"modelActivity",Toast.LENGTH_SHORT).show();
        //跳回到主项目的activity
//        ARouter.getInstance().build("/ExampleActivity/module/").navigation();
        ToastUtil.showMessage("登录状态"+ ServiceFactory.getInstance().getAccountService().isLogin());
    }
}
