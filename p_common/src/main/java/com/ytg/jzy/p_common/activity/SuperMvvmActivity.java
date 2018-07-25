package com.ytg.jzy.p_common.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.ytg.jzy.p_common.mvvmcore.BaseViewModel;


/**
 * Created by goldze on 2017/6/15.
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 */

public abstract class SuperMvvmActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends MCompatActivity  {
    protected V binding;
    protected VM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViewDataBinding(savedInstanceState);


        initViewObservable();

        viewModel.onCreate();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
        viewModel = null;
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true },
        // 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        binding.setVariable(initVariableId(), viewModel = initViewModel());
    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(initVariableId(), viewModel);
        }
    }


    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public abstract VM initViewModel();
    public abstract void initViewObservable() ;
}
