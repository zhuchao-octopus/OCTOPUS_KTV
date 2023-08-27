package com.zhuchao.android.ktv.adapter;

import android.content.Context;

import com.zhuchao.android.ktv.utils.LocalJsonResolutionUtil;

public class ContentDataAdapterBridge {

    public static String getDataForTabCode(Context context, String tabCode)
    {
        String json = null;
        switch (tabCode) {
            case "c40248cac1f44c278f8bd23a0bba8b4f":
                json = LocalJsonResolutionUtil.getJson(context, "My.json");
                break;
            case "7359d189a049468d9d4e280fd1ec15c5":
                json = LocalJsonResolutionUtil.getJson(context, "WatchTv.json");
                break;
            case "1b14cb1608d3449c83585b48d47b53c1":
                json = LocalJsonResolutionUtil.getJson(context, "Clear4k.json");
                break;
            case "5f6874e8106e41a680e05fe49fe4a198":
                json = LocalJsonResolutionUtil.getJson(context, "Children.json");
                break;
            case "50e4dfe685a84f929ba08952d6081877":
                json = LocalJsonResolutionUtil.getJson(context, "Featured.json");
                break;
            case "dae28835ebac4f629cc610b4d5a8df25":
                json = LocalJsonResolutionUtil.getJson(context, "Years70.json");
                break;
            case "5e1958d0cf9341589db884d83aca79e3":
                json = LocalJsonResolutionUtil.getJson(context, "Everything.json");
                break;
            case "c4a72503d2374b188cf74767f2276220":
                json = LocalJsonResolutionUtil.getJson(context, "VIP.json");
                break;
            case "8146c5ff88a245b9af2ce7d2bf301b27":
                json = LocalJsonResolutionUtil.getJson(context, "TVSeries.json");
                break;
            case "7412804a6aa24ca9be25fd8cd26f1995":
                json = LocalJsonResolutionUtil.getJson(context, "Movie.json");
                break;
            case "d179143bacc948d28748338562a94648":
                json = LocalJsonResolutionUtil.getJson(context, "Variety.json");
                break;
            case "9c58bbdacc1449a4bb84ad6af16ba20d":
                json = LocalJsonResolutionUtil.getJson(context, "Classroom.json");
                break;
            case "c33db6793aba48bea06b075c35c8be5a":
                json = LocalJsonResolutionUtil.getJson(context, "Anime.json");
                break;
            case "65504aa451fb4b159bbfeb7161750411":
                json = LocalJsonResolutionUtil.getJson(context, "Basketball.json");
                break;
            case "a4c28944cb0448579007c6c20c037127":
                json = LocalJsonResolutionUtil.getJson(context, "Physical.json");
                break;
            case "d971d4585bd14e6fadab1aa2d27b71d6":
                json = LocalJsonResolutionUtil.getJson(context, "Game.json");
                break;
            case "a868db298ef84dcbb22d919d02f473cb":
                json = LocalJsonResolutionUtil.getJson(context, "Documentary.json");
                break;
            case "634e89b44aeb4b2a99e9a1bb449daf8b":
                json = LocalJsonResolutionUtil.getJson(context, "Life.json");
                break;
            case "9a5fd09ddfa64c4b95b3dc02b27c7576":
                json = LocalJsonResolutionUtil.getJson(context, "OrientalTheatre.json");
                break;
            case "695ed6a510934a93a9593b034a99fc01":
                json = LocalJsonResolutionUtil.getJson(context, "Car.json");
                break;
            case "b9c9229ef6534682919d7af67438e4d6":
                json = LocalJsonResolutionUtil.getJson(context, "Funny.json");
                break;
        }
        return json;
    }

    public static String getActivityTitle(Context context)
    {
        return LocalJsonResolutionUtil.getJson(context, "MyTitle.json");
    }
}
