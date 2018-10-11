package com.ubustus.ctafbcv.app

import android.app.Application
import com.ubustus.ctafbcv.others.MySharedPreferences

val preferences:MySharedPreferences by lazy { MyApp.prefs!! }

class MyApp: Application(){

    companion object {
        var prefs: MySharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()
        prefs = MySharedPreferences(applicationContext)
    }
}