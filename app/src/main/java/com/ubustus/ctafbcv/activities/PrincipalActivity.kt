package com.ubustus.ctafbcv.activities

import android.content.Intent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.ubustus.ctafbcv.R
import com.ubustus.ctafbcv.clases.Network

class PrincipalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        val btnDesign = findViewById<Button>(R.id.BtnDesignaciones)
        btnDesign.setOnClickListener {
            //Toast.makeText(this, "designaciones", Toast.LENGTH_SHORT).show()
            if (Network.hayRed(this)) {
                goToDesignacionesPage()
            }else{
                Toast.makeText(this, "Comprueba si hay red" , Toast.LENGTH_SHORT ).show()
            }
        }

    }
    private fun goToDesignacionesPage()= startActivity(Intent(this, DesignacionesActivity::class.java))

}
