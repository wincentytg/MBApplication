package com.ytg.jzy.p_common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ytg.jzy.p_common.R;
import com.ytg.jzy.p_common.dialog.DialogOnTextView;
import com.ytg.jzy.p_common.mvpcore.presenter.BasePresenter;
import com.ytg.jzy.p_common.utils.DensityUtil;
import com.ytg.jzy.p_common.utils.TextUtil;
import com.ytg.jzy.p_common.view.SetTextSizeView;

public class SettingTextSizeActivity extends SuperPresenterActivity implements MenuItem.OnMenuItemClickListener {

    private TextView text_show;
    private SetTextSizeView setTextSizeView;
    //0.85 小, 1 标准大小, 1.15 大，1.3 超大 ，1.45 特大

    private  Float[] str_size=new Float[]{0.85f,1.0f,1.15f,1.3f,1.45f,1.6f};
    private TextView samll_text;
    private TextView standard_text;

    private int fontCurrentProgress;
    private float fontTextSize;
    private float mPositiveSize;
    private  int mPositivePosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle(R.string.str_settext);
        setNavigationOnClickListener(this);
        initView();

        fontCurrentProgress = TextUtil.getCurrentPoint();
        fontTextSize = TextUtil.getPointTextSize();
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
    private void initView() {
        text_show = (TextView) findViewById(R.id.setting_textshow);
        setTextSizeView = (SetTextSizeView) findViewById(R.id.setting_TextSizeView);
        samll_text = (TextView) findViewById(R.id.samll_textsize);
        standard_text = (TextView) findViewById(R.id.standard_textsize);

        TextUtil.setTextViewSize(text_show,17);
        setTextSizeView.setOnItemSizeListener(new SetTextSizeView.OnItemSizeListener() {
            @Override
            public void onItemSize(int size) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) standard_text.getLayoutParams();
                lp.leftMargin = size- DensityUtil.dip2px(22);
                standard_text.setLayoutParams(lp);
            }
        });

        setTextSizeView.setOnPointResultListener(new SetTextSizeView.OnPointResultListener() {
            @Override
            public void onPointResult(int position) {
                Float size= str_size[position];
                float a=DensityUtil.sp2px(17)*size;
                text_show.setTextSize(TypedValue.COMPLEX_UNIT_PX,a);
                mPositiveSize = size;
                mPositivePosition = position;
                TextUtil.setPointTextSize(size);
                TextUtil.setCurrentPoint(position);
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_text_size;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        createDialog();
        return true;
    }

    private void createDialog() {
        final DialogOnTextView dialog = new DialogOnTextView(this);
        dialog.setDialogMsg("新的设置需重启才能生效");
        dialog.setLeftBtn(View.VISIBLE, "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                text_show.setTextSize(TypedValue.COMPLEX_UNIT_PX,DensityUtil.sp2px(17)*fontTextSize);
                setTextSizeView.reset(fontCurrentProgress);
                TextUtil.setPointTextSize(fontTextSize);
                TextUtil.setCurrentPoint(fontCurrentProgress);
                finish();
            }
        });
        dialog.setRightBtn(View.VISIBLE, "确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextUtil.setPointTextSize(mPositiveSize);
                TextUtil.setCurrentPoint(mPositivePosition);
//                        Intent intent = getBaseContext().getPackageManager()
//                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
//                        PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//                        AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 50, restartIntent); // 1秒钟后重启应用

                Intent intent = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        dialog.show();

    }

    /**
     * 处理按键按下事件
     *
     * @param keyCode 按键编号
     * @param event   案件类型
     * @return 是否处理
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && event.getAction()== KeyEvent.ACTION_DOWN) {
            createDialog();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
