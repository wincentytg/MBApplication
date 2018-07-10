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

/**
 * 自定义菜单（图标、文本）
 * @author YTG
 */
public class SubMenu {

	private int menuId;
	private int icon;
	private String title;
	private String desc;
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	/**
	 * 
	 */
	public SubMenu() {
		super();

	}
	
	/**
	 * @param menuId
	 * @param icon
	 * @param title
	 * @param desc
	 */
	public SubMenu(int menuId, int icon, String title, String desc) {
		super();
		this.menuId = menuId;
		this.icon = icon;
		this.title = title;
		this.desc = desc;
	}
	
	
}
