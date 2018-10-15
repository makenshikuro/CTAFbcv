package com.ubustus.ctafbcv.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.ubustus.ctafbcv.R
import com.ubustus.ctafbcv.app.preferences


class MainActivity : AppCompatActivity() {
    var txtCategoria:String = ""
    var txtRol:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spCategoria = findViewById<Spinner>(R.id.spCategoria)
        val spRol = findViewById<Spinner>(R.id.spRol)
        val btnGuardar = findViewById<Button>(R.id.BtnGuardarDatos)
        btnGuardar.setOnClickListener {
            if(validaDatos()){
                //Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show()
                goToPrincipalActivity()
            }
        }

        spCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        v: android.view.View, position: Int, id: Long) {
                txtCategoria = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spRol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        v: android.view.View, position: Int, id: Long) {
                txtRol = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    fun validaDatos(): Boolean {
        var etLicencia = findViewById<EditText>(R.id.ET_licencia).text
        var etOrigen = findViewById<EditText>(R.id.ET_origen).text
        var etDni = findViewById<EditText>(R.id.ET_dni).text
        var etPassWeb = findViewById<EditText>(R.id.ET_PassWeb).text

        if (etLicencia.isNullOrEmpty() || etOrigen.isNullOrEmpty() || etDni.isNullOrEmpty() || etPassWeb.isNullOrEmpty()){
            Toast.makeText(this, "Faltan campos por completar", Toast.LENGTH_SHORT).show()
            return false
        }

        preferences.licencia = etLicencia.toString()
        preferences.origen = etOrigen.toString()
        preferences.dni = etDni.toString()
        preferences.passWeb = etPassWeb.toString()
        preferences.firstRun = false
        Toast.makeText(this, "Los valores se han guardado", Toast.LENGTH_SHORT).show()

        return true
    }

    private fun goToPrincipalActivity()= startActivity(Intent(this, PrincipalActivity::class.java))

}
