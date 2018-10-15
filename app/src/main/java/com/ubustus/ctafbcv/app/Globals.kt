package com.ubustus.ctafbcv.app

import android.app.Application
import com.ubustus.ctafbcv.others.MySharedPreferences

val preferences:MySharedPreferences by lazy { Globals.prefs!! }

class Globals: Application(){
    val eurosxkm = 0.16f
    val minimo = 4
    val urlOficina = "https://intrafeb.feb.es/OficinaWebArbitro/"
    val urlDesignaciones = "https://intrafeb.feb.es/OficinaWebArbitro/Paginas/Designaciones.aspx"




    companion object {
        var prefs: MySharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()
        prefs = MySharedPreferences(applicationContext)
    }
}