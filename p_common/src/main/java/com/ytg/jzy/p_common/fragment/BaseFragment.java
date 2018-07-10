package com.ytg.jzy.p_common.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ytg.jzy.p_common.YTGApplicationContext;
import com.ytg.jzy.p_common.dialog.LoadingDialog;
import com.ytg.jzy.p_common.request.MRequestManager;
import com.ytg.jzy.p_common.request.RequestBack;
import com.ytg.jzy.p_common.tools.SharedPreferencesHelper;
import com.ytg.jzy.p_common.utils.Event;

import java.util.LinkedHashMap;

import de.greenrobot.event.EventBus;
/**
 *
 * @author 于堂刚
 */
@SuppressLint("NewApi")
public abstract class BaseFragment extends Fragment implements OnClickListener {
    protected SharedPreferencesHelper sp;
    protected Context mContext;
    protected View rootView;
    protected LoadingDialog mLoadDialog;
//    private MenuTab tabData;
    protected Activity mActivity;
    private boolean loaded = false;

    private int containerId;

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public BaseFragment() {
        super();
        mContext = getActivity();
    }

    public BaseFragment(Activity activity,
                        Context context) {
        mActivity = activity;
        mContext = context;
    }

    protected View mViewEmpty;//listview数据为空时用到

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity();
        sp = YTGApplicationContext.sp;
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(setContentViewId(), container, false);
        initViews();
        initContent();
        initEvents();
        return rootView;
    }

//    public void attachTabData(MenuTab tabData) {
//        this.tabData = tabData;
//    }

    protected abstract int setContentViewId();
    public void requestData(String url, LinkedHashMap<String, String> map, RequestBack back) {
        MRequestManager.getInstance().url("").setParamsMap(map).request(mContext, back);
    }
    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) rootView.findViewById(id);
    }

    /**
     * 初始化视图 *
     */
    protected abstract void initViews();

    /**
     * 初始化事件 *
     */
    protected abstract void initEvents();
    protected abstract void initContent();

    public View findViewById(int id) {
        return rootView.findViewById(id);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 发送event事件
     *
     * @param event
     */
    public void postEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 接event事件
     *
     * @param event
     */
    public void onEventMainThread(Event event) {
        switch (event.getmIntTag()) {
            case Event.LOG_OUT:
                // finish();
                break;

            default:
                break;
        }
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    protected void setEmptyView(BaseAdapter adapter) {
        if (null != mViewEmpty) {
            if (adapter.getCount() == 0) {
                mViewEmpty.setVisibility(View.VISIBLE);
            } else {
                mViewEmpty.setVisibility(View.GONE);
            }
        }

    }

    /*云信Tfragment*/
    public interface State {
        public boolean isCurrent(BaseFragment fragment);
    }

    private State state;

    public void setState(State state) {
        this.state = state;
    }


    /**
     * 通过Class跳转界面 *
     */
    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        startActivity(intent);
    }

    /**
     * leave current page
     */
    public void onLeave() {

    }

    /**
     * notify current scrolled
     */
    public void onCurrentScrolled() {
        // NO OP
    }

    public void onCurrentTabClicked() {
        // NO OP
    }

    public void onCurrentTabDoubleTap() {
        // NO OP
    }
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {

    }

    /**
     * 不可见
     */
    protected void onInvisible() {

    }
}
