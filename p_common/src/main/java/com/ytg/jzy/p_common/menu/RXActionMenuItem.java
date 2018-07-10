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

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.ytg.jzy.p_common.YTGApplicationContext;


/**
 * 浮动溢出菜单
 * @author YTG
 * @since 2017/3/18
 */
public class RXActionMenuItem implements MenuItem{

	private final int mId;
	private final int mGroup;

	private int mTitleId;
	private CharSequence mTitle;

	private int iconId;
	private Drawable mIconDrawable;

	private ContextMenuInfo mContextMenuInfo;
	
	private OnMenuItemClickListener mClickListener;


	private int mFlags = ENABLED;
	private static final int CHECKABLE = 0x00000001;
	private static final int CHECKED = 0x00000002;
	private static final int EXCLUSIVE = 0x00000004;
	private static final int HIDDEN = 0x00000008;
	private static final int ENABLED = 0x00000010;

	public RXActionMenuItem(int id, int group) {
		mId = id;
		mGroup = group;
	}

	public char getAlphabeticShortcut() {
		return '\0';
	}

	public int getGroupId() {
		return mGroup;
	}

	public Drawable getIcon() {
		if(mIconDrawable == null) {
			if(iconId != 0) {
				return YTGApplicationContext.getContext().getResources().getDrawable(iconId);
			}
			return null;
		}
		
		return mIconDrawable;
	}

	public Intent getIntent() {
		return null;
	}

	public int getItemId() {
		return mId;
	}

	public ContextMenuInfo getMenuInfo() {
		return mContextMenuInfo;
	}

	public char getNumericShortcut() {
		return '\0';
	}

	public int getOrder() {
		return 0;
	}

	public SubMenu getSubMenu() {
		return null;
	}

	public CharSequence getTitle() {
		
		if(mTitle == null) {
			
			if(mTitleId != 0) {
				return YTGApplicationContext.getContext().getResources().getString(mTitleId);
			}
		}
		return mTitle;
	}

	public CharSequence getTitleCondensed() {
		return null;
	}

	public boolean hasSubMenu() {
		return false;
	}

	public boolean isCheckable() {
		return false;
	}

	public boolean isChecked() {
		return false;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		return true;
	}

	public final boolean performClick() {
		if(mClickListener != null) {
			return mClickListener.onMenuItemClick(this);
		}
		
		return false;
	}
	
	public MenuItem setAlphabeticShortcut(char alphaChar) {
		return this;
	}

	public MenuItem setCheckable(boolean checkable) {
		return this;
	}


	public MenuItem setChecked(boolean checked) {
		return this;
	}

	public MenuItem setEnabled(boolean enabled) {
		return this;
	}

	public MenuItem setIcon(Drawable icon) {
		mIconDrawable = icon;
		return this;
	}

	public MenuItem setIcon(int iconRes) {
		iconId = iconRes;
		return this;
	}

	public MenuItem setIntent(Intent intent) {
		return this;
	}

	public MenuItem setNumericShortcut(char numericChar) {
		return this;
	}
	
	public MenuItem setContextMenuInfo(ContextMenuInfo contextMenuInfo) {
		mContextMenuInfo = contextMenuInfo;
		return this;
	}

	public MenuItem setOnMenuItemClickListener(
			OnMenuItemClickListener menuItemClickListener) {
		mClickListener = menuItemClickListener;
		return this;
	}

	public MenuItem setShortcut(char numericChar, char alphaChar) {
		return this;
	}

	public MenuItem setTitle(CharSequence title) {
		mTitle = title;
		return this;
	}

	public MenuItem setTitle(int title) {
		mTitleId = title;
		return this;
	}

	public MenuItem setTitleCondensed(CharSequence title) {
		return this;
	}

	public MenuItem setVisible(boolean visible) {
		return this;
	}


	public void setShowAsAction(int show) {
		// Do nothing. ActionMenuItems always show as action buttons.
	}

	public MenuItem setActionView(View actionView) {
		return null;
	}

	public View getActionView() {
		return null;
	}

	@Override
	public MenuItem setActionView(int resId) {
		return null;
	}

	@Override
	public ActionProvider getActionProvider() {
		return null;
	}

	@Override
	public MenuItem setActionProvider(ActionProvider actionProvider) {
		return null;
	}

	@Override
	public MenuItem setShowAsActionFlags(int actionEnum) {
		return null;
	}

	@Override
	public boolean expandActionView() {
		return false;
	}

	@Override
	public boolean collapseActionView() {
		return false;
	}

	@Override
	public boolean isActionViewExpanded() {
		return false;
	}

	@Override
	public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
		// No need to save the listener; ActionMenuItem does not support
		// collapsing items.
		return null;
	}

}
