/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.zhuchao.android.cabinet;

import static com.zhuchao.android.fbase.FileUtils.EmptyString;
import static com.zhuchao.android.net.TNetUtils.fromJson;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;

import com.zhuchao.android.fbase.DataID;
import com.zhuchao.android.fbase.EventCourier;
import com.zhuchao.android.fbase.FileUtils;
import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.fbase.ObjectList;
import com.zhuchao.android.fbase.TAppUtils;
import com.zhuchao.android.fbase.TCourierEventBus;
import com.zhuchao.android.fbase.TTask;
import com.zhuchao.android.fbase.TTaskInterface;
import com.zhuchao.android.fbase.eventinterface.InvokeInterface;
import com.zhuchao.android.fbase.eventinterface.TRequestEventInterface;
import com.zhuchao.android.fbase.eventinterface.TaskCallback;
import com.zhuchao.android.net.TNetUtils;
import com.zhuchao.android.persist.TPersistent;
import com.zhuchao.android.serialport.TUartFile;
import com.zhuchao.android.session.TDeviceManager;
import com.zhuchao.android.session.TPlayManager;
import com.zhuchao.android.session.TTaskManager;
import com.zhuchao.android.session.TTaskQueue;
import com.zhuchao.android.video.VideoList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class CABINET { // implements SessionCompleteCallback
    private static final String TAG = "CABINET";
    @SuppressLint("StaticFieldLeak")
    private static TPlayManager tPlayManager = null;
    @SuppressLint("StaticFieldLeak")
    public static TTaskManager tTaskManager = null;//new TTaskManager(null);
    @SuppressLint("StaticFieldLeak")
    public static TNetUtils tNetUtils = null;//new NetUtils();
    @SuppressLint("StaticFieldLeak")
    public static TAppUtils tAppUtils = null;
    public static TDeviceManager tDeviceManager = null;
    public static TUartFile tUartFile = null;
    @SuppressLint("StaticFieldLeak")
    public static TPersistent tPersistent = null;
    public static TCourierEventBus tCourierEventBus = null;
    public static final TTaskQueue tTaskQueue = new TTaskQueue();
    public static ObjectList properties = new ObjectList();
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static synchronized void InitialModules(Context context) {
        try {
            //String mmLog = TPlatform.GetSystemProperty(MMLog.LOG_SYSTEM_PROPERTY);
            if (!EmptyString(null)) {
                MMLog.i(TAG, "MMLog module is disabled!!!");
                MMLog.setDebugOnOff(false);
            } else {
                MMLog.setDebugOnOff(true);
                MMLog.i(TAG, "MMLog module is enabled!!!");
            }
        } catch (Exception e) {
            MMLog.i(TAG, e.getMessage());
        }
        if (tPlayManager == null) {
            getPlayManager(context);//初始化播放器组件
        }
        if (tPersistent == null) {
            tPersistent = new TPersistent(context, context.getPackageName());
        }
        if (tAppUtils == null) {
            tAppUtils = new TAppUtils(context);
            tAppUtils.registerApplicationsReceiver();
        }
        if (tDeviceManager == null) {
            tDeviceManager = new TDeviceManager();
        }
        if (tCourierEventBus == null) {
            tCourierEventBus = new TCourierEventBus();
        }
        if (tTaskManager == null) {
            tTaskManager = new TTaskManager(context);
        }
        if (tNetUtils == null) {
            tNetUtils = new TNetUtils(context);
        }

    }

    public static synchronized void FreeModules(Context context) {
        try {
            if (tPlayManager != null)
                tPlayManager.free();
            if (tTaskManager != null)
                tTaskManager.free();
            if (tNetUtils != null)
                tNetUtils.free();
            if (tDeviceManager != null)
                tDeviceManager.closeAllUartDevice();
            //if (tSourceManager != null)
            //    tSourceManager.freeFree();
            if (tCourierEventBus != null)
                tCourierEventBus.free();

            if (tPersistent != null)
                tPersistent.commit();//持久化数据

            if (tAppUtils != null) {
                tAppUtils.free();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public static synchronized TPlayManager getPlayManager(Context context) {
        if (tPlayManager == null && context != null) {
            tPlayManager = new TPlayManager(context, null);
            tPlayManager.setPlayOrder(DataID.PLAY_MANAGER_PLAY_ORDER2);//循环顺序播放
            tPlayManager.setAutoPlaySource(DataID.SESSION_SOURCE_FAVORITELIST);//自动播放源列表
        }
        return tPlayManager;
    }

    public static synchronized TPlayManager getPlayManager() {
        if (tPlayManager == null) {
            tPlayManager = new TPlayManager(MApplication.getAppContext(), null);
            tPlayManager.setPlayOrder(DataID.PLAY_MANAGER_PLAY_ORDER2);//循环顺序播放
            tPlayManager.setAutoPlaySource(DataID.SESSION_SOURCE_FAVORITELIST);//自动播放源列表
        }
        return tPlayManager;
    }

    public static void AutoPlay() {
        try {
            if (!getPlayManager(null).isPlaying())
                getPlayManager(null).autoPlay();
            else
                MMLog.log(TAG, "is playing already");

        } catch (Exception e) {
            MMLog.e(TAG, e.getMessage());
        }
    }

    public static void PausePlay() {
        try {
            getPlayManager(null).playPause();
        } catch (Exception e) {
            //e.printStackTrace();
            MMLog.e(TAG, e.getMessage());
        }
    }

    public static synchronized void NextPlay() {
        try {
            getPlayManager(null).playNext();
        } catch (Exception e) {
            //e.printStackTrace();
            MMLog.e(TAG, e.getMessage());
        }
    }

    public static synchronized void StopPlay() {
        try {
            getPlayManager(null).stopPlay();
        } catch (Exception e) {
            MMLog.e(TAG, e.getMessage());
        }
    }

    public static synchronized void StopFree() {
        try {
            getPlayManager(null).stopFree();
        } catch (Exception e) {
            MMLog.e(TAG, e.getMessage());
        }
    }

    public static synchronized void StopFree_t() {
        try {
            getPlayManager(null).stopFree_t();
        } catch (Exception e) {
            MMLog.e(TAG, e.getMessage());
        }
    }

    public static synchronized void ResumePlay() {
        try {
            if (!getPlayManager(null).isPlaying()) {
                getPlayManager(null).resumePlay();
            }
        } catch (Exception e) {
            MMLog.e(TAG, e.getMessage());
        }
    }

    public static void sendDataToUart(byte[] bitData) {
        tCourierEventBus.post(new EventCourier(tUartFile.getDeviceTag(),
                DataID.DEVICE_EVENT_UART_WRITE,
                bitData
        ));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getFWVersionName() {
        return Build.MODEL + "," + Build.VERSION.SDK_INT + "," + Build.VERSION.RELEASE;
    }

    public static ObjectList getRequestProperties() {
        ObjectList parameters = new ObjectList();
        parameters.putString("pname", properties.getString("product_model", null));
        parameters.putString("mac", properties.getString("mac", "123").toLowerCase(Locale.ROOT));
        parameters.putString("modelType", properties.getString("board_model", "123"));
        parameters.putInt("brandId", properties.getInt("customer_id", 123));
        parameters.putString("cname", properties.getString("brand_name", "123"));
        parameters.putString("typeWay", properties.getString("typeWay", "1"));//typeWay 1录入 2自由
        //parameters.putString("mac", "00:da:32:88:00:6d"); //for test
        return parameters;
    }

    public static String getJSONParameter() {
        JSONObject jsonObj = new JSONObject();
        //String nf = null;
        try {
            //if (tNetUtils != null && tNetUtils.getNetworkInformation().getInternetIP() != null) {
            //    if (tNetUtils.getNetworkInformation().getInternetIP().trim().length() > 4) {
            //        nf = tNetUtils.getNetworkInformation().regionToJson();
            //    }
            //}
            jsonObj.put("name", properties.getString("product_name", null));
            jsonObj.put("brand", properties.getString("brand_name", null));
            jsonObj.put("customer", properties.getString("brand_name", null));
            jsonObj.put("mac", properties.getString("mac", null));
            jsonObj.put("ip", null);
            jsonObj.put("region", null);

            jsonObj.put("appVersion", properties.getString("versionName", null));
            jsonObj.put("fwVersion", null);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        //MMLog.d(TAG,"JSONParameter = "+jsonObj.toString());
        return jsonObj.toString();
    }

    public static void configJhzUpdateSession(boolean startAgainFlag) {
        final String SESSION_UPDATE_JHZ_TEST_UPDATE_NAME = "SESSION_JHZ_UPDATE_MODUlE_TEST";
        TTaskInterface tTask = tTaskManager.getObjectByName(SESSION_UPDATE_JHZ_TEST_UPDATE_NAME);
        if (tTask == null) {
            MMLog.i(TAG, "NOT FOUND TASK SESSION_UPDATE_JHZ_TEST_UPDATE_NAME!!");
            return;
        }
        if (EmptyString(properties.getString("product_name", null))) return;
        if (!tTask.isBusy()) {
            ((TRequestEventInterface) (tTask)).setRequestParameter(getJSONParameter());
            if (startAgainFlag || tTask.isTimeOut(24 * 60 * 60 * 1000))
                ((TTaskInterface) (tTask)).startAgainDelayed(2 * 60000);
            else
                ((TTaskInterface) (tTask)).startDelayed(2 * 60000);
        } else {
            //if(((TRequestEventInterface) (tTask)).getRequestParameter() == null)
            ((TRequestEventInterface) (tTask)).setRequestParameter(getJSONParameter());
        }
    }

}