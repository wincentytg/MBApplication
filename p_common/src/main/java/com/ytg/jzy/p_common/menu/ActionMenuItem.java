/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.ytg.jzy.p_common.menu;

import android.view.MenuItem;
import android.view.View;

/**
 * @author YTG
 * @since 2017/3/8
 */
public class ActionMenuItem {

	/**
	 * Menu id
	 */
	private int menuId;
	
	/**
	 * Menu icon or resource id
	 */
	private int resId;
	
	/**
	 * callback when menu click
	 */
	private MenuItem.OnMenuItemClickListener itemClickListener;
	
	/**
	 * callback when menu longClick
	 */
	private View.OnLongClickListener longClickListener;
	
	/**
	 * custom menuItem
	 */
	private View customView;
	
	/**
	 * custom menuItem for longClick
	 */
	private View longClickCustomView;
	
	/**
	 * Menu desc
	 */
	private CharSequence title;
	
	/**
	 * Menu ActionType
	 */
	private ActionType actionType;
	
	/**
	 * Menu Visiable
	 */
	private boolean visible = true;
	
	/**
	 * Menu enabled.
	 */
	private boolean enabled = true;
	
	
	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}


	public MenuItem.OnMenuItemClickListener getItemClickListener() {
		return itemClickListener;
	}

	public void setItemClickListener(
			MenuItem.OnMenuItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	public View.OnLongClickListener getLongClickListener() {
		return longClickListener;
	}

	public void setLongClickListener(View.OnLongClickListener longClickListener) {
		this.longClickListener = longClickListener;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public View getCustomView() {
		return customView;
	}

	public void setCustomView(View customView) {
		this.customView = customView;
	}

	public View getLongClickCustomView() {
		return longClickCustomView;
	}

	public void setLongClickCustomView(View longClickCustomView) {
		this.longClickCustomView = longClickCustomView;
	}

	public CharSequence getTitle() {
		return title;
	}

	public void setTitle(CharSequence title) {
		this.title = title;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public enum ActionType {
		TEXT,
		BUTTON
	}
}
