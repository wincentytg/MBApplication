package com.ytg.jzy.p_common.utils;

import android.content.Intent;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

public class Event extends EventBus {
	public enum EventTag {
		LOG_OUT(1), LOG_ON(2), DASHANG(3);
		private int name;

		public int getName() {
			return name;
		}

		EventTag(int name) {
			this.name = name;
		}

		EventTag() {
		}
	}

	public int getEventTag() {
		return EventTag.LOG_OUT.getName();
	}

	public static final int LOG_OUT = 1;
    /**
     * 网络链接
     */
	public static final int NET_WORK_STATE_CONNNECTED = 2;
    /**
     * 网络断开
     */
	public static final int NET_WORK_STATE_DISCONNNECT = 3;
	private EventTag tag;
	private int mIntTag;
	private int id;
	private Object object;
	private Object object2;
	private Intent intent;
	HashMap<String, String> map = new HashMap<String, String>();
	
	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

	public EventTag getTag() {
		return tag;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public Event setTag(EventTag tag) {
		this.tag = tag;
		return this;
	}

	public int getmIntTag() {
		return mIntTag;
	}

	public Event setmIntTag(int mIntTag) {
		this.mIntTag = mIntTag;
		return this;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object getObject2() {
		return object2;
	}

	public void setObject2(Object object2) {
		this.object2 = object2;
	}

}
