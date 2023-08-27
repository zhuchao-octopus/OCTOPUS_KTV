package com.zhuchao.android.cabinet;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

public class MApplication extends Application {
    protected static Context appContext = null;//需要使用的上下文对象
    public static WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
    public static Context getAppContext()
    {
        return appContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this.getApplicationContext();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        CABINET.FreeModules(this);
    }

}
