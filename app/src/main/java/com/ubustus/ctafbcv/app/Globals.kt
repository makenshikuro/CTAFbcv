package com.ubustus.ctafbcv.app

import android.app.Application
import com.ubustus.ctafbcv.others.MySharedPreferences

val preferences:MySharedPreferences by lazy { Globals.prefs!! }

class Globals: Application(){
    var eurosxkm = 0.16f
    var minimo = 4
    private var data = 200



    companion object {
        var prefs: MySharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()
        prefs = MySharedPreferences(applicationContext)
    }
}