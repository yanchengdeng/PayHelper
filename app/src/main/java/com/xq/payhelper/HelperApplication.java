package com.xq.payhelper;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.xq.payhelper.common.Constants;
import com.xq.payhelper.common.PreferenceUtil;
import com.xq.payhelper.db.BillDao;

import rxhttp.RxHttpPlugins;

public class HelperApplication extends Application {
    private static Context context;
    private static BillDao billDao;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtil.getInstance().init(this);
        context = this;
        billDao = new BillDao(this);
    }

    public static Context getContext() {
        return context;
    }

    public static BillDao billDao() {
        return billDao;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(Constants.IS_DEBUG);
        RxHttpPlugins.init(RxHttpPlugins.getOkHttpClient()).setDebug(Constants.IS_DEBUG);
    }
}
