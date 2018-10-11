package com.ubustus.ctafbcv.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ubustus.ctafbcv.R
import com.ubustus.ctafbcv.app.Globals



class DesignacionesActivity : AppCompatActivity() {

    val g = application as Globals
    var eurosxkm = g.eurosxkm
    var minimo = g.minimo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designaciones)
    }
}
