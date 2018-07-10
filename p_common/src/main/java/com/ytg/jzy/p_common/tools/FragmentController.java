package com.ytg.jzy.p_common.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * fragment切换控制器, 初始化时直接add全部fragment, 然后利用show和hide进行切换控制
 */
public class FragmentController {

    private int containerId;
    private FragmentManager fm;
    private ArrayList<Fragment> fragments;

    public FragmentController(FragmentActivity activity, int containerId, ArrayList<Fragment> fragments) {
        this.containerId = containerId;
        this.fragments = fragments;
        this.fm = activity.getSupportFragmentManager();
        initFragment();
    }

    public void initFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            ft.add(containerId, fragments.get(i), String.valueOf(i));
        }
        ft.commit();
    }

    public void showFragment(int position) {
        hideFragments();
        Fragment fragment = fragments.get(position);
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
        ft.commit();
    }

    public void hideFragments() {
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }
public boolean hasShow(){
	for (Fragment fragment : fragments) {
        if (fragment != null) {
        	if(fragment.isVisible()){
        		return true;
        	}
        }
    }
	return false;
}
}