package com.ytg.jzy.http.ok;

import android.content.Context;

import com.ytg.jzy.p_common.request.IRequestStrategy;
import com.ytg.jzy.p_common.request.RequestBack;
import com.ytg.jzy.p_common.request.RequestOptions;
import com.ytg.jzy.p_common.utils.LogUtil;
import com.ytg.jzy.p_common.utils.NetWorkUtils;
import com.ytg.p_retrofit_rx.convert_factory.StringConverterFactory;
import com.ytg.p_retrofit_rx.utils.BaseObserverString;
import com.ytg.p_retrofit_rx.utils.MCookieManger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

/**
 * okhttp的封装
 */
public class RequestOK implements IRequestStrategy {
    public static final String BASE_URL = "http://106.39.15.102:8080/task/login/";
    public static final int DEFAULT_TIMEOUT = 10000;
    private Context mContext;
    public Retrofit retrofit;
    public ApiService apiService;
    private static volatile RequestOK mManager;

    private RequestOK(Context context) {
        mContext = context;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        LogUtil.i("--------------》RequestOK"+context.getCacheDir());
        File cacheFile = new File(context.getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 20); //20Mb

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache)
                .cookieJar(new MCookieManger(context))
                .build();


//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public synchronized static RequestOK getInstance(Context context) {
        if (mManager == null) {
            mManager = new RequestOK(context);
        }
        return mManager;
    }

    @Override
    public void loadData(Context context, RequestOptions options,
                         final RequestBack back) {
        Observable ob = null;
        if (options instanceof RequestOptionRF) {
//            if (((RequestOptionRF) options).methodName == RequestOptionRF.Method.insertData) {
//                ob = service.getWeather(options.paramsMap);
//            }
            ob = apiService.executePost(options.url,options.paramsMap);
            if (null != ob) {//统一处理回调
                BaseObserverString observer = new BaseObserverString() {
                    @Override
                    protected void onSuccees(JSONObject data) throws Exception {
                        LogUtil.i("--------------》onSuccees");
                        back.onSuccess(data);
                    }

                    @Override
                    protected void onCodeError(int error,String msg) throws Exception {
                        super.onCodeError(error,msg);
                        LogUtil.i("--------------》onCodeError"+msg);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        LogUtil.i("--------------》onFailure");
                        back.onFail(e.getCause());
                    }

                    @Override
                    public void onNext(String o) {
                        LogUtil.i("--------------》onNext");
                        try {
                            back.onSuccess(new JSONObject(o));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                };

                ob.compose(threadTransformer())
                        .subscribe(observer);
                MRXmanager.getInstance().add(options.url,observer);
//                observer.dispose();//取消请求
            }
        }


    }

    public ObservableTransformer threadTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtils.IsNetWorkEnable(mContext)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response originalResponse = chain.proceed(request);
            if (NetWorkUtils.IsNetWorkEnable(mContext)) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }

}
