package com.ubustus.ctafbcv.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ubustus.ctafbcv.R
import com.ubustus.ctafbcv.app.Globals
import com.ubustus.ctafbcv.clases.Partido
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class DesignacionesActivity : AppCompatActivity() {

    val g = application as Globals
    var eurosxkm = g.eurosxkm
    var minimo = g.minimo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designaciones)
    }

    fun designacion(){

        doAsync {
            var result = buscaDesignacion()
            uiThread {
                //toast(result)
            }
        }


    }
    private fun buscaDesignacion(): List<Partido>{

        return emptyList()
    }
}
