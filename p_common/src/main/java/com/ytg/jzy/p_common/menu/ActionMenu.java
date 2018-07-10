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


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ytg.jzy.p_common.YTGApplicationContext;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义菜单（图标、文本）
 * @author YTG
 */
public class ActionMenu implements ContextMenu{

    private Context mContext;

    private CharSequence mHeaderTitle;

    private ArrayList<RXActionMenuItem> mItems;

    /**
     *
     */
    public ActionMenu() {
        mContext = YTGApplicationContext.getContext();
        mItems = new ArrayList<>();
    }


    public Context getContext() {
        return mContext;
    }

    public List<RXActionMenuItem> getMenuItems() {
        return mItems;
    }

    public boolean isMenuNotEmpty() {
        return mItems.size() != 0;
    }

    public MenuItem add(CharSequence title) {
        return add(0, 0, size(), title);
    }

    public MenuItem add(int titleRes) {
        return add(0, 0, size(), titleRes);
    }

    public MenuItem add(int itemId , int titleRes) {
        return add(0, itemId, size(), mContext.getResources().getString(titleRes));
    }
    public MenuItem add(int itemId , String titleRes) {
        return add(0, itemId, size(), titleRes);
    }

    public MenuItem add(int itemId , String titleRes ,int iconRes) {
        MenuItem menuItem = add(0, itemId, size(), titleRes);
        menuItem.setIcon(iconRes);
        return menuItem;
    }

    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return add(groupId, itemId, order, mContext.getResources().getString(titleRes));
    }

    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        RXActionMenuItem item = new RXActionMenuItem(itemId ,groupId);
        item.setTitle(title);
        mItems.add(order, item);
        return item;
    }

    public final MenuItem add(int itemId , int titleRes , int iconRes) {
        RXActionMenuItem item = new RXActionMenuItem(itemId ,0);
        item.setTitle(titleRes);
        item.setIcon(iconRes);
        mItems.add(item);
        return item;
    }

    public int addIntentOptions(int groupId, int itemId, int order,
                                ComponentName caller, Intent[] specifics, Intent intent, int flags,
                                MenuItem[] outSpecificItems) {

        return 0;
    }

    public SubMenu addSubMenu(CharSequence title) {
        // TODO Implement submenus
        return null;
    }

    public SubMenu addSubMenu(int titleRes) {
        // TODO Implement submenus
        return null;
    }

    public SubMenu addSubMenu(int groupId, int itemId, int order,
                              CharSequence title) {
        // TODO Implement submenus
        return null;
    }

    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        // TODO Implement submenus
        return null;
    }

    public final CharSequence getHeaderTitle() {
        return mHeaderTitle;
    }

    public void clear() {
        final ArrayList<RXActionMenuItem> items = mItems;
        final int itemCount = items.size();
        for (int i = 0; i < itemCount; i++) {
            RXActionMenuItem ccpActionMenuItem = items.get(i);
            ccpActionMenuItem.setContextMenuInfo(null);
            ccpActionMenuItem.setOnMenuItemClickListener(null);
        }

        mItems.clear();
        mHeaderTitle = null;
    }

    public void close() {
    }

    private int findItemIndex(int id) {
        final ArrayList<RXActionMenuItem> items = mItems;
        final int itemCount = items.size();
        for (int i = 0; i < itemCount; i++) {
            if (items.get(i).getItemId() == id) {
                return i;
            }
        }

        return -1;
    }

    public MenuItem findItem(int id) {
        int index = findItemIndex(id);
        if(index == -1) {
            return null;
        }
        return mItems.get(index);
    }

    public MenuItem getItem(int index) {
        return mItems.get(index);
    }

    public boolean hasVisibleItems() {
        return false;
    }

    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean performIdentifierAction(int id, int flags) {
        return false;
    }

    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        return false;
    }

    public void removeGroup(int groupId) {

    }

    public void removeItem(int id) {

    }

    public void setGroupCheckable(int group, boolean checkable,
                                  boolean exclusive) {

    }

    public void setGroupEnabled(int group, boolean enabled) {

    }

    public void setGroupVisible(int group, boolean visible) {

    }

    @SuppressLint("ResourceType")
    public final ContextMenu setHeaderTitle(int headerTitle) {
        if (headerTitle > 0) {
            setHeaderTitle(YTGApplicationContext.getContext().getString(headerTitle));
        }
        return this;
    }

    public final ContextMenu setHeaderTitle(CharSequence headerTitle) {
        if (headerTitle == null)
            return this;
        mHeaderTitle = headerTitle;
        return this;
    }

    public void setQwertyMode(boolean isQwerty) {

    }

    public int size() {
        if(mItems == null) {
            return 0;
        }
        return mItems.size();
    }

    @Override
    public ContextMenu setHeaderIcon(int iconRes) {
        return this;
    }

    @Override
    public ContextMenu setHeaderIcon(Drawable icon) {
        return this;
    }

    @Override
    public ContextMenu setHeaderView(View view) {
        return this;
    }

    @Override
    public void clearHeader() {

    }


    /**
     * Interface definition for a callback to be invoked when the {@link ActionMenu} menu
     * for this view is being built.
     */
    public interface OnCreateActionMenuListener {
        /**
         * Called when the {@link ActionMenu} menu for this view is being built. It is not
         * safe to hold onto the menu after this method returns.
         *
         * @param menu The {@link ActionMenu} menu that is being built
         */
        void OnCreateActionMenu(ActionMenu menu);
    }

    /**
     * Interface definition for a callback to be invoked when the {@link ActionMenu} menu
     * for this view is being selcected
     */
    public interface OnActionMenuItemSelectedListener {
        /**
         * Called when the {@link ActionMenu} menu for this view is being selcected. It is not
         * safe to hold onto the menu after this method returns.
         *
         * @param menu The {@link ActionMenu} menu that is being selcected
         */
        void OnActionMenuSelected(MenuItem menu, int position);
    }

    /**
     * Interface definition for a callback to be invoked when the {@link ActionMenu} menu
     * for this view is being set icon
     */
    public interface OnBuildActionMenuIconListener {
        /**
         * Called when the {@link ActionMenu} menu for this view is being set icon
         * @param icon The ImageView that icon whill set
         * @param menu The ImageView that in {@link ActionMenu}
         */
        void OnBuildActionMenuIcon(ImageView icon, MenuItem menu);
    }

    /**
     * Interface definition for a callback to be invoked when the {@link ActionMenu} menu
     * for this view is being set text;
     */
    public interface OnBuildActionMenuTextListener {
        /**
         * Called when the {@link ActionMenu} menu for this view is being set text
         * @param view The TextView that text whill set
         * @param menu The TextView that in {@link ActionMenu}
         */
        void OnBuildActionMenuText(TextView view, MenuItem menu);
    }
}
