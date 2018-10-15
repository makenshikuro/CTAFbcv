package com.ubustus.ctafbcv.others

import android.content.Context

class MySharedPreferences(context: Context){
    //Nombre fichero shared preferences
    private val fileName = "mis_preferencias"
    // Instancia de ese fichero
    private val prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    // Por cada una de las variables que vamos a guardar en nuestro fichero shared preferences

    var licencia:String
        get() = prefs.getString("licencia","")
        set(value) = prefs.edit().putString("licencia", value).apply()

    var origen:String
        get() = prefs.getString("origen","")
        set(value) = prefs.edit().putString("origen", value).apply()
    var dni:String
        get() = prefs.getString("dni","")
        set(value) = prefs.edit().putString("dni", value).apply()
    var passWeb:String
        get() = prefs.getString("passWeb","")
        set(value) = prefs.edit().putString("passWeb", value).apply()
    var categoria:String
        get() = prefs.getString("categoria","")
        set(value) = prefs.edit().putString("categoria", value).apply()
    var rol:String
        get() = prefs.getString("rol","")
        set(value) = prefs.edit().putString("rol", value).apply()
    var firstRun:Boolean
        get() = prefs.getBoolean("firstRun",true)
        set(value) = prefs.edit().putBoolean("firstRun", value).apply()



}