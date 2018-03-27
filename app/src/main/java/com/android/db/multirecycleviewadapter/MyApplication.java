/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2015, Allen, china, shanghai
**                          All Rights Reserved
**
**
**
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/
package com.android.db.multirecycleviewadapter;

import android.app.Application;

import com.android.db.multirecycleviewadapter.util.Utils;


public class MyApplication extends Application {
    private static MyApplication appContext;

    public static MyApplication getInstance() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        Utils.init(this);
    }
}
