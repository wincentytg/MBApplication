package com.ytg.jzy.p_common.request;

import android.content.Context;

/**
 *
 * @author 于堂刚
 */
public interface IRequestStrategy {

	void loadData(Context context, RequestOptions options,RequestBack back);


}
