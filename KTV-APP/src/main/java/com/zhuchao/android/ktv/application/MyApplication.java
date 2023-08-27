package com.zhuchao.android.ktv.application;


import com.zhuchao.android.cabinet.CABINET;
import com.zhuchao.android.cabinet.MApplication;

public class MyApplication extends MApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ////////////////////////////////////////////////////////////////////////////////////////////
        //初始化各基础核心模块组件
        CABINET.InitialModules(appContext);
    }
}
