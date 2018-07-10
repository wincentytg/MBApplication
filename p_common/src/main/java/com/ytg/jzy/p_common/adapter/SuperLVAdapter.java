package com.ytg.jzy.p_common.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
/**
 *
 * @author 于堂刚
 */
public abstract class SuperLVAdapter<T> extends BaseAdapter {
	protected int mIdResource;
	public Context mContext;

	public ArrayList<T> mArrayList;
	public ViewHolderBase holder = new ViewHolderBase();

	public SuperLVAdapter(Context mContext, int mIdResource,
                          ArrayList<T> mArrayList) {
		this.mContext = mContext;
		this.mIdResource = mIdResource;
		this.mArrayList = mArrayList;
	}

	public SuperLVAdapter(Context mContext,
                          ArrayList<T> mArrayList) {
		this.mContext = mContext;
		this.mArrayList = mArrayList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(mIdResource,
					null);
		}
		initViewContent(convertView, position);
		return convertView;
	}

//	protected abstract void getConctentViewId(int mIdResource) ;

	public abstract void initViewContent(View convertView, int position);

	public class ViewHolderBase {
		// I added a generic return type to reduce the casting noise in client
		// code
		@SuppressWarnings("unchecked")
		public <T extends View> T get(View view, int id) {
			SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
			if (viewHolder == null) {
				viewHolder = new SparseArray<View>();
				view.setTag(viewHolder);
			}
			View childView = viewHolder.get(id);
			if (childView == null) {
				childView = view.findViewById(id);
				viewHolder.put(id, childView);
			}
			return (T) childView;
		}
	}
	/**
	 *  清空列表数据  刷新界面
	 */
	public void clear(){
		mArrayList.clear();
		notifyDataSetChanged();
	}
}
