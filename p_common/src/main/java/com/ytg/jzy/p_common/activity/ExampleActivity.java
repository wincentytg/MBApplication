package com.ytg.jzy.p_common.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ytg.jzy.p_common.R;
import com.ytg.jzy.p_common.mvpcore.presenter.BasePresenter;
import com.ytg.jzy.p_common.tools.SearchViewHelper;

public class ExampleActivity extends SuperPresenterActivity implements MenuItem.OnMenuItemClickListener{
    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_text_size;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setNavigationOnClickListener(null);//返回键取消掉
        setActionBarTitle(R.string.str_settext);
//        mBaseActivity.setFirstButtonLeftInfo(R.drawable.ic_clear_24, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        mBaseActivity.setSecondButtonLeftInfo("btn", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mBaseActivity.addSearchMenu(true,new SearchViewHelper());
        mBaseActivity.setActionBarShadowVisibility(true);
        mBaseActivity.setActionBarSubTitle("SubTitle");
        mBaseActivity.setActionBarSupplementTitle("Title");
        mBaseActivity.setActionMenuItem(1,"btn1",this);
        mBaseActivity.setActionMenuItem(3,"btn3",this);
        mBaseActivity.setActionMenuItem(2,R.drawable.ic_clear_24,"btn2",this);
//showPostingDialog();
showPostingDialog("加载动画");
//showPostingDialog(R.string.app_back,true);

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
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
