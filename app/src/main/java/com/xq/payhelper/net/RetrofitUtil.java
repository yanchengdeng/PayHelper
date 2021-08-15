package com.xq.payhelper.net;

import com.blankj.utilcode.util.SPUtils;
import com.xq.payhelper.common.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.xq.payhelper.common.Constants.DEFAULT_TIMEOUT;
import static com.xq.payhelper.common.Constants.IS_DEBUG;

public class RetrofitUtil {

    private static RetrofitUtil util;
    private UserService userService;

    private  Retrofit retrofit;

    public static RetrofitUtil getInstance() {
        if (util == null) {
            synchronized (RetrofitUtil.class) {
                if (util == null) {
                    util = new RetrofitUtil();
                }
            }
        }
        return util;
    }

    private RetrofitUtil() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(IS_DEBUG ? HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(interceptor);
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(SPUtils.getInstance().getString(Constants.BASE_URL_KEY))
                .build();
        userService = retrofit.create(UserService.class);
    }


    public void resetUrl(String urlHost){
        if (retrofit != null){
           retrofit.newBuilder().baseUrl(urlHost);
        }
    }

    public UserService userService() {
        return userService;
    }

}
